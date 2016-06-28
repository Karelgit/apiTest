package com.gengyun.service;

import com.gengyun.utils.ExportXLS;
import com.gengyun.utils.PropertyHelper;
import com.yeezhao.guizhou.client.SpellCheckerClient;
import jxl.write.WriteException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hadoop on 2015/7/21.
 */

@Service("LinkUnAvailService")
public class LinkUnAvailService {
    private PropertyHelper propertyHelper;
    private JedisPool jedisPool;
    private Configuration hbConfig;
    private SpellCheckerClient client;
    private HTable table;


    public void initRedis(String tableName) {
        propertyHelper = new PropertyHelper("redisconf");
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(Integer.valueOf(propertyHelper.getValue("MAXTOTAL")));
        config.setMaxIdle(Integer.valueOf(propertyHelper.getValue("IDLE")));
        config.setMaxWaitMillis(Integer.valueOf(propertyHelper.getValue("MAXWAIT")));
        config.setTestOnBorrow(true);
        jedisPool = new JedisPool(config, propertyHelper.getValue("IP"), Integer.valueOf(propertyHelper.getValue("PORT")));
    }


    public String hbaseToRedis(String tableName) {
        hbConfig = new Configuration();
        hbConfig.addResource("hbase-site.xml");

        Jedis jedis = null;
        ResultScanner rs = null;
        try {
            table = new HTable(hbConfig, tableName);
            Scan scan = new Scan();
            scan.setCaching(50);
            scan.addColumn(Bytes.toBytes("crawlerData"), Bytes.toBytes("url"));
            scan.addColumn(Bytes.toBytes("crawlerData"), Bytes.toBytes("text"));

            rs = table.getScanner(scan);
            jedis = jedisPool.getResource();
            jedis.select(9);

            for (Result r : rs) {
                String url = Bytes.toString(r.getValue(Bytes.toBytes("crawlerData"), Bytes.toBytes("url")));
                String text = Bytes.toString(r.getValue(Bytes.toBytes("crawlerData"), Bytes.toBytes("text")));

                String errorWords = client.query(text);

                jedis.hset(tableName + "_errorwords", url, errorWords);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "hbase to Redis is processed!";
    }

    public String exportFromRedis(String tableName) {
        initRedis(tableName);
        Jedis jedis = jedisPool.getResource();
        jedis.select(9);

        Map<String, String> errorWordsMap = new HashMap<String, String>();
        errorWordsMap = jedis.hgetAll(tableName + "_errorwords");
        ExportXLS exportXlS = new ExportXLS();
        int i = 0;
        for (Map.Entry<String, String> entry : errorWordsMap.entrySet()) {
            try {
                exportXlS.createExcel(tableName, i, entry.getKey(), entry.getValue());
                i++;
            } catch (WriteException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "export from Redis is processed!";
    }

    public void validTest() {
        System.out.println("test valid!");
    }

    //test
    public static void main(String[] args) {
        System.out.println("tableName" + "_errorwords" +
                new SimpleDateFormat("YYYYmmdd-HHmmss").format(new Date()));
    }

}