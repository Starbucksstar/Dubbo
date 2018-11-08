package com.star.dubboprovider.service.impl;

import com.star.dubbo.annotation.DubboService;
import com.star.dubboprovider.service.UserService;
import org.springframework.stereotype.Service;

@Service
@DubboService
public class UserServiceImpl implements UserService
{
	@Override
	public String getUserName(int id)
	{
		System.out.println("name执行了一次");
		return "高星+"+id;
	}

	@Override
	public Integer getUserAge(String name)
	{
		System.out.println("age执行了一次");
		return 25+name.length();
	}
}
