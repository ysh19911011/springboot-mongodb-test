package net.poweroak.service.impl;

import org.springframework.stereotype.Service;

import net.poweroak.dao.impl.BaseDaoImpl;
import net.poweroak.entity.User;
import net.poweroak.service.UserService;

@Service("userService")
public class UserServiceImpl extends BaseDaoImpl<User> implements UserService{

}
