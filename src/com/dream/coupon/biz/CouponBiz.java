package com.dream.coupon.biz;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.conn.ConnectTimeoutException;
import org.xml.sax.SAXException;
import com.dream.coupon.entity.Coupon;
import com.dream.coupon.utils.CouponXmlParser;
import com.dream.coupon.utils.HttpUtils;

public class CouponBiz {
	public ArrayList<Coupon> getCoupons(String uri,List<? extends NameValuePair> params,int method){
		ArrayList<Coupon> coupons = null;
		try{
			HttpEntity entity = HttpUtils.getEntity(uri, params, method);
			InputStream in = HttpUtils.getStream(entity);
			coupons = new CouponXmlParser().parse(in);
		} catch (ConnectTimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return coupons;
	}
}
