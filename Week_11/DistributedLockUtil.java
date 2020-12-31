package com.github.rookie45.distributedredis.util;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.ScriptOutputType;
import io.lettuce.core.SetArgs;
import io.lettuce.core.api.sync.RedisCommands;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicReference;

public class DistributedLockUtil {

    private static final String SUCCESS = "OK";

    // 过期时间单位为毫秒
    private static final long DEFAULT_EXPIRE_TIME = 10000L;

    private static final String LOCK_LUA = "if redis.call('setnx',KEYS[1],ARGV[1]) == 1 then " +
            "redis.call('expire',KEYS[1],ARGV[2]) return 1 else return 0 end;";

    private static final String UNLOCK_LUA = "if redis.call('get',KEYS[1]) == ARGV[1] then " +
            "return redis.call('del',KEYS[1]) else return 0 end;";

    private static AtomicReference<String> LUA_SHA = new AtomicReference<>();

    private static RedisCommands<String, String> redisCommands; 
    
    static {
        RedisURI redisURI = RedisURI.builder()
                .withHost("localhost")
                .withPort(6379)
                .withTimeout(Duration.of(60, ChronoUnit.SECONDS))
                .build();
        redisCommands = RedisClient.create(redisURI).connect().sync();
    }
    
    public boolean lock(String lockKey, String lockValue) throws InterruptedException {
        return lock(lockKey, lockValue, DEFAULT_EXPIRE_TIME);
    }

    public boolean lock(String lockKey, String lockValue, long expireTime) throws InterruptedException {
        // 省略参数校验
        return lock(lockKey, lockValue, expireTime, 0, 0L);
    }

    public boolean lock(String lockKey, String lockValue, long expireTime, int retry, long interval) throws InterruptedException {
        // 省略参数校验
        SetArgs args = SetArgs.Builder.nx().px(expireTime);
        while (retry >= 0) {
            if (SUCCESS.equals(redisCommands.set(lockKey, lockValue, args))) {
                return true;
            }
            --retry;
            // 间隔时间单位为毫秒
            Thread.sleep(interval);
        }
        return false;
    }

    public boolean lockWithLua(String lockKey, String lockValue, long expireTime){
        LUA_SHA.compareAndSet(null, redisCommands.scriptLoad(LOCK_LUA));
        return redisCommands.evalsha(LUA_SHA.get(), ScriptOutputType.BOOLEAN, lockKey, lockValue, String.valueOf(expireTime));
    }

    public boolean unLock(String lockKey, String lockValue) {
        LUA_SHA.compareAndSet(null, redisCommands.scriptLoad(UNLOCK_LUA));
        return redisCommands.evalsha(LUA_SHA.get(), ScriptOutputType.BOOLEAN, lockKey, lockValue);
    }
}
