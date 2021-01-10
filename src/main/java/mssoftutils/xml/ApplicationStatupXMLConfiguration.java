package mssoftutils.xml;

public class ApplicationStatupXMLConfiguration extends XMLConfigurationsHandler {

	public ApplicationStatupXMLConfiguration() {
		super("config/application.xml");

		String postePrefix = this.getNodeContent("/config/poste/prefix");
		String posteSuffix = this.getNodeContent("/config/poste/suffix");

		System.setProperty("application.poste.prefix", postePrefix);
		System.setProperty("application.poste.suffix", posteSuffix);
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
