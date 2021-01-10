package mssoftutils.update;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import net.lingala.zip4j.ZipFile;

public class SoftwareUpdateInstaller {

	public static final String UPDATE_LAUNCHER = "update.bat";

	public void install(SoftwareVersion softwareVersion) throws IOException, InterruptedException {
		// LSMDFJLQSDFJ -> 2.0 -> update.bat

		File outputFile = new File("updateOutput.log");
		File errorFile = new File("updateError.log");

		Path workingPath = Files.createTempDirectory(null);
		Path zipPath = Paths.get(SoftwareUpdateDownloader.UPDATES_FOLDER + "/" + softwareVersion.getVersion() + ".zip");

		String zipDirectory = workingPath.toAbsolutePath() + "/" + softwareVersion.getVersion();
		Path zipDirectoryPath = Paths.get(zipDirectory);

		ZipFile zipFile = new ZipFile(zipPath.toFile());
		zipFile.extractAll(workingPath.toString());

		ProcessBuilder processBuilder = new ProcessBuilder(new String[] { 
				"CMD", "/c",
				"START", "update.bat"
		});
		processBuilder.redirectError(errorFile);
		processBuilder.redirectOutput(outputFile);
		processBuilder.directory(zipDirectoryPath.toAbsolutePath().toFile());
		processBuilder.environment().put("MS_SOFTWARE_DIRECTORY", Paths.get("").toAbsolutePath().toString());

		Process p = processBuilder.start();
		p.waitFor();
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		SoftwareUpdateInstaller softwareUpdateInstaller = new SoftwareUpdateInstaller();

		SoftwareVersion softwareVersion = new SoftwareVersion();
		softwareVersion.setVersion("3.0");

		softwareUpdateInstaller.install(softwareVersion);
	}
}
