package com.github.rookie45.distributedredis.util;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.sync.RedisCommands;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class DistributedCounterUtil {
    private static RedisCommands<String, String> redisCommands;

    static {
        RedisURI redisURI = RedisURI.builder()
                .withHost("localhost")
                .withPort(6379)
                .withTimeout(Duration.of(60, ChronoUnit.SECONDS))
                .build();
        redisCommands = RedisClient.create(redisURI).connect().sync();
    }

    public void setCounter(String key, long value) {
        String currValue = redisCommands.get(key);
        if (null == currValue || 0 > Integer.valueOf(currValue)) {
            redisCommands.set(key, String.valueOf(value));
        }
    }

    public boolean decrease(String key) {
        return 0 < redisCommands.decr(key);
    }

}
