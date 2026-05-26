package com.kantboot.functional.file.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.kantboot.functional.file.dao.repository.FunctionalFileRecordRepository;
import com.kantboot.functional.file.dao.repository.FunctionalFileRepository;
import com.kantboot.functional.file.dao.repository.FunctionalFileThumbnailRepository;
import com.kantboot.functional.file.domain.entity.FunctionalFile;
import com.kantboot.functional.file.domain.entity.FunctionalFileGroup;
import com.kantboot.functional.file.domain.entity.FunctionalFileRecord;
import com.kantboot.functional.file.domain.entity.FunctionalFileThumbnail;
import com.kantboot.functional.file.exception.FunctionalFileException;
import com.kantboot.functional.file.service.IFunctionalFileGroupService;
import com.kantboot.functional.file.service.IFunctionalFileService;
import com.kantboot.functional.file.util.FunctionalFileUtil;
import com.kantboot.functional.file.util.ImageCompressUtil;
import com.kantboot.user.account.service.IUserAccountService;
import com.kantboot.util.file.FileUtil;
import com.kantboot.util.http.HttpRequestHeaderUtil;
import com.kantboot.util.rest.exception.BaseException;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 文件管理的Service接口实现类
 * @author FangMoFang
 */
@Slf4j
@Service
public class FunctionalFileServiceImpl implements IFunctionalFileService {

    @Resource
    private FunctionalFileRepository repository;

    @Resource
    private FunctionalFileThumbnailRepository thumbnailRepository;

    @Resource
    private IUserAccountService userAccountService;

    @Resource
    private HttpRequestHeaderUtil httpRequestHeaderUtil;

    @Resource
    private FunctionalFileUtil functionalFileUtil;

    @Resource
    private IFunctionalFileGroupService groupService;

    @Resource
    private FunctionalFileRecordRepository fileRecordRepository;

    private final static Cache<String, Object> CACHE = Caffeine.newBuilder()
            .expireAfterWrite(7, TimeUnit.DAYS)
            .build();

    @Override
    public FunctionalFile getById(Long id) {
        // 从缓存中获取文件
        FunctionalFile fileByIdFromCache = functionalFileUtil.getFileByIdFromCache(id);
        if (fileByIdFromCache != null) {
            return fileByIdFromCache;
        }
        // 从数据中库中获取文件
        FunctionalFile file = repository.findById(id).orElse(null);
        if (file != null) {
            functionalFileUtil.setFileByIdToCache(file);
            return file;
        }
        // 如果文件不存在，抛出异常
        throw FunctionalFileException.FILE_NOT_EXIST;
    }

    @Override
    public FunctionalFile getByPath(String path) {
        Object ifPresent = CACHE.getIfPresent("FunctionalFile:path:" + path);
        if (ifPresent != null) {
            return (FunctionalFile) ifPresent;
        }
        // 从数据库中获取
        FunctionalFile file = repository.findFirstByPath(path);
        if (file != null) {
            return file;
        }
        CACHE.put("FunctionalFile:path:" + path, path);
        // 如果文件不存在，抛出异常
        throw FunctionalFileException.FILE_NOT_EXIST;
    }

