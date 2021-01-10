package mssoftutils.scalehandlers.ls21530e;

public class PLU {
	private String departement = "00", tare = "0000", barcode = "27", weightUnit = "4", shelfTime = "0f",
			discount = "00", serieStable = "0000000005000000", pcsType = "0";

	private String plusName = "", lfCode = "", codus = "", unitPrice = "";
	private String msg1 = "00";

	public PLU(String plu_name, String lfCode, String code, String unitPrice, boolean addMsg1) {
		this.plusName = plu_name;
		this.lfCode = lfCode;
		this.codus = code;
		this.unitPrice = unitPrice;

		if (addMsg1)
			this.msg1 = "01";
	}

	public PLU() {
		this.plusName = "";
		this.lfCode = "";
		this.codus = "";
		this.unitPrice = "";
	}

	public String getPluName() {
		return this.plusName;
	}

	public String getLfCode() {
		return this.lfCode;
	}

	public String getCodus() {
		return this.codus;
	}

	public String getBarcode() {
		return this.barcode;
	}

	public Integer getLfCodeAsInteger() {
		return Integer.parseInt(this.lfCode);
	}

	public String getPluRepresentation() {
		String result = "";

		switch (this.plusName.trim()) {
		case "":
			for (int r = 0; r < 128; r++)
				result += "0";

			break;
		default:
			result = this.getFormattedPluName() + this.getFormattedLFCode() + this.tare + this.departement
					+ this.getFormattedBarcode() + this.getFormattedUnitPrice() + this.pcsType + this.weightUnit
					+ this.shelfTime + this.discount + this.msg1 + this.serieStable + this.getFormattedCodus();

			break;
		}

		return result;
	}

	public String getFormattedPluName() {
		String pluName = this.plusName;
		String result = "";

		if (!pluName.trim().equals("")) {
			for (int i = 0; i < pluName.length(); i++) {
				char letter = pluName.charAt(i);
				int r = (int) letter;

				result = result + Integer.toHexString(r);
			}

			int diff = (72 - result.length()) / 2;

			for (int d = 0; d < diff; d++)
				result = result + "20";
		}

		return result;
	}

	public String getFormattedUnitPrice() {
		if (!this.unitPrice.trim().equals("")) {
			this.unitPrice = this.unitPrice.replace(".", "").replace(",", "");

			int taille = this.unitPrice.length();
			int zeros = 8 - taille;

			for (int n = 0; n < zeros; n++)
				this.unitPrice = "0" + this.unitPrice;
		}

		return this.unitPrice;
	}

	public String getFormattedLFCode() {
		if (!this.lfCode.trim().equals("")) {
			int taille = lfCode.length();
			int zeros = 6 - taille;

			for (int i = 0; i < zeros; i++)
				this.lfCode = "0" + this.lfCode;
		}

		return this.lfCode;
	}

	private String getFormattedCodus() {
		if (!this.codus.trim().equals("")) {
			int taille = this.codus.length();
			int zeros = 10 - taille;

			for (int i = 0; i < zeros; i++)
				this.codus = "0" + this.codus;
		}

		return codus;
	}

	private String getFormattedBarcode() {
		String barcode = "";

		if (!this.barcode.trim().equals("")) {
			barcode = Integer.toHexString(Integer.parseInt(this.barcode));

			int taille = barcode.length();
			int zeros = 2 - taille;

			for (int i = 0; i < zeros; i++)
				barcode = "0" + barcode;
		}

		return barcode;
	}

	@Override
	public String toString() {
		return this.getPluName();
	}
}
