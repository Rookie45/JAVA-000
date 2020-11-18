package com.sl.java00.springboot.homework.lesson10.singleton;

//实现了延时加载，与线程安全
public class SingletonDemo3 {
        private volatile static SingletonDemo3 SingletonDemo3;

        private SingletonDemo3() {
        }

        public static SingletonDemo3 newInstance() {
            if (SingletonDemo3 == null) {
                synchronized (SingletonDemo3.class) {
                    if (SingletonDemo3 == null) {
                        SingletonDemo3 = new SingletonDemo3();
                    }
                }
            }
            return SingletonDemo3;
        }
    }
