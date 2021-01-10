package mssoftutils.scalehandlers.serialscalehandlers;

import java.util.HashSet;
import java.util.Set;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import mssoftutils.datas.Weight;
import mssoftutils.datas.Weight.WeightUnit;
import mssoftutils.scalehandlers.ISerialScaleInterceptor;

public abstract class SerialScaleHandler implements SerialPortEventListener {

	private String serialPortIdentifiar;
	private SerialPort serialPort;
	private Set<ISerialScaleInterceptor> iSerialScaleInterceptors = new HashSet<>();

	public void connect(String serialPortIdentifiar) throws SerialPortException {
		this.serialPortIdentifiar = serialPortIdentifiar;
		this.serialPort = new SerialPort(this.serialPortIdentifiar);
		this.serialPort.openPort();
		this.serialPort.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
				SerialPort.PARITY_NONE);
		this.serialPort.addEventListener(this);
	}

	public boolean isOpened() {
		return this.serialPort.isOpened();
	}

	public void disconnect() throws SerialPortException {
		if (this.serialPort != null && this.serialPort.isOpened())
			this.serialPort.closePort();
	}

	@Override
	public void serialEvent(SerialPortEvent event) {
		if (event.isRXCHAR()) {
			try {
				String data = this.serialPort.readString();

				if (data != null && !data.replaceAll(" ", "").equals("")) {
					data = data.replaceAll(" ", "");

					Weight weight = new Weight();

					int kgIndex = data.indexOf("kg");

					if (kgIndex != -1) {
						weight.setWeightUnit(WeightUnit.KG);

						data = data.replaceAll("kg", "");
					} else {
						weight.setWeightUnit(WeightUnit.G);

						data = data.replaceAll("g", "");
					}

					weight.setWeight(Double.parseDouble(data));

					this.iSerialScaleInterceptors.forEach(e -> {
						e.onWeightIntercepted(weight);
					});
				}
			} catch (SerialPortException e) {
				e.printStackTrace();
			}
		}
	}

	public void addSerialScaleInterceptor(ISerialScaleInterceptor iSerialScaleInterceptor) {
		this.iSerialScaleInterceptors.add(iSerialScaleInterceptor);
	}

	public void removeSerialScaleInterceptor(ISerialScaleInterceptor iSerialScaleInterceptor) {
		this.iSerialScaleInterceptors.remove(iSerialScaleInterceptor);
	}
}
