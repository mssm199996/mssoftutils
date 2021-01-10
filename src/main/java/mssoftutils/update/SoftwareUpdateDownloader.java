package mssoftutils.update;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.util.IOUtil.ProgressListener;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.DbxUserFilesRequests;
import com.dropbox.core.v2.files.DownloadZipErrorException;
import com.dropbox.core.v2.files.DownloadZipResult;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderBuilder;
import com.dropbox.core.v2.files.ListFolderErrorException;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.users.FullAccount;

import mssoftutils.update.SoftwareVersionDownloader.Software;

public class SoftwareUpdateDownloader {

	public static final String UPDATES_FOLDER = "updates";
	public static final String API_KEY = "zt7djPtcvUYAAAAAAAAAAdz8giysTuow7p9RizOFILbrw7FcwtS1G-jnOVv4wwy1";

	private static DbxRequestConfig CONFIG;
	private static DbxClientV2 CLIENT;

	private Software software;

	public SoftwareUpdateDownloader(Software software) {
		this.software = software;

		if (SoftwareUpdateDownloader.CONFIG == null) {
			SoftwareUpdateDownloader.CONFIG = DbxRequestConfig.newBuilder("dropbox/" + this.software).build();
			SoftwareUpdateDownloader.CLIENT = new DbxClientV2(SoftwareUpdateDownloader.CONFIG,
					SoftwareUpdateDownloader.API_KEY);
		}
	}

	public String getAccountDisplayName() throws DbxApiException, DbxException {
		FullAccount account = SoftwareUpdateDownloader.CLIENT.users().getCurrentAccount();

		return account.getName().getDisplayName();
	}

	public boolean isAlreadyDownloaded(SoftwareVersion softwareVersion) {
		Path artifactFolder = Paths
				.get(SoftwareUpdateDownloader.UPDATES_FOLDER + "/" + softwareVersion.getVersion() + ".zip");

		return Files.exists(artifactFolder);
	}

	public long getFolderSize(SoftwareVersion softwareVersion) throws ListFolderErrorException, DbxException {
		String source = this.getSourceFolder(softwareVersion);
		DbxUserFilesRequests dbxUserfilesRequests = SoftwareUpdateDownloader.CLIENT.files();

		ListFolderBuilder listFolderBuilder = dbxUserfilesRequests.listFolderBuilder(source).withRecursive(true);
		ListFolderResult listFolderResult = listFolderBuilder.start();
		List<Metadata> metadatas = listFolderResult.getEntries();

		long result = 0;

		for (Metadata metadata : metadatas)
			if (metadata instanceof FileMetadata)
				result += FileMetadata.class.cast(metadata).getSize();

		return result;
	}

	public void downloadAndStore(SoftwareVersion softwareVersion, ProgressListener progressListener)
			throws IOException, DownloadZipErrorException, DbxException {
		Path updatesFolder = Paths.get(SoftwareUpdateDownloader.UPDATES_FOLDER);

		if (!Files.exists(updatesFolder))
			Files.createDirectory(updatesFolder);

		// ----------------------------------------------------------------------------

		Path artifactFolder = Paths
				.get(SoftwareUpdateDownloader.UPDATES_FOLDER + "/" + softwareVersion.getVersion() + ".zip");

		if (Files.exists(artifactFolder))
			Files.delete(artifactFolder);

		// ----------------------------------------------------------------------------

		String source = this.getSourceFolder(softwareVersion);
		FileOutputStream fileOutputStream = new FileOutputStream(artifactFolder.toFile());

		DbxDownloader<DownloadZipResult> downloader = SoftwareUpdateDownloader.CLIENT.files().downloadZip(source);
		downloader.download(fileOutputStream, progressListener);

		fileOutputStream.close();
	}

	private String getSourceFolder(SoftwareVersion softwareVersion) {
		return "/" + this.software.getDropBoxRootFolder() + "/updates/" + softwareVersion.getVersion();
	}

	public static void main(String[] args) throws IOException, DbxApiException, DbxException {
		SoftwareUpdateDownloader softwareUpdateDownloader = new SoftwareUpdateDownloader(Software.MSMARKET);

		SoftwareVersion softwareVersion = new SoftwareVersion();
		softwareVersion.setVersion("3.0");

		softwareUpdateDownloader.downloadAndStore(softwareVersion, bytesWritten -> {
			System.out.println("Downloaded: " + bytesWritten);
		});
	}
}
