package mssoftutils.network;

import javafx.beans.property.SimpleStringProperty;

public class RemoteMachine {

	private SimpleStringProperty ipAddress = new SimpleStringProperty("");
	private SimpleStringProperty hostName = new SimpleStringProperty("");

	public String getIpAddress() {
		return ipAddress.getValue();
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress.setValue(ipAddress);
	}

	public SimpleStringProperty ipAddressProperty() {
		return this.ipAddress;
	}

	public String getHostName() {
		return hostName.getValue();
	}

	public void setHostName(String hostName) {
		this.hostName.setValue(hostName);
	}

	public SimpleStringProperty hostNameProperty() {
		return this.hostName;
	}

	@Override
	public int hashCode() {
		return this.getIpAddress().hashCode();
	}

	@Override
	public String toString() {
		return this.getIpAddress() + ": " + this.getHostName();
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof RemoteMachine && this.getIpAddress().equals(((RemoteMachine) o).getIpAddress());
	}
}
