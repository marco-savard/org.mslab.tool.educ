package org.mslab.tool.educ.shared.text;

import com.google.gwt.i18n.client.LocaleInfo;

public class GeoFormat {
	private String _north = "N", _south = "S", _east = "E", _west = "W"; //by default
	
	public static void main(String[] args) {
		double lat = 46.803283;
		double lon = -71.242796;
		
		GeoFormat format = GeoFormat.getInstance(LocaleInfo.getCurrentLocale()); 
		String la = format.formatLatitude(lat, false); 
		String lo = format.formatLatitude(lon, false);
		
		System.out.println(la + "; " + lo);
	}

	public static GeoFormat getInstance(LocaleInfo locale) {
		GeoFormat format = new GeoFormat(locale); 
		return format;
	}
	
	private GeoFormat(LocaleInfo locale) {
		if ((getLanguage(locale).equals("fr"))) {
			_west = "O";
		}
	}

	private String getLanguage(LocaleInfo locale) {
		String info = locale.getLocaleName();
		String language = info.substring(0, 2);
		return language;
	}

	public String formatLatitude(double lat, boolean html) {
		String hemis = (lat >= 0) ? _north : _south;
		String txt = formatCoordinate(lat, html, hemis); 
		return txt;
	}

	public String formatLongitude(double lon, boolean html) {
		String hemis = (lon >= 0) ? _east : _west;
		String txt = formatCoordinate(lon, html, hemis); 
		return txt;
	}
	
	private String formatCoordinate(double coor, boolean html, String hemis) {
		coor = Math.abs(coor);
		int deg = (int)Math.floor(coor);
		int min = (int)Math.floor((60 * coor) - (60 * deg));
		int sec = (int)Math.floor((60*60*coor) - (60*60*deg) - (60*min));
		
		String patt = html ? "{0}&deg; {1}&prime; {2}&Prime; {3}" : "{0}deg {1}\' {2}\" {3}";
		String txt = MessageFormat.format(patt, new Object[] {deg, min, sec, hemis}); 
		return txt;
	}

}
