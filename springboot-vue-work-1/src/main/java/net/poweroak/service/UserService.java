package net.poweroak.service;

import java.util.List;

import net.poweroak.entity.User;

public interface UserService {
	public User findById(Integer id);
	
	public List<User> findAll();
	
//	public Integer count();
	
//	public Integer delete(String id);
	
	public Integer insert(User users);
}
