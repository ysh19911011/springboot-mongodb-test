package net.poweroak.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
public class Test1 {
	@RequestMapping("/test/{str}")
	@ResponseBody
	public String test(@PathVariable String str,Model model){
		System.out.println(str);
		return str;
	}
	@RequestMapping(value="/test1",method=RequestMethod.POST)
	@ResponseBody
	public String test1(@RequestBody String message){
		System.out.println(message);
		return message;
	}
	@RequestMapping("/test2")
	public String test2(Model model){
//		model.addAttribute("title","title");
		return "/test_socket";
	}
	@RequestMapping("/index")
	public String test3() {
		return "/index";
	}
}
