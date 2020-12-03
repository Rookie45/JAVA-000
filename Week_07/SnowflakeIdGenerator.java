package com.sl.homework.week0701.idgenerator;

import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 53 bits unique id:
 * <p>
 * |--------|--------|--------|--------|--------|--------|--------|--------|
 * |00000000|00011111|11111111|11111111|11111111|11111111|11111111|11111111|
 * |--------|---xxxxx|xxxxxxxx|xxxxxxxx|xxxxxxxx|xxx-----|--------|--------|
 * |--------|--------|--------|--------|--------|---xxxxx|xxxxxxxx|xxx-----|
 * |--------|--------|--------|--------|--------|--------|--------|---xxxxx|
 * <p>
 * Maximum ID = 11111_11111111_11111111_11111111_11111111_11111111_11111111
 * <p>
 * Maximum TS = 11111_11111111_11111111_11111111_111
 * <p>
 * Maximum NT = ----- -------- -------- -------- ---11111_11111111_111 = 65535
 * <p>
 * Maximum SH = ----- -------- -------- -------- -------- -------- ---11111 = 31
 * <p>
 * It can generate 64k unique id per IP and up to 2106-02-07T06:28:15Z.
 */
@Slf4j
public class SnowflakeIdGenerator {

    private static final String HOSTNAME_REGEX = "^.*\\D+([0-9]+)$";

    private static final Pattern PATTERN_HOSTNAME = Pattern.compile(HOSTNAME_REGEX);

    private static final long OFFSET = LocalDate.of(2000, 1, 1).atStartOfDay(ZoneId.of("Z")).toEpochSecond();

    private static final long MAX_NEXT = 0b11111_11111111_111L;

    private static final long SHARD_ID = getServerIdAsLong();

    private static long offset = 0;

    private static long lastEpoch = 0;

    /**
     * 根据当前机器时间生成id，以秒为精度
     * @return id
     */
    public static long nextId() {
        return nextId(System.currentTimeMillis() / 1000);
    }

    private static synchronized long nextId(long epochSecond) {
        if (epochSecond < lastEpoch) {
            epochSecond = lastEpoch;
            log.warn("[IdGenerator][nextId] time go back.");
        }

        if (lastEpoch != epochSecond) {
            //记录最新时间戳，并重置自增
            lastEpoch = epochSecond;
            reset();
        }

        offset++;
        long next = offset & MAX_NEXT;
        if (0 == next) {
            return nextId(epochSecond + 1);
        }
        return generateId(epochSecond, next, SHARD_ID);
    }

    private static void reset() {
        offset = 0;
    }

    /**
     * 生成id
     * @param epochSecond 32位时间戳，2^32/3600/24/365=136年，1970+136=2106
     * @param next 16位自增，这也说明1秒，1台机器支持65535个不重复序列号
     * @param shareId 5位机器标识，支持0-31，也就是32台
     * @return id
     */
    private static long generateId(long epochSecond, long next, long shareId) {
        // 32位时间戳 + 16位自增 + 5位机器标识
        return ((epochSecond - OFFSET) << 21) | (next << 5) | shareId;
    }

    /**
     * 获取主机名中的数值
     * @return 主机名对应的机器标识
     */
    private static long getServerIdAsLong() {
        try {
            String hostName = InetAddress.getLocalHost().getHostName();
            Matcher matcher = PATTERN_HOSTNAME.matcher(hostName);
            if (matcher.matches()) {
                long n = Long.parseLong(matcher.group(1));
                if (n >= 0 && n < 32) {
                    log.info("[IdGenerator][getServerIdAsLong] convert hostname {} to server id {}",
                            hostName, n);
                    return n;
                }
            }
        } catch (UnknownHostException e) {
            log.error("[IdGenerator][getServerIdAsLong] acquire hostname failed.", e);
        }
        return 0;
    }
}
