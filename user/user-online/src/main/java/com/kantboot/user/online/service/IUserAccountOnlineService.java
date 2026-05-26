package com.kantboot.user.online.service;

import java.util.Map;

public interface IUserAccountOnlineService {

    /**
     * 隐身
     */
    void invisible(Long userAccountId, Boolean isInvisible);

    /**
     * 对自己隐身
     */
    void invisibleSelf(Boolean isInvisible);

    /**
     * 心跳
     */
    void heartbeat(Long userAccountId);

    /**
     * 对自己心跳
     */
    void heartbeatSelf();

    /**
     * 上线
     */
    void online(Long userAccountId);

    /**
     * 上线，传map事件
     */
    void online(Long userAccountId,Map<String, Object> event);

    /**
     * 自己上线
     */
    void onlineSelf();

    /**
     * 离线
     */
    void offline(Long userAccountId);

    /**
     * 自己离线
     */
    void offlineSelf();

}
