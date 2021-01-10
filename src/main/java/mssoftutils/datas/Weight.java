package mssoftutils.datas;

public class Weight {

	private Double weight;
	private WeightUnit weightUnit;

	public Weight(Double weight, WeightUnit weightUnit) {
		this.weight = weight;
		this.weightUnit = weightUnit;
	}

	public Weight() {
		this.weight = 0.0;
		this.weightUnit = WeightUnit.KG;
	}

	public Double getValueAsKg() {
		switch (this.weightUnit) {
		case KG:
			return this.weight;
		case G:
			return this.weight / 1000.0;
		default:
			return null;
		}
	}

	public Double getValueAsG() {
		return this.getValueAsKg() * 1000.0;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public WeightUnit getWeightUnit() {
		return weightUnit;
	}

	public void setWeightUnit(WeightUnit weightUnit) {
		this.weightUnit = weightUnit;
	}

	@Override
	public String toString() {
		return this.weight.toString() + this.weightUnit;
	}

	public static enum WeightUnit {
		KG, G
	}
}
