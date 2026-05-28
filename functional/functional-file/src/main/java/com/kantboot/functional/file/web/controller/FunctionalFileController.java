package com.kantboot.functional.file.web.controller;

import com.kantboot.functional.file.domain.entity.FunctionalFile;
import com.kantboot.functional.file.service.IFunctionalFileService;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件
 * 用于管理文件的上传、下载、删除等
 */
@AuthInit(name = "文件", description = "文件", sourceLanguageCode = "zh_CN")
@RestController
@RequestMapping("/functional-file-web/file")
public class FunctionalFileController {


    @Resource
    private IFunctionalFileService fileService;

    /**
     * 上传文件
     *
     * @param file      文件
     * @param groupCode 文件组编码
     * @param code      文件编码
     * @return 文件
     */
    @AuthInit(name = "上传文件", description = "上传文件", allPass = true, sourceLanguageCode = "zh_CN")
    @RequestMapping("/upload")
    public RestResult<FunctionalFile> upload(MultipartFile file, String groupCode, String code) {
        return RestResult.success(fileService.upload(file, groupCode, code), CommonSuccessStateConsts.UPLOAD_SUCCESS);
    }

    @AuthInit(name = "根据URL上传文件", description = "根据URL上传文件", allPass = true, sourceLanguageCode = "zh_CN")
    @RequestMapping("/uploadByUrl")
    public RestResult<FunctionalFile> uploadByUrl(
            @RequestParam("/url") String url,
            @RequestParam("groupCode") String groupCode,
            @RequestParam("code") String code) {
        return RestResult.success(fileService.uploadByUrl(url, groupCode, code), CommonSuccessStateConsts.UPLOAD_SUCCESS);
    }

    /**
     * 根据文件id查询缩略图
     *
     * @param fileId 文件id
     * @return 缩略图
     */
    @AuthInit(name = "根据文件id查询缩略图", description = "根据文件id查询缩略图", noNeedLogin = true, sourceLanguageCode = "zh_CN")
    @RequestMapping("/getThumbnailListByFileId")
    public RestResult<Object> getThumbnailListByFileId(
            @RequestParam("fileId") Long fileId) {
        return RestResult.success(fileService.getThumbnailListByFileId(fileId), CommonSuccessStateConsts.GET_SUCCESS);
    }

    /**
     * 根据文件组编码和编码查询缩略图
     */
    @AuthInit(name = "根据文件组编码和编码查询缩略图", description = "根据文件组编码和编码查询缩略图", noNeedLogin = true, sourceLanguageCode = "zh_CN")
    @RequestMapping("/getThumbnailListByGroupCodeAndCode")
    public RestResult<Object> getThumbnailListByGroupCodeAndCode(
            @RequestParam("fileGroupCode") String fileGroupCode,
            @RequestParam("fileCode") String fileCode) {
        return RestResult.success(fileService.getThumbnailListByGroupCodeAndCode(fileGroupCode, fileCode), CommonSuccessStateConsts.GET_SUCCESS);
    }

    /**
     * 访问文件
     *
     * @param id 文件id
     * @return 文件
     */
    @AuthInit(name = "访问文件", description = "访问文件", noNeedLogin = true, sourceLanguageCode = "zh_CN")
    @RequestMapping("/visit")
    public ResponseEntity<FileSystemResource> visit(
            @RequestParam("id") Long id) {
            return fileService.visit(id);
    }

    /**
     * 根据路径访问文件
     */
    @AuthInit(name = "根据路径访问文件", description = "根据路径访问文件", noNeedLogin = true, sourceLanguageCode = "zh_CN")
    @RequestMapping("/visitByPath")
    public ResponseEntity<FileSystemResource> visitByPath(@RequestParam("path") String path) {
        try{
            return fileService.visitByPath(path);
        }catch (Exception e){
            // 返回404
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 根据分组编码和编码访问文件
     *
     * @param groupCode 分组编码
     * @param code      编码
     * @return 文件
     */
    @AuthInit(name = "根据分组编码和编码访问文件", description = "根据分组编码和编码访问文件", noNeedLogin = true, sourceLanguageCode = "zh_CN")
    @RequestMapping("/visitByGroupCodeAndCode")
    public ResponseEntity<FileSystemResource> visitByGroupCodeAndCode(
            @RequestParam("groupCode") String groupCode,
            @RequestParam("code") String code) {
        return fileService.visitByGroupCodeAndCode(groupCode, code);
    }

    /**
     * 根据全编码访问文件
     *
     * @param fullCode 全编码
     * @return 文件
     */
    @AuthInit(name = "根据全编码访问文件", description = "根据全编码访问文件", noNeedLogin = true, sourceLanguageCode = "zh_CN")
    @RequestMapping("/visitByFullCode")
    public ResponseEntity<FileSystemResource> visitByFullCode(
            @RequestParam("fullCode") String fullCode) {
        return fileService.visitByFullCode(fullCode);
    }

    /**
     * 访问缩略图
     *
     * @param thumbnailId 缩略图id
     * @return 缩略图
     */
    @AuthInit(name = "访问缩略图", description = "访问缩略图", noNeedLogin = true, sourceLanguageCode = "zh_CN")
    @RequestMapping("/visitThumbnail")
    public ResponseEntity<FileSystemResource> visitThumbnail(
            @RequestParam("thumbnailId") Long thumbnailId) {
        return fileService.visitThumbnail(thumbnailId);
    }

}
