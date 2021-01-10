package mssoftutils.others;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;

public abstract class BarcodeListener implements EventHandler<KeyEvent> {

	private KeyCode previousKey = null;
	protected StringBuilder result = new StringBuilder("");

	@Override
	public void handle(KeyEvent event) {
		try {
			KeyCode key = event.getCode();

			if (this.previousKey != null && (this.previousKey.isDigitKey() || this.previousKey.isLetterKey())) {
				String shiftedKey = (new KeyCodeCombination(this.previousKey, KeyCombination.SHIFT_DOWN)).getName();

				this.result.append(shiftedKey.charAt(shiftedKey.length() - 1));
			}

			this.previousKey = key;

			if (key == KeyCode.ENTER) {
				this.onEnterReaded(this.result.toString());

				if (!this.result.toString().equals(""))
					this.onCodeReaded(this.result.toString());

				this.result = new StringBuilder("");
				this.previousKey = null;
			} else if (key == KeyCode.ESCAPE) {

			}

			if (this.previousKey.isDigitKey() || this.previousKey.isLetterKey())
				event.consume();
		} catch (Exception e) {

		}
	}

	public abstract void onCodeReaded(String barcode);

	public abstract void onEnterReaded(String barcode);
}
