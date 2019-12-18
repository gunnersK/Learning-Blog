package com.yjb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {
	
	@RequestMapping("/a/b")
//	@ResponseBody
	public String a(){
		System.out.println("---");
		return "index";
	}

}
