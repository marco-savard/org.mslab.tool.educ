package org.mslab.tool.educ.shared.types;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.mslab.tool.educ.shared.text.MessageFormat;
import org.mslab.tool.educ.shared.types.educ.City;

@SuppressWarnings("serial")
public class GeoLocation implements Serializable {
	private static final String[] PROVINCES = new String[] {
		"NL", "NS", "PE", "", "NB", "", //A, B, C, E
		"QC", "QC", "", "QC", //G, H, J
		"ON", "ON", "ON", "ON", "", "ON", "", //K, L, M, N, P
		"MB", "SK", "AB", "", //R, S, T
		"BC", "", "NT", "YK", "" //V, X, Y
	}; 

	PostalCode _postalCode;
	
	City _city;
	public City getCity() { return _city; }
	public void setCity(String city) { _city = new City(city); }
	
	double _lat, _lon;
	
	//required by GWT
	@SuppressWarnings("unused")
	private GeoLocation() {}
	
	public GeoLocation(double lat, double lon) {
		this(lat, lon, (PostalCode)null, "");
	}

	public GeoLocation(double lat, double lon, PostalCode postalCode, String cityName) {
		_lat = lat;
		_lon = lon;
		_postalCode = postalCode;
		_city = new City(cityName); 
	}

	public GeoLocation(PostalCode postalCode, String cityName) {
		_postalCode = postalCode;
		_city = new City(cityName); 
	}
	
	@Override
	public String toString() {
		String s = MessageFormat.format("{0};{1};{2};\"{3}\"", new Object[] {_postalCode, _lat, _lon, _city}); 
		return s;
	}

	public double getLatitude() { return _lat; }
	public void setLatitude(double lat) { _lat = lat; }
	
	public double getLongitude() { return _lon; }
	public void setLongitude(double lon) { _lon = lon; }

	public PostalCode getPostalCode() {
		return _postalCode;
	}

	public String getProvinceCode() {
		String postalCode = _postalCode.toString();
		char ch = (postalCode.length() == 6) ? postalCode.charAt(0) : ' ';
		String provCode = PROVINCES[ch-'A']; 
		return provCode;
	}
	
	public String getProvinceName() {
		String provCode = getProvinceCode(); 
		String provName = getProvinceNames().get(provCode); 
		return provName;
	}

	private static Map<String, String> getProvinceNames() {
		if (_provinceNames == null) {
			_provinceNames = new HashMap<String, String>();
			int nb = PROVINCE_NAMES.length / 2; 
			for (int i=0; i<nb; i++) {
				_provinceNames.put(PROVINCE_NAMES[i*2], PROVINCE_NAMES[i*2+1]);
			}
		}

		return _provinceNames;
	}
	private static Map<String, String> _provinceNames = null;
	
	private static final String[] PROVINCE_NAMES = new String[] {
		"NL", "Terre-Neuve", 
		"NS", "Nouvelle-Ecosse",
		"PE", "Ile-du-Prince-Edouard",
		"NB", "Nouveau-Brunswick",
		"QC", "Québec", 
		"ON", "Ontario", 
		"MB", "Manitoba", 
		"SK", "Saskatchewan", 
		"AB", "Alberta", 
		"BC", "Colombie-Britannique", 
		"NT", "Territoire-du-Nord-Ouest", 
		"YK", "Yukon", 
		"NU", "Nunavut",
	}; 
	
	
	//in km
	public double computeDistanceFrom(GeoLocation location) {
		if (location == null) {
			return 0;
		}
		
		double lat0 = toRadians(location.getLatitude()); 
		double lon0 = toRadians(location.getLongitude());
		double lat1 = toRadians(getLatitude()); 
		double lon1 = toRadians(getLongitude());
		
		double deltaLon = Math.abs(lon0 - lon1); 
		double greatCircle = (Math.sin(lat0) * Math.sin(lat1)) + (Math.cos(lat0) * Math.cos(lat1) * Math.cos(deltaLon)); 
		double deltaAngle = Math.acos(greatCircle); 
		double distance = EARTH_RADIUS * deltaAngle;
		
		//0.1 km of precision
		//distance = ((int)(distance * 10) / 10.0); 
		return distance;
	}
	private static final double EARTH_RADIUS = 6371.01; //mean radius in km
	
	private double toRadians(double degs) {
		double rads = degs * Math.PI / 180.0;
		return rads;
	}
	


}
