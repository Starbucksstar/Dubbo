package com.star.dubboconsumer.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.star.dubbo.annotation.DubboConsumer;
import com.star.dubbo.core.DubboFactory;
import com.star.user.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserManageService
{
	private UserService userService;

	public void getUserInfo(){
		//userService = DubboFactory.getDubboService(UserService.class);
		System.out.println("用户名："+userService.getUserName(11)+"年龄："+userService.getUserAge("哈哈")+"出生地："+userService.getUserPlace(11));
	}
}
