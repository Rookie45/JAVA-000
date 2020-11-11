**题目：**

1、把示例代码，运行一遍，思考课上相关的问题。也可以做一些比较。

**答题如下：**

> 已经存在synchronized锁机制，为什么JDK还需要再实现一个显示的锁Lock？

原因：synchronized方式存在如下问题，所以显示提供Lock方式解决这些问题

- 同步块的阻塞无法中断，不能interruptibly
- 同步块的阻塞无法控制超时，无法自动解锁
- 同步块无法异步处理锁，不能立即知道是否可以拿到锁
- 同步块无法根据条件灵活的加锁解锁

> Lock性能比synchronized高吗？

在Java1.5中及之前，synchronized是一个重量级操作，性能不如Lock，在之后的版本对synchronized有性能优化，到java1.9，两者性能基本持平。官方还是建议使用synchronized，因为对它的优化是持续的。不过synchronized的适用场景相对简单的逻辑可以使用它，而对于复杂的同步场景，还是建议使用Lock（ReentrantLocK）。

> 什么是可重入锁？

可重入锁又名递归锁，是指在同一个线程在外层方法获取锁的时候，在进入内层方法会自动获取锁，能够一定程度防止线程阻塞。

> 什么是公平锁？

公平锁意味着排队靠前的优先，非公平锁则是都是同样机会。

> 为什么unpark需要一个线程作为入参？

因为一个park线程，无法自己唤醒自己，所以需要其他线程来唤醒，其他线程通过这个入参知道唤醒哪个线程。

> 到底有锁好，还是无锁好？

写比较频繁时，无锁的自旋重试过多，此时有锁要好些；而读多写少，无锁更好，毕竟锁的开销更大。

> 分段思想改进原子类，还有哪些使用这个思想？

快排，G1 GC，ConcurrentHashMap

> 多线程之间如何互相协作？

- wait/notify
- Lock/Condition
- Semaphore
- CountdownLatch
- CyclicBarrier



**题目：**

2、思考有多少种方式，在main函数启动一个新线程，运行一个方法，拿到这个方法的返回值后，退出主线程？写出你的方法，越多越好，提交到github。

**答题如下：**

[Homework03.java](https://github.com/Rookie45/JAVA-000/blob/main/Week_04/Homework03.java)



参考：

[java进阶训练营](https://u.geekbang.org/subject/java/1000579?utm_source=u_list_web&utm_medium=u_list_web&utm_term=u_list_web)
