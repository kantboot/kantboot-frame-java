package com.kantboot.official.plugin.tool.area;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.kantboot.thirdparty.alicloud.market.service.IThirdpartyAlicloudmarketRequestService;
import com.kantboot.tool.area.domain.entity.ToolArea;
import com.kantboot.tool.area.domain.entity.ToolAreaLocation;
import com.kantboot.tool.area.service.IToolAreaService;
import com.kantboot.tool.area.slot.ToolAreaLocationSlot;
import com.kantboot.util.rest.exception.BaseException;
import io.netty.util.internal.StringUtil;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 工具区域插件的插槽类
 */
@Configuration
public class OfficialPluginOfToolAreaLocationSlot {

    @Resource
    private IThirdpartyAlicloudmarketRequestService requestService;

    @Resource
    private IToolAreaService areaService;


    @Bean
    public ToolAreaLocationSlot toolAreaLocationSlot() {
        return new ToolAreaLocationSlot() {

            @Override
            public ToolAreaLocation getLocationByLongitudeAndLatitude(BigDecimal longitude, BigDecimal latitude) {
                Object location =
                        requestService.execute("jmgeocode.regeoQuery", Map.of("location", longitude.toString() + "," + latitude.toString()));
                JSONObject jsonObject = JSONObject.parseObject(location.toString());
                if (jsonObject.getInteger("code") != 200) {
                    String errMsg = jsonObject.getString("errMsg");
                    if(StrUtil.isNotEmpty(errMsg)){
                        errMsg = jsonObject.getString("msg");
                    }
                    throw BaseException.of("toolAreaSlotNotUseMapPlugin:"+jsonObject.get("code"),
                            errMsg, "zh_CN");
                }
                JSONObject data = jsonObject.getJSONObject("data");
                JSONObject bodyData = new JSONObject();
                try{
                    JSONArray regeocodes = data.getJSONArray("regeocodes");
                    bodyData = regeocodes.getJSONObject(0);
                } catch (Exception e) {
                    throw BaseException.of("toolAreaSlotNotUseMapPlugin:parseError",
                            "解析返回数据失败，请检查数据格式", "zh_CN");
                }
                String address = bodyData.getString("formatted_address");
                String minCode = bodyData.getJSONObject("addressComponent").getString("adcode");
                if(StrUtil.isEmpty(minCode)){
                    throw BaseException.of("toolAreaSlotNotUseMapPlugin:parseError",
                            "未能获取此位置", "zh_CN");
                }


                ToolArea byCode = areaService.getByMinCode(minCode);
                if(byCode==null){
                    return null;
                }

                ToolAreaLocation toolAreaLocation = new ToolAreaLocation();
                toolAreaLocation.setAddress(address);
                if(byCode.getCode()!=null){
                    toolAreaLocation.setAreaCode(byCode.getCode());
                }
                toolAreaLocation.setDataOfRequest(jsonObject.toString());
                return toolAreaLocation;
            }

        };
    }


}
