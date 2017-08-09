package com.wjw.rabbitMq_Exchange.consumer.direct;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;
import com.wjw.rabbitMq.rabbitMq_workQueues.MyChannel;

public class Consumer_Direct1 {

	private static String Exchange_Name = "Exchange_Direct";
	private static String Exchange_Name_Black = "exchange_Black";
	private static String Queue_Name = "Queue_Direct1";
	public static void main(String[] args) throws Exception{
		Channel channel = MyChannel.getChannel("localhost");
		new Consumer_Direct1().direct_exchange_multiple_blindings(channel);
	}
	
	public void direct_multiple_blindings(Channel channel) throws Exception{
		channel.queueDeclare(Queue_Name, true, false, false, null);
		channel.exchangeDeclare(Exchange_Name, "direct");
		channel.queueBind(Queue_Name, Exchange_Name, "black");
		
		QueueingConsumer consumer = new QueueingConsumer(channel);
		//取消 autoAck
		channel.basicConsume(Queue_Name,false,consumer);  
		
		while(true){
			Delivery delivery = consumer.nextDelivery();
			String message = new String(delivery.getBody(), "UTF-8");  
			System.out.println("ReceiveLogsTopic1 [x] Received '" +"':'" + message + "'");  
			
			channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
		}
	}

	
	public void direct_exchange_multiple_blindings(Channel channel) throws Exception{
		if(null != channel){
			channel.queueDeclare(Queue_Name, true, false, false, null);
			//声明交换器
			channel.exchangeDeclare(Exchange_Name_Black, "direct", false);
			channel.queueBind(Queue_Name, Exchange_Name_Black, "Black");
			
			QueueingConsumer consumer = new QueueingConsumer(channel);
			//取消 autoAck
			channel.basicConsume(Queue_Name,false,consumer);  
			
			while(true){
				Delivery delivery = consumer.nextDelivery();
				String message = new String(delivery.getBody(), "UTF-8");  
				System.out.println("ReceiveLogsTopic1 [x] Received '" +"':'" + message + "'");  
				
				channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
			}
		}
	}
	
}
