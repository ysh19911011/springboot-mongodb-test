package com.poweroak.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class HexUtil {
	
	//一个数据包最大字节数
	private int len=512;
	//解析的文件
	private File file;
	
	//标识头1
	private final int IDENTITY_1=0xEE;
	//标识头2
	private final int IDENTITY_2=0xAA;
	//源设备
	private final int SOURCE_DEVICE=0x00;
	//目标设备
	private final int TARGER_DEVICE=0x04;
	//功能码
	private final int FUNCTION_CODE=0x07;
	//最大基地址
	private final int MAX_BASESITE=0x33;
	//最大偏移地址
	private final int MAX_OFFSET=0x7FFF;
	//基地址  读第一行定义
	private int baseSite;
	//偏移地址  每输出一个数据包 增len
	private int offset=0;
	//包序号  每输出一个数据包 增1
	private int dataSerial=1;
	//当前读取行数
	private int currentRow=0;
	//存储已读实际数据  输出数据包后清空
	private String data="";
	//hex文件实际数据总长度
	private int totalLength=0;
	//存储连续地址数
	private int continuousSign=0;
	
	public HexUtil(int len, File file) {
		super();
		this.len = len;
		this.file = file;
	}
	public List<String> parseHexFile() {
		//当前累计数据长度  超过规定最大长度后输出数据并清零
		int currentNum=0;
		//剩余数据长度 
		int remainNum=0;
		//获取总实际数据长度
		getTotalLength();
		
		//存放数据包
		List<String> dataList=new ArrayList<String>();
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        try {
            inputStream = new FileInputStream(file);
            //转成 reader 以 行 为单位读取文件
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            //当前行字符串
            String str=null;
            while((str=bufferedReader.readLine())!=null) {
            	if(currentRow==0&&"04".equals(str.substring(7, 9))) {
            		//取第一行基地址
            		baseSite=Integer.parseInt(str.substring(9, 11)+str.substring(11, 13), 16);
            	}
            	currentRow++;
            	//04标识开始
        		if("04".equals(str.substring(7, 9))) {
        			
        			if(currentRow!=1) {
        				//读到第二个04时,不论当前已有多少数据,输出后重置
        				if(!data.isEmpty()) {
        					createData(currentNum,dataList);
        					data="";
        					dataSerial++;
        					remainNum=0;
        				}
    					//剩余数据赋为当前长度后归零
    					baseSite=Integer.parseInt(str.substring(9, 11)+str.substring(11, 13), 16);
    					//判断出现04功能码时  基地址是否到0033 到了0030 则开始根据偏移地址判断读到哪  没到还按照循环逻辑继续读  
    					if(baseSite<MAX_BASESITE) {
        					continue;
        				}else {
        					//基地址变了之后  重新计数  第一行时 偏移地址有变化不输出
        					currentRow=0;
        					while((str=bufferedReader.readLine())!=null) {
        						currentRow++;
        						//代表当前行数据长度字符串
        	        			String lengthSign=str.substring(1, 3);
        	        			int length=Integer.parseInt(lengthSign,16);
        	        			//临时存放  用来判断文件中偏移地址是否连续
        	        			int tempSign=continuousSign;
        	        			//当前行3-7位 判断地址连续
        	        			int currentSign=Integer.parseInt(str.substring(3, 7),16);
        	        			//先判断当前行是否超出最大偏移地址  如果超过  输出包后 跳出循环
        	        			if(currentSign>MAX_OFFSET) {
        	        				createData(currentNum,dataList);
        	    					offset=currentSign;
        	    					dataSerial++;
        	    					break;
        	        			}
        	        			//不管是否连续  都要换成计算下一行的值
        	        			continuousSign=currentSign+length/2;
        	        			/**
        	        			 * 判断上下行地址是否连续  上一行Integer.parseInt(str.substring(3, 7),16)+上一行的数据长度(字节)/2 是否跟此行Integer.parseInt(str.substring(3, 7),16)相等,
        	        			 * 若相等则连续   不相等则输出一包,从此行开始下一包
        	        			 */
        	    				if(currentSign!=tempSign) {
        	    					//不连续
        	    					if(currentRow!=1) {
        	    						createData(currentNum,dataList);
        	    						dataSerial++;
        	    					}
        	    					data=str.substring(9, 9+length*2);
        	    					currentNum=length;
        	    					offset=currentSign;
        	    					continue;
        	    				}
        	    				
        	    				if(currentNum+length<=len) {
        	    					currentNum+=length;
        	    					data+=str.substring(9, 9+length*2);
        	    				}else {
        	    					remainNum=currentNum+length-len;
        	    					data+=str.substring(9, 9+(length-remainNum)*2);
        	    					currentNum=len;
        	    					//输出一条数据  偏移地址递增
        	    					createData(currentNum,dataList);
        	    					//剩余数据赋为当前长度后归零
        	    					currentNum=remainNum;
        	    					data=str.substring(9+(length-remainNum)*2, 9+length*2);
        	    					remainNum=0;
        	    					offset+=len/2;
        	    					if(offset>=0xFFFF) {
        	    						baseSite+=1;
        	    						offset=0;
        	    					}
        	    					dataSerial++;
        	    				}
        	    				
            				}
        				}
//    					//读一行后完毕
//    					if((str=bufferedReader.readLine())!=null) {
//    						//变更当前基地址与偏移地址
//        					baseSite=temp;
//        					offset=Integer.parseInt(str.substring(3,5)+str.substring(5,7),16);
//        					//输出最后一行
//        					int length=Integer.parseInt(str.substring(1, 3),16);
//        					currentNum=length;
//        					data=str.substring(9, 9+length*2);
//        					createData(currentNum,dataList);
//        					dataSerial++;
//        				}
//    					if((str=bufferedReader.readLine())!=null) {
//    						//变更当前基地址与偏移地址
//        					baseSite=temp;
//        					offset=Integer.parseInt(str.substring(3,5)+str.substring(5,7),16);
//        					//输出最后一行
//        					int length=Integer.parseInt(str.substring(1, 3),16);
//        					currentNum=length;
//        					data=str.substring(9, 9+length*2);
//        					createData(currentNum,dataList);
//        					offset+=length/2;
//        					dataSerial++;
//        				}
        				break;
        			}
        			
        		}else if("01".equals(str.substring(7, 9))) {
        			//01标识结束 输出当前数据  结束循环
        			createData(currentNum,dataList);
//        			System.out.println("********************************解析结束**************************************************");
        			break;
        		}else {
        			//代表当前行数据长度字符串
        			String lengthSign=str.substring(1, 3);
        			int length=Integer.parseInt(lengthSign,16);
        			//临时存放  用来判断
        			int tempSign=continuousSign;
        			//当前行3-7位 判断地址连续
        			int currentSign=Integer.parseInt(str.substring(3, 7),16);
        			//不管是否连续  都要换成计算下一行的值
        			continuousSign=currentSign+length/2;
        			/**
        			 * 判断上下行地址是否连续  上一行Integer.parseInt(str.substring(3, 7),16)+上一行的数据长度(字节)/2 是否跟此行Integer.parseInt(str.substring(3, 7),16)相等,
        			 * 若相等则连续   不相等则输出一包,从此行开始下一包
        			 */
    				if(currentSign!=tempSign) {
    					createData(currentNum,dataList);
    					data=str.substring(9, 9+length*2);
    					currentNum=length;
    					offset=currentSign;
    					dataSerial++;
    					continue;
    				}
    				
    				if(currentNum+length<=len) {
    					currentNum+=length;
    					data+=str.substring(9, 9+length*2);
    				}else {
    					remainNum=currentNum+length-len;
    					data+=str.substring(9, 9+(length-remainNum)*2);
    					currentNum=len;
    					//输出一条数据  偏移地址递增
    					createData(currentNum,dataList);
    					//剩余数据赋为当前长度后归零
    					currentNum=remainNum;
    					data=str.substring(9+(length-remainNum)*2, 9+length*2);
    					remainNum=0;
    					offset+=len/2;
    					if(offset>=0xFFFF) {
    						baseSite+=1;
    						offset=0;
    					}
    					dataSerial++;
    				}
//        			}
        		}
        		
            }
        }catch (Exception e) {
			// TODO: handle exception
        	e.printStackTrace();
		}finally {
			if(Util.checkNotNull(inputStream)) {
				try {
					inputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(Util.checkNotNull(bufferedReader)) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return dataList;
	}
	/**
	 * 获取hex文件数据总长度
	 */
	private void getTotalLength() {
		//当前累计数据长度  超过规定最大长度后输出数据并清零
		int currentNum=0;
		//剩余数据长度 
		int remainNum=0;
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        try {
            inputStream = new FileInputStream(file);
            //转成 reader 以 行 为单位读取文件
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            //当前行字符串
            String str=null;
            while((str=bufferedReader.readLine())!=null) {
            	if(currentRow==0&&"04".equals(str.substring(7, 9))) {
            		//取第一行基地址
            		baseSite=Integer.parseInt(str.substring(9, 11)+str.substring(11, 13), 16);
            	}
            	
            	//04标识开始
        		if("04".equals(str.substring(7, 9))) {
        			if(currentRow!=0) {
        				totalLength++;
    					//剩余数据赋为当前长度后归零
        				baseSite=Integer.parseInt(str.substring(9, 11)+str.substring(11, 13), 16);
    					//判断出现04功能码时  基地址是否到0033 到了0030 则开始根据偏移地址判断读到哪  没到还按照循环逻辑继续读  
    					if(baseSite<MAX_BASESITE) {
        					continue;
        				}else {
        					currentRow=0;
        					while((str=bufferedReader.readLine())!=null) {
        						currentRow++;
        						//代表当前行数据长度字符串
        	        			String lengthSign=str.substring(1, 3);
        	        			int length=Integer.parseInt(lengthSign,16);
        	        			//临时存放  用来判断文件中偏移地址是否连续
        	        			int tempSign=continuousSign;
        	        			//当前行3-7位 判断地址连续
        	        			int currentSign=Integer.parseInt(str.substring(3, 7),16);
        	        			//先判断当前行是否超出最大偏移地址  如果超过  输出包后 跳出循环
        	        			if(currentSign>MAX_OFFSET) {
        	        				totalLength++;
        	    					break;
        	        			}
        	        			//不管是否连续  都要换成计算下一行的值
        	        			continuousSign=currentSign+length/2;
        	        			/**
        	        			 * 判断上下行地址是否连续  上一行Integer.parseInt(str.substring(3, 7),16)+上一行的数据长度(字节)/2 是否跟此行Integer.parseInt(str.substring(3, 7),16)相等,
        	        			 * 若相等则连续   不相等则输出一包,从此行开始下一包
        	        			 */
        	        			
        	        			//不管是否连续  都要换成下一行的计算值
        	        			continuousSign=currentSign+length/2;
        	    				if(currentSign!=tempSign) {
        	    					if(currentRow!=1) {
        	    						totalLength++;
        	    					}
        	    					currentNum=length;
        	    					continue;
        	    				}
        	    				if(currentNum+length<=len) {
        	    					currentNum+=length;
        	    				}else {
        	    					remainNum=currentNum+length-len;
        	    					currentNum=len;
        	    					//输出一条数据  偏移地址递增
        	    					totalLength++;
        	    					//剩余数据赋为当前长度后归零
        	    					currentNum=remainNum;
        	    					remainNum=0;
        	    				}
        	    				
            				}
        				}
    					//读一行后完毕
//    					if((str=bufferedReader.readLine())!=null) {
//        					totalLength++;
//        				}
//    					if((str=bufferedReader.readLine())!=null) {
//        					totalLength++;
//        				}
        				break;
        			}
        			
        		}else if("01".equals(str.substring(7, 9))) {
        			//01标识结束 输出当前数据  结束循环
        			totalLength++;
//		        			System.out.println("********************************解析结束**************************************************");
        			break;
        		}else {
        			//代表当前行数据长度字符串
        			String lengthSign=str.substring(1, 3);
        			
        			int length=Integer.parseInt(lengthSign,16);
        			
        			int currentSign=Integer.parseInt(str.substring(3, 7),16);
        			
        			//临时存放  用来判断
        			int tempSign=continuousSign;
        			
        			//不管是否连续  都要换成下一行的计算值
        			continuousSign=currentSign+length/2;
    				if(currentSign!=tempSign) {
    					totalLength++;
    					currentNum=length;
    					continue;
    				}
    				if(currentNum+length<=len) {
    					currentNum+=length;
    				}else {
    					remainNum=currentNum+length-len;
    					currentNum=len;
    					//输出一条数据  偏移地址递增
    					totalLength++;
    					//剩余数据赋为当前长度后归零
    					currentNum=remainNum;
    					remainNum=0;
    				}
//		        			}
        		}
        		currentRow++;
            }
        }catch (Exception e) {
			// TODO: handle exception
        	e.printStackTrace();
		}finally {
			if(Util.checkNotNull(inputStream)) {
				try {
					inputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(Util.checkNotNull(bufferedReader)) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
        //用于逻辑中判断   跑完后初始化
        continuousSign=0;
        baseSite=0;
        currentRow=0;
//        String totalData="";
//        //读到第二个04时 保存下一行数据长度
//        Integer lastLength=0;
//        try {
//            inputStream = new FileInputStream(file);
//            //转成 reader 以 行 为单位读取文件
//            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//            //当前行字符串
//            String str=null;
//            while((str=bufferedReader.readLine())!=null) {
//            	
//            	//04标识开始
//        		if("04".equals(str.substring(7, 9))) {
//        			if(currentRow==0) {
//        				continue;
//        			}else {
//        				if((str=bufferedReader.readLine())!=null) {
//        					String lengthSign=str.substring(1, 3);
//            				lastLength=Integer.parseInt(lengthSign,16);
//            				totalData+=str.substring(9, 9+lastLength*2);
//        				}
//        				break;
//        			}
//        		}else if("01".equals(str.substring(7, 9))) {
//        			break;
//        		}else {
//        			//代表当前行数据长度字符串
//        			String lengthSign=str.substring(1, 3);
//    				int length=Integer.parseInt(lengthSign,16);
//    				totalData+=str.substring(9, 9+length*2);
//        		}
//        		currentRow++;
//        		
//            }
//            //初始化baseSite和currentRow
//            
//        }catch (Exception e) {
//			// TODO: handle exception
//        	e.printStackTrace();
//		}
//        if(totalData.length()%len==0) {
//        	totalLength=(totalData.length()/2-lastLength)/len+1; 
//        }else {
//        	totalLength=(totalData.length()/2-lastLength)/len+2; 
//        }
       
	}
	/**
	 * 生成数据包
	 * @param dataLength
	 * @param dataList
	 */
	private void createData(int dataLength,List<String> dataList) {
		String frameHeader="";
		String dataArea="";
		String validArea="";
		frameHeader=byteHex(IDENTITY_1)+" "+byteHex(IDENTITY_2)+" "+
				byteHex(SOURCE_DEVICE)+" "+byteHex(TARGER_DEVICE)+" "+
				byteHex(FUNCTION_CODE)+" "+hexLowAndHigh(totalLength);
		dataArea=hexLowAndHigh(dataSerial)+" "+hexLowAndHigh(offset)+" "+hexLowAndHigh(baseSite)+" "+
				hexLowAndHigh(dataLength/2)+" "+formatData(data);
		dataArea+=checkSum(dataArea);
		validArea=checkSum(frameHeader+" "+dataArea);
		dataList.add(frameHeader+" "+dataArea+" "+validArea);
	}
	/**
	 * 10进制转为低八位+高八位16进制字符串
	 * @param num
	 * @return
	 */
	private String hexLowAndHigh(int num) {
		num=num&0xFFFF;
		int high=num>>8;
		int low=num&0xFF;
		return byteHex(low)+" "+byteHex(high);
	}
	/**
	 * 8位16进制数补0
	 * @param num
	 * @return
	 */
	private static String byteHex(int num) {
		String hexNum="";
		if(num<16) {
			hexNum="0"+Integer.toHexString(num).toUpperCase();
		}else {
			hexNum=Integer.toHexString(num).toUpperCase();
		}
		
		return hexNum;
	}
	/**
	 * 数据区校验
	 */
//	private String dataSum(int dataLength) {
//		char[] chars=data.toCharArray();
//		int sum=0;
//		sum+=offset&0xFF+offset>>8;
//		sum+=baseSite&0xFF+baseSite>>8;
//		sum+=dataLength&0xFF+dataLength>>8;
//		for(int i=1;i<chars.length;i+=2) {
//			sum+=Integer.parseInt(""+chars[i-1]+chars[i],16);
//		}
//		return hexLowAndHigh(sum&0xFFFF);
//	}
	/**
	 * 将字符串数据分为AA BB CC形式
	 * @param data
	 * @return
	 */
	private String formatData(String data) {
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<data.length();i+=2) {
			sb.append(data.substring(i,i+2)).append(" ");
		}
		return sb.toString();
	}
	//16位 补0
//	private String twoByteHex(int num) {
//		String hexStr=Integer.toHexString(num).toUpperCase();
//		if(hexStr.length()<4) {
//			int temp=4-hexStr.length();
//			for(int i=0;i<temp;i++) {
//				hexStr="0"+hexStr;
//			}
//		}
//		return hexStr;
//	}
	//包 校验区校验和
	private String checkSum(String str) {
		String[] nums=str.split(" ");
		int sum=0;
		for(int i=2;i<nums.length;i++) {
			if(!EmptyUtil.isNullOrEmpty(nums[i])) {
				sum+=Integer.parseInt(nums[i],16);
			}
		}
		return hexLowAndHigh(sum&0xFFFF);
	}
	
	
	
	public static void main(String[] args) throws FileNotFoundException {
		List<String>list=new HexUtil(512,new File("C:\\Users\\admin\\Desktop\\DSPv66.hex")).parseHexFile();
		for(int i=188;i<list.size();i++) {
//			if(i==203) {
				System.out.println(list.get(i));
//			}
		}
		System.out.println(list.size());
	}
}
