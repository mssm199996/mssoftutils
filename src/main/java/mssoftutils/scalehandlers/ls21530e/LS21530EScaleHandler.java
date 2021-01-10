package mssoftutils.scalehandlers.ls21530e;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import mssoftutils.scalehandlers.IScaleHandler;
import mssoftutils.scalehandlers.IScaleProduct;
import mssoftutils.scalehandlers.UDPPacketsTransferer;

public class LS21530EScaleHandler implements IScaleHandler {
	private String ip, shopName;

	private int port;
	private File labelFile;

	public LS21530EScaleHandler(String ip, int port, File labelfile) {
		this.ip = ip;
		this.port = port;
		this.labelFile = labelfile;
	}

	@Override
	public void configureBarcode() throws IOException {
	}

	@Override
	public void configureDesign(String shopName) throws IOException {
		this.shopName = shopName;

		if (this.shopName.length() >= 29)
			this.shopName = shopName.substring(0, 28);

		(new UDPPacketsTransferer(this.getLabelPacksFromFile(this.labelFile), this.ip, this.port)).send();
	}

	@Override
	public void addAllProducts(Collection<? extends IScaleProduct> scaleProducts) throws IOException {
		List<PLU> pluList = new LinkedList<>();

		scaleProducts.forEach(produit -> {
			String prix = String.format("%.2f", produit.getPrice());

			PLU plu = new PLU(produit.getName(), produit.getIndex() + "", produit.getBarcode(), prix, true);
			pluList.add(plu);
		});

		Set<String> encounteredCodes = new HashSet<>();
		Iterator<PLU> pluIterator = pluList.iterator();

		while (pluIterator.hasNext()) {
			PLU currentPlu = pluIterator.next();

			if (encounteredCodes.contains(currentPlu.getLfCode()))
				pluIterator.remove();
			else
				encounteredCodes.add(currentPlu.getLfCode());
		}

		List<PLU> sortedPlusList = pluList.stream().sorted(Comparator.comparingInt(PLU::getLfCodeAsInteger))
				.collect(Collectors.toList());

		PLUGenerator pluGenerator = new PLUGenerator(sortedPlusList, this.shopName, true);

		UDPPacketsTransferer udpPacketsTransferer = new UDPPacketsTransferer(pluGenerator.getAllPacks(), this.ip,
				this.port);
		udpPacketsTransferer.send();
	}

	@Override
	public void close() throws IOException {
	}

	public List<String> getLabelPacksFromFile(File file) {
		List<String> result = new LinkedList<>();

		try {
			InputStream flux = new FileInputStream(file);
			InputStreamReader lecture = new InputStreamReader(flux);

			try (BufferedReader buff = new BufferedReader(lecture)) {
				String pack;

				while ((pack = buff.readLine()) != null)
					result.add(pack);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
}
