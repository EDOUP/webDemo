package com.ed.demo;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
	private Environment env;
	
	@Autowired
	private JmsMessagingTemplate jmsMessagingTemplate;
	
	@RequestMapping("/send")
	public void send(String text) {
		jmsMessagingTemplate.convertAndSend("testQueue", text);
	}
	/**
	 * 发送短信
	 */
	@RequestMapping("/sendmap")
	public void sendMap() {
		Map map=new HashMap<>();
		map.put("mobile","");
		map.put("template_code","传递的是一个map");
		map.put("sign_name", "");
		map.put("template_param", "{\"code\":\"45834\"}");
		
		
		//jmsMessagingTemplate.convertAndSend("testQueue_Map", map);
		jmsMessagingTemplate.convertAndSend("sms", map);
	}
	/**
	 * 发送普通邮件
	 */
	@RequestMapping("/sendmail")
	public void sendMail() {
		Map map=new HashMap<>();
		map.put("to","your email ");
		map.put("subject","java测试邮件");
		map.put("content", "消息中间件普通mail测试成功");
		
		jmsMessagingTemplate.convertAndSend("mail", map);
	}
	/**
	 * 发送html格式邮件
	 */
	@RequestMapping("/sendhtmlmail")
	public void sendHtmlMail() {
		Map map=new HashMap<>();
		map.put("to","your email ");
		map.put("subject","java测试邮件");
		map.put("content", "消息中间件Html Mail测试成功");
		
		jmsMessagingTemplate.convertAndSend("htmlmail", map);
	}
	/**
	 * 发送带附件的邮件
	 */
	@RequestMapping("/sendattmail")
	public void sendAttachmentMail() {
		
		Map map=new HashMap<>();
		map.put("to","417431006@qq.com");
		map.put("subject","java测试邮件");
		map.put("content", "消息中间件带附件 Mail测试成功");
		map.put("filePath", env.getProperty("email.attachment.path"));
		jmsMessagingTemplate.convertAndSend("attachment_mail", map);
	}
	/**
	 * 发送图片邮件
	 */
	@RequestMapping("/sendpicmail")
	public void sendPictureMail() {
		
		Map map=new HashMap<>();
		map.put("to","your email ");
		map.put("subject","java测试邮件");
		map.put("content", "消息中间件 发送内嵌图片 Mail测试成功");
		map.put("filePath", env.getProperty("email.attachment.path"));
		map.put("rscId", "n001");
		jmsMessagingTemplate.convertAndSend("picture_mail", map);
	}
	
	/**
	 * 发送模板邮件
	 */
	@RequestMapping("/sendtplmail")
	public void sendTemplateMail() {
		
		Map map=new HashMap<>();
		map.put("to","your email ");
		map.put("subject","注册模板邮件");
		map.put("id", "006");
		jmsMessagingTemplate.convertAndSend("template_mail", map);
	}
}
