package com.wjw.rabbitMq_Exchange.producer.direct;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.wjw.rabbitMq.rabbitMq_workQueues.MyChannel;

/**
 * @author Administrator
 *
 *交换器直连（direct）方式
 *
 *多队列绑定方式
 *
 */
public class Exchange_Direct {
	
	private static String Exchange_Name = "Exchange_Direct";
	private static String Exchange_Name_Black = "exchange_Black";
	private static String Exchange_Name_White = "exchange_White";
	private static String Exchange_Name_Orange = "exchange_Orange";
	private static String Queue_Name1 = "Queue_Direct1";
	private static String Queue_Name2 = "Queue_Direct2";
	public static void main(String[] args) throws Exception {
		Channel channel = MyChannel.getChannel("localhost");

		//多队列绑定
		new Exchange_Direct().direct_exchange_multiple_blindings(channel);
		
        MyChannel.close();
	}

	
	/**
	 * direct 方式多重绑定。
	 * 多队列绑定
	 */
	public void direct_multiple_blindings(Channel channel) throws Exception{
		if(null != channel){
			//第一个队列声明
			channel.queueDeclare(Queue_Name1, true, false, false, null);
			//第二个队列声明
			channel.queueDeclare(Queue_Name2, true, false, false, null);
			
			//声明一个交换器
			channel.exchangeDeclare(Exchange_Name, "direct");
			
			//交换器和队列的绑定
			channel.queueBind(Queue_Name1, Exchange_Name, "black");
			channel.queueBind(Queue_Name2, Exchange_Name, "black");
			
			for(int i = 1;i<10;i++){
				String message = "发送颜色black，id = "+i;  
		        channel.basicPublish(Exchange_Name,"black",null,message.getBytes()); //此处delete为路由键；  
		        System.out.println(" [x] Sent '"+ message+"'");  
		        Thread.sleep(1000);
			}
			
		}
	}
	
	
	/**
	 * direct 方式多重绑定。
	 * 单个队列绑定多个交换器
	 * @param channel
	 * @throws Exception
	 */
	public void direct_exchange_multiple_blindings(Channel channel) throws Exception{
		if(null != channel){
			
			String[] colours = new String[]{"Black","White","Orange"};
			
			//第一个队列声明
			channel.queueDeclare(Queue_Name1, true, false, false, null);
			//第二个队列声明
			channel.queueDeclare(Queue_Name2, true, false, false, null);
			
			//声明交换器
			channel.exchangeDeclare(Exchange_Name_Black, "direct", false);
			channel.exchangeDeclare(Exchange_Name_White, "direct", false);
			channel.exchangeDeclare(Exchange_Name_Orange, "direct", false);
			
			//队列和交换器绑定
			channel.queueBind(Queue_Name1, Exchange_Name_Black, "Black");
			channel.queueBind(Queue_Name2, Exchange_Name_White, "White");
			channel.queueBind(Queue_Name2, Exchange_Name_Orange, "Orange");
			
			for(int i = 0;i<100;i++){
				int index = new Random().nextInt(3);
				String message = "发送颜色"+colours[index]+"，id = "+i;
				channel.basicPublish("exchange_"+colours[index], colours[index], null, message.getBytes());
				System.out.println(" [x] Sent '"+ message+"'");  
		        Thread.sleep(1000);
			}
		}
	}
	
	
}
