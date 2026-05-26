package com.kantboot.util.qrcode.util;

import cn.hutool.extra.qrcode.QrConfig;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * 二维码工具类
 */
public class QrCodeUtil {

    public static String getSvg(String content){
        QrConfig qrConfig = QrConfig.create()
                .setRatio(8)
                .setErrorCorrection(ErrorCorrectionLevel.M);
        return cn.hutool.extra.qrcode.QrCodeUtil.generateAsSvg(content,qrConfig);
    }

}
