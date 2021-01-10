package mssoftutils.scalehandlers.ls21530e;

import java.util.LinkedList;
import java.util.List;

public class PLUGenerator {
	private final int m1MaxLength = 22;

	private int pluPackNumber = 0, lfPackNumber = 248;
	private boolean centered = true;

	private String m1 = "", packText;

	private List<String> packs = new LinkedList<>();
	private List<PLU> plus = new LinkedList<>();

	private final String firstPack1 = "0e010086572332200313215513000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
			firstPack2 = "0e0300ff0f0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
			firstPack3 = "0e04000004ff07000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
			firstPack4 = "0e04000002ff02000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
			firstPack5 = "0e04003003f403000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
			firstPack6 = "77000080000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
			firstPack7 = "77010000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
			firstPack8 = "77020000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
			firstPack9 = "77030000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
			firstPack10 = "77040000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
			firstPack11 = "77050000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";

	private final String middlePack1 = "02f603ff000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
			middlePack2 = "02f703ff000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";

	private final String lastPack1 = "0e090000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000640064006400640064006400640064006400640064006400640064006400640064006400640064006400640064006400640064006400640064006400640064006400640064006400640064006400640064006400640064006400640064006400640064",
			lastPack2 = "0e020086572332000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";

	public PLUGenerator(List<PLU> pluList, String message, boolean centered) {
		this.centered = centered;
		this.plus.addAll(pluList);

		this.correctPluList();
		this.addAllPacksToList(message);
	}

	private String getFirstPackText() {
		String result;

		switch (this.pluPackNumber) {
		case 0:
			result = "0b" + "00" + "00";
			break;
		default:
			String hexIndex = Integer.toHexString(this.pluPackNumber);

			result = "01" + this.addZero(hexIndex) + "00";

			break;
		}

		this.pluPackNumber++;

		return result;
	}

	private String getFirstLFCodeText() {
		String hexIndex = Integer.toHexString(this.lfPackNumber);
		String firstTxt = "02" + this.addZero(hexIndex) + "03";

		this.lfPackNumber++;

		return firstTxt;

	}

	private void addPLUPacks() {
		for (int f = 0; f < this.plus.size(); f += 4) {
			String pack = this.getFirstPackText() + this.plus.get(f).getPluRepresentation()
					+ this.plus.get(f + 1).getPluRepresentation() + this.plus.get(f + 2).getPluRepresentation()
					+ this.plus.get(f + 3).getPluRepresentation();

			this.packs.add(pack);
		}
	}

	private void addLFcodePacks() {
		for (int p = 0; p < plus.size(); p += 84) {
			this.packText = getFirstLFCodeText();

			if ((this.plus.size() - p) > 83) {
				for (int d = 0; d < 84; d++)
					this.packText = this.packText + this.plus.get(p + d).getFormattedLFCode();

				int zeros = 518 - this.packText.length();

				for (int i = 0; i < zeros; i++)
					this.packText = this.packText + "0";

				this.packs.add(this.packText);
			}

			if ((this.plus.size() - p) < 84) {
				for (int d = p; d < this.plus.size(); d++)
					this.packText = this.packText + this.plus.get(d).getFormattedLFCode();

				int zeros = 518 - this.packText.length();

				for (int i = 0; i < zeros; i++)
					this.packText = this.packText + "0";

				this.packs.add(this.packText);
			}
		}
	}

	private void addMiddlePacks() {
		this.addMessageM1(this.m1);
		this.packs.add(this.middlePack2);
	}

	private void addLastPacks() {
		this.packs.add(this.lastPack1);
		this.packs.add(this.lastPack2);
	}

	private void addFirstEleven() {
		this.packs.add(this.firstPack1);
		this.packs.add(this.firstPack2);
		this.packs.add(this.firstPack3);
		this.packs.add(this.firstPack4);
		this.packs.add(this.firstPack5);
		this.packs.add(this.firstPack6);
		this.packs.add(this.firstPack7);
		this.packs.add(this.firstPack8);
		this.packs.add(this.firstPack9);
		this.packs.add(this.firstPack10);
		this.packs.add(this.firstPack11);
	}

