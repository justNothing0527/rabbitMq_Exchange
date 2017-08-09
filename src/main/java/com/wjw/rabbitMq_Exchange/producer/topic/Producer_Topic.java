package com.wjw.rabbitMq_Exchange.producer.topic;

import com.rabbitmq.client.Channel;
import com.wjw.rabbitMq.rabbitMq_workQueues.MyChannel;

/**
 * @author Administrator
 *任何发送到Topic Exchange的消息都会被转发到所有关心RouteKey中指定话题的Queue上
 */
public class Producer_Topic {
	
	private static String Exchange_Name = "Exchange_Topic";
	private static String Queue_Name = "Queue_Topic";
	public static void main(String[] args) throws Exception{
		Channel channel = MyChannel.getChannel("localhost");
		new Producer_Topic().send_topic(channel);
		MyChannel.close();
	}
	
	private void send_topic(Channel channel) throws Exception{
		//定义topic 类型的exchange
		channel.exchangeDeclare(Exchange_Name, "topic");
		
		// 待发送的消息  
        String[] routingKeys = new String[]{"quick.orange.rabbit",   
                                            "lazy.orange.elephant",   
                                            "quick.orange.fox",   
                                            "lazy.brown.fox",   
                                            "quick.brown.fox",   
                                            "quick.orange.male.rabbit",   
                                            "lazy.orange.male.rabbit"};
        
        //      发送消息  
        for(String severity :routingKeys){  
            String message = "发送一只 "+severity+" 这样的动物，请查收!";  
            channel.basicPublish(Exchange_Name, severity, null, message.getBytes());  
            System.out.println("TopicSend [x] Sent '" + severity + "':'" + message + "'");  
        }
	}

}
