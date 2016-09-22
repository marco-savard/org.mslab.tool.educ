package org.mslab.tool.educ.client.tool.services;

import com.google.gwt.core.client.GWT;

public class ServiceStore {
	
	private static GeoServiceAsync _service = null;
	public static GeoServiceAsync getService() {
		if (_service == null) {
			try {
				_service = GWT.create(GeoService.class);
			} catch (RuntimeException ex) {
				handleException(ex); 
			}
		}
		
		return _service;
	} //end getService()
	
	private static void handleException(RuntimeException ex) {
		// TODO Auto-generated method stub
		
	}
}
