package mssoftutils.others;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.control.TextField;

public abstract class TextFieldBarcodeListener extends BarcodeListener {

	private static Map<String, String> ALPHABET = new HashMap<>();
	private TextField textField;

	static {
		TextFieldBarcodeListener.ALPHABET.put("à", "0");
		TextFieldBarcodeListener.ALPHABET.put("&", "1");
		TextFieldBarcodeListener.ALPHABET.put("é", "2");
		TextFieldBarcodeListener.ALPHABET.put("\"", "3");
		TextFieldBarcodeListener.ALPHABET.put("'", "4");
		TextFieldBarcodeListener.ALPHABET.put("(", "5");
		TextFieldBarcodeListener.ALPHABET.put("-", "6");
		TextFieldBarcodeListener.ALPHABET.put("è", "7");
		TextFieldBarcodeListener.ALPHABET.put("_", "8");
		TextFieldBarcodeListener.ALPHABET.put("ç", "9");

		TextFieldBarcodeListener.ALPHABET.put("0", "0");
		TextFieldBarcodeListener.ALPHABET.put("1", "1");
		TextFieldBarcodeListener.ALPHABET.put("2", "2");
		TextFieldBarcodeListener.ALPHABET.put("3", "3");
		TextFieldBarcodeListener.ALPHABET.put("4", "4");
		TextFieldBarcodeListener.ALPHABET.put("5", "5");
		TextFieldBarcodeListener.ALPHABET.put("6", "6");
		TextFieldBarcodeListener.ALPHABET.put("7", "7");
		TextFieldBarcodeListener.ALPHABET.put("8", "8");
		TextFieldBarcodeListener.ALPHABET.put("9", "9");

		TextFieldBarcodeListener.ALPHABET.put("A", "A");
		TextFieldBarcodeListener.ALPHABET.put("B", "B");
		TextFieldBarcodeListener.ALPHABET.put("C", "C");
		TextFieldBarcodeListener.ALPHABET.put("D", "D");
		TextFieldBarcodeListener.ALPHABET.put("E", "E");
		TextFieldBarcodeListener.ALPHABET.put("F", "F");
		TextFieldBarcodeListener.ALPHABET.put("G", "G");
		TextFieldBarcodeListener.ALPHABET.put("H", "H");
		TextFieldBarcodeListener.ALPHABET.put("I", "I");
		TextFieldBarcodeListener.ALPHABET.put("J", "J");
		TextFieldBarcodeListener.ALPHABET.put("K", "K");
		TextFieldBarcodeListener.ALPHABET.put("L", "L");
		TextFieldBarcodeListener.ALPHABET.put("M", "M");
		TextFieldBarcodeListener.ALPHABET.put("N", "N");
		TextFieldBarcodeListener.ALPHABET.put("O", "O");
		TextFieldBarcodeListener.ALPHABET.put("P", "P");
		TextFieldBarcodeListener.ALPHABET.put("Q", "Q");
		TextFieldBarcodeListener.ALPHABET.put("R", "R");
		TextFieldBarcodeListener.ALPHABET.put("S", "S");
		TextFieldBarcodeListener.ALPHABET.put("T", "T");
		TextFieldBarcodeListener.ALPHABET.put("U", "U");
		TextFieldBarcodeListener.ALPHABET.put("V", "V");
		TextFieldBarcodeListener.ALPHABET.put("W", "W");
		TextFieldBarcodeListener.ALPHABET.put("X", "X");
		TextFieldBarcodeListener.ALPHABET.put("Y", "Y");
		TextFieldBarcodeListener.ALPHABET.put("Z", "Z");
	}

	public TextFieldBarcodeListener(TextField textField) {
		this.textField = textField;
	}

	@Override
	public void onEnterReaded(String barcode) {
		String content = this.textField.getText();
		StringBuilder result = new StringBuilder();

		for (char character : content.toCharArray()) {
			String interm = TextFieldBarcodeListener.ALPHABET.get(String.valueOf(character));

			if (interm != null)
				result.append(interm);
		}

		this.result = result;

		if (this.clearTextAfterEnter())
			this.textField.setText("");
	}

	public boolean clearTextAfterEnter() {
		return true;
	}
}
