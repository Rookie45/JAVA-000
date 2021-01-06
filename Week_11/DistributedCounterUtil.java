package com.github.rookie45.distributedredis.util;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.ScriptOutputType;
import io.lettuce.core.api.sync.RedisCommands;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicReference;

public class DistributedCounterUtil {

    private static final String DECR_LUA = "if tonumber(redis.call('get',KEYS[1])) > 0 then " +
            "redis.call('decr',KEYS[1]) return 1 else return 0 end;";

    private static AtomicReference<String> LUA_DECR = new AtomicReference<>();

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
        if (null == currValue || 1 > Integer.valueOf(currValue)) {
            redisCommands.set(key, String.valueOf(value));
        }
    }

    public boolean decrease(String key) {
        LUA_DECR.compareAndSet(null, redisCommands.scriptLoad(DECR_LUA));
        return redisCommands.evalsha(LUA_DECR.get(), ScriptOutputType.BOOLEAN, key);
    }

}
