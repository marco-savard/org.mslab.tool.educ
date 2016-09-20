package org.mslab.commons.client.tool.educ.people.viewer;

import java.util.List;

import org.mslab.commons.client.tool.educ.EducContext;
import org.mslab.commons.client.tool.educ.EducLoader;
import org.mslab.commons.shared.types.ContactSuggestNames;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;

public class EducPeopleSuggestBoxFamilyName extends SuggestBox {
	
	EducPeopleSuggestBoxFamilyName() {
		super(getGivenNameOracle());
		setWidth("9em");
		Style style = getElement().getStyle();
		style.setBorderStyle(BorderStyle.NONE);
		getElement().setPropertyString("placeholder", "Rechercher un nom de famille");
	}

	private static MultiWordSuggestOracle getGivenNameOracle() {
		MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
		ContactSuggestNames suggestNames = EducLoader.getInstance().getLoadedSuggestNames();
		List<String> familyNames = suggestNames.getFamilyNames();
		oracle.addAll(familyNames);
		return oracle;
	}

}
