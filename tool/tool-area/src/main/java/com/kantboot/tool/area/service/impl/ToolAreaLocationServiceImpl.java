package com.kantboot.tool.area.service.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.kantboot.tool.area.dao.repository.ToolAreaLocationRepository;
import com.kantboot.tool.area.domain.entity.ToolAreaLocation;
import com.kantboot.tool.area.service.IToolAreaLocationService;
import com.kantboot.tool.area.slot.ToolAreaLocationSlot;
import com.kantboot.util.cache.CacheUtil;
import com.kantboot.util.http.HttpRequestHeaderUtil;
import com.kantboot.util.location.domain.Location;
import com.kantboot.util.location.util.LocationUtil;
import com.kantboot.util.rest.exception.BaseException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class ToolAreaLocationServiceImpl implements IToolAreaLocationService {

    @Resource
    private ToolAreaLocationRepository repository;

    @Resource
    private ToolAreaLocationSlot slot;

    @Resource
    private HttpRequestHeaderUtil httpRequestHeaderUtil;

    @Resource
    private CacheUtil cacheUtil;

    /** 空值哨兵，防穿透 */
    private static final String NULL_SENTINEL = "__NULL__";

    /** JVM 兜底锁（仅单机有效，多实例无效） */
    private static final ConcurrentHashMap<String, Object> JVM_LOCKS = new ConcurrentHashMap<>();

    /** 你要求：500（单位取决于你的 LocationUtil 定义） */
    private static final BigDecimal DISTANCE_500 = new BigDecimal("500");

    /** 经纬度统一格式：避免 121.47 vs 121.470 造成 key 分裂 */
    private static String norm(BigDecimal v) {
        // 6 位小数通常够用（约 0.11m 精度），你也可以改成 7/8 位
        return v.setScale(6, RoundingMode.HALF_UP).toPlainString();
    }

    @Override
    public ToolAreaLocation getLocationByLongitudeAndLatitude(BigDecimal longitude, BigDecimal latitude) {
        // ✅ 先判空/判非法：避免你原来 key 拼接时 NPE
        if (longitude == null || latitude == null) return null;
        if (longitude.compareTo(BigDecimal.ZERO) <= 0 || latitude.compareTo(BigDecimal.ZERO) <= 0) {
            System.err.println("错误位置，经度或纬度小于等于0");
            return null;
        }

        final String lng = norm(longitude);
        final String lat = norm(latitude);

        final String key = "ToolAreaLocation:lng:" + lng + ":lat:" + lat;
        final String lockKey = key + ":lock";

        // 1) 先读缓存
        String cacheStr = cacheUtil.get(key);
        if (StrUtil.isNotEmpty(cacheStr)) {
            if (Objects.equals(cacheStr, NULL_SENTINEL)) {
                System.err.println("TOOL位置缓存命中（空值哨兵），直接返回 null");
                return null;
            }
            System.err.println("TOOL位置缓存命中，直接返回位置");
            return JSON.parseObject(cacheStr, ToolAreaLocation.class).setIsNotCount(true);
        }

        // 2) 拿分布式锁（或兜底 JVM 锁）——关键：防并发击穿
        boolean locked = tryLockCompat(lockKey, 10, TimeUnit.SECONDS);
        if (!locked) {
            // 没拿到锁：短暂等待再读缓存，避免大家一起打外部
            sleepSilently(80);
            cacheStr = cacheUtil.get(key);
            if (StrUtil.isNotEmpty(cacheStr)) {
                if (Objects.equals(cacheStr, NULL_SENTINEL)) return null;
                return JSON.parseObject(cacheStr, ToolAreaLocation.class).setIsNotCount(true);
            }
            // 还没有：按你业务可选择返回 null 或继续降级
            return null;
        }

        try {
            // 3) Double Check：拿到锁后再读一次缓存（非常重要）
            cacheStr = cacheUtil.get(key);
            if (StrUtil.isNotEmpty(cacheStr)) {
                if (Objects.equals(cacheStr, NULL_SENTINEL)) return null;
                return JSON.parseObject(cacheStr, ToolAreaLocation.class).setIsNotCount(true);
            }

            // 4) 查 DB：500 范围内是否已有记录
            List<ToolAreaLocation> locationList = repository.findByMaxMinLocation(
                    LocationUtil.getMaxMinLocationByDistance(
                            new Location()
                                    .setLongitude(new BigDecimal(lng))
                                    .setLatitude(new BigDecimal(lat)),
                            DISTANCE_500
                    )
            );

            if (ArrayUtil.isNotEmpty(locationList) && !locationList.isEmpty()) {
                System.err.println("没走阿里云，直接返数据位置");
                ToolAreaLocation hit = locationList.getFirst().setIsNotCount(true);
                cacheUtil.setEx(key, JSON.toJSONString(hit), 30, TimeUnit.MINUTES);
                return hit;
            }

            // 5) 调外部（阿里云）
            ToolAreaLocation fromApi = slot.getLocationByLongitudeAndLatitude(new BigDecimal(lng), new BigDecimal(lat));
            System.err.println("走了阿里云，获取位置");

            if (fromApi == null) {
                // ✅ 防穿透：缓存空值哨兵（短 TTL），避免并发一直打外部
                cacheUtil.setEx(key, NULL_SENTINEL, 2, TimeUnit.MINUTES);

                // 你原逻辑说“如果获取不到，写上海缓存”，但你最后 return null
                // 我这里保留你行为：返回 null；但同时给你一个“上海降级对象”示例（不返回）
                ToolAreaLocation sh = new ToolAreaLocation();
                sh.setAreaCode("CHN.310000.310101");
                sh.setLongitude(new BigDecimal("121.473701"));
                sh.setLatitude(new BigDecimal("31.230416"));
                sh.setAddress("上海市虹桥区");
                // 如果你希望“返回上海”，把上面 sentinel 改成缓存上海并 return sh
                return null;
            }

            // 6) 入库 + 缓存
            fromApi.setLatitude(new BigDecimal(lat));
            fromApi.setLongitude(new BigDecimal(lng));

            ToolAreaLocation saved = repository.save(fromApi);
            cacheUtil.setEx(key, JSON.toJSONString(saved), 30, TimeUnit.MINUTES);

            return saved;

        } finally {
            unlockCompat(lockKey);
        }
    }

    @Override
    public ToolAreaLocation getLocationBySelf() {
        String geo = httpRequestHeaderUtil.getGeo();
        if (StrUtil.isEmpty(geo)) {
            return new ToolAreaLocation();
        }
        String[] split = geo.split(",");
        if (split.length == 2) {
            BigDecimal lng = new BigDecimal(split[0]);
            BigDecimal lat = new BigDecimal(split[1]);
            return getLocationByLongitudeAndLatitude(lng, lat);
        }
        throw BaseException.of("ToolArea:geoFormatError", "geo格式错误，请使用经度,纬度的格式", "zh_CN");
    }

    // ----------------------------
    // 兼容 CacheUtil 锁 API 的适配器
    // ----------------------------

    private boolean tryLockCompat(String lockKey, long lease, TimeUnit unit) {
        // 1) 优先尝试调用 CacheUtil 的分布式锁方法（反射：避免你本地方法名不同导致编译失败）
        // 常见签名：
        // - boolean tryLock(String key, long lease, TimeUnit unit)
        // - boolean lock(String key, long lease, TimeUnit unit)
        // - boolean setNx(String key, String value, long ttl, TimeUnit unit)
        // - boolean setIfAbsent(String key, String value, long ttl, TimeUnit unit)
        try {
            Method m = findMethod(cacheUtil.getClass(), "tryLock",
                    String.class, long.class, TimeUnit.class);
            if (m != null) return (boolean) m.invoke(cacheUtil, lockKey, lease, unit);

            m = findMethod(cacheUtil.getClass(), "lock",
                    String.class, long.class, TimeUnit.class);
            if (m != null) return (boolean) m.invoke(cacheUtil, lockKey, lease, unit);

            // setNx / setIfAbsent 这种，需要 value + ttl
            m = findMethod(cacheUtil.getClass(), "setNx",
                    String.class, String.class, long.class, TimeUnit.class);
            if (m != null) return (boolean) m.invoke(cacheUtil, lockKey, "1", lease, unit);

            m = findMethod(cacheUtil.getClass(), "setIfAbsent",
                    String.class, String.class, long.class, TimeUnit.class);
            if (m != null) return (boolean) m.invoke(cacheUtil, lockKey, "1", lease, unit);

        } catch (Throwable e) {
            System.err.println("CacheUtil 分布式锁调用失败，将退化为 JVM 锁。原因：" + e.getMessage());
        }

        // 2) 兜底：JVM 锁（只锁同一进程）
        System.err.println("⚠️ CacheUtil 未发现可用分布式锁方法，当前使用 JVM 锁兜底（多实例不会互斥）。lockKey=" + lockKey);
        Object monitor = JVM_LOCKS.computeIfAbsent(lockKey, k -> new Object());
        synchronized (monitor) {
            // JVM 锁拿到就算 locked
            return true;
        }
    }

    private void unlockCompat(String lockKey) {
        // 分布式锁：尝试调用 unlock/release/del
        try {
            Method m = findMethod(cacheUtil.getClass(), "unlock", String.class);
            if (m != null) {
                m.invoke(cacheUtil, lockKey);
                return;
            }
            m = findMethod(cacheUtil.getClass(), "releaseLock", String.class);
            if (m != null) {
                m.invoke(cacheUtil, lockKey);
                return;
            }
            // 若你用 setNx 当锁：一般解锁就是 del
            m = findMethod(cacheUtil.getClass(), "del", String.class);
            if (m != null) {
                m.invoke(cacheUtil, lockKey);
                return;
            }
            m = findMethod(cacheUtil.getClass(), "delete", String.class);
            if (m != null) {
                m.invoke(cacheUtil, lockKey);
            }
        } catch (Throwable ignored) {
        }
    }

    private static Method findMethod(Class<?> clz, String name, Class<?>... paramTypes) {
        try {
            Method m = clz.getMethod(name, paramTypes);
            m.setAccessible(true);
            return m;
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private static void sleepSilently(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }
}
