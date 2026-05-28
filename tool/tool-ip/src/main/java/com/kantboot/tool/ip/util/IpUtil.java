package com.kantboot.tool.ip.util;

public class IpUtil {

    /**
     * 判断IP格式是否正确
     * 要IpV4和IpV6都能通过
     */
    public static boolean isValidIp(String ip) {
        if (ip == null || ip.isEmpty()) {
            return false;
        }
        String ipv4Pattern =
            "^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
            "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
            "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
            "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        String ipv6Pattern =
            "^([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$|" +
            "^([0-9a-fA-F]{1,4}:){1,7}:$|" +
            "^([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}$|" +
            "^([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}$|" +
            "^([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}$|" +
            "^([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}$|" +
            "^([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}$|" +
            "^[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})$|" +
            "^:((:[0-9a-fA-F]{1,4}){1,7}|:)$";

        return ip.matches(ipv4Pattern) || ip.matches(ipv6Pattern);
    }


}
