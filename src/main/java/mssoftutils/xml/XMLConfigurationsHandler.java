package mssoftutils.xml;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLConfigurationsHandler {

	private StringWriter writer = null;
	private Document document = null;
	private Transformer transformer = null;
	private Result output = null;
	private Source input = null;

	private XPath xPath = XPathFactory.newInstance().newXPath();
	private String documentPath;

	public XMLConfigurationsHandler(String documentPath) {
		try {
			DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

			this.writer = new StringWriter();
			this.transformer = TransformerFactory.newInstance().newTransformer();
			this.document = documentBuilder.parse(new FileInputStream(documentPath));
			this.input = new DOMSource(this.document);
			this.output = new StreamResult(this.writer);
			this.documentPath = documentPath;

		} catch (ParserConfigurationException | SAXException | IOException | TransformerFactoryConfigurationError
				| TransformerConfigurationException e) {
			e.printStackTrace();
		}
	}

	public void modifyNode(String xpathQueryNodeSelector, String newValue) {
		Node node = this.selectNodeUsingXPath(xpathQueryNodeSelector);

		if (node != null)
			node.setTextContent(newValue);
	}

	public String getNodeContent(String xpathQueryNodeSelector) {
		Node node = this.selectNodeUsingXPath(xpathQueryNodeSelector);

		if (node != null)
			return node.getTextContent();

		return null;
	}

	public void validateDocument() {
		try {
			this.writer.getBuffer().delete(0, this.writer.getBuffer().capacity());

			this.transformer.transform(this.input, this.output);
			
			Files.write(Paths.get(this.documentPath), this.writer.getBuffer().toString().getBytes());
		} catch (TransformerException | IOException e) {
			e.printStackTrace();
		}
	}

	private Node selectNodeUsingXPath(String xpathQueryNodeSelector) {
		try {
			NodeList nodes = (NodeList) this.xPath.evaluate(xpathQueryNodeSelector, this.document,
					XPathConstants.NODESET);

			if (nodes.getLength() > 0)
				return nodes.item(0);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		return null;
	}
}
