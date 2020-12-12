package com.sl.homework.week0601.designpattern;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 单例的特点：对象实例任何情况下，有且至多一个。
 * 所以这个类的构造函数不能暴露，咋整，私有化呗
 * 但做人不能一毛不拔，得外面提供一个（不能再多），
 * 那可以提前new好，也可以来取的时候再new
 */
public class SingletonDemo {
    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            threadPool.execute(() -> {
                System.out.println(Thread.currentThread().getName() + " acquire Singleton1：" + Singleton1.INSTANCE.toString());
                System.out.println(Thread.currentThread().getName() + " acquire Singleton2：" + Singleton2.getInstance().toString());
                System.out.println(Thread.currentThread().getName() + " acquire Singleton3：" + Singleton3.getInstance().toString());
                System.out.println(Thread.currentThread().getName() + " acquire Singleton4：" + Singleton4.getInstance().toString());
            });
        }
    }

    // 枚举的这种方式，个人推荐
    enum Singleton1 {
        INSTANCE;
    }

    static class Singleton2 {
        private static Singleton2 INSTANCE;

        private Singleton2() {
        }

        public static Singleton2 getInstance() {
            if (null == INSTANCE) {
                synchronized (Singleton2.class) {
                    if (null == INSTANCE) {
                        INSTANCE = new Singleton2();
                    }
                }
            }
            return INSTANCE;
        }
    }

    static class Singleton3 {
        private static final Singleton3 INSTANCE = new Singleton3();

        private Singleton3() {
        }

        public static Singleton3 getInstance() {
            return INSTANCE;
        }
    }

    // 内部类的这种方式，个人推荐
    static class Singleton4 {

        private Singleton4() {
        }

        private static class InnerClass {
            private static final Singleton4 INSTANCE = new Singleton4();
        }

        public static Singleton4 getInstance() {
            return InnerClass.INSTANCE;
        }
    }
}
