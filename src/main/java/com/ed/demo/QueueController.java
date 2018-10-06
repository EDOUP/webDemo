package com.ed.demo;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 消息的生产者
 * @author Administrator
 *
 */
@RestController
public class QueueController {
	
	@Autowired
	private JmsMessagingTemplate jmsMessagingTemplate;
	
	@RequestMapping("/send")
	public void send(String text) {
		jmsMessagingTemplate.convertAndSend("testQueue", text);
	}
	
	@RequestMapping("/sendmap")
	public void sendMap() {
		Map map=new HashMap<>();
		map.put("mobile","23414212");
		map.put("content","传递的是一个map");
		
		
		jmsMessagingTemplate.convertAndSend("testQueue_Map", map);
	}
	
}
