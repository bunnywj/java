package com.localhost.sql;

import java.util.Hashtable;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ParseXML {
	private Hashtable<String, String> action;

	public Hashtable<String, String> getActionTable() {
		return this.action;
	}

	@SuppressWarnings("unchecked")
	public void parseXML(String fileName) throws DocumentException {
		this.action = new Hashtable<String, String>();

		SAXReader sax = new SAXReader();
		Document doc = sax.read(fileName);
		Element root = doc.getRootElement();
		List<Element> rootElems = root.elements();
		for (int i = 0; i < rootElems.size(); ++i) {
			String actionClass = rootElems.get(i).element("class").getTextTrim();
			action.put(actionClass.toLowerCase() + ".html", actionClass);
		}
	}
}
