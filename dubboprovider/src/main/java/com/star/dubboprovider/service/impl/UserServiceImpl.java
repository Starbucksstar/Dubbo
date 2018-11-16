package com.star.dubboprovider.service.impl;

import com.star.dubbo.annotation.DubboService;
import com.star.user.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author gaoxing
 */
@DubboService
@Service
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

	@Override
	public String getUserPlace(int id)
	{
		System.out.println("place执行了一次");
		return "武汉";
	}
}
