package org.mslab.tool.educ.server.postalcodes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.mslab.tool.educ.shared.text.MessageFormat;
import org.mslab.tool.educ.shared.text.Text;
import org.mslab.tool.educ.shared.types.GeoLocation;
import org.mslab.tool.educ.shared.types.PostalCode;

public class PostalCodeFinder {
	
	public PostalCodeFinder() {	
	}

	public GeoLocation find(String code) {
		code = code.toUpperCase(); 
		String resname = code.substring(0, 2) + ".csv"; 
		URL url = PostalCodeFinder.class.getResource(resname); 
		GeoLocation postalCode;
		
		try {
			InputStream input = (url == null) ? null : url.openStream();
			if (input == null) {
				postalCode = null;
			} else {
				postalCode = findInResource(input, code); 
				input.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			postalCode = null;
		}
		
		return postalCode;
	}

	private GeoLocation findInResource(InputStream input, String code) throws IOException {
		InputStreamReader ir = new InputStreamReader(input); 
		BufferedReader br = new BufferedReader(ir); 
		GeoLocation location = null; 
		int bestMatch = 0;
		String line;
		
		try {
			while (true) {
				line = br.readLine();
				if (line == null) {
					break;
				} else {
					String[] values = line.split(";");
					int match = compare(code, values[0], bestMatch+1); 
					
					if (match > bestMatch) {
						bestMatch = match;
						double lat = Double.parseDouble(values[1]); 
						double lon = Double.parseDouble(values[2]); 
						String city = new Text(values[3]).trimQuotes().toString(); 
						
						location = new GeoLocation(lat, lon, new PostalCode(values[0]), city);
					}
				}
				
				if (bestMatch == 6) {
					break;
				}
			}
		} catch (Exception ex) {
			String msg = MessageFormat.format("Error on {0}", new Object[] {code});
			int i=0; 
			i++;
		} //end try
		
		
		
		
		return location;
	}

	private int compare(String thisCode, String otherCode, int len) {
		boolean matches;
		int match = len-1;
		
		for (int i=len; i<=6; i++) {
			matches = thisCode.regionMatches(0, otherCode, 0, i);
			
			if (! matches) {
				break;
			} else {
				match = i;
			}
		}
		return match;
	}

}
