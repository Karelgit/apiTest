package com.gengyun.service;

/**
 * Created by Administrator on 2016/6/29.
 */
public class exportToXLS {
    public static void main(String[] args) {
        LinkUnAvailService linkUnAvailService =new LinkUnAvailService();
        linkUnAvailService.exportFromRedis("www.gygov.gov.cn");
    }
}
