package mssoftutils.datas.statistics;

public class StatisticHolder {

	private String label;
	private Double data;

	public StatisticHolder(String label, Double data) {
		this.label = label;
		this.data = data;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Double getData() {
		return data;
	}

	public void setData(Double data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return this.label + ": " + this.data;
	}

	public static enum GroupByMethod {
		JOURNALIER, HEBDOMADAIRE, MENSUEL, ANNUEL, TOTAL
	}

	public static enum StatisticType {
		SUM, AVG, COUNT
	}
}
