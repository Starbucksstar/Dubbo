package com.star.dubboconsumer.controller;

import com.star.dubbo.annotation.DubboConsumer;
import com.star.dubbo.core.DubboFactory;
import com.star.dubboprovider.service.UserService;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Data
public class UserTest
{
	private UserService userService;

	@RequestMapping(value = "/haha")
	public Object getName(){
		userService = DubboFactory.getDubboService(UserService.class,"1.0.0","basic.zk.address");
		System.out.println(userService.getUserName(110)+":"+userService.getUserAge("star"));
		return userService.getUserName(110);
	}

}
