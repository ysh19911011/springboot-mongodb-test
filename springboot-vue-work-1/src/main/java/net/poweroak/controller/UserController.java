 package net.poweroak.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.poweroak.entity.User;
import net.poweroak.service.UserService;
@Controller
@RequestMapping("/user")

public class UserController {
	@Resource
	private UserService userService;
	@SuppressWarnings("deprecation")
	@RequestMapping("/index")
	public String index(HttpServletRequest request,Model model){
		List<User> users=userService.findAll();
//		for(User user:users){
//			System.out.println(user.getName());
//		}
		model.addAttribute("users",users);
		return "/jsp/index";
	}
	@RequestMapping("/reg")
	@ResponseBody
	public String index(HttpServletRequest request,Model model,User user){
		System.out.println(user.getName());
		System.out.println(user.getSex());
		System.out.println(user.getAge());
		userService.insert(user);
		
		return "1";
	}
	@RequestMapping("demo")
	public String testCopy(HttpServletRequest request) {
		return "/jsp/copy1";
	}
	@RequestMapping("demo1")
	public String testCopy2(HttpServletRequest request) {
		return "/jsp/copy2";
	}
}
