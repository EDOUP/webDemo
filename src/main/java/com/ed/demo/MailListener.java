package com.ed.demo;

import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


@Component
public class MailListener {
	
	@Autowired
	private MailUtil mailUtil;
	
	@Autowired
	private Environment evn;
	
	@Autowired
	private TemplateEngine templateEngine;
	
	@JmsListener(destination = "template_mail")
	public void testTemplateMailTest(Map<String,String> map){
		System.out.println("接收信息 :"+map);
		Context context = new Context();
		context.setVariable("code", map.get("id"));
		
		String emailContent=templateEngine.process("emailTemplate", context);
		mailUtil.sendHtmlMail(map.get("to"),map.get("subject"),emailContent);
	}
	
	@JmsListener(destination = "pinyougou_emailcode")
	public void pinyouTemplateEmailCode(Map<String,String> map){
		Context context = new Context();
		context.setVariable("code", map.get("emailCode"));
		
		String emailContent=templateEngine.process("pinyougouEmailTemplate", context);
		mailUtil.sendHtmlMail(map.get("to"),map.get("subject"),emailContent);
	}
	
	@JmsListener(destination = "mail")
	public void sendMail(Map<String,String> map) {
			System.out.println("接收信息 :"+map);
			mailUtil.sendSimpleMail(map.get("to"),map.get("subject"),map.get("content"));
			System.out.println("Result:"+"发送邮件成功");
	
	}
	
	@JmsListener(destination = "htmlmail")
	public void sendHtmlMail(Map<String,String> map){
			System.out.println("接收信息 :"+map);
			String content="<html>\n"
					+ "<body>\n"
					+ "<h3>"+map.get("content")
					+"</body>\n"
					+ "</html>";
			
			mailUtil.sendHtmlMail(map.get("to"),map.get("subject"),content);
			System.out.println("Result:"+"发送html邮件成功");
	
	}
	
	@JmsListener(destination = "attachment_mail")
	public void sendAttachmentMail(Map<String,String> map){
			//System.out.println("接收信息 :"+map);	
			mailUtil.sendAttachmentMail(map.get("to"),map.get("subject"),map.get("content"), map.get("filePath"));
			//System.out.println("Result:"+"发送带附件邮件成功");
	
	}
	
	@JmsListener(destination = "picture_mail")
	public void sendPictureMail(Map<String,String> map){
			//System.out.println("接收信息 :"+map);	
			String content="<html>\n"
					+ "<body>\n"
					+ "<h3>"+"这是有图片的邮件:<img src=\'cid:"+map.get("rscId")
					+"\'></img> <img src=\'cid:"+map.get("rscId")+"\'></img></body>\n"
					+ "</html>";
			
			mailUtil.sendPictureMail(map.get("to"),map.get("subject"),content, map.get("filePath"), map.get("rscId"));
			//System.out.println("Result:"+"发送内嵌图片邮件成功");
	
	}

}
