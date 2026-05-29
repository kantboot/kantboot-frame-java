package com.kantboot.ai.chat.exception;

import com.kantboot.util.rest.exception.BaseException;

public class AiChatException {
    public static final BaseException ROLE_NOT_EXIST = BaseException.of("aiChatRoleNotExist", "AI role does not exist");
    public static final BaseException MODEL_NOT_EXIST = BaseException.of("aiChatModelNotExist", "AI model does not exist");
    public static final BaseException DIALOG_NOT_EXIST = BaseException.of("aiChatDialogNotExist", "AI dialog does not exist");
    public static final BaseException LANGUAGE_NOT_SUPPORTED = BaseException.of("aiChatLanguageNotSupported", "Language not supported by this role");
    public static final BaseException DIALOG_LOCKED = BaseException.of("aiChatDialogLocked", "Dialog is being answered, please wait");
}
