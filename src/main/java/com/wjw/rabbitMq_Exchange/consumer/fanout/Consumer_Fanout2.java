package com.wjw.rabbitMq_Exchange.consumer.fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;
import com.wjw.rabbitMq.rabbitMq_workQueues.MyChannel;

public class Consumer_Fanout2 {

	private static String Queue_Name2 = "Queue_Fanout2";
	private static String Exchange_Name = "Exchange_Fanout";
	public static void main(String[] args) throws Exception{
		Channel channel = MyChannel.getChannel("localhost");
		new Consumer_Fanout2().recevieMsg(channel);
	}

	public void recevieMsg(Channel channel) throws Exception{
		if(null != channel){
			channel.queueDeclare(Queue_Name2, false, false, true, null);
			channel.queueBind(Queue_Name2, Exchange_Name, "");
			QueueingConsumer consumer = new QueueingConsumer(channel);
			channel.basicConsume(Queue_Name2, false, consumer);
			while(true){
				Delivery delivery = consumer.nextDelivery();
				String msg = new String(delivery.getBody(),"utf-8");
				System.out.println("Received msg:" +msg);  
				channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
			}
		}
	}
	

}
