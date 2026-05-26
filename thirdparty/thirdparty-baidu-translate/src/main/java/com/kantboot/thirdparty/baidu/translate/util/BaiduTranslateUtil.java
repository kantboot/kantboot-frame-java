package com.kantboot.thirdparty.baidu.translate.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.kantboot.thirdparty.baidu.translate.exception.ThirdpartyBaiduTranslateException;
import com.kantboot.util.rest.exception.BaseException;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BaiduTranslateUtil {

    public static final Map<String, String> hashMap = new HashMap<>();

    static {
        hashMap.put("af_ZA", "afr");
        hashMap.put("ar", "ara");
        hashMap.put("ar_DZ", "arq");
        hashMap.put("az_AZ", "aze");
        hashMap.put("be_BY", "bel");
        hashMap.put("bg_BG", "bul");
        hashMap.put("ca_ES", "cat");
        hashMap.put("cs_CZ", "cs");
        hashMap.put("cy_GB", "wel");
        hashMap.put("da_DK", "dan");
        hashMap.put("de", "de");
        hashMap.put("dv_MV", "div");
        hashMap.put("el_GR", "el");
        hashMap.put("en", "en");
        hashMap.put("et_EE", "est");
        hashMap.put("eu_ES", "baq");
        hashMap.put("fa_IR", "per");
        hashMap.put("fi_FI", "fin");
        hashMap.put("fo_FO", "fao");
        hashMap.put("fr_BE", "fr");
        hashMap.put("fr_CA", "frn");
        hashMap.put("gl_ES", "glg");
        hashMap.put("gu_IN", "guj");
        hashMap.put("he_IL", "heb");
        hashMap.put("hi_IN", "hi");
        hashMap.put("ba", "bos");
        hashMap.put("hr_HR", "hrv");
        hashMap.put("hu_HU", "hu");
        hashMap.put("hy_AM", "arm");
        hashMap.put("is_IS", "ice");
        hashMap.put("it", "it");
        hashMap.put("ja_JP", "jp");
        hashMap.put("ka_GE", "geo");
        hashMap.put("kn_IN", "kan");
        hashMap.put("ko_KR", "kor");
        hashMap.put("kok_IN", "kok");
        hashMap.put("ky_KG", "kir");
        hashMap.put("lt_LT", "lit");
        hashMap.put("lv_LV", "lav");
        hashMap.put("mi_NZ", "mao");
        hashMap.put("mk_MK", "mac");
        hashMap.put("ms", "may");
        hashMap.put("mt_MT", "mlt");
        hashMap.put("nl_NL", "nl");
        hashMap.put("ns_ZA", "sot");
        hashMap.put("pa_IN", "pan");
        hashMap.put("pl_PL", "pl");
        hashMap.put("pt_BR", "pot");
        hashMap.put("pt_PT", "pt");
        hashMap.put("qu", "que");
        hashMap.put("ro_RO", "rom");
        hashMap.put("ru_RU", "ru");
        hashMap.put("sa_IN", "san");
        hashMap.put("se", "sme");
        hashMap.put("sk_SK", "sk");
        hashMap.put("sl_SI", "slo");
        hashMap.put("st_ZA", "ped");
        hashMap.put("es", "spa");
        hashMap.put("syr_SY", "syr");
        hashMap.put("ta_IN", "tam");
        hashMap.put("te_IN", "tel");
        hashMap.put("th_TH", "th");
        hashMap.put("tr_TR", "tr");
        hashMap.put("ts_ZA", "tso");
        hashMap.put("tt_RU", "tat");
        hashMap.put("uk_UA", "ukr");
        hashMap.put("ur_PK", "urd");
        hashMap.put("vi_VN", "vie");
        hashMap.put("xh_ZA", "xho");
        hashMap.put("zh", "zh");
        hashMap.put("zh_CN", "zh");
        hashMap.put("zh_HK", "yue");
        hashMap.put("zh_MO", "cht");
        hashMap.put("zh_TW", "cht");
        hashMap.put("sq_AL", "alb");
        hashMap.put("sr_BA", "srp");
        hashMap.put("sr_SP", "src");
        hashMap.put("sv", "swe");
        hashMap.put("tl_PH", "fil");
        hashMap.put("zu_ZA", "zul");
    }

    /**
     * 国际语言编码转成百度编码
     */
    public static String toBaiduCode(String languageCode) {
        return hashMap.get(languageCode);
    }

    /**
     * 百度编码转成国际语言编码
     */
    public static String toLanguageCode(String baiduCode) {
        for (Map.Entry<String, String> entry : hashMap.entrySet()) {
            if (entry.getValue().equals(baiduCode)) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * 翻译
     *
     * @param appid 百度翻译的appid
     * @param key   百度翻译的key
     * @param q     要翻译的文本
     * @param from  源语言
     * @param to    目标语言
     * @return 翻译后的文本
     */
    @SneakyThrows
    public static String translate(
            String q,
            String from,
            String to,
            String appid,
            String key) {
        try {
            try{
                if(StrUtil.isEmpty(from)){
                    // 如果from为空，则先进行语言检测
                    from = BaiduTranslateUtil.detectLanguage(q, appid, key);
                }
            } catch (Exception e) {
                // 不处理
            }

            String uri = "https://fanyi-api.baidu.com/api/trans/vip/translate";

            String salt = UUID.randomUUID().toString().replaceAll("-", "");

            // 签名
            String sign = MD5.create().digestHex(appid + q + salt + key, "UTF-8");

            // 参数,要url转换
            String param = "?q=" + URLEncoder.encode(q, "UTF-8") + "&from=" + from + "&to=" + to + "&appid=" + appid + "&salt=" + salt + "&sign=" + sign;

            String url = uri + param;

            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url(url)
                    .get()
                    .build();

            okhttp3.Response response = new okhttp3.OkHttpClient().newCall(request).execute();
            String text = response.body().string();
            // 获取trans_result中的第1个dst
            JSONObject jsonObject = JSON.parseObject(text);
            JSONArray transResult = jsonObject.getJSONArray("trans_result");
            if (transResult == null || transResult.size() == 0) {
                return BaiduTranslateUtil.translate(q, from, "en", appid, key);
            }

            String result = transResult.getJSONObject(0).getString("dst");
            if (to.equals("yue")) {
                return BaiduTranslateUtil.translate(result, "zh", "cht", appid, key);
            }
            return result;
        } catch (Exception e) {
            return BaiduTranslateUtil.translateThrow(q, from, "en", appid, key);
        }

    }

    public static String translateThrow(String q, String from, String to, String appid, String key) {
        for (int i = 0; i < 100; i++) {
            try {
                Thread.sleep(500);
                return translate(q, from, to, appid, key);
            } catch (Exception e) {
                i++;
            }
        }
        throw BaseException.of("translateError", "Translation failed");
    }


    /**
     * 语种识别
     *
     * @param q     要识别的文本
     * @param appid 百度翻译的appid
     * @param key   百度翻译的key
     * @return 识别后的语言编码
     */
    public static String detectLanguage(String q, String appid, String key) {
        String uri = "https://fanyi-api.baidu.com/api/trans/vip/language";

        String salt = UUID.randomUUID().toString().replaceAll("-", "");

        // 签名
        String sign = MD5.create().digestHex(appid + q + salt + key, "UTF-8");

        // 参数,要url转换
        String param = null;
        try {
            param = "?q=" + URLEncoder.encode(q, "UTF-8") + "&appid=" + appid + "&salt=" + salt + "&sign=" + sign;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        String url = uri + param;

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .get()
                .build();


        okhttp3.Response response = null;
        try {
            response = new okhttp3.OkHttpClient().newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String responseBody = null;
        try {
            responseBody = response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JSONObject jsonObject = JSON.parseObject(responseBody);
        String errorCode = jsonObject.getString("error_code");
        if("52003".equals(errorCode)){
            throw ThirdpartyBaiduTranslateException.CODE_52003;
        }

        if ("0".equals(errorCode)) {
            String string = jsonObject.getJSONObject("data").getString("src");
            if (string == null || string.isEmpty()) {
                throw BaseException.of("languageDetectError", "Language detection failed","en");
            }
            String languageCode = toLanguageCode(string);
            if (languageCode == null) {
                throw BaseException.of("languageDetectError", "Language detection failed","en");
            }
            return languageCode;
        }
        return detectLanguageThrow(q, appid, key);
    }

    public static String detectLanguageThrow(String q, String appid, String key) {
        for (int i = 0; i < 100; i++) {
            try {
                Thread.sleep(500);
                return detectLanguage(q, appid, key);
            } catch (Exception e) {
                i++;
            }
        }
        throw BaseException.of("languageDetectError", "Language detection failed");
    }

}
