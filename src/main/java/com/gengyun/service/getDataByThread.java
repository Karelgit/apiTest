package com.gengyun.service;

import java.lang.management.ManagementFactory;

/**
 * Created by Administrator on 2016/6/29.
 */
public class getDataByThread {
    /*public static void main(String[] args) {
        System.out.println("*************PID**************：  " +  ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);
        MyThread mt = new MyThread();
        Thread thread0 = new Thread(mt);
        Thread thread1 = new Thread(mt);
        Thread thread2 = new Thread(mt);
        Thread thread3 = new Thread(mt);
        Thread thread4 = new Thread(mt);
        Thread thread5 = new Thread(mt);
        Thread thread6 = new Thread(mt);
        Thread thread7 = new Thread(mt);
        Thread thread8 = new Thread(mt);
        Thread thread9 = new Thread(mt);
        thread0.start();
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();
        thread6.start();
        thread7.start();
        thread8.start();
        thread9.start();

    }*/

    public static void main(String[] args) {
        System.out.println("*************PID**************：  " +  ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);
        LinkUnAvailService linkUnAvailService =new LinkUnAvailService();
        linkUnAvailService.hbaseToRedis(args[0]);
    }
}
