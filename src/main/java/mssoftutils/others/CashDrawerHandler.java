package mssoftutils.others;

import java.io.IOException;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public class CashDrawerHandler implements SerialPortEventListener {

	private String serialPortIdentifiar;
	private SerialPort serialPort;

	private void connect(String serialPortIdentifiar) throws SerialPortException {
		this.serialPortIdentifiar = serialPortIdentifiar;
		this.serialPort = new SerialPort(this.serialPortIdentifiar);
		this.serialPort.openPort();
		this.serialPort.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
				SerialPort.PARITY_NONE);
		this.serialPort.addEventListener(this);
	}

	private void disconnect() throws SerialPortException {
		if (this.serialPort != null && this.serialPort.isOpened())
			this.serialPort.closePort();
	}

	public void open(String port) throws IOException, SerialPortException {
		this.connect(port);
		this.serialPort.writeByte((byte) 0);
		this.disconnect();
	}

	@Override
	public void serialEvent(SerialPortEvent serialPortEvent) {
		if (serialPortEvent.isRXCHAR()) {
			try {
				String data = this.serialPort.readString();

				if (data != null && !data.replaceAll(" ", "").equals("")) {
					data = data.replaceAll(" ", "");

					System.out.println("Data: " + data);
				}
			} catch (SerialPortException e) {
				e.printStackTrace();
			}
		}
	}
}