package mssoftutils.scalehandlers.oldscale;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.IntStream;

import mssoftutils.scalehandlers.IScaleHandler;
import mssoftutils.scalehandlers.IScaleProduct;

public class OldNetworkScaleHandler implements IScaleHandler {

	private static final int DEFAULT_PORT = 4001;
	private static final int TIME_OUT = 5000;

	private Socket socket;
	private String scaleIpAdress;
	private Integer scalePort;

	private BufferedReader scaleReader;
	private BufferedWriter scaleWriter;

	public OldNetworkScaleHandler(String scaleIpAdress, int scalePort) {
		this.scaleIpAdress = scaleIpAdress;
		this.scalePort = scalePort;
	}

	public OldNetworkScaleHandler(String scaleIpAdress) {
		this(scaleIpAdress, OldNetworkScaleHandler.DEFAULT_PORT);
	}

	public void configureBarcode() throws IOException {
		this.writeOneLine("!0O010800020202000100000100000000000000000000000001");
		this.readOneLine();
	}

	public void configureDesign(String shopName) throws IOException {
		if (shopName.length() > 27)
			shopName = shopName.substring(0, 27);

		StringBuffer stringBuffer = new StringBuffer("");

		shopName.chars().forEach(c -> {
			if (c < 10)
				stringBuffer.append("00");
			else if (c < 100)
				stringBuffer.append("0");

			stringBuffer.append(c);
		});

		String x = Integer.toString(this.calculateCenteredXCoordinate(shopName));
		String y = "07";
		String shopNameAsHexa = stringBuffer.toString();

		if (x.length() == 1)
			x = "0" + x;

		this.writeMultipleLines(Arrays.asList(
				"!0T00A45350302120000000000000000000000000000000000000000000000000314220306180000000331181903281903310000000000000000000000000000000000000000000000000319330700000007",
				"!0R00A03022203021800000000000000000015" + x + y
						+ "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000032218000000000000000000000000000000000000000000000000000000",
				"!0Z01A078101116040075071041058000B", "!0Z02A080085058000B",
				"!0Z03A077101114099105032112111117114032118111116114101032102105100101108105116101032033000B",
				"!0Z04A084111116097108000B", "!0Z05A086097108105100000B", "!0Z06A" + shopNameAsHexa + "000B",
				"!0Z07A040047112099115041000B", "!0Z08A040112099115041000B", "!0Z09A040053048048103041000B",
				"!0Z10A040047053048048103041000B", "!0Z11A040103041000B", "!0Z12A040047103041000B",
				"!0Z13A040049048048103041000B", "!0Z14A040047049048048103041000B", "!0Z15A040053048103041000B",
				"!0Z16A040047053048103041000B", "!0Z17A078101116040107103041000B", "!0Z18A040049048103041000B",
				"!0Z19A040047049048103041000B", "!0Z20A040107103041000B", "!0Z21A040047107103041000B",
				"!0Z22A040047107103041000B", "!0Z23A084111116097108058000B", "!0Z24A076046076046000B",
				"!0Z25A066077080049000B", "!0Z26A066077080050000B", "!0Z27A066077080051000B", "!0Z28A066077080052000B",
				"!0Z29A066077080053000B", "!0Z30A066077080054000B", "!0Z31A066077080055000B",
				"!0Z32A066077080056000B"));
	}

	@Override
	public void addAllProducts(Collection<? extends IScaleProduct> scaleProducts) throws IOException {
		for (IScaleProduct product : scaleProducts)
			this.addProduct(product);
	}

	public void addProduct(IScaleProduct product) throws IOException {
		final int indexLength = 4;
		final int barcodeLengthLimit = 7;
		final int leftPriceLimit = 4;
		final int rightPriceLimit = 2;

		String barcode = product.getBarcode();
		String name = product.getName();

		int index = product.getIndex();
		double price = product.getPrice();

		if (index > 4000)
			throw new IllegalArgumentException("The index must be whithin [0; 4000]");
		if (barcode.length() > barcodeLengthLimit)
			throw new IllegalArgumentException("The barcode length must be <= 6");

		String indexAsString = Integer.toString(index);

		StringBuffer stringBuffer = new StringBuffer("");
		stringBuffer.append("!0V");

		IntStream.range(0, indexLength - indexAsString.length()).forEach(i -> stringBuffer.append("0"));
		stringBuffer.append(index + "A");

		IntStream.range(0, barcodeLengthLimit - barcode.length()).forEach(i -> stringBuffer.append("0"));
		stringBuffer.append(barcode);

		int leftWeight = (int) price;
		int rightWeight = (int) Math.ceil(((price - leftWeight) * 100));

		String leftWeightAsString = Integer.toString(leftWeight);
		String rightWeightAsString = Integer.toString(rightWeight);

		IntStream.range(0, leftPriceLimit - leftWeightAsString.length()).forEach(i -> stringBuffer.append("0"));
		stringBuffer.append(leftWeight);
		stringBuffer.append(rightWeight);
		IntStream.range(0, rightPriceLimit - rightWeightAsString.length()).forEach(i -> stringBuffer.append("0"));

		stringBuffer.append("000000000000000000000000000000000000000000000000000000000000B");

		name.chars().forEach(c -> {
			if (c < 10)
				stringBuffer.append("00");
			else if (c < 100)
				stringBuffer.append("0");

			stringBuffer.append(c);
		});

		stringBuffer.append("000C000D000E");

		this.writeOneLine(stringBuffer.toString());
		this.readOneLine();
	}

	private int calculateCenteredXCoordinate(String text) {
		final double ticketLength = 45.0;

		double x = (-1.55 / 2.0 * text.length()) + ticketLength / 2.0;

		return this.toInt(x);
	}

	private int toInt(double x) {
		double r = x - ((int) x);

		if (r < 0.5)
			return ((int) x);
		else
			return ((int) x) + 1;
	}

	public void connect() throws UnknownHostException, IOException {
		this.socket = new Socket(this.scaleIpAdress, this.scalePort);
		this.socket.setSoTimeout(OldNetworkScaleHandler.TIME_OUT);

		this.scaleReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
		this.scaleWriter = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
	}

	public void writeOneLine(String payload) throws IOException {
		this.scaleWriter.write(payload + "\r\n");
		this.scaleWriter.flush();
	}

	public void writeMultipleLines(Collection<String> payloads) throws IOException {
		for (String payload : payloads) {
			this.writeOneLine(payload);
			this.readOneLine();
		}
	}

	public String readOneLine() throws IOException {
		return this.scaleReader.readLine();
	}

	public void close() throws IOException {
		this.scaleReader.close();
		this.scaleWriter.close();
		this.socket.close();
	}
}
