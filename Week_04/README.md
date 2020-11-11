学习笔记

java并发包
===

> 并发包的路径java.util.concurrent.*，又称为JUC包，大致分为锁，原子类，线程池，工具类以及线程安全集合类

大致各类型代表罗列：

- 锁机制类：Lock，Condition，ReadWriteLock
- 原子类操作：AtomicInteger
- 线程池类：Future，Callable，Executor
- 信号量工具类：CountDownLatch, CyclicBarrier, Semaphore
- 并发集合类：CopyOnWriteArrayList, ConcurrentMap

什么是锁
===

> 锁是在执行多线程时用于强行限制资源访问的同步机制，即用于在并发控制中保证对互斥要求的满足。

### 锁类型

- 可重入锁（synchronized和ReentrantLock）：在执行对象中所有同步方法不用再次获得锁
- 可中断锁（synchronized就不是可中断锁，而Lock是可中断锁）：在等待获取锁过程中可中断
- 公平锁（ReentrantLock和ReentrantReadWriteLock）： 按等待获取锁的线程的等待时间进行获取，等待时间长的具有优先获取锁权利
- 读写锁（ReadWriteLock和ReentrantReadWriteLock）：对资源读取和写入的时候拆分为2部分处理，读的时候可以多线程一起读，写的时候必须同步地写

### Lock类型

#### 公平锁/非公平锁

- 公平锁是指多个线程按照申请锁的顺序来获取锁。
- 非公平锁是指多个线程获取锁的顺序并不是按照申请锁的顺序，有可能后申请的线程比先申请的线程优先获取锁。有可能，会造成优先级反转或者饥饿现象。
- 对于ReentrantLock而言，通过构造函数指定该锁是否是公平锁，默认是非公平锁。非公平锁的优点在于吞吐量比公平锁大。
- 对于Synchronized而言，也是一种非公平锁。由于其并不像ReentrantLock是通过AQS的来实现线程调度，所以并没有任何办法使其变成公平锁。

#### 可重入锁

- 可重入锁又名递归锁，是指在同一个线程在外层方法获取锁的时候，在进入内层方法会自动获取锁。
- 对于Java ReentrantLock而言, 他的名字就可以看出是一个可重入锁，其名字是Re entrant Lock重新进入锁。
- 对于Synchronized而言,也是一个可重入锁。可重入锁的一个好处是可一定程度避免死锁。

#### 独享锁/共享锁

- 独享锁是指该锁一次只能被一个线程所持有。
- 共享锁是指该锁可被多个线程所持有。
- 对于Java ReentrantLock而言，其是独享锁。但是对于Lock的另一个实现类ReadWriteLock，其读锁是共享锁，其写锁是独享锁。
- 读锁的共享锁可保证并发读是非常高效的，读写，写读 ，写写的过程是互斥的。
- 独享锁与共享锁也是通过AQS来实现的，通过实现不同的方法，来实现独享或者共享。
- 对于Synchronized而言，当然是独享锁。

#### 互斥锁/读写锁

- 上面讲的独享锁/共享锁就是一种广义的说法，互斥锁/读写锁就是具体的实现。
- 互斥锁在Java中的具体实现就是ReentrantLock
- 读写锁在Java中的具体实现就是ReadWriteLock

#### 乐观锁/悲观锁

- 乐观锁与悲观锁不是指具体的什么类型的锁，而是指看待并发同步的角度。
- 悲观锁认为对于同一个数据的并发操作，一定是会发生修改的，哪怕没有修改，也会认为修改。因此对于同一个数据的并发操作，悲观锁采取加锁的形式。悲观的认为，不加锁的并发操作一定会出问题。
- 乐观锁则认为对于同一个数据的并发操作，是不会发生修改的。在更新数据的时候，会采用尝试更新，不断重新的方式更新数据。乐观的认为，不加锁的并发操作是没有事情的。
- 从上面的描述我们可以看出，悲观锁适合写操作非常多的场景，乐观锁适合读操作非常多的场景，不加锁会带来大量的性能提升。
- 悲观锁在Java中的使用，就是利用各种锁。
- 乐观锁在Java中的使用，是无锁编程，常常采用的是CAS算法，典型的例子就是原子类，通过CAS自旋实现原子操作的更新。

#### 分段锁

- 分段锁其实是一种锁的设计，并不是具体的一种锁，对于ConcurrentHashMap而言，其并发的实现就是通过分段锁的形式来实现高效的并发操作。
- 我们以ConcurrentHashMap来说一下分段锁的含义以及设计思想，ConcurrentHashMap中的分段锁称为Segment，它即类似于HashMap(JDK7与JDK8中HashMap的实现)的结构，即内部拥有一个Entry数组，数组中的每个元素又是一个链表;同时又是一个ReentrantLock(Segment继承了ReentrantLock)。
- 当需要put元素的时候，并不是对整个hashmap进行加锁，而是先通过hashcode来知道他要放在那一个分段中，然后对这个分段进行加锁，所以当多线程put的时候，只要不是放在一个分段中，就实现了真正的并行的插入。
- 但是，在统计size的时候，可就是获取hashmap全局信息的时候，就需要获取所有的分段锁才能统计。
- 分段锁的设计目的是细化锁的粒度，当操作不需要更新整个数组的时候，就仅仅针对数组中的一项进行加锁操作。

#### 偏向锁/轻量级锁/重量级锁

