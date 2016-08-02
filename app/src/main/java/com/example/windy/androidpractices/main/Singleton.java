package com.example.windy.androidpractices.main;

/**
 * Created by windog on 2016/7/23.
 * <p>
 * 饿汉式单例类. 在类初始化时，已经自行实例化
 * 类初始化时，就已经直接 new 一个出来（实例化了），而且是 final ，保证有且仅有一个对象，即单例。
 * 要得到此类的对象，只能通过提供的 getInstance() 方法，而且因为是 final ，也保证了线程安全。
 * 这是与懒汉式不同的地方。但是可能会多占内存。
 */
public class Singleton {

    private static final Singleton singleton = new Singleton();

    //静态工厂方法
    public static Singleton getInstance() {
        return singleton;
    }
}

/* 懒汉式单例模式，只在需要实例化一个对象时，才去 new 一个出来，就是懒汉式。不催就不动。
    不会存在白耗内存的情况，但是可能会线程不安全。
    需要再加一些检查锁的，或者同步的方法。这里不深究。
* */
class Singleton1 {

    private static Singleton1 singleton1 = null;

    //静态工厂方法
    public static Singleton1 getInstance() {
        if (singleton1 == null) {
            singleton1 = new Singleton1();
        }
        return singleton1;
    }
}