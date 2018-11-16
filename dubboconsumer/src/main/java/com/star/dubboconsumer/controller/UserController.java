package com.star.dubboconsumer.controller;

import com.star.dubboconsumer.service.UserManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController
{
	@Autowired
	private UserManageService userManageService;

	@RequestMapping(value = "/haha")
	public Object getName(){
		userManageService.getUserInfo();
		return 0;
	}

}
