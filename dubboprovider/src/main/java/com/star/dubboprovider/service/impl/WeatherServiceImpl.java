package com.star.dubboprovider.service.impl;

import com.star.dubbo.annotation.DubboService;
import org.springframework.stereotype.Service;

@Service
@DubboService
public class WeatherServiceImpl implements com.star.user.service.WeatherService
{
	@Override
	public String getWeather(String place)
	{
		return "晴天";
	}
}
