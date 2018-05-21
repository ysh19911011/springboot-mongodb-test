package net.poweroak.socket.handler;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.group.ChannelGroupFutureListener;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import net.poweroak.utils.Util;

/**
 */
public abstract class ServerHandler extends SimpleChannelInboundHandler<String> {
	private static final Logger logger = LoggerFactory.getLogger(ServerHandler.class);
	/**
	 * key:ChannelId.asLongText  value:ChannelHandlerContext
	 */
	private static Map<String,ChannelHandlerContext> map=new ConcurrentHashMap<String,ChannelHandlerContext>();
	/**
	 * key:userId,用户连接的时候发送的   value:channelId
	 */
	protected static Map<String,String> id2ChannelMap=new ConcurrentHashMap<String,String>();
	
	/**
	 * 连接会话管理器 */
	protected ChannelGroup channelManager = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	
	/**
	 * 服务端业务处理器
	 * @param ctx
	 * @param message 消息内容
	 */
	protected abstract void channelProcessor(ChannelHandlerContext ctx, byte[] message);
	
	/**
	 * 收到新的客户端连接
	 */
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		channelManager.add(ctx.channel());
		String uuid=ctx.channel().id().asLongText();
		ServerHandler.addChannelMap(uuid, ctx);
		System.out.println("a new user come in..."+uuid);
		System.out.println(map);
		ChannelGroupFuture result=sendAll("已经连接上服务器",channelManager);
		result.addListener(new ChannelGroupFutureListener() {

			public void operationComplete(ChannelGroupFuture future) throws Exception {
				if(future.isSuccess()){
					System.out.println("消息发送成功");
				}
				
			}
		});
		super.handlerAdded(ctx);
	}
	
	/**
	 * 客户端断开连接
	 */
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		channelManager.remove(ctx.channel());
		ServerHandler.removeServerChannel(ctx.channel().id().asLongText());
		ServerHandler.removeId2Channle(ctx.channel().id().asLongText());
		super.handlerRemoved(ctx);
	}
	
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("Client Connected: " + ctx.channel().remoteAddress() + ", Session Id: " + ctx.channel().id());
		}
	}
	
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("Client DisConnected: "+ctx.channel().id().asLongText());
		}
		
		super.channelInactive(ctx);
    }
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx){
//		System.out.println("服务端读取消息完毕");
		ctx.fireChannelReadComplete();
	}
	@Override
	public void channelRead(ChannelHandlerContext ctx,Object message){
		byte[] bytes=(byte[])message;
		channelProcessor(ctx,bytes);
		System.out.println(new String((byte[])message));
	}
	/**
	 * 读取客户端消息
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String message) throws Exception {
//		channelProcessor(ctx, message);
		logger.error("channelRead0");
	}
	protected static void addChannelMap(String channelId,ChannelHandlerContext ctx){
		map.put(channelId, ctx);
	}
	protected static ChannelHandlerContext getServerChannel(String channelId){
		return map.get(channelId);
	}
	protected static void removeServerChannel(String channelId){
		map.remove(channelId);
	}
	protected static void addId2Channel(String id,String channelId){
		id2ChannelMap.put(id,channelId);
	}
	protected static String id2Channel(String id){
		return id2ChannelMap.get(id); 
	}
	protected static void removeId2Channle(String channelId){
		Iterator<Entry<String, String>> iteratorCharger = id2ChannelMap.entrySet().iterator();
		String deleteKey = null;
		while (iteratorCharger.hasNext()) {
			Entry<String, String> entry = (Entry) iteratorCharger.next();
			if(entry.getValue().equals(channelId)){
				deleteKey=entry.getKey();
			}
		}
		if(Util.checkNotNull(deleteKey)){
			System.out.println(deleteKey+"is leave");
			id2ChannelMap.remove(deleteKey);
		}
	}
	/**
	 * 发送消息
	 * @param userId
	 * @param message
	 * @return
	 */
	public static ChannelFuture sendMessage(String userId,String message){
		String channelId=ServerHandler.id2Channel(userId);
		ChannelHandlerContext ctx=ServerHandler.getServerChannel(channelId);
		byte[] bytes=message.getBytes();
		if(Util.checkNotNull(ctx)) {
			return ctx.writeAndFlush(bytes);
		}
		return null;
		
	}
	public static ChannelGroupFuture sendAll(String message,ChannelGroup group){
		byte[]bytes=message.getBytes();
		return group.writeAndFlush(bytes);
	}
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		super.exceptionCaught(ctx, cause);
		cause.printStackTrace();
		ctx.close();
	}
}
