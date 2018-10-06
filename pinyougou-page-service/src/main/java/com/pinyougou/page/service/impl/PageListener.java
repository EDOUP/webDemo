package com.pinyougou.page.service.impl;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pinyougou.page.service.ItemPageService;

/**
 *  监听类用于生成网页
 * @author Administrator
 *
 */
@Component
public class PageListener implements MessageListener{
	
	@Autowired
	private ItemPageService ItemPageService;
	
	@Override
	public void onMessage(Message message) {
		
		TextMessage textMessage=(TextMessage) message;
		
		try {
			String text = textMessage.getText();
			System.out.println("接收到消息:"+text);
			boolean b = ItemPageService.genItemHtml(Long.parseLong(text));
			System.out.println("页面生成结果："+b);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	

}
