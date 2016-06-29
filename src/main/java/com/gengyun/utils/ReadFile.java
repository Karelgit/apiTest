package com.gengyun.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Administrator on 2016/6/29.
 */
public class ReadFile {

    public static LinkedBlockingQueue readByLine(String filePath)    {
        File file = new File(filePath);
        BufferedReader reader = null;
        LinkedBlockingQueue<String> lbq = new LinkedBlockingQueue<String>();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                System.out.println("line " + line + ": " + tempString);
                lbq.offer(tempString);
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return lbq;
    }

    public static void main(String[] args) {
        readByLine("C:\\Users\\Administrator\\Desktop\\yz-guizhou-spellcheck_0624\\apiTest\\data\\tables.txt");
    }
}
