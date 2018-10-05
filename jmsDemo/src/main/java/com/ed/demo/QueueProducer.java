package com.ed.demo;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
/**
 * 点对点模式
 * @author Administrator
 *
 */
public class QueueProducer {
	
	public static void main(String[] args) throws JMSException {
		//1.创建连接工厂
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://119.23.246.229:61616");
		//2.创建连接
		Connection connection = connectionFactory.createConnection();
		//3.启动连接
		connection.start();
		//4.获取session（获取对象） 
		Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);//参数1：是否启动事务   参数2：消息的确认方式
		//5.创建队列对象
		Queue queue = session.createQueue("test-queue");
		//6.创建消息的生产者
		MessageProducer producer = session.createProducer(queue);
		//7.创建消息对象（文本消息）
		TextMessage textMessage = session.createTextMessage("品优购jms Demo 测试:点对点模式");
		//8.发送消息
		producer.send(textMessage);
		//9.关闭资源
		producer.close();
		session.close();
		connection.close();
		
		
	}
	

}
