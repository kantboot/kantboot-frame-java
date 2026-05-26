package com.kantboot.officical.plugin.tool.ip;


import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.kantboot.thirdparty.alicloud.market.service.IThirdpartyAlicloudmarketRequestService;
import com.kantboot.tool.area.domain.entity.ToolArea;
import com.kantboot.tool.area.service.IToolAreaService;
import com.kantboot.tool.ip.domain.entity.ToolIp;
import com.kantboot.tool.ip.slot.ToolIpSlot;
import com.kantboot.util.rest.exception.BaseException;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * 工具区域插件的插槽类
 */
@Configuration
public class OfficialPluginOfToolIpSlot {

    @Resource
    private IThirdpartyAlicloudmarketRequestService requestService;

    @Resource
    private IToolAreaService areaService;


    @Bean
    public ToolIpSlot toolIpSlot() {
        return new ToolIpSlot() {

            @Override
            public ToolIp getByIp(String ip) {
                Object obj = requestService.execute( "jmipquery3.ipSearch", Map.of("ip", ip));
                JSONObject result = JSONObject.from(obj);
                System.err.println(result.toJSONString());
                if (!result.getBoolean("success")) {
                    String errMsg = result.getString("msg");
                    if (errMsg == null || errMsg.isEmpty()) {
                        errMsg = "查询IP信息失败";
                    }
                    throw BaseException.of("ipSearchError:"+result.getString("code"), errMsg, "zh_CN");
                }

                JSONObject data = result.getJSONObject("data");

                ToolIp toolIp = new ToolIp();
                toolIp.setIp(data.getString("ip"));

                String countryCode = data.getString("areaCode");
                System.err.println(countryCode);
                String miniCode = data.getString("code");
                if (StrUtil.isNotEmpty(miniCode)) {
                    try {
                        toolIp.setAreaCode(areaService.getByMinCode(miniCode).getCode());
                    } catch (Exception e) {

                    }
                }else if( StrUtil.isEmpty(toolIp.getAreaCode())&&StrUtil.isNotEmpty(countryCode)) {
                    if(countryCode.length()==2) {
                        ToolArea byAlpha2Code = areaService.getByAlpha2Code(countryCode);
                        if(byAlpha2Code!=null) {
                            toolIp.setAreaCode(byAlpha2Code.getCode());
                        }
                    }
                    else if(countryCode.length()==3) {
                        ToolArea byAlpha3Code = areaService.getByAlpha3Code(countryCode);
                        if(byAlpha3Code!=null) {
                            toolIp.setAreaCode(byAlpha3Code.getCode());
                        }
                    }
                }

                toolIp.setIsp(data.getString("isp"));
                toolIp.setLv0Name(data.getString("nation"));
                toolIp.setTimeZone(data.getString("time_zone"));
                toolIp.setZipCode(data.getString("zip_code"));
                toolIp.setWeatherStation(data.getString("weather_station"));
                toolIp.setLv1Name(data.getString("province"));
                toolIp.setLv2Name(data.getString("city"));
                toolIp.setLongitude(data.getBigDecimal("longitude"));
                toolIp.setLatitude(data.getBigDecimal("latitude"));
                toolIp.setElevation(data.getString("elevation"));
                toolIp.setDataOfRequest(result.toString());
                toolIp.setSourceLanguageCode("zh_CN");

                String province = toolIp.getLv1Name();
                if("中国香港".equals(province)){
                    toolIp.setAreaCode("HKG");
                }else if("中国澳门".equals(province)){
                    toolIp.setAreaCode("MAC");
                } else if("中国台湾".equals(province)){
                    toolIp.setAreaCode("TWN");
                }

                return toolIp;
            }

        };
    }


}
