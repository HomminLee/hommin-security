package com.hommin.security.service.impl;

import com.hommin.security.service.HelloService;
import org.springframework.stereotype.Service;

/**
 * @author Hommin
 *
 */
@Service
public class HelloServiceImpl implements HelloService {

	@Override
	public String greeting(String name) {
		System.out.println("greeting");
		return "hello "+name;
	}

}
