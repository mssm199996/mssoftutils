package mssoftutils.others;

import java.util.List;

public abstract class BarcodesHandler {
	public String getCode128BarCode() {
		List<String> bareCodes = this.fetchAlreadyUsedCode128();

		String bareCode = "";

		do {
			bareCode = this.getRandomCode128Barcode();
		} while (bareCodes.contains(bareCode));

		return bareCode;
	}

	private String getRandomCode128Barcode() {
		String result = "";

		for (int i = 1; i <= 6; i++)
			result += this.getRandomDigit() + "";

		return result;
	}

	private int getRandomDigit() {
		return (int) ((9.0 - 0.0) * (Math.random()) + 0.0);
	}

	public abstract List<String> fetchAlreadyUsedCode128();
}
