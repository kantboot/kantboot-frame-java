package com.kantboot.user.account.service;

import com.kantboot.user.account.domain.vo.LoginVO;

public interface IUserAccountInitService {

    LoginVO initByUsernameAndPassword(String username, String password);

    LoginVO initByEmailAndPassword(String email, String password);

}