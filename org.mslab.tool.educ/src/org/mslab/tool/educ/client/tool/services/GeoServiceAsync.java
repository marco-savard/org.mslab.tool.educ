package org.mslab.tool.educ.client.tool.services;

import java.util.List;

import org.mslab.tool.educ.shared.types.Captcha;
import org.mslab.tool.educ.shared.types.Contact;
import org.mslab.tool.educ.shared.types.ContactSuggestNames;
import org.mslab.tool.educ.shared.types.GeoLocation;
import org.mslab.tool.educ.shared.types.educ.Organization;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GeoServiceAsync {
	
	void createCaptcha(int captchaWidth, int captchaHeight,
			AsyncCallback<Captcha> callback);
	
	void findPostalCode(String code, AsyncCallback<GeoLocation> callback);
	void findPostalCodes(List<String> postalCodes, AsyncCallback<List<GeoLocation>> callback);
	
	void getClientLocation(AsyncCallback<GeoLocation> callback);
	
	void getSuggestNames(AsyncCallback<ContactSuggestNames> callback);
	void getContacts(int nbContacts, AsyncCallback<List<Contact>> callback);
	
	void getListOrganizations(AsyncCallback<List<Organization>> callback);
	
}
