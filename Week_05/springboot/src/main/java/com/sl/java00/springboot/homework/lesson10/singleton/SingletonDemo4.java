package com.sl.java00.springboot.homework.lesson10.singleton;

//线程安全，调用效率高，可以延时加载
public class SingletonDemo4 {

    private static class SingletonClassInstance{
        private static final SingletonDemo4 instance=new SingletonDemo4();
    }

    private SingletonDemo4(){}

    public static SingletonDemo4 getInstance(){
        return SingletonClassInstance.instance;
    }

}
