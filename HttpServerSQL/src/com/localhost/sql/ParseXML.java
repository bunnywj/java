package com.localhost.sql;

import java.util.*;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ParseXML {
	// 定义对象表，前面为uri，后面为对应uri的实际对象
	private Hashtable<String, Object> objectTable;

	public Hashtable<String, Object> getObjectTable() {
		return this.objectTable;
	}

	private Object createObject(String objectName)
			throws ClassNotFoundException, IllegalAccessException,
			InstantiationException {
		Class<?> c = Class.forName(objectName);
		return c.newInstance();
	}

	@SuppressWarnings("unchecked")
	public void parseXML(String fileName) throws DocumentException,
			InstantiationException, IllegalAccessException, ClassNotFoundException{
		objectTable = new Hashtable<String, Object>();
		
		SAXReader sax = new SAXReader();
		Document doc = sax.read(fileName);
		Element root = doc.getRootElement();
		List<Element> rootElems = root.elements();

		for (int i = 0; i < rootElems.size(); ++i) {
			String uriName = rootElems.get(i).element("URLName").getTextTrim();
			String objectName = rootElems.get(i).element("ObjectName")
					.getTextTrim();
			objectTable.put(uriName, createObject(objectName));
		}
	}
}
