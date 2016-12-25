package com.gengyun.service;

import com.gengyun.utils.ExportXLS;
import com.gengyun.utils.PostUtil;
import com.gengyun.utils.PropertyHelper;
import com.yeezhao.guizhou.client.SpellCheckerClient;
import jxl.write.WritableWorkbook;
import org.apache.commons.cli.ParseException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HConstants;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

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


    public void initRedis() {
        propertyHelper = new PropertyHelper("redisconf");
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(Integer.valueOf(propertyHelper.getValue("MAXTOTAL")));
        config.setMaxIdle(Integer.valueOf(propertyHelper.getValue("IDLE")));
        config.setMaxWaitMillis(Integer.valueOf(propertyHelper.getValue("MAXWAIT")));
        config.setTestOnBorrow(true);
        jedisPool = new JedisPool(config, propertyHelper.getValue("IP"), Integer.valueOf(propertyHelper.getValue("PORT")));
    }


    public String hbaseToRedis(String tableName) {
        initRedis();
        hbConfig = new Configuration();
        hbConfig.addResource("hbase-site.xml");
        hbConfig.setLong(HConstants.HBASE_REGIONSERVER_LEASE_PERIOD_KEY, 1800000);
        client = new SpellCheckerClient();
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
            int i = 0;
            for (Result r : rs) {
                String url = Bytes.toString(r.getValue(Bytes.toBytes("crawlerData"), Bytes.toBytes("url")));
                String text = Bytes.toString(r.getValue(Bytes.toBytes("crawlerData"), Bytes.toBytes("text")));
                String errorWords = client.query(text);
                if(errorWords !="") {
                    jedis.hset(tableName + "_errorwords", url, errorWords);
                }
                i++;
                System.out.println("scanning the record " +i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            hbConfig.clear();
        }
        System.out.println(tableName + " is formal ended!");
        return null;
    }

    public String exportFromRedis(String tableName) {
        initRedis();
        Jedis jedis = jedisPool.getResource();
        jedis.select(9);
        WritableWorkbook workbook = null;

        Map<String, String> errorWordsMap = new HashMap<String, String>();
        errorWordsMap = jedis.hgetAll(tableName + "_errorwords");
        ExportXLS exportXlS = new ExportXLS();
        workbook = exportXlS.createExcel(tableName,workbook);
        int i = 0;
        for (Map.Entry<String, String> entry : errorWordsMap.entrySet()) {
            exportXlS.addRecord(i, entry.getKey(), entry.getValue());
            System.out.println("processing...");
            i++;
        }
        exportXlS.close(workbook);
        System.out.println("export from Redis is end!");
        return "export from Redis is processed!";
    }

    public void getHbaseDateByTid(String taskid,String wholeSiteURI) throws ParseException,JSONException{
        initRedis();
        client = new SpellCheckerClient();
        Jedis jedis = null;
        JSONObject jsonObject = null;
        String startRow = taskid + "|";
        String nextRow = null;
        int size = 100;
        int j = 0;
        PostUtil pu = new PostUtil();
        Map<String, String> param = new HashMap<String, String>();
        do{
            param.put("taskId", taskid);
            param.put("startRow", startRow);
            param.put("size", String.valueOf(size));
            String response = null;

            response = pu.postMethod(wholeSiteURI, param);

            jsonObject = new JSONObject(response);
            size = jsonObject.getInt("size");

            pickErrorWord(jsonObject,jedis,taskid, j);

            nextRow = jsonObject.getString("nextRow");
            startRow = nextRow;
            param.put("startRow",nextRow);
            param.put("size",String.valueOf(size));
        }while (jsonObject.getInt("size") == 100);
        System.out.println("j: " + j);
    }

    public void pickErrorWord(JSONObject jsonObject, Jedis jedis, String taskid, int j) {
        int count = 0;
        try {
            for(int i=0; i<jsonObject.getJSONArray("data").length(); i++)   {
                String url = jsonObject.getJSONArray("data").getJSONObject(i).getString("url");
                String content = jsonObject.getJSONArray("data").getJSONObject(i).getString("content");
                jedis = jedisPool.getResource();
                jedis.select(9);
                if (null !=content&& !content.equals("")){
                    String errorWords = client.query(content);
                    if(errorWords !="") {
                        jedis.hset(taskid + "_errorwords", url, errorWords);
                    }
                    count++;
                    System.out.println("scanning the record "+taskid);
                }
                jedisPool.returnResource(jedis);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        j++;
    }
}
