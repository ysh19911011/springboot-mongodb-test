package net.poweroak.socket;

import io.netty.channel.ChannelFuture;

/**
 * Socket服务端响应接口，需要在调用的App中进行实现
 * @Author 许亮
 * @Create 2016-7-12 23:35:14
 */
public interface NioSocketServerResponse {
	/**
	 * 处理Socket客户端发送过来的消息数据
	 * @param socketClientMessage Socket客户端发送过来的消息数据
	 */
	void handle(String socketClientMessage);
	
	/**
	 * 收到Socket客户端的消息后，处理该消息并返回处理结果
	 * @param socketClientMessage Socket客户端发送过来的消息数据
	 * @return
	 */
	String handleAndResponse(String socketClientMessage,String channelId);
	
	void setEquipmentOnlineStatus(String eqpSn, String channelId);
	
	ChannelFuture sendMessage(String channelId,String message);
}
