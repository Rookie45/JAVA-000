**题目1：**

跑一跑课上的各个例子，加深对多线程的理解

**答题如下**：

```java
public class DaemonThread {
    
    public static void main(String[] args) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Thread t = Thread.currentThread();
                System.out.println("当前线程:" + t.getName());
                //设置为守护进程后，并不会打印这条语句
            }
        };
        Thread thread = new Thread(task);
        thread.setName("test-thread-1");
        thread.setDaemon(true);
        thread.start();
    }
}
无输出

public class ThreadMain2 {

    public static void main(String[] args) {

        ThreadB threadB = new ThreadB();
        for (int i = 0; i < 1; i++) {
            new Thread(threadB, "线程名称：（" + i + "）").start();
        }

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //返回对当前正在执行的线程对象的引用
        Thread threadMain = Thread.currentThread();
        System.out.println("这是主线程：");
        System.out.println("返回当前线程组中活动线程的数目：" + Thread.activeCount());
        System.out.println("主线程的名称：" + threadMain.getName());
        System.out.println("返回该线程的标识符：" + threadMain.getId());
        System.out.println("返回线程的优先级：" + threadMain.getPriority());
        System.out.println("返回线程的状态：" + threadMain.getState());
        System.out.println("返回该线程所属的线程组：" + threadMain.getThreadGroup());
        System.out.println("测试线程是否为守护线程：" + threadMain.isDaemon());
    }
}

这是线程B
这是线程的名称：线程名称：（0）
返回当前线程线程名称：（0）的线程组中活动线程的数量:3
返回该线程线程名称：（0）的标识符:12
返回该线程线程名称：（0）的优先级:5
返回该线程线程名称：（0）的状态:RUNNABLE
返回该线程线程名称：（0）所属的线程组:java.lang.ThreadGroup[name=main,maxpri=10]
测试该线程线程名称：（0）是否处于活跃状态:true
测试该线程线程名称：（0）是否为守护线程:false
这是主线程：
返回当前线程组中活动线程的数目：2
主线程的名称：main
返回该线程的标识符：1
返回线程的优先级：5
返回线程的状态：RUNNABLE
返回该线程所属的线程组：java.lang.ThreadGroup[name=main,maxpri=10]
测试线程是否为守护线程：false

    
public class Counter {
    private int sum = 0;
    public void incr() {
        sum++;
    }
    public int getSum() {
        return sum;
    }
    
    public static void main(String[] args) throws InterruptedException {
        int loop = 10000;
        
        // test single thread
        Counter counter = new Counter();
        for (int i = 0; i < loop; i++) {
            counter.incr();
        }
        System.out.println("single thread: " + counter.getSum());
    
        // test multiple threads
        final Counter counter2 = new Counter();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < loop / 2; i++) {
                counter2.incr();
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < loop / 2; i++) {
                counter2.incr();
            }
        });
        t1.start();
        t2.start();
        while (Thread.activeCount()>2){//当前线程的线程组中的数量>2
            Thread.yield();
        }
        System.out.println("multiple threads: " + counter2.getSum());
    }
}
single thread: 10000
multiple threads: 6988
并发问题，多线程操作共享变量，两次操作仅一次生效
```



**题目2：**

完善网关的例子，试着调整其中的线程池参数

**答题如下**：

```java
===============Run 1===============
线程池的参数：cores=8*2；workQueue=2048   
int cores = Runtime.getRuntime().availableProcessors() * 2;
long keepAliveTime = 1000;
int queueSize = 2048;
//CallerRunsPolicy在任务被拒绝添加后，会调用当前线程池的所在的线程去执行被拒绝的任务。
RejectedExecutionHandler handler = new ThreadPoolExecutor.CallerRunsPolicy();
ExecutorService proxyService = new ThreadPoolExecutor(cores, cores,
               keepAliveTime, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(queueSize),
               new NamedThreadFactory("proxyService"), handler);

PS E:\Book\训练营\week3\day6> sb -u http://localhost:8888/api/hello -c 20 -N 60
Starting at 2020/11/4 22:12:40
[Press C to stop the test]
232124  (RPS: 3615.6)
---------------Finished!----------------
Finished at 2020/11/4 22:13:44 (took 00:01:04.2300773)
Status 200:    232126

RPS: 3802 (requests/second)
Max: 799ms
Min: 0ms
Avg: 1.6ms

  50%   below 1ms
  60%   below 1ms
  70%   below 1ms
  80%   below 2ms
  90%   below 4ms
  95%   below 6ms
  98%   below 8ms
  99%   below 11ms
99.9%   below 28ms

===============Run 2===============
线程池的参数：cores=8；workQueue=2048
int cores = Runtime.getRuntime().availableProcessors();
int queueSize = 2048;

PS E:\Book\训练营\week3\day6> sb -u http://localhost:8888/api/hello -c 20 -N 60
Starting at 2020/11/4 22:16:01
[Press C to stop the test]
262917  (RPS: 4093)
---------------Finished!----------------
Finished at 2020/11/4 22:17:05 (took 00:01:04.2962116)
Status 200:    262923

RPS: 4304.9 (requests/second)
Max: 682ms
Min: 0ms
Avg: 0.7ms

  50%   below 0ms
  60%   below 0ms
  70%   below 1ms
  80%   below 1ms
  90%   below 2ms
  95%   below 3ms
  98%   below 5ms
  99%   below 7ms
99.9%   below 17ms

===============Run 3===============
线程池的参数：cores=8*2；workQueue=1024
int cores = Runtime.getRuntime().availableProcessors()*2;
int queueSize = 1024;

PS E:\Book\训练营\week3\day6> sb -u http://localhost:8888/api/hello -c 20 -N 60
Starting at 2020/11/4 22:21:22
[Press C to stop the test]
269740  (RPS: 4199.9)
---------------Finished!----------------
Finished at 2020/11/4 22:22:26 (took 00:01:04.2971503)
Status 200:    269747

RPS: 4415.1 (requests/second)
Max: 716ms
Min: 0ms
Avg: 0.5ms

  50%   below 0ms
  60%   below 0ms
  70%   below 0ms
  80%   below 0ms
  90%   below 1ms
  95%   below 3ms
  98%   below 4ms
  99%   below 5ms
99.9%   below 16ms
    
===============Run 4===============
线程池的参数：cores=8；workQueue=1024
int cores = Runtime.getRuntime().availableProcessors();
int queueSize = 1024;

PS E:\Book\训练营\week3\day6> sb -u http://localhost:8888/api/hello -c 20 -N 60
Starting at 2020/11/4 22:35:58
[Press C to stop the test]
261256  (RPS: 4064.3)
---------------Finished!----------------
Finished at 2020/11/4 22:37:02 (took 00:01:04.4471028)
Status 200:    261259

RPS: 4269.9 (requests/second)
Max: 748ms
Min: 0ms
Avg: 1ms

  50%   below 0ms
  60%   below 1ms
  70%   below 1ms
  80%   below 1ms
  90%   below 3ms
  95%   below 4ms
  98%   below 6ms
  99%   below 7ms
99.9%   below 17ms
    
```

依据数据来说，本机上，核心线程数=最大线程数=CPU核心数，线程等待队列1024大小时，吞吐量最高；等待队列的大小影响，不如核心线程数的大。



参考

[java进阶训练营](https://u.geekbang.org/subject/java/1000579?utm_source=u_list_web&utm_medium=u_list_web&utm_term=u_list_web)