package mssoftutils.scalehandlers;

import java.io.IOException;
import java.util.Collection;

public interface IScaleHandler {
	void configureBarcode() throws IOException;

	void configureDesign(String shopName) throws IOException;

	void close() throws IOException;

	void addAllProducts(Collection<? extends IScaleProduct> scaleProducts) throws IOException;
}