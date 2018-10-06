package com.ed.demo;

import java.util.Map;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {
	
	@JmsListener(destination = "testQueue")
	public void readMessae(String text) {
		System.out.println("接收到消息:"+text);
	}
	
	@JmsListener(destination = "testQueue_Map")
	public void readMapMessae(Map map) {
		System.out.println("接收到消息:"+map);
	}
	
	
}
