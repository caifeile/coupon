package com.dream.coupon.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import com.dream.coupon.entity.Coupon;

public class CouponXmlParser {
	private class MusicXmlHandler extends DefaultHandler {
		private ArrayList<Coupon> coupons;
		private Coupon coupon;
		private String tagName;

		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			// TODO Auto-generated method stub
			String data = new String(ch, start, length);
			if ("partnerId".equals(tagName)) {
				coupon.setPartnerId(Integer.parseInt(data));
			} else if ("partnerName".equals(tagName)) {
				coupon.setPartnerName(data);
			} else if ("partnerAddress".equals(tagName)) {
				coupon.setPartnerAddress(data);
			} else if ("couponImage".equals(tagName)) {
				coupon.setCouponImage(data);
			} else if ("partnerImage".equals(tagName)) {
				coupon.setPartnerImage(data);
			} else if ("title".equals(tagName)) {
				coupon.setTitle(data);
			} else if ("tag".equals(tagName)) {
				coupon.setTag(data);
			} else if ("indate".equals(tagName)) {
				coupon.setIndate(data);
			}
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			// TODO Auto-generated method stub
			if ("coupon".equals(localName)) {
				coupons.add(coupon);
			}
			tagName = null;
		}

		@Override
		public void startDocument() throws SAXException {
			// TODO Auto-generated method stub
			coupons = new ArrayList<Coupon>();
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			// TODO Auto-generated method stub
			tagName = localName;
			if ("coupon".equals(localName)) {
				coupon = new Coupon();
				coupon.setId(Integer.parseInt(attributes.getValue("id")));
			}
		}

	}

	public ArrayList<Coupon> parse(InputStream in)
			throws ParserConfigurationException, SAXException, IOException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		MusicXmlHandler handler = new MusicXmlHandler();
		parser.parse(in, handler);
		return handler.coupons;
	}

}
