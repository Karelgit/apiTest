package com.gengyun.service;

import com.gengyun.utils.FileUtil;
import org.apache.commons.cli.ParseException;
import org.codehaus.jettison.json.JSONException;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.List;

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
//        linkUnAvailService.hbaseToRedis(args[0]);

        String wholeSiteURI = "http://222.85.149.3:18910/getData.json";
        List<String> taskidList = null;
        try {
            taskidList = FileUtil.readFile("/home/hbase/taskid.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(int i=0;i<taskidList.size();i++)    {
            try {
                linkUnAvailService.getHbaseDateByTid(taskidList.get(i),wholeSiteURI);
                System.out.println("***************nomal ending!******************");
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
