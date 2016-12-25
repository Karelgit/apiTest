package com.gengyun.controller;

import com.gengyun.service.LinkUnAvailService;
import com.yeezhao.guizhou.client.SpellCheckerClient;
import org.apache.commons.cli.ParseException;
import org.codehaus.jettison.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

public class AppTests {
    private LinkUnAvailService linkUnAvailService;

    @Before
    public void init() {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
        linkUnAvailService = (LinkUnAvailService) context.getBean("linkUnAvailService");
    }

    @Test
    public void test()  {
        try {
            linkUnAvailService.getHbaseDateByTid("88625eb701e69906265b133c9511be91","http://222.85.149.3:18910/getData.json");
            System.out.println("nomal endding!");
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testErrorWord() throws FileNotFoundException{
        File input = new File("D:\\temp\\_input.txt");
        Scanner cin = new Scanner(new FileInputStream(input), "UTF-8");
        StringBuilder sb = new StringBuilder();

        while(cin.hasNextLine()) {
            sb.append(cin.nextLine()).append(System.lineSeparator());
        }
        System.out.println(sb);
        System.out.println(new SpellCheckerClient().query(sb.toString()));
    }
}
