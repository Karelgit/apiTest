package com.gengyun.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by karel on 2016/7/22.
 */
public class FileUtil {
    public static final List<String> readFile(String filePath) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
        List urlList = new ArrayList<String>();
        for (String line = br.readLine(); line != null; line = br.readLine()) {
            if(!line.startsWith("#"))   {
                urlList.add(line.trim());
                System.out.println(line.trim());
            }
        }
        br.close();
        return urlList;
    }

    public static void writeToFile(String filePath,String content) {
        try {
            FileWriter fw = new FileWriter(filePath);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.flush();
            bw.close();
            fw.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception{
        readFile("D:\\temp\\_input.txt");
//        writeToFile("D:\\test779.txt","1234");
    }
}
