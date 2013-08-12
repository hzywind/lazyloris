package com.lazyloris.modeler.impl;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;

import com.lazyloris.modeler.Model;
import com.lazyloris.modeler.ModelParser;

public abstract class XsltModelParser implements ModelParser {

	private Map<String, Resource> xsls;

	@Override
	public List<Model> parse(InputStream is) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			factory.setIgnoringElementContentWhitespace(true);
			factory.setValidating(false);
			factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(is);
			return parse(transform(doc, builder.newDocument()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	abstract List<Model> parse(Document doc);

	private Document transform(Document doc, Document result) {
		String xmlns = doc.getDocumentElement().getNamespaceURI();
		Resource xsl = xsls.get(xmlns);
		if (xsl == null)
			throw new RuntimeException(String.format("No xsl for xmlns [%s].",
					xmlns));
		try {
			Transformer transformer = TransformerFactory.newInstance()
					.newTransformer(new StreamSource(xsl.getInputStream()));
			DOMResult rc = new DOMResult(result);

			transformer.transform(new DOMSource(doc), rc);

			return (Document) rc.getNode();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Required
	public void setXsls(Map<String, Resource> xsls) {
		this.xsls = xsls;
	}

}
