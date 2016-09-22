package org.mslab.tool.educ.client.tool.services;

import java.util.List;

import org.mslab.tool.educ.shared.types.Captcha;
import org.mslab.tool.educ.shared.types.Contact;
import org.mslab.tool.educ.shared.types.ContactSuggestNames;
import org.mslab.tool.educ.shared.types.GeoLocation;
import org.mslab.tool.educ.shared.types.educ.Organization;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("geo")
public interface GeoService extends RemoteService {
	
	Captcha createCaptcha(int captchaWidth, int captchaHeight) throws IllegalArgumentException;
	
	GeoLocation findPostalCode(String code) throws IllegalArgumentException;
	List<GeoLocation> findPostalCodes(List<String> postalCodes) throws IllegalArgumentException;
	
	GeoLocation getClientLocation() throws IllegalArgumentException;
	
	ContactSuggestNames getSuggestNames() throws IllegalArgumentException;

	List<Contact> getContacts(int nbContacts) throws IllegalArgumentException;
	
	List<Organization> getListOrganizations() throws IllegalArgumentException;

	
}
