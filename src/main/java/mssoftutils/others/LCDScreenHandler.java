package mssoftutils.others;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicLong;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public class LCDScreenHandler implements SerialPortEventListener {

	private final AtomicLong lcdTimeOut = new AtomicLong(10_000);

	private String serialPortIdentifiar;
	private SerialPort serialPort;
	private String defaultContent;
	private Timer timer;

	public void connect(String serialPortIdentifiar, String defaultContent, Long lcdTimeOut)
			throws SerialPortException {
		this.lcdTimeOut.set(lcdTimeOut);
		this.serialPortIdentifiar = serialPortIdentifiar;
		this.serialPort = new SerialPort(this.serialPortIdentifiar);
		this.serialPort.openPort();
		this.serialPort.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
				SerialPort.PARITY_NONE);
		this.serialPort.addEventListener(this);
		this.defaultContent = defaultContent;
	}

	public boolean isOpened() {
		if (this.serialPort != null)
			return this.serialPort.isOpened();
		else
			return false;
	}

	public void disconnect() throws SerialPortException {
		if (this.serialPort != null && this.serialPort.isOpened())
			this.serialPort.closePort();
	}

	public void clear() throws IOException, SerialPortException {
		this.serialPort.writeByte((byte) 12);
		this.serialPort.writeByte((byte) 11);
	}

	public void moveDownByOneLine() throws SerialPortException {
		this.serialPort.writeBytes(new byte[] { 27, 91, 66 });
	}

	public void send(String message) throws IOException, SerialPortException {
		StringBuilder line1 = new StringBuilder("");
		StringBuilder line2 = new StringBuilder("");

		int n = message.length();

		int remainingNumberOfSpacesInLine1 = 20;
		int remainingNumberOfSpacesInLine2 = 20;

		for (int i = 0; i < n; i++) {
			if (remainingNumberOfSpacesInLine1 > 0) {
				line1.append(message.charAt(i));
				remainingNumberOfSpacesInLine1 -= 1;
			} else if (remainingNumberOfSpacesInLine2 > 0) {
				line2.append(message.charAt(i));
				remainingNumberOfSpacesInLine2 -= 1;
			} else
				break;
		}

		boolean append1 = true;
		for (int i = 0; i < remainingNumberOfSpacesInLine1; i++) {
			if (append1)
				line1.append(' ');
			else
				line1.insert(0, ' ');
			append1 = !append1;
		}

		boolean append2 = true;
		for (int i = 0; i < remainingNumberOfSpacesInLine2; i++) {
			if (append2)
				line2.append(' ');
			else
				line2.insert(0, ' ');
			append2 = !append2;
		}

		String display = line1.toString() + line2.toString();

		this.serialPort.writeBytes(display.getBytes());
		this.restartRecoveryTimer();
	}

	public void clearAndSend(String message) throws IOException, SerialPortException {
		this.clear();
		this.send(message);
	}

	public void setLcdTimeOut(Long timeOut) {
		this.lcdTimeOut.set(timeOut);
	}

	private void restartRecoveryTimer() {
		if (this.timer != null)
			this.timer.cancel();

		this.timer = new Timer();
		this.timer.schedule(new TimerTask() {

			@Override
			public void run() {
				try {
					clearAndSend(LCDScreenHandler.this.defaultContent);
				} catch (IOException | SerialPortException e) {
					e.printStackTrace();
				}
			}
		}, this.lcdTimeOut.get(), this.lcdTimeOut.get());
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
