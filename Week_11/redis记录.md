redis的lua脚本格式

```lua
redis 127.0.0.1:6379> EVAL script numkeys key [key ...] arg [arg ...] 
```

- **script**： 参数是一段 Lua 5.1 脚本程序。脚本不必(也不应该)定义为一个 Lua 函数。
- **numkeys**： 用于指定键名参数的个数。
- **key [key ...]**： 从 EVAL 的第三个参数开始算起，表示在脚本中所用到的那些 Redis 键(key)，这些键名参数可以在 Lua 中通过全局变量 KEYS 数组，用 1 为基址的形式访问( KEYS[1] ， KEYS[2] ，以此类推)。
- **arg [arg ...]**： 附加参数，在 Lua 中通过全局变量 ARGV 数组访问，访问的形式和 KEYS 变量类似( ARGV[1] 、 ARGV[2] ，诸如此类)。

举例：

```lua
eval "if redis.call('setnx',KEYS[1],ARGV[1]) == 1 then redis.call('expire',KEYS[1],ARGV[2]) return 1 else return 0 end" 2 "key1" "key1" "value1" "60"

eval "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end" 1 "lock1" "thread-1"
```



```java
io.lettuce.core.api.sync.RedisCommands类调用lua脚本的函数
<T> T evalsha(String digest, ScriptOutputType type, K[] keys, V... values);
这里需要注意的是keys是数组，如果不以数组传入，则调用的是下面这个函数。
<T> T evalsha(String digest, ScriptOutputType type, K... keys);
```



直接执行`del mylock` 会导致 **释放了不该释放的锁** ，如下举例：

|        |                                |                                           |                                    |
| :----: | ------------------------------ | ----------------------------------------- | ---------------------------------- |
| 时间线 | 线程1                          | 线程2                                     | 线程3                              |
| 时刻1  | 执行 setnx mylock val1 加锁    | 执行 setnx mylock val2 加锁               | 执行 setnx mylock val2 加锁        |
| 时刻2  | 加锁成功                       | 加锁失败                                  | 加锁失败                           |
| 时刻3  | 执行任务...                    | 尝试加锁...                               | 尝试加锁...                        |
| 时刻4  | 任务继续（锁超时，自动释放了） | setnx 获得了锁（因为线程1的锁超时释放了） | 仍然尝试加锁...                    |
| 时刻5  | 任务完毕，del mylock 释放锁    | 执行任务中...                             | 获得了锁（因为线程1释放了线程2的） |
|  ...   |                                |                                           |                                    |



**参考**

[Lua脚本在redis分布式锁场景的运用](https://www.cnblogs.com/demingblog/p/9542124.html)

