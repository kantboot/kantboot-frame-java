package com.kantboot.user.account.util;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class SnowflakeConverter {
    // 全大写字符集（排除0/O/I）
    private static final String BASE32_ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZ";
    private static final int BASE = BASE32_ALPHABET.length();
    private static final Map<Character, Integer> CHAR_INDEX_MAP = new HashMap<>();

    static {
        // 预生成字符索引映射
        for (int i = 0; i < BASE32_ALPHABET.length(); i++) {
            CHAR_INDEX_MAP.put(BASE32_ALPHABET.charAt(i), i);
        }
    }

    public static String snowflakeToInvite(BigInteger snowflakeId) {
        if (snowflakeId.compareTo(BigInteger.ZERO) < 0) {
            throw new IllegalArgumentException("ID必须为非负数");
        }

        StringBuilder sb = new StringBuilder();
        BigInteger base = BigInteger.valueOf(BASE);

        while (snowflakeId.compareTo(BigInteger.ZERO) > 0) {
            BigInteger[] divRem = snowflakeId.divideAndRemainder(base);
            sb.append(BASE32_ALPHABET.charAt(divRem[1].intValue()));
            snowflakeId = divRem[0];
        }

        return sb.length() == 0 ?
                String.valueOf(BASE32_ALPHABET.charAt(0)) :
                sb.reverse().toString();
    }

    public static BigInteger inviteToSnowflake(String inviteCode) {
        BigInteger result = BigInteger.ZERO;
        BigInteger base = BigInteger.valueOf(BASE);

        for (char c : inviteCode.toCharArray()) {
            Integer index = CHAR_INDEX_MAP.get(c);
            if (index == null) {
                throw new IllegalArgumentException("无效字符: " + c);
            }
            result = result.multiply(base).add(BigInteger.valueOf(index));
        }

        return result;
    }

}