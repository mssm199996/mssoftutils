package mssoftutils.others;

import java.util.LinkedList;
import java.util.List;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public abstract class SearchController<T> {

	private Thread searchThread;
	private ObservableList<T> datasource;

	public SearchController() {
		this.datasource = FXCollections.observableList(new LinkedList<T>());
	}

	public SearchController(ObservableList<T> datasource) {
		this.datasource = datasource;
	}
 
	public void search() {
		if (this.searchThread != null && this.searchThread.isAlive())
			this.searchThread.interrupt();

		this.searchThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					List<T> result = selectFromDatabase();

					Platform.runLater(() -> {
						datasource.clear();
						datasource.addAll(result);

						performAfterSearch();
					});
				} catch (NullPointerException exp) {
				}
			}
		});

		this.searchThread.start();
	}

	protected abstract List<T> selectFromDatabase() throws NullPointerException;

	public ObservableList<T> getDatasource() {
		return this.datasource;
	}

	protected void performAfterSearch() {
	}
}
