package com.kantboot.functional.translate.service.impl;

import cn.hutool.core.util.StrUtil;
import com.kantboot.functional.translate.dao.repository.FunctionalTranslateTextRepository;
import com.kantboot.functional.translate.domain.entity.FunctionalTranslateText;
import com.kantboot.functional.translate.service.IFunctionalTranslateService;
import com.kantboot.functional.translate.slot.FunctionalTranslateSlot;
import com.kantboot.util.cache.CacheUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 翻译服务实现类
 * @author 方某方
 */
@Service
public class FunctionalTranslateServiceImpl
        implements IFunctionalTranslateService {

    @Resource
    private FunctionalTranslateSlot functionalTranslateSlot;

    @Resource
    private FunctionalTranslateTextRepository repository;

    @Resource
    private CacheUtil cacheUtil;

    /**
     * 翻译文本的方法
     * @param sourceLanguageCode 源语言代码
     * @param targetLanguage 目标语言代码
     * @param text 需要翻译的文本
     * @return 翻译后的文本
     */
    public String translateText(String sourceLanguageCode, String targetLanguage, String text) {

        // 根据源语言代码、目标语言代码和源文本查找已翻译的记录
        List<FunctionalTranslateText> texts
                = repository.findBySourceLanguageCodeAndTargetLanguageCodeAndSourceText(sourceLanguageCode,
                targetLanguage, text);

        // 如果找到已翻译的记录，则返回目标文本
        if (texts != null && !texts.isEmpty()) {
            return texts.getFirst().getTargetText();
        }

        // 如果没有找到已翻译的记录，则调用翻译插槽进行翻译
        String targetText = functionalTranslateSlot.translateText(sourceLanguageCode, targetLanguage, text);

        if (targetText == null || targetText.isEmpty()) {
            // 如果翻译结果为空，则返回源文本
            return text;
        }
        if(StrUtil.isNotEmpty(targetLanguage)){
            // 保存翻译结果到数据库
            repository.save(new FunctionalTranslateText()
                    .setSourceLanguageCode(sourceLanguageCode)
                    .setTargetLanguageCode(targetLanguage)
                    .setSourceText(text)
                    .setTargetText(targetText));
        }

        // 返回翻译后的文本
        return targetText;
    }

}
