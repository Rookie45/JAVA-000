package com.sl.java00.springboot.homework.lesson10.singleton;

//线程安全，调用效率高，不能延时加载，可以天然的防止反射和反序列化调用
public enum SingletonDemo5 {

    //枚举元素本身就是单例
    INSTANCE;

    //添加自己需要的操作
    public void singletonOperation(){
    }
}
