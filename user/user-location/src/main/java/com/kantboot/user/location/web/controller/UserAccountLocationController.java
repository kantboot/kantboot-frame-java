package com.kantboot.user.location.web.controller;

import com.kantboot.util.auth.annotation.AuthInit;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AuthInit(name = "用户账号位置", description = "用户账号位置", sourceLanguageCode = "zh_CN")
@RestController
@RequestMapping("/user-location-web/userAccountLocation")
public class UserAccountLocationController {

}
