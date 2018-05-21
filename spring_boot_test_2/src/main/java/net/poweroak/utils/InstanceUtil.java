package net.poweroak.utils;

import java.io.FileReader;
import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonNodeReader;

public class InstanceUtil {
	/**
	 * demo json实例常量
	 */
	public static final int DEMO_INSTANCE=0;
	
	public static JsonNode getInstance(int type) {
		JsonNode instance=null;
		switch (type) {
		case DEMO_INSTANCE:
			instance=readJsonFile("src/main/resources/schema_test.json");
			break;

		default:
			instance=readJsonFile("src/main/resources/schema_test.json");
			break;
		}
		return instance;
		
	}
	private static JsonNode readJsonFile(String filePath) {
		  JsonNode instance = null;
		  try {
		    instance = new JsonNodeReader().fromReader(new FileReader(filePath));
		  } catch (IOException e) {
			  System.out.println("获取instance异常");
		    e.printStackTrace();
		  }
		  return instance;
		}
}
