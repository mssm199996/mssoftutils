package mssoftutils.others;

import java.util.List;

import javafx.scene.control.TableColumn;

public abstract class ColumnSumer<T> {

	private TableColumn<T, Double> column;
	private List<T> dataset;
	private String initialHeader;

	public ColumnSumer(TableColumn<T, Double> column, List<T> dataset) {
		this.setColumn(column);
		this.setDataset(dataset);
		this.setInitialHeader(column.getText());
	}

	public void update() {
		Double value = 0.0;

		for (T entity : this.dataset)
			value += this.valueForUpdate(entity);

		this.column.setText(this.getInitialHeader() + ": " + String.format("%.2f", value));
	}

	public List<T> getDataset() {
		return dataset;
	}

	public void setDataset(List<T> dataset) {
		this.dataset = dataset;
	}

	public TableColumn<T, Double> getColumn() {
		return column;
	}

	public void setColumn(TableColumn<T, Double> column) {
		this.column = column;
	}

	public String getInitialHeader() {
		return initialHeader;
	}

	public void setInitialHeader(String initialHeader) {
		this.initialHeader = initialHeader;
	}

	public abstract Double valueForUpdate(T item);
}
