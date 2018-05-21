package net.poweroak.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import net.poweroak.dao.UserMapper;
import net.poweroak.entity.User;
@Service
public class UserServiceImpl implements UserService {
	@Resource
	private UserMapper userMapper;
	@Override
	public User findById(Integer id) {
		// TODO Auto-generated method stub
		return userMapper.selectByPrimaryKey(id);
	}
	@Override
	public List<User> findAll() {
		// TODO Auto-generated method stub
		return userMapper.selectAll();
	}
	@Override
	public Integer insert(User users) {
		// TODO Auto-generated method stub
		return userMapper.insert(users);
	}

}
