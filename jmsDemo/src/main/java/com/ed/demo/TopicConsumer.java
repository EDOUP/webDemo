package com.ed.demo;

import java.io.IOException;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

public class TopicConsumer {
	public static void main(String[] args) throws JMSException, IOException {
		//1.创建连接工厂
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://119.23.246.229:61616");
		//2.创建连接
		Connection connection = connectionFactory.createConnection();
		//3.启动连接
		connection.start();
		//4.获取session（获取对象） 
		Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);//参数1：是否启动事务   参数2：消息的确认方式
		//5.创建主题对象
		 Topic topic = session.createTopic("test-topic");
		//6.创建消息的消费者
		 MessageConsumer consumer = session.createConsumer(topic);
		//7.设置监听
		 consumer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				TextMessage textMessage=(TextMessage) message;
				try {
					System.out.println("从activeMq接收的消息:"+textMessage.getText());
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
		//8.等待键盘
		 System.in.read();//等待监听，不让资源提前关闭
		//9.关闭资源
		consumer.close();
		session.close();
		connection.close();
		
		
	}
}
