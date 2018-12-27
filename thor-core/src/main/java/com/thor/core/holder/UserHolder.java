package com.thor.core.holder;

import com.thor.core.entity.User;

/**
 * 用户线程池
 * @auth ldang
 *
 */
public class UserHolder {
	
	private static final ThreadLocal<User> contextHolder = new ThreadLocal<User>();
	
	public static void set(User user) {
		contextHolder.set(user);
	}
	
	public static User get() {
		return contextHolder.get();
	}
	
	public static void clear() {
		contextHolder.remove();
	}
}