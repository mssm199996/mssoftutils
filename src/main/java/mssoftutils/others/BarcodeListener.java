package mssoftutils.others;

import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;

public abstract class BarcodeListener implements EventHandler<KeyEvent> {

	private KeyCode previousKey = null;
	private String result = "";

	@Override
	public void handle(KeyEvent event) {
		KeyCode key = event.getCode();

		if (this.previousKey != null && (this.previousKey.isDigitKey() || this.previousKey.isLetterKey())) {
			String shiftedKey = (new KeyCodeCombination(this.previousKey, KeyCombination.SHIFT_DOWN)).getName();

			this.result += shiftedKey.charAt(shiftedKey.length() - 1);
		}

		this.previousKey = key;

		if (key == KeyCode.ENTER) {

			Object source = event.getSource();

			if (source instanceof TextField && this.clearTextAfterEnter()) {
				TextField textSource = (TextField) source;

				textSource.setText("");
			}

			if (!this.result.equals(""))
				this.onCodeReaded(this.result);

			this.result = "";
			this.previousKey = null;
		}

		event.consume();
	}

	public abstract void onCodeReaded(String barcode);

	public boolean clearTextAfterEnter() {
		return true;
	}
}
