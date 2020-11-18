package com.sl.java00.springboot.homework.lesson10.singleton;

//线程安全，调用效率高，但是不能延时加载
public class SingletonDemo1 {
     
    private static SingletonDemo1 instance = new SingletonDemo1();
     
    //构造器私有化
    private SingletonDemo1(){}
     
    public static SingletonDemo1 getInstance(){
        return instance;
    }
}
