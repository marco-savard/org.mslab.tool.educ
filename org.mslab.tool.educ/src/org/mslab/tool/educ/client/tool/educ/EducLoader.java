package org.mslab.commons.client.tool.educ;

import org.mslab.commons.client.tool.services.ServiceStore;
import org.mslab.commons.shared.types.ContactSuggestNames;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class EducLoader {
	private ContactSuggestNames _loadedSuggestNames; 
	private Runnable _onLoadComplete;
	
	public static EducLoader load(Runnable onLoadComplete) {
		_instance = new EducLoader(onLoadComplete);
		return _instance;
	}
	
	private static EducLoader _instance;
	
	public static EducLoader getInstance() {
		//TODO if _instance == null??
		
		return _instance;
	}
	
	private EducLoader(Runnable onLoadComplete) {
		_onLoadComplete = onLoadComplete;
		loadSuggestNames();
	}

	private void loadSuggestNames() {
		AsyncCallback<ContactSuggestNames> callback = new AsyncCallback<ContactSuggestNames>() {

			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(ContactSuggestNames suggestNames) {
				onSuggestNamesLoaded(suggestNames);
			}
		}; 
		
		ServiceStore.getService().getSuggestNames(callback);	
	}
	
	private void onSuggestNamesLoaded(ContactSuggestNames suggestNames) {
		_loadedSuggestNames = suggestNames;
		_onLoadComplete.run();
		
	}

	public ContactSuggestNames getLoadedSuggestNames() {
		return _loadedSuggestNames;
	}









}
