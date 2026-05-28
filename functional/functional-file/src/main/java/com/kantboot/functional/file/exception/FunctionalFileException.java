package com.kantboot.functional.file.exception;


import com.kantboot.util.rest.exception.BaseException;

public class FunctionalFileException {

    /**
     * 文件不存在
     * File does not exist
     */
    public static final BaseException FILE_NOT_EXIST = BaseException.of("fileNotExist", "File does not exist","en");

    /**
     * 文件组不存在
     * File group does not exist
     */
    public static final BaseException FILE_GROUP_NOT_EXIST = BaseException.of("fileGroupNotExist", "File group does not exist","en");

    /**
     * 编码不可为空
     * Code cannot be empty
     */
    public static final BaseException CODE_CANNOT_BE_EMPTY = BaseException.of("codeCannotBeEmpty", "Code cannot be empty","en");

    /**
     * thumbnailPathError
     */
    public static final BaseException THUMBNAIL_PATH_ERROR = BaseException.of("thumbnailPathError", "Thumbnail path error","en");

}
