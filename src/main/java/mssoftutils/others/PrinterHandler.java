package mssoftutils.others;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;

import com.profesorfalken.wmi4java.WMI4Java;
import com.profesorfalken.wmi4java.WMIClass;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;

@SuppressWarnings("deprecation")
public abstract class PrinterHandler {
	public static List<String> AVAILABLE_PRINTERS = Arrays
			.stream((WMI4Java.get().properties(Arrays.asList("Name", "WorkOffline"))
					.filters(Arrays.asList("$_.WorkOffline -eq 0")).getRawWMIObjectOutput(WMIClass.WIN32_PRINTER))
							.split("(\r?\n)"))
			.filter(line -> line.startsWith("Name")).map(line -> line.replaceFirst(".* : ", "")).sorted()
			.collect(Collectors.toList());

	protected PrintService printer = null;
	protected JRPrintServiceExporter exporter = new JRPrintServiceExporter();;

	public void initPrinter(String printerName) {
		this.printer = null;

		PrintService[] printServices = PrinterJob.lookupPrintServices();

		for (PrintService printerService : printServices) {
			if (printerService.getName().equals(printerName)) {
				this.printer = printerService;
			}

			if (this.printer != null)
				break;
		}

		this.doAfterInit();
	}

	protected void printJob(byte[] commands) {
		if (this.printer != null && PrinterHandler.AVAILABLE_PRINTERS.contains(this.printer.getName())) {
			Doc myDoc = new SimpleDoc(commands, DocFlavor.BYTE_ARRAY.AUTOSENSE, null);
			DocPrintJob job = this.printer.createPrintJob();

			try {
				job.print(myDoc, null);
			} catch (PrintException e) {
				e.printStackTrace();
			}
		}
	}

	protected void printImageJob(BufferedImage image, int numberCopies) throws FileNotFoundException {
		PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
		pras.add(new Copies(numberCopies));

		if (this.printer != null && PrinterHandler.AVAILABLE_PRINTERS.contains(this.printer.getName())) {
			PrinterJob printJob = PrinterJob.getPrinterJob();
	        printJob.setPrintable(new ImagePrintable(printJob, image));

	        if (printJob.printDialog()) {
	            try {
	                printJob.print();
	            } catch (PrinterException prt) {
	                prt.printStackTrace();
	            }
	        }
		}
	}

	protected void printUsingJasperReport(JasperPrint jasperPrint, int numberOfCopies) {
		try {
			PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
			printRequestAttributeSet.add(new Copies(numberOfCopies));

			this.exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			this.exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, this.printer);
			this.exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET,
					this.printer.getAttributes());
			this.exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET,
					printRequestAttributeSet);
			this.exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);
			this.exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE);
			this.exporter.setParameter(JRPrintServiceExporterParameter.IGNORE_PAGE_MARGINS, Boolean.FALSE);
			this.exporter.exportReport();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected abstract void doAfterInit();

	public class ImagePrintable implements Printable {

		private double x, y, width;

		private int orientation;

		private BufferedImage image;

		public ImagePrintable(PrinterJob printJob, BufferedImage image) {
			PageFormat pageFormat = printJob.defaultPage();
			this.x = pageFormat.getImageableX();
			this.y = pageFormat.getImageableY();
			this.width = pageFormat.getImageableWidth();
			this.orientation = pageFormat.getOrientation();
			this.image = image;
		}

		@Override
		public int print(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException {
			if (pageIndex == 0) {
				int pWidth = 0;
				int pHeight = 0;
				if (orientation == PageFormat.PORTRAIT) {
					pWidth = (int) Math.min(width, (double) image.getWidth());
					pHeight = pWidth * image.getHeight() / image.getWidth();
				} else {
					pHeight = (int) Math.min(width, (double) image.getHeight());
					pWidth = pHeight * image.getWidth() / image.getHeight();
				}
				g.drawImage(image, (int) x, (int) y, pWidth, pHeight, null);
				return PAGE_EXISTS;
			} else {
				return NO_SUCH_PAGE;
			}
		}

	}
}
