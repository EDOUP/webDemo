package com.ed.demo;

import java.io.File;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Component
public class MailUtil {
	
	private final Logger logger=LoggerFactory.getLogger(this.getClass());
	
	@Value("${spring.mail.username}")
	private String from;
	@Autowired
	private JavaMailSender mailSender;
	
	/**
	 * 发送普通邮件
	 * @param to 目的地
	 * @param subject 主题
	 * @param content 内容
	 */
	public void sendSimpleMail(String to,String subject,String content) {
		
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);
		message.setText(content);
		message.setFrom(from);
		
		mailSender.send(message);
	}
	
	/**
	 * 发送Html邮件
	 * @param to 目的地
	 * @param subject 主题
	 * @param content 内容
	 * @throws MessagingException 
	 */
	public void sendHtmlMail(String to,String subject,String content){
		logger.info("发送HTML邮件开始:\n{},\n{},\n{}",to,subject,content);
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(message,true);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(content,true);
			helper.setFrom(from);
			mailSender.send(message);
			logger.info("发送HTML邮件成功");
		} catch (MessagingException e) {
			logger.error("发送HTML邮件失败:",e);
		}
	};
	/**
	 * 发送附件邮件
	 * @param to 目的地
	 * @param subject 主题
	 * @param content 内容
	 * @throws MessagingException 
	 */
	public void sendAttachmentMail(String to,String subject,String content,String filePath) {
		logger.info("发送带附件邮件开始:\n{},\n{},\n{},\n{}",to,subject,content,filePath);
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(message,true);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(content,true);
			helper.setFrom(from);
			
			FileSystemResource file = new FileSystemResource(new File(filePath));
			String fileName=file.getFilename();
			helper.addAttachment(fileName, file);
			mailSender.send(message);
			logger.error("发送带附件邮件成功");
		} catch (MessagingException e) {
			logger.error("发送带附件邮件失败:",e);
		}
	}
	
	public void sendPictureMail(String to,String subject,String content,String filePath,String rscId){
		
		logger.info("发送静态文件开始:\n{},\n{},\n{},\n{},\n{}",to,subject,content,filePath,rscId);
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(message,true);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(content,true);
			helper.setFrom(from);
			
			FileSystemResource file = new FileSystemResource(new File(filePath));
			String fileName=file.getFilename();
			helper.addInline(rscId, file);
			helper.addInline(rscId, file);
			mailSender.send(message);
			logger.info("发送静态邮件成功");
		} catch (MessagingException e) {
			
			logger.error("发送静态邮件失败:",e);
		}
	}
	
	
}
