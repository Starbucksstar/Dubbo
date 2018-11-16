package com.star.dubboprovider.bean;

import lombok.Data;

@Data
public class MoreThread
{
	private static ThreadLocal<Integer> threadLocal = new ThreadLocal<Integer>(){
		@Override
		protected Integer initialValue()
		{
			return 0;
		}
	};

	public Integer add(){
		threadLocal.set(threadLocal.get()+1);
		return threadLocal.get();
	}

}
