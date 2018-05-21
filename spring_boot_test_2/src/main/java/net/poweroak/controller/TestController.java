package net.poweroak.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;

import net.poweroak.entity.Demo;
import net.poweroak.service.DemoService;

@Controller
@RequestMapping("/test")
public class TestController {
	@Resource
	private DemoService demoService;
	
	@RequestMapping("/test1/{str}")
	@ResponseBody
	public String test1(@PathVariable String str,Model model){
		System.out.println(str);
		return str;
	}
	/**
	 * 数据展示页面跳转
	 * @param model
	 * @return
	 */
	@RequestMapping("chartPage")
	public String chartPage(Model model) {
		return "/chart";
	}
	@RequestMapping("showChart")
	@ResponseBody
	public JSONObject showChart() {
		List<Demo> demos=demoService.find(new Query());
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		/**
		 * 封装值
		 */
		List<String> dates = new ArrayList<String>();
		List<Object> datas = new ArrayList<Object>();
		Set<String> names = new HashSet<String>();
		//查询出结果集
		String nameStr = "";
		for (Demo demo:demos) {
			dates.add(demo.getDate());
			names.add(demo.getName());
			datas.add(demo.getValue());
			nameStr = demo.getName();
		}
		JSONObject json = new JSONObject();
		if(datas.size()>0){
			json.put("name",nameStr);
			json.put("type","line");
			json.put("smooth",true);
			json.put("symbol","none");
			json.put("sampling","average");
			json.put("yAxisIndex:",1);
			json.put("itemStyle", JSONObject.parse("{normal: {color: '"+getColor(1)[1]+"'}}")); 
			json.put("areaStyle", JSONObject.parse("{normal: {color:'"+getColor(1)[0]+"'}}"));// image: imageDom, repeat: 'repeat'// 支持为 HTMLImageElement, HTMLCanvasElement，不支持路径字符串
			json.put("data", datas);
			jsonArray.add(json);
		}
		jsonObject.put("datas", jsonArray);
		jsonObject.put("dates",dates);
		jsonObject.put("names", names);
		return jsonObject;
	}
	private String[] getColor(Integer value){
		Map<Integer,String[]> colorMap=new HashMap<Integer, String[]>();
		String[] color1={"#e8fafc","rgb(30,205,226)","rgb(30,205,226,0.1)"};
		String[] color2={"#f6e8fd","rgb(165,31,234)","rgb(165,31,234,0.1)"};
		String[] color3={"#fff5e5","rgb(255,153,0)","rgb(255,153,0,0.1)"};
		String[] color4={"#f5faea","rgb(153,204,51)","rgb(153,204,51,0.1)"};
		String[] color5={"#fce8e8","rgb(226,30,30)","rgb(226,30,30,0.1)"};
		String[] color6={"default","default","default"};
		colorMap.put(0, color6);
		colorMap.put(1, color1);
		colorMap.put(2, color2);
		colorMap.put(3, color3);
		colorMap.put(4, color4);
		colorMap.put(5, color5);
		colorMap.put(6, color6);
		return colorMap.get(value);
	}
}
