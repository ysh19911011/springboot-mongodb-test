package net.poweroak.socket;


import java.io.IOException;

import javax.annotation.Resource;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import io.netty.channel.ChannelFuture;
import net.poweroak.entity.Demo;
import net.poweroak.service.DemoService;
import net.poweroak.utils.InstanceUtil;
import net.poweroak.utils.JSONSchemaValidator;
import net.poweroak.utils.Util;
@Component
public class NioSocketResponseImpl implements NioSocketServerResponse {
	@Resource
	private DemoService demoService;
	public void handle(String socketClientMessage) {
		System.out.println(socketClientMessage);
	}

	public String handleAndResponse(String socketClientMessage,String channelId) {
		System.out.println("处理消息："+socketClientMessage);
		boolean tf=JSONSchemaValidator.validatorSchema(socketClientMessage, InstanceUtil.getInstance(InstanceUtil.DEMO_INSTANCE));
		if(tf) {
			JSONObject jsonObject=JSON.parseObject(socketClientMessage);
			Demo demo=new Demo();
			demo=JSON.parseObject(String.valueOf(jsonObject.get("content")),new TypeReference<Demo>() {});
			if(Util.checkNotNull(demo)) {
				demo.setId(new ObjectId());
				demoService.save(demo);
				try {
					MyWebSocket.sendInfo("收到个消息");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("存储对象------------------"+demo);
			}
			
		}
		return "收到";
	}

	public void setEquipmentOnlineStatus(String eqpSn, String channelId) {
		
	}

	public ChannelFuture sendMessage(String channelId,String message) {
		return null;
//		ChannelHandlerContext ctx=ServerHandler.getServerChannel(channelId);
//		return ctx.writeAndFlush(message);
	}

}
