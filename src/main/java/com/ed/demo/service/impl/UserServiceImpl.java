package com.ed.demo.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.ed.demo.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Override
	public String getName() {
		
		return "success";
	}

}