- 这三种锁是指锁的状态，并且是针对Synchronized。在Java 5通过引入锁升级的机制来实现高效Synchronized。这三种锁的状态是通过对象监视器在对象头中的字段来表明的。
- 偏向锁是指一段同步代码一直被一个线程所访问，那么该线程会自动获取锁。降低获取锁的代价。
- 轻量级锁是指当锁是偏向锁的时候，被另一个线程所访问，偏向锁就会升级为轻量级锁，其他线程会通过自旋的形式尝试获取锁，不会阻塞，提高性能。
- 重量级锁是指当锁为轻量级锁的时候，另一个线程虽然是自旋，但自旋不会一直持续下去，当自旋一定次数的时候，还没有获取到锁，就会进入阻塞，该锁膨胀为重量级锁。重量级锁会让其他申请的线程进入阻塞，性能降低。

#### 自旋锁

- 在Java中，自旋锁是指尝试获取锁的线程不会立即阻塞，而是采用循环的方式去尝试获取锁，这样的好处是减少线程上下文切换的消耗，缺点是循环会消耗CPU。
- 我们知道，java线程其实是映射在内核之上的，线程的挂起和恢复会极大的影响开销. 并且jdk官方人员发现，很多线程在等待锁的时候，在很短的一段时间就获得了锁，所以它们在线程等待的时候，并不需要把线程挂起，而是让他无目的的循环，一般设置10次。 这样就避免了线程切换的开销，极大的提升了性能。
- 而适应性自旋，是赋予了自旋一种学习能力，它并不固定自旋10次一下。他可以根据它前面线程的自旋情况，从而调整它的自旋，甚至是不经过自旋而直接挂起。

#### 锁的最佳实践

Doug Lea推荐的三个用锁的最佳实践，它们分别是：

1. 永远只在更新对象的成员变量时加锁
2. 永远只在访问可变的成员变量时加锁
3. 永远不在调用其他对象的方法时加锁

KK总结-最小使用锁

1. 降低锁范围：锁定代码的范围/作用域
2. 细分锁粒度：将一个大锁，拆分成多个小锁

并发原子类
===

> java.util.concurrent.atomic包下面，针对基本类型多线程场景更方便使用，提供了原子类，其底层实现原理：
>
> 1. volatile保证读写操作都可见（注意不保证原子）
> 2. 使用CAS指令，作为乐观锁实现，通过自旋重试保证写入。

**基本数据类型原子类**

- AtomicBoolen: boolean类型原子类
- AtomicInteger: int类型原子类
- AtomicLong: long类型原子类

**数组类型原子类**

- AtomicIntegerArray：Int数组类型原子类
- AtomicLongArray：long数组类型原子类
- AtomicReferenceArray：引用类型原子类

**引用类型原子类**

- AtomicReference：
- AtomicReferenceFieldUpdater：
- AtomicMarkableReference：
- AtomicStampedReference：

**字段类型原子类**

- AtomicIntegerFieldUpdater：int类型字段原子类
- AtomicLongFieldUpdater：long类型字段原子类
- AtomicReferenceFieldUpdater：引用型字段原子类

**ABA问题**

ABA问题，因为CAS在操作值的时候，需要检查值有没有发生变化，如果没有发生变化则更新，但是如果一个值原来是A，变成了B，又变成了A，那么使用CAS进行检查时会发现它的值并没有发生变化。Java中提供了**AtomicStampedReference**来解决

**LongAddr**

LongAdder 对 AtomicLong 的改进，改进思路为：AtomicInteger和AtomicLong里的value是所有
线程竞争读写的热点数据，将单个value拆分成跟线程一样多的数组Cell[]；每个线程写自己的Cell[i]++，最后对数组求和。

并发工具类
===

并发工具类有个核心类AbstractQueuedSynchronizer，即队列同步器。它是构建锁或者其他同步组件的基础（如Semaphore、CountDownLatch、ReentrantLock、ReentrantReadWriteLock），是JUC并发包中的核心基础组件。

- AbstractQueuedSynchronizer：抽象队列式的同步器
- 两种资源共享方式: 独占 | 共享，子类负责实现公平 OR 非公平

**Semaphore - 信号量**

使用场景：同一时间控制并发线程数

1. 准入数量 N
2. N =1 则等价于独占锁

CountdownLatch

场景: Master 线程等待 Worker 线程把任务执行完

重要方法|说明

public CountDownLatch(int count) 构造方法（总数）
void await() throws InterruptedException 等待数量归0
boolean await(long timeout, TimeUnit unit) 限时等待
void countDown() 等待数减1
long getCount() 返回剩余数量

CyclicBarrier

场景: 任务执行到一定阶段, 等待其他任务对齐。

重要方法|说明

CyclicBarrier(int parties) 构造方法（需要等待的数量）
public CyclicBarrier(int parties, Runnable barrierAction) 构造方法（需要等待的数量, 需要执行的任务）
int await() 任务内部使用; 等待大家都到齐
int await(long timeout, TimeUnit unit) 任务内部使用; 限时等待到齐
void reset() 重新一轮

CountdownLatch与CyclicBarrier比较

Future/FutureTask/CompletableFuture

Future/FutureTask关注单个线程/任务执行结果

CompletableFuture是异步回调，可以组合。

并发工具类详解
===



常用线程安全类型
===



并发编程相关内容
===



并发编程经验总结
===



**参考**

[java进阶训练营](https://u.geekbang.org/subject/java/1000579?utm_source=u_list_web&utm_medium=u_list_web&utm_term=u_list_web)

[Synchronized与Lock的区别与应用场景](http://guanzhou.pub/2020/03/04/Sync-and-lock/)
