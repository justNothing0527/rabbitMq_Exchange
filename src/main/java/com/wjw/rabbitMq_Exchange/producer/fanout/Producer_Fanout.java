package com.wjw.rabbitMq_Exchange.producer.fanout;

import com.rabbitmq.client.Channel;
import com.wjw.rabbitMq.rabbitMq_workQueues.MyChannel;

/**
 * @author Administrator
 *	fanout 类型exchange
 *任何发送到Fanout Exchange的消息都会被转发到与该Exchange绑定(Binding)的所有Queue上
 *
 *一个exchange可以绑定多个queue
 *一个queue可以绑定多个exchange
 *
 */
public class Producer_Fanout {

	private static String Queue_Name1 = "Queue_Fanout1";
	private static String Queue_Name2 = "Queue_Fanout2";
	private static String Queue_Name3 = "Queue_Fanout3";
	private static String Exchange_Name = "Exchange_Fanout";
	
	public static void main(String[] args) throws Exception{
		Channel channel = MyChannel.getChannel("localhost");
		new Producer_Fanout().sendMsgByFanout(channel);
		MyChannel.close();
	}

	public void sendMsgByFanout(Channel channel) throws Exception{
		//创建三个队列
		channel.queueDeclare(Queue_Name1, false, false, true, null);
		channel.queueDeclare(Queue_Name2, false, false, true, null);
		channel.queueDeclare(Queue_Name3, false, false, true, null);
		
		//创建fanout类型的交换器
		channel.exchangeDeclare(Exchange_Name, "fanout",false);
		
		//exchange和queue绑定
		for(int i = 1;i<4;i++){
			String queneName = "Queue_Fanout"+i;
			channel.queueBind(queneName, Exchange_Name, "");
		}
		
		System.out.println("发送消息开始");
		for(int i = 1;i<10;i++){
			String msg = "开会啦，在会议室"+i;
			channel.basicPublish(Exchange_Name, "", null, msg.getBytes());
			System.out.println("发送消息："+msg);
			Thread.sleep(3000);
		}
		
	}
	
}
