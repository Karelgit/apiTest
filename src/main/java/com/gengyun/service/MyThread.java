package com.gengyun.service;

import com.gengyun.utils.ReadFile;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Administrator on 2016/6/29.
 */
public class MyThread implements Runnable{
    public LinkUnAvailService linkUnAvailService;

    @Override
    public void run() {
        String readPath ="/home/hbase/tables.txt";
//        String readPath="C:\\Users\\Administrator\\Desktop\\yz-guizhou-spellcheck_0624\\apiTest\\data\\tables.txt";
        LinkedBlockingQueue<String> lbq = ReadFile.readByLine(readPath);
        String tableName = lbq.poll();
        if(tableName != null)   {
            linkUnAvailService = new LinkUnAvailService();
            linkUnAvailService.hbaseToRedis(tableName);
        }

    }
}

