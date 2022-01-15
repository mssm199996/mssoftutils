package mssoftutils.update;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class SoftwareVersionDownloader {

	private ObjectMapper objectMapper;
	private URI serviceBaseUrl;

	public SoftwareVersionDownloader(URI serviceBaseUrl) {
		this.serviceBaseUrl = serviceBaseUrl;

		SimpleModule module = new SimpleModule();
		module.addDeserializer(LocalDate.class, new LocalDateDeserializer());
		module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());

		this.objectMapper = new ObjectMapper();
		this.objectMapper.registerModule(module);
	}

	public List<SoftwareVersion> downloadSoftwareVersions(Software software) throws IOException {
		String urlPath = this.serviceBaseUrl + "/restapi/softwareVersion/findAllBySoftware?software=" + software;

		URL url = new URL(urlPath);

		HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
		httpURLConnection.setRequestMethod("GET");
		httpURLConnection.setRequestProperty("Content-Type", "application/json");
		httpURLConnection.setConnectTimeout(35000);
		httpURLConnection.setReadTimeout(35000);

		StringBuffer softwareVersionsAsStringArray = new StringBuffer();
		String inputLine = null;
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(httpURLConnection.getInputStream(), Charset.forName("UTF-8")));

		while ((inputLine = bufferedReader.readLine()) != null)
			softwareVersionsAsStringArray.append(inputLine);

		bufferedReader.close();
		httpURLConnection.disconnect();

		return this.objectMapper.readValue(softwareVersionsAsStringArray.toString(),
				new TypeReference<List<SoftwareVersion>>() {
				});
	}

	public static enum Software {
		MSMARKET("MS MARKET"), MSMDOCSOFFICE("MS DOCS OFFICE"), KAFILELYATIM("KAFIL EL YATIM");

		private String dropBoxRootFolder;

		Software(String dropBoxRootFolder) {
			this.dropBoxRootFolder = dropBoxRootFolder;
		}
		
		public String getDropBoxRootFolder() {
			return this.dropBoxRootFolder;
		}
	}

	public static void main(String[] args) throws IOException {
		SoftwareVersionDownloader s = new SoftwareVersionDownloader(getBaseURI());
		System.out.println(s.downloadSoftwareVersions(Software.MSMARKET));
	}

	private static URI getBaseURI() {
		return URI.create("https://ms-software-utils.herokuapp.com");
	}
}
