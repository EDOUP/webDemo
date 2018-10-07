package com.ed.sms;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;

@Component
public class SmsListener {
	
	@Autowired
	private SmsUtil smsUtil;
	
	@JmsListener(destination = "sms")
	public void sendSms(Map<String,String> map) {
		try {
			SendSmsResponse response = smsUtil.sendSms(map.get("mobile"), map.get("template_code"), map.get("sign_name"), map.get("template_param"));
			System.out.println("Code:"+response.getCode());
			System.out.println("Message:"+response.getMessage());
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
