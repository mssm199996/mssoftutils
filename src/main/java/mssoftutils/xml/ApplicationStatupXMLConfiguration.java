package mssoftutils.xml;

public class ApplicationStatupXMLConfiguration extends XMLConfigurationsHandler {

	public ApplicationStatupXMLConfiguration() {
		super("config/application.xml");
	}

	public String getInitialUsername() {
		return this.getNodeContent("/config/defaultUsername");
	}

	public String getInitialPassword() {
		return this.getNodeContent("/config/defaultPassword");
	}

	public boolean getInitialSavePassword() {
		return this.getNodeContent("/config/savePasswordBehavior").equals("Y");
	}

	public void setInitialUsername(String username) {
		this.modifyNode("/config/defaultUsername", username);
	}

	public void setInitialPassword(String password) {
		this.modifyNode("/config/defaultPassword", password);
	}

	public void setInitialSavePassword(boolean isSavePassword) {
		this.modifyNode("/config/savePasswordBehavior", isSavePassword ? "Y" : "N");
	}
}
