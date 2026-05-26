package com.kantboot.util.web.controller;

import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.qrcode.util.QrCodeUtil;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AuthInit(name = "二维码工具", description = "二维码工具", sourceLanguageCode = "zh_CN")
@RestController
@RequestMapping("/util-qrcode-web/qrCode")
public class UtilQrCodeController {

    @Resource
    private HttpServletRequest request;

    @Resource
    private HttpServletResponse response;

    /**
     * 获取对应的svg二维码
     */
    @RequestMapping("/getSvg")
    public RestResult<?> getSvg(@RequestParam("content") String content) {
        // 添加浏览器缓存
        response.addHeader("Cache-Control", "max-age=86400");

        String svg = QrCodeUtil.getSvg(content);
        return RestResult.success(svg, CommonSuccessStateConsts.OPERATION_SUCCESS);
    }

    /**
     * 获取对应的svg二维码图片
     */
    @RequestMapping("/getSvgImage")
    public void getSvgImage(@RequestParam("content") String content,HttpServletResponse response) {
        // 添加浏览器缓存
        response.addHeader("Cache-Control", "max-age=86400");

        // 设置响应类型为SVG图片
        response.setContentType("image/svg+xml");

        String svg = QrCodeUtil.getSvg(content);

        try {
            response.getWriter().write(svg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