	public void addAllPacksToList() {
		this.m1 = "";

		this.addFirstEleven();
		this.addPLUPacks();
		this.addMiddlePacks();
		this.addLFcodePacks();
		this.addLastPacks();
	}

	public void addAllPacksToList(String oneLineMessage) {
		this.m1 = oneLineMessage;
		this.addFirstEleven();
		this.addPLUPacks();
		this.addMiddlePacks();
		this.addLFcodePacks();
		this.addLastPacks();
	}

	public void addAllPacksToList(String messageOne, String messageTwo) {
		this.m1 = messageOne + "[[&&&]]" + messageTwo;
		this.addFirstEleven();
		this.addPLUPacks();
		this.addMiddlePacks();
		this.addLFcodePacks();
		this.addLastPacks();
	}

	private List<PLU> correctPluList() {
		int mod = 4 - (this.plus.size() % 4);

		if (mod > 0 && mod < 4) {
			for (int d = 0; d < mod; d++) {
				this.plus.add(new PLU());
			}
		}

		return this.plus;
	}

	private String addZero(String hexText) {
		String result = "";

		if (!hexText.trim().equals("")) {
			switch (hexText.length()) {

			case 1:
				result = "0" + hexText;

				break;
			default:
				result = hexText;

				break;
			}
		}

		return result;
	}

	private void addMessageM1(String message) {
		String specialVar;
		String debutPack1Line = "620000" + "00" + "000000000000";
		String debutPack2Line;
		String oneLineMessageInHex = "";
		String line1, line2;
		String line1InHex = "";
		String line2InHex = "";

		if (message == null)
			message = "teste";

		if (message.trim().equals("")) {
			this.packs.add(this.middlePack1);
		} else {
			String finalPack;

			if (message.trim().contains("[[&&&]]")) {
				line1 = centerText(message.substring(0, message.indexOf("[[&&&]]")));// centrer le texte
				line2 = centerText(message.substring(message.indexOf("[[&&&]]") + 7)); // centrer le texte

				specialVar = Integer.toHexString(line1.length() + 9);

				if (specialVar.length() == 1)
					specialVar = "0" + specialVar;

				debutPack2Line = "620000" + specialVar + "000000000000";

				for (int i = 0; i < line1.length(); i++) {
					char letter = line1.charAt(i);
					int r = (int) letter;

					line1InHex = line1InHex + Integer.toHexString(r);
				}

				for (int i = 0; i < line2.length(); i++) {
					char letter = line2.charAt(i);
					int r = (int) letter;

					line2InHex = line2InHex + Integer.toHexString(r);
				}

				finalPack = debutPack2Line + line1InHex + "ffff" + line2InHex;

				int zeros = 518 - finalPack.length();

				for (int z = 0; z < zeros; z++)
					finalPack = finalPack + "0";
			} else {
				message = centerText(message);

				for (int i = 0; i < message.length(); i++) {
					char letter = message.charAt(i);
					int r = (int) letter;

					oneLineMessageInHex = oneLineMessageInHex + Integer.toHexString(r);
				}

				int zeros = 518 - 20 - oneLineMessageInHex.length();

				finalPack = debutPack1Line + oneLineMessageInHex;

				for (int z = 0; z < zeros; z++)
					finalPack = finalPack + "0";
			}

			this.packs.add(finalPack);
		}

	}

	private String centerText(String text) {
		if (this.centered) {
			int halfdif = (this.m1MaxLength - text.length()) / 2;

			for (int i = 0; i < halfdif; i++)
				text = " " + text;
		}

		return text;
	}

	public List<String> getAllPacks() {
		return this.packs;
	}
}