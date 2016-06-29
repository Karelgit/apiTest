package com.gengyun.controller;

import com.gengyun.service.LinkUnAvailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/api")
public class API {

	@Autowired
	private LinkUnAvailService linkUnAvailService;

	@RequestMapping(value="/test",method = RequestMethod.GET)
	public String test(String tid ) {
		System.out.println(tid);
		return "hello";
	}

	@RequestMapping(value="/hbaseToRedis",method = RequestMethod.GET)
	public String hbaseToRedis(String tableName) {
		return linkUnAvailService.hbaseToRedis(tableName);
	}

	@RequestMapping(value="/exportFromRedis",method = RequestMethod.GET)
	public String exportFromRedis(String tableName) {
		return linkUnAvailService.exportFromRedis(tableName);
	}


}