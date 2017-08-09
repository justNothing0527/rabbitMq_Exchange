package com.wjw.rabbitMq_Exchange.consumer.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;
import com.wjw.rabbitMq.rabbitMq_workQueues.MyChannel;

public class Consumer_Topic2 {

	private static String Exchange_Name = "Exchange_Topic";
	private static String Queue_Name = "Queue_Topic";
	public static void main(String[] args) throws Exception {
		Channel channel = MyChannel.getChannel("localhost");
		new Consumer_Topic2().receiveMsg(channel);
	}

	public void receiveMsg(Channel channel) throws Exception{
		if(null != channel){
			channel.queueDeclare(Queue_Name, false, false, true, null);
			channel.exchangeDeclare(Exchange_Name, "topic");
			String[] routingKeys = new String[]{"*.*.rabbit", "lazy.#"};   
			//绑定路由关键字  
	        for (String bindingKey : routingKeys) {  
	            channel.queueBind(Queue_Name, Exchange_Name, bindingKey);  
	        }
			
			QueueingConsumer consumer = new QueueingConsumer(channel);
			channel.basicConsume(Queue_Name, false, consumer);
			
			while(true){
				Delivery delivery = consumer.nextDelivery();
				String msg = new String(delivery.getBody(),"utf-8");
				System.out.println("Received msg:" +msg+" --------routing key is "+ delivery.getEnvelope().getRoutingKey());  
				channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
			}
		}
	}

}
