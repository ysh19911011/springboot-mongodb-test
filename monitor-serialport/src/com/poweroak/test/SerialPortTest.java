package com.poweroak.test;

import java.util.List;

import com.poweroak.util.MonitorSerialPortListener;
import com.poweroak.util.SerialPortUtil;
import com.poweroak.util.Util;

import gnu.io.SerialPort;
import gnu.io.SerialPortEventListener;

public class SerialPortTest {

	public static void main(String[] args) throws InterruptedException {
		
		// TODO Auto-generated method stub
		List<String> list=SerialPortUtil.findPort();
		SerialPort port=SerialPortUtil.openPort("COM2", 9600);
		
		SerialPortUtil.sendToPort(port,Util.prepareHexBytes("01 1F"));
		SerialPortEventListener listener=new MonitorSerialPortListener(port);
		SerialPortUtil.addListener(port, listener);
	}

}
