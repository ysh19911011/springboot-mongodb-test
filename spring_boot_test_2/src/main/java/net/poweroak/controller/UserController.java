package net.poweroak.controller;

import javax.annotation.Resource;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Query;
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
	@RequestMapping("/register")
	public String register() {
		return "register";
	}
	@RequestMapping(value="/registerSub")
	@ResponseBody
	public String registerSub(Model model, User user) {
		user.setId(new ObjectId());
		System.out.println(user.getName()+""+user.getSex()+user.getAge());
		userService.save(user);
		model.addAttribute("user",user);
		return "1";
	}
	@RequestMapping("/success")
	public String success(Model model) {
		User user=userService.findOne(new Query());
		model.addAttribute("user", user);
		return "success";
	}
}
