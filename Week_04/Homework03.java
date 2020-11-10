package com.sl.java00.week04;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 本周作业：（必做）思考有多少种方式，在main函数启动一个新线程或线程池，
 * 异步运行一个方法，拿到这个方法的返回值后，退出主线程？
 * 写出你的方法，越多越好，提交到github。
 * <p>
 * 一个简单的代码参考：
 */
public class Homework03 {

    private static Integer resultWithMethod = new Integer(-1);

    public static void main(String[] args) {

        method1();
        method2();
        method3();
        method4();
        method5();
        method6();
        method7();
        method8();
        method9();
        method10();
        method11();
        method12();
        method13();
        method14();
        method15();
        method16();

    }

    //16. 利用ConcurrentHashMap的CAS线程安全的读写
    private static void method16() {
        long start = System.currentTimeMillis();
        final Map<String, Integer> count = new ConcurrentHashMap<>();
        int result = -1;
        // 在这里创建一个线程或线程池，异步执行 下面方法
        Thread thread = new Thread(() -> {
            Integer value = Integer.valueOf(sum());
            count.put("result", value);
        });
        thread.start();

        try {
            // 预计子线程计算完成时间
            Thread.sleep(78L);
            //这是得到的返回值
            result = count.get("result").intValue();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 确保  拿到result 并输出
        System.out.println("method16异步计算结果为：" + result);

        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
    }

    //15. CopyOnWriteArrayList线程安全的读写List
    private static void method15() {
        long start = System.currentTimeMillis();
        List<Integer> safeCollection = new CopyOnWriteArrayList();
        int result = -1;
        // 在这里创建一个线程或线程池，异步执行 下面方法
        Thread thread = new Thread(() -> {
            safeCollection.add(Integer.valueOf(sum()));
        });
        thread.start();

        try {
            // 预计子线程计算完成时间
            Thread.sleep(78L);
            //这是得到的返回值
            result = safeCollection.get(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 确保  拿到result 并输出
        System.out.println("method15异步计算结果为：" + result);

        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
    }

    //14. Collections.synchronizedList()强制将入参的集合对象的操作加上同步
    private static void method14() {
        long start = System.currentTimeMillis();
        List<Integer> safeCollection = Collections.synchronizedList(new ArrayList<Integer>());
        int result = -1;
        // 在这里创建一个线程或线程池，异步执行 下面方法
        Thread thread = new Thread(() -> {
            safeCollection.add(Integer.valueOf(sum()));
        });
        thread.start();

        try {
            // 预计子线程计算完成时间
            Thread.sleep(78L);
            //这是得到的返回值
            result = safeCollection.get(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 确保  拿到result 并输出
        System.out.println("method14异步计算结果为：" + result);

        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
    }

    //13. 线程安全的集合类Vector
    private static void method13() {
        long start = System.currentTimeMillis();
        Vector<Integer> safeCollection = new Vector<>();
        int result = -1;
        // 在这里创建一个线程或线程池，异步执行 下面方法
        Thread thread = new Thread(() -> {
            safeCollection.add(Integer.valueOf(sum()));
        });
        thread.start();

        try {
            // 预计子线程计算完成时间
            Thread.sleep(78L);
            //这是得到的返回值
            result = safeCollection.get(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 确保  拿到result 并输出
        System.out.println("method13异步计算结果为：" + result);

        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
    }

    //12. 利用CompletableFuture工具类，异步执行完，join等待执行结果
    private static void method12() {
        long start = System.currentTimeMillis();
        // 在这里创建一个线程或线程池，异步执行 下面方法
        CompletableFuture.runAsync(() -> {
            resultWithMethod = sum();
        }).join();
        //这是得到的返回值
        int result = resultWithMethod;

        // 确保  拿到result 并输出
        System.out.println("method12异步计算结果为：" + result);

        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
    }

    //11. 利用FutureTask类get()方法，获取子线程计算结果
    private static void method11() {
        long start = System.currentTimeMillis();
        int result = -1;
        // 在这里创建一个线程或线程池，异步执行 下面方法
        FutureTask<Integer> task = new FutureTask<Integer>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return Integer.valueOf(sum());
            }
        });
        new Thread(task).start();
        //这是得到的返回值
        try {
            result = task.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        // 确保  拿到result 并输出
        System.out.println("method11异步计算结果为：" + result);

        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
    }

    //10. 利用CyclicBarrier的await()方法，让主线程等待子线程完成状态
    private static void method10() {
        long start = System.currentTimeMillis();
        int result = -1;
        // 在这里创建一个线程或线程池，异步执行下面方法
        Thread thread = new Thread(() -> {
            resultWithMethod = Integer.valueOf(sum());
        });
        CyclicBarrier barrier = new CyclicBarrier(1, thread);
        thread.start();

        try {
            barrier.await();
            //这是得到的返回值
            result = resultWithMethod;
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }

        // 确保  拿到result 并输出
        System.out.println("method10异步计算结果为：" + result);

        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
    }

    //9. 利用CountDownLatch的countDown()和await()方法，让主线程等待子线程完成
    private static void method9() {
        long start = System.currentTimeMillis();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        int result = -1;
        // 在这里创建一个线程或线程池，异步执行下面方法
        Thread thread = new Thread(() -> {
            resultWithMethod = Integer.valueOf(sum());
            countDownLatch.countDown();

        });
        thread.start();

        try {
            countDownLatch.await();
            //这是得到的返回值
            result = resultWithMethod;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 确保  拿到result 并输出
        System.out.println("method9异步计算结果为：" + result);

        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
    }

    //8. 利用Semaphore为1时，具有独占性，形成多线程访问互斥
    private static void method8() {
        long start = System.currentTimeMillis();
        int result = -1;
        Semaphore semaphore = new Semaphore(1);
        // 在这里创建一个线程或线程池，异步执行下面方法
        Thread thread = new Thread(() -> {
            try {
                semaphore.acquire();
                resultWithMethod = Integer.valueOf(sum());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphore.release();
            }
        });
        thread.start();

        try {
            // 让主线程比子线程晚些获取锁
            Thread.sleep(1L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            semaphore.acquire();
            //这是得到的返回值
            result = resultWithMethod;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }

        // 确保  拿到result 并输出
        System.out.println("method8异步计算结果为：" + result);

        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
    }

    //7. 利用原子类的特性，完成多线程间共享变量读写
    private static void method7() {
        long start = System.currentTimeMillis();
        AtomicInteger atomicInteger = new AtomicInteger(-1);
        int result = -1;
        // 在这里创建一个线程或线程池，异步执行 下面方法
        Thread thread = new Thread(() -> {
            atomicInteger.set(Integer.valueOf(sum()));
        });
        thread.start();

        try {
            // 预计子线程计算完成时间
            Thread.sleep(78L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //这是得到的返回值
        result = atomicInteger.get();

        // 确保  拿到result 并输出
        System.out.println("method7异步计算结果为：" + result);

        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
    }

    //6. 利用LockSupport的park,unpark方法，将主线程暂停，由子线程恢复
    private static void method6() {
        long start = System.currentTimeMillis();
        int result = -1;
        Thread mainThread = Thread.currentThread();
        // 在这里创建一个线程或线程池，异步执行下面方法
        Thread thread = new Thread(() -> {
            resultWithMethod = Integer.valueOf(sum());
            LockSupport.unpark(mainThread);
        });
        thread.start();

        LockSupport.park();
//        LockSupport.parkUntil(resultWithMethod, 10);
        //这是得到的返回值
        result = resultWithMethod;

        // 确保  拿到result 并输出
        System.out.println("method6异步计算结果为：" + result);

        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
    }

    //5. 利用ReentrantReadWriteLock，writeLock()和readLock()互斥读写共享变量
    private static void method5() {
        long start = System.currentTimeMillis();
        ReadWriteLock lock = new ReentrantReadWriteLock(true);
        int result = -1;
        // 在这里创建一个线程或线程池，异步执行下面方法
        Thread thread = new Thread(() -> {
            try {
                lock.writeLock().lock();
                resultWithMethod = sum();
            } finally {
                lock.writeLock().unlock();
            }

        });
        thread.start();

        try {
            // 让主线程比子线程晚些获取锁
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            lock.readLock().lock();
            result = resultWithMethod;
        } finally {

            lock.readLock().unlock();
        }

        // 确保  拿到result 并输出
        System.out.println("method5异步计算结果为：" + result);

        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
    }

    //4. 利用ReentrantLock，lock与unlock搭配，互斥访问共享变量
    private static void method4() {
        long start = System.currentTimeMillis();
        ReentrantLock lock = new ReentrantLock(true);
        int result = -1;
        // 在这里创建一个线程或线程池，异步执行下面方法
        Thread thread = new Thread(() -> {
            try {
                lock.lock();
                resultWithMethod = sum();
            } finally {
                lock.unlock();
            }

        });
        thread.start();

        try {
            // 让主线程比子线程晚些获取锁
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            lock.lock();
            //这是得到的返回值
            result = resultWithMethod.intValue();
        } finally {
            lock.unlock();
        }

        // 确保  拿到result 并输出
        System.out.println("method4异步计算结果为：" + result);

        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
    }

    //3. 利用synchronized锁，让主线程和子线程互斥访问共享变量
    private static void method3() {
        long start = System.currentTimeMillis();
        int result = -1;

        // 在这里创建一个线程或线程池，异步执行下面方法
        Thread thread = new Thread(() -> {
            synchronized (resultWithMethod) {
                resultWithMethod = sum();
//                resultWithMethod.notifyAll();
            }
        });
        thread.start();

        try {
            // 让主线程比子线程晚些获取锁
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        synchronized (resultWithMethod) {
//            try {
//                resultWithMethod.wait();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            result = resultWithMethod;
            //这是得到的返回值
        }

        // 确保  拿到result 并输出
        System.out.println("method3异步计算结果为：" + result);

        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
    }

    //2. 利用Future和Callable接口可以线程执行的返回值
    private static void method2() {
        long start = System.currentTimeMillis();
        int result = -1;
        // 在这里创建一个线程或线程池，异步执行下面方法
        ExecutorService threadPool = Executors.newFixedThreadPool(1);
        Future<Integer> future = threadPool.submit(() -> {
            return sum();
        });

        try {
            //这是得到的返回值
            result = future.get().intValue();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        // 确保  拿到result 并输出
        System.out.println("method2异步计算结果为：" + result);

        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
        // 结束
        threadPool.shutdown();
    }

    //1. 创建线程和静态变量，将计算结果存入静态变量
    private static void method1() {
        long start = System.currentTimeMillis();
        int result = -1;
        // 在这里创建一个线程或线程池，异步执行下面方法
        Thread thread = new Thread(() -> {
            resultWithMethod = Integer.valueOf(sum());
        });
        thread.start();
        try {
            thread.join();
            //这是得到的返回值
            result = resultWithMethod.intValue();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 确保  拿到result 并输出
        System.out.println("method1异步计算结果为：" + result);

        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
    }

    private static int sum() {
        return fibo(36);
    }

    private static int fibo(int a) {
        if (a < 2)
            return 1;
        return fibo(a - 1) + fibo(a - 2);
    }
}
