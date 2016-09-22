package org.mslab.tool.educ.client.core.system;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class IpInfo {	
	
	private IpInfo(String country, String region, String city, String location, String postalCode) {
		_country = country;
		_region = region;
		_city = city;
		
		int idx = location.indexOf(','); 
		_latitude = Double.parseDouble(location.substring(0, idx-1));
		_longitude = Double.parseDouble(location.substring(idx+1));
		_postalCode = postalCode;
	}
	
	//properties
	private String _country;
	public String getCountry() { return _country;}
	
	private String _region;
	public String getRegion() { return _region;}
	
	private String _city;
	public String getCity() { return _city;}
	
	private String _postalCode;
	public String getPostalCode() { return _postalCode;}
	
	private double _latitude;
	public double getLatitude() { return _latitude; } 
	
	private double _longitude;
	public double getLongitude() { return _longitude; } 
	
	public static void request(final Callback callback) {
		String URL = "http://ipinfo.io/json";	
		JsonpRequestBuilder builder = new JsonpRequestBuilder();
		
		builder.requestObject(URL, new AsyncCallback<JSONreceiver>() {
		      public void onFailure(Throwable caught) {
		         System.err.println(caught.toString());
		         callback.onSuccess(null);
		      }
		      
			public void onSuccess(JSONreceiver data) {
				IpInfo ipInfo = new IpInfo(data.getCountry(), data.getRegion(), data.getCity(), 
					data.getLocation(), data.getPostalCode());
				callback.onSuccess(ipInfo);
		      }
		});
	}
	
	//
	// inner classes
	//
	public interface Callback {
		public void onSuccess(IpInfo info);
	}
	
	//see http://ipinfo.io/developers
	public static class JSONreceiver extends JavaScriptObject {
		protected JSONreceiver() {}
		public final native String getCountry() /*-{
	        return this.country;
		}-*/;
		
		public final native String getRegion() /*-{
            return this.region;
	    }-*/;
		
		public final native String getCity() /*-{
		    return this.city;
		}-*/;
		
		public final native String getLocation() /*-{
	        return this.loc;
	    }-*/;
		
		public final native String getPostalCode() /*-{
        	return this.postal;
    	}-*/;
		
		//also: hostname, ip, org, phone
	}

	





}
