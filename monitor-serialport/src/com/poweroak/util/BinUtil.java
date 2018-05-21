package com.poweroak.util;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * bin解析
 * 
 * @author wsgen
 *
 */
public class BinUtil {
	/**
	 * 目标地址
	 */
	private String target = "03";
	/**
	 * 源文件
	 */
	private File file;
	/**
	 * 每包的长度
	 */
	private Integer size;
	/**
	 * bin总长度
	 */
	private Integer sum=0;
	
	/**
	 * 包序号
	 */
	private Integer index = 0;

	/**
	 * 包地址
	 */
	private Integer address = 0;

	public BinUtil(String target, File file, Integer size) {
		super();
		this.target = target;
		this.file = file;
		this.size = size;
	}

	/**
	 * 读取并解析bin文件
	 * 
	 * @param file
	 * @param len
	 * @return
	 */
	public List<String> parseBin() {
		List<String> result = new ArrayList<String>();
		DataInputStream is = null;
		try {
			is = new DataInputStream(new FileInputStream(file));
			byte[] buff = new byte[size];
			int len;
			while ((len = is.read(buff)) != -1) {
				sum++;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (Util.checkNotNull(is)) {
					is.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			is = new DataInputStream(new FileInputStream(file));
			byte[] buff = new byte[size];
			int len;
			while ((len = is.read(buff)) != -1) {
				StringBuffer sb = new StringBuffer();
				index++;
				if(len<size){
					byte[] temp = new byte[len];
					System.arraycopy(buff, 0, temp, 0, len);
					sb.append(assemble(toHex(temp),len));
				}else{
					sb.append(assemble(toHex(buff),size));
				}
				address+=size;
				result.add(sb.toString());
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(Util.checkNotNull(is)) {
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	/**
	 * 组装包
	 * @param datas
	 * @param len
	 * @return
	 */
	private String assemble(String datas, int len) {
		StringBuffer result = new StringBuffer();
		result.append("EE ").append("AA ");
		StringBuffer buffer = new StringBuffer();
		//拼接针头
		buffer.append("00 ").append(target).append(" ").append("03 ").append(dataLength(sum, true)).append(" ");
		//拼接包体：数据包序号(低8位+高8位)+地址(32位，低16位(低8位+高8位)+高16位(低8位+高8位))+数据长度(低8位+高8位)+数据包512字节或1024字节
		buffer.append(dataLength(index, true)).append(" ");
		StringBuffer pack = new StringBuffer();
		pack.append(dataAddress(address)).append(" ").append(dataLength(len, true)).append(" ").append(datas).append(packChk(pack.toString())).append(" ");
		buffer.append(pack);
		//拼接chk
		buffer.append(sumChk(buffer.toString()));
		result.append(buffer);
		//拼接和chk
		return result.toString();
	}

	/**
	 * 和校验
	 * @param buffer
	 * @return
	 */
	private String sumChk(String buffer) {
		Integer chkSum = 0;
		String[] packs = buffer.split(" ");
		for (String str : packs) {
			if(Util.checkNotNull(str)){
				chkSum += Integer.parseInt(str, 16);
			}
		}
		String hex = Integer.toHexString(chkSum);
		for (int i = hex.length(); i < 8; i=hex.length()) {
			hex = "0"+hex;
		}
		String low = hex.substring(4, hex.length());
		low = low.substring(2, low.length())+" "+low.substring(0,2);
		return low;
	}

	/**
	 * 每包的校验
	 * @param pack
	 * @return
	 */
	private String packChk(String pack) {
		Integer chkSum = 0;
		String[] packs = pack.split(" ");
		for (String str : packs) {
			if(Util.checkNotNull(str)){
				chkSum += Integer.parseInt(str, 16);
			}
		}
		return dataLength(chkSum, true);
	}

	/**
	 * 转16进制
	 * @param result
	 * @return
	 */
	private String toHex(byte[] result) { 
	  StringBuffer sb = new StringBuffer(result.length * 2); 
	  for (int i = 0; i < result.length; i++) { 
	    sb.append(Character.forDigit((result[i] & 240) >> 4, 16)); 
	    sb.append(Character.forDigit(result[i] & 15, 16)); 
	    sb.append(" ");
	  } 
	  return sb.toString(); 
	} 

	/**
	 * 数据长度
	 * @param s
	 * @param reverse
	 * @return
	 */
	private String dataLength(Integer s,boolean reverse){
		String hex = Integer.toHexString(s);
		if(hex.length()<4){
			for (int i = hex.length(); i < 4; i=hex.length()) {
				hex = "0"+hex;
			}
		}else if(hex.length()>4){
			//取低8位
			hex = hex.substring(hex.length()-4);
		}
		if(reverse){
			hex = hex.substring(2, hex.length())+" "+hex.substring(0,2);
		}else{
			hex = hex.substring(0,2)+" "+hex.substring(2, hex.length());
		}
		return hex;
	}
	/**
	 * 数据地址
	 * @param s
	 * @param reverse
	 * @return
	 */
	private String dataAddress(Integer s){
		String hex = Integer.toHexString(s);
		for (int i = hex.length(); i < 8; i=hex.length()) {
			hex = "0"+hex;
		}
		String low = hex.substring(4, hex.length());
		String high = hex.substring(0,4);
		hex = low.substring(2, low.length())+" "+low.substring(0,2)+" " +high.substring(2, high.length())+" "+high.substring(0,2);
		return hex;
	}
}
