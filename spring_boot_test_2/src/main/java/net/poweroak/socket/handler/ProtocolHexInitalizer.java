package net.poweroak.socket.handler;

import org.apache.log4j.Logger;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import net.poweroak.socket.NioSocketServerResponse;

/**
 * 专用16进制数据处理器，其中注册了decoder、handler、encoder三个处理器，并且有先后顺序。<br>
 * 服务端或客户端收到消息的处理顺序为：decoder → handler → encoder<br>
 * @Author 许亮
 * @Create 2016-7-12 16:24:38
 */
public class ProtocolHexInitalizer extends ChannelInitializer<SocketChannel> {
	private final Logger logger = Logger.getLogger(ProtocolHexInitalizer.class);
	private NioSocketServerResponse nioChannelResponse;
	
	public ProtocolHexInitalizer(NioSocketServerResponse nioChannelResponse) {
		this.nioChannelResponse = nioChannelResponse;
	}

	@Override
	protected void initChannel(SocketChannel socketChannel) throws Exception {
		ChannelPipeline channelPipeline = socketChannel.pipeline();
		channelPipeline.addLast("frameDecoder",new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4));
		channelPipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
		channelPipeline.addLast("decoder", new ByteArrayDecoder());
		channelPipeline.addLast("encoder", new ByteArrayEncoder());
		
		channelPipeline.addLast("handler", new ServerHandler() {

			@Override
			public void channelActive(ChannelHandlerContext ctx) throws Exception {
//				logger.error("发送指令："+Constants.COMMANDS[0]);
//				ctx.channel().writeAndFlush(Constants.COMMANDS[0]);
			}

			@Override
			protected void channelProcessor(ChannelHandlerContext ctx, byte[] message) {
				String response = nioChannelResponse.handleAndResponse(new String(message),ctx.channel().id().asLongText()); // 上层业务处理
//				ctx.channel().writeAndFlush(response);
				
			}
			
		});
	}
}
