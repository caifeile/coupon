package com.dream.coupon.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import com.dream.coupon.entity.Partner;

public class PartnerDetailXmlParser {
	
	private class PartnerXmlHandler extends DefaultHandler {
		private HashMap<String,Object> map;
		private ArrayList<HashMap<String,Object>> coupons;
		private HashMap<String,Object> coupon;
		private Partner partner;
		private String tagName;
		private StringBuilder sb = new StringBuilder();
		private String bfPartnerName = null;

		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			sb.append(ch,start,length);
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			String data = sb.toString();
			if ("title".equals(tagName)) {
				coupon.put("title",data);
			} else if ("rebate".equals(tagName)) {
				coupon.put("rebate",data);
			} else if ("indate".equals(tagName)) {
				coupon.put("indate",data);
			} else if ("name".equals(tagName)) {
				bfPartnerName = data;
				partner.setTitle(data);
			} else if ("image".equals(tagName)) {
				partner.setImage(data);
			} else if ("phone".equals(tagName)) {
				partner.setPhone(data);
			} else if ("address".equals(tagName)){
				partner.setAddress(data);
			} else if ("hours".equals(tagName)){
				partner.setHours(data);
			} else if ("content".equals(tagName)){
				partner.setContent(data);
			}
			
			if ("coupon".equals(localName)) {
				coupons.add(coupon);
			}
			tagName = null;
		}
		
		@Override
		public void endDocument() throws SAXException{
			map.put("coupons", coupons);
			map.put("partner",partner);
		}

		@Override
		public void startDocument() throws SAXException {
			map = new HashMap<String,Object>();
			coupons = new ArrayList<HashMap<String,Object>>();
			partner = new Partner();
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			tagName = localName;
			if ("coupon".equals(localName)) {
				coupon = new HashMap<String,Object>();
				coupon.put("id",(Integer.parseInt(attributes.getValue("id"))));
				if(bfPartnerName!=null){
					coupon.put("partnerName", bfPartnerName);
				}
			}else if("partner".equals(localName)){
				partner.setId(Integer.parseInt(attributes.getValue("id")));
			}
			sb.setLength(0);
		}
	}

	public HashMap<String,Object> parse(InputStream in)
			throws ParserConfigurationException, SAXException, IOException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		PartnerXmlHandler handler = new PartnerXmlHandler();
		parser.parse(in, handler);
		return handler.map;
	}
}