    @Override
    public FunctionalFile upload(MultipartFile file, String groupCode, String code) {
        FunctionalFileGroup byCode = groupService.getByCode(groupCode);
        // 如果文件组不存在，抛出异常
        if (byCode == null) {
            throw FunctionalFileException.FILE_GROUP_NOT_EXIST;
        }

        // TODO 判断是否压缩,如果需要压缩，则进行压缩
        if (Boolean.TRUE.equals(byCode.getNeedCompress())
                && byCode.getCompressSize() != null
                && byCode.getCompressSize() > 0
                && file != null
                && file.getSize() > byCode.getCompressSize()) {

            file = ImageCompressUtil.compressIfNeeded(file, byCode.getCompressSize());
        }



        if (byCode == null) {
            // 如果文件组不存在，抛出异常
            throw FunctionalFileException.FILE_GROUP_NOT_EXIST;
        }

        // 获取文件MD5
        String md5 = FileUtil.getMd5(file);
        // 获取上传时的文件名
        String originalName = file.getOriginalFilename();
        String fileType = "UNKNOWN";
        // 获取文件类型
        if (originalName != null) {
            fileType = FileUtil.getSuffix(originalName);
        }
        // UUID
        String uuid = IdUtil.simpleUUID();
        // 文件名称
        String fileName = uuid+"_"+md5 + "." + fileType;
        String fullCode = null;
        if(code != null) {
            fullCode = groupCode + "." + code;
        }

        // 如果文件后缀是m4a，则修改为mp3
        // 因为.m4a文件会在很多操作系统上，使用后直接删除
        if (StrUtil.isEmpty(originalName) && originalName.endsWith(".m4a")) {
            originalName = originalName.replace(".m4a", ".mp3");
        }

        FunctionalFile functionalFile = new FunctionalFile();
        functionalFile.setGroupCode(groupCode);
        functionalFile.setCode(code);
        functionalFile.setFullCode(fullCode);
        functionalFile.setName(fileName);
        functionalFile.setOriginalName(originalName);
        functionalFile.setType(fileType);
        functionalFile.setContentType(file.getContentType());
        functionalFile.setSize(file.getSize());
        functionalFile.setMd5(md5);
        functionalFile.setUserAccountIdOfUpload(userAccountService.getSelfIdNoThrow());
        functionalFile.setIpOfUpload(httpRequestHeaderUtil.getIp());

        FunctionalFile firstByMd5AndGroupCode = repository.findFirstByMd5AndGroupCode(md5, groupCode);
        FunctionalFile result = null;
        if (firstByMd5AndGroupCode != null) {
            // 如果文件已经存在，则返回已存在的文件
            result = firstByMd5AndGroupCode;
        } else {
            // 获取当前时间
            LocalDateTime now = LocalDateTime.now();
            int monthValue = now.getMonthValue();
            String monthStr = monthValue < 10 ? "0" + monthValue : String.valueOf(monthValue);
            int dayOfMonth = now.getDayOfMonth();
            String dayStr = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);

            String pathPrefix = now.getYear()+"-"+monthStr+"-"+dayStr;

            String pathPrefix2 = byCode.getPath() + "/" + pathPrefix;
            Path p = Paths.get(pathPrefix2).normalize();
            try {
                Files.createDirectories(p);
            } catch (IOException e) {
                throw BaseException.of(
                        "pathPrefixError",
                        "路径异常：" + p.toAbsolutePath() + "，原因：" + e.getClass().getSimpleName() + " - " + e.getMessage(),
                        "zh_CN"
                );
            }


            functionalFile.setPath(pathPrefix + "/" + fileName);
            result = repository.save(functionalFile);
            // 保存文件
            try {
                file.transferTo(new File(byCode.getPath() + "/" + functionalFile.getPath()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // 判断是否需要缩略
            if (byCode.getHasThumbnail()) {
                uploadThumbnail(result);
            }
        }

        FunctionalFileRecord record = BeanUtil.copyProperties(result, FunctionalFileRecord.class);
        record.setId(null);
        assert result.getId() != null;
        record.setFileId(result.getId());
        fileRecordRepository.save(record);

        return result;
    }

    @Override
    public FunctionalFile uploadByUrl(String url, String groupCode, String code) {
        MultipartFile file = FileUtil.downloadFromUrl(url);
        return upload(file, groupCode, code);
    }

    @Modifying
    @Transactional
    @Override
    @SneakyThrows
    public void uploadThumbnail(FunctionalFile file) {
        ThreadUtil.execute(() -> {
            List<String> md5List = new ArrayList<>();
            List<FunctionalFileThumbnail> thumbnails = new ArrayList<>();
            // 获取文件的分组
            FunctionalFileGroup group = groupService.getByCode(file.getGroupCode());

            // 获取文件的路径
            String path = group.getPath() + "/" + file.getName();
            // 获取缩略图的路径
            String thumbnailPath = group.getThumbnailPath();
            // 如果文件夹不存在则创建
            File thumbnailPathFile = new File(thumbnailPath);
            if (!thumbnailPathFile.exists()) {
                if (!thumbnailPathFile.mkdirs()) {
                    throw FunctionalFileException.THUMBNAIL_PATH_ERROR;
                }
            }

            for (int i = 1; i < 10; i++) {
                // 计算缩略图质量
                BigDecimal divide = new BigDecimal(i).divide(new BigDecimal(20), 2, RoundingMode.HALF_UP);
                Float quality = divide.floatValue();
                MultipartFile compressImage = FileUtil.compressImage(path, divide.doubleValue());
                String md5 = FileUtil.getMd5(compressImage);
                boolean isAdd = true;
                for (String oldMd5 : md5List) {
                    if (oldMd5.equals(md5)) {
                        isAdd = false;
                        break;
                    }
                }
                if (isAdd) {
                    md5List.add(md5);
                    FunctionalFileThumbnail fileThumbnailIn = new FunctionalFileThumbnail()
                            .setFileId(file.getId())
                            .setQuality(quality)
                            .setGroupCode(file.getGroupCode())
                            .setMd5(md5)
                            .setName(md5 + "." + file.getType())
                            .setSize(compressImage.getSize())
                            .setType(file.getType())
                            .setContentType(file.getContentType());
                    thumbnails.add(fileThumbnailIn);
                    try {
                        compressImage.transferTo(new File(thumbnailPath + "/" + fileThumbnailIn.getName()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            thumbnailRepository.saveAll(thumbnails);

        });

    }

    @Override
    public List<FunctionalFileThumbnail> getThumbnailListByFileId(Long fileId) {
        String cacheKey = "FunctionalFile:thumbnails:" + fileId;
        // 从缓存中获取缩略图列表
        List<FunctionalFileThumbnail> thumbnailsFromCache = (List<FunctionalFileThumbnail>) CACHE.getIfPresent(cacheKey);
        if (thumbnailsFromCache != null) {
            return thumbnailsFromCache;
        }
        // 从数据库中获取缩略图列表
        List<FunctionalFileThumbnail> thumbnails = thumbnailRepository.findByFileIdOrderByQualityAsc(fileId);
        if (thumbnails != null && !thumbnails.isEmpty()) {
            // 将缩略图列表存入缓存
            CACHE.put(cacheKey, thumbnails);
            return thumbnails;
        }
        return thumbnails;
    }

    @Override
    public List<FunctionalFileThumbnail> getThumbnailListByGroupCodeAndCode(String groupCode, String code) {
        return thumbnailRepository.findByGroupCodeAndCode(groupCode,code);
    }

    @Override
    public List<FunctionalFile> getListByGroupCode(String groupCode) {
        return repository.findByGroupCode(groupCode);
    }

    @Override
    public ResponseEntity<FileSystemResource> visit(Long id) {
        FunctionalFile byId = getById(id);
        String path = groupService.getPathByCode(byId.getGroupCode()) + "/" + byId.getPath();
        // 根据路径、文件名、文件类型、文件大小返回ResponseEntity
        return functionalFileUtil.getResponseEntityByPath(path,
                byId.getName(),
                byId.getContentType(),
                byId.getSize());
    }

    @Override
    public ResponseEntity<FileSystemResource> visitByPath(String path) {
        FunctionalFile byPath = getByPath(path);
        String fullPath = groupService.getPathByCode(byPath.getGroupCode()) + "/" + byPath.getPath();
        // 根据路径、文件名、文件类型、文件大小返回ResponseEntity
        return functionalFileUtil.getResponseEntityByPath(fullPath,
                byPath.getName(),
                byPath.getContentType(),
                byPath.getSize());
    }

    @Override
    public FunctionalFile getByGroupCodeAndCode(String groupCode, String code) {
        return null;
    }

    @Override
    public FunctionalFile getByFullCode(String fullCode) {
        String cacheKey = "FunctionalFile:fullCode:" + fullCode;
        // 从缓存中获取文件
        FunctionalFile fileFromCache = (FunctionalFile) CACHE.getIfPresent(cacheKey);
        if (fileFromCache != null) {
            return fileFromCache;
        }
        // 从数据库中获取文件
        FunctionalFile file = repository.findFirstByFullCode(fullCode);
        if (file != null) {
            // 将文件存入缓存
            CACHE.put(cacheKey, file);
            return file;
        }
        // 如果文件不存在，抛出异常
        throw FunctionalFileException.FILE_NOT_EXIST;
    }


    @Override
    public ResponseEntity<FileSystemResource> visitByGroupCodeAndCode(String groupCode, String code) {
        FunctionalFile file = getByGroupCodeAndCode(groupCode, code);
        if (file == null) {
            throw FunctionalFileException.FILE_NOT_EXIST;
        }
        String path = groupService.getPathByCode(file.getGroupCode()) + "/" + file.getPath();
        // 根据路径、文件名、文件类型、文件大小返回ResponseEntity
        return functionalFileUtil.getResponseEntityByPath(path,
                file.getName(),
                file.getContentType(),
                file.getSize());
    }


    @Override
    public ResponseEntity<FileSystemResource> visitByFullCode(String fullCode) {
        FunctionalFile file = getByFullCode(fullCode);
        if (file == null) {
            throw FunctionalFileException.FILE_NOT_EXIST;
        }
        String path = groupService.getPathByCode(file.getGroupCode()) + "/" + file.getPath();
        // 根据路径、文件名、文件类型、文件大小返回ResponseEntity
        return functionalFileUtil.getResponseEntityByPath(path,
                file.getName(),
                file.getContentType(),
                file.getSize());
    }

    @Override
    public ResponseEntity<FileSystemResource> visitThumbnail(Long thumbnailId) {
        Object ifPresent = CACHE.getIfPresent("FunctionalFileThumbnail:byId:" + thumbnailId);
        if (ifPresent != null) {
            FunctionalFileThumbnail thumbnail = (FunctionalFileThumbnail) ifPresent;
            String path = groupService.getPathByCode(thumbnail.getGroupCode()) + "/" + thumbnail.getName();
            // 根据路径、文件名、文件类型、文件大小返回ResponseEntity
            return functionalFileUtil.getResponseEntityByPath(path,
                    thumbnail.getName(),
                    thumbnail.getContentType(),
                    thumbnail.getSize());
        }
        FunctionalFileThumbnail thumbnail = thumbnailRepository.findById(thumbnailId)
                .orElseThrow(() -> FunctionalFileException.FILE_NOT_EXIST);
        CACHE.put("FunctionalFileThumbnail:byId:" + thumbnailId, thumbnail);
        String path = groupService.getPathByCode(thumbnail.getGroupCode()) + "/" + thumbnail.getName();
        // 根据路径、文件名、文件类型、文件大小返回ResponseEntity
        return functionalFileUtil.getResponseEntityByPath(path,
                thumbnail.getName(),
                thumbnail.getContentType(),
                thumbnail.getSize());
    }

    @Override
    public String toBase64(Long id) {
        FunctionalFile byId = getById(id);
        String path = groupService.getPathByCode(byId.getGroupCode()) + "/" + byId.getPath();
        return FileUtil.toBase64(path, byId.getContentType());
    }
}
