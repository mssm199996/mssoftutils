package mssoftutils.datas;

public class MSProgressiveRounder {

	public double round(double number) { // 11.01 , 11.10
		String numberAsString = String.format("%.2f", number);

		boolean rounded = false;

		StringBuilder resultAsString = new StringBuilder("");

		for (int i = numberAsString.length() - 1; i >= 0; i--) {
			char currentDigit = numberAsString.charAt(i);

			if (Character.isDigit(currentDigit)) {
				if (currentDigit == '0') {
					resultAsString.append('0');
				} else if (rounded) {
					resultAsString.append(currentDigit);
				} else {
					resultAsString.append('0');

					rounded = true;
				}
			} else
				resultAsString.append('.');
		}

		resultAsString = resultAsString.reverse();

		return Double.parseDouble(resultAsString.toString());
	}
}
