package com.poweroak.util;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public class MonitorSerialPortListener implements SerialPortEventListener {
	private static final Logger logger=Logger.getLogger(MonitorSerialPortListener.class);
	private SerialPort serial;
	
	public MonitorSerialPortListener(SerialPort serial) {
		this.serial=serial;
	}
	@Override
	public void serialEvent(SerialPortEvent event) {
		if(event.getEventType()==SerialPortEvent.DATA_AVAILABLE) {
			String msg=Util.ByteArrayToHexadecimal(SerialPortUtil.readFromPort(serial), false);
			logger.debug(serial.getName()+"收到消息:-----------------"+msg);
			if(msg.equals("0110")) {
				SerialPortUtil.sendToPort(serial, Util.prepareHexBytes("01 0F"));
			}
		}
		
	}

}
