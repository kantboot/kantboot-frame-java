package com.kantboot.functional.translate.dao.repository;

import com.kantboot.functional.translate.domain.entity.FunctionalTranslateText;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FunctionalTranslateTextRepository
    extends JpaRepository<FunctionalTranslateText,Long> {

    /**
     * 根据源语言编码、目标语言编码、原文本查询翻译文本
     */
    List<FunctionalTranslateText> findBySourceLanguageCodeAndTargetLanguageCodeAndSourceText(
            String sourceLanguageCode, String targetLanguageCode, String sourceText);

}
