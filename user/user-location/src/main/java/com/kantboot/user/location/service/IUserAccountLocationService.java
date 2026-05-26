package com.kantboot.user.location.service;

import com.kantboot.user.location.domain.entity.UserAccountLocation;

public interface IUserAccountLocationService {

    /**
     * 保存实体
     * @param entity 实体
     * @return 保存后的实体
     */
    UserAccountLocation save(UserAccountLocation entity);

    /**
     * 保存用户自身的位置信息
     * @return 保存后的实体
     */
    UserAccountLocation saveSelf();

}
