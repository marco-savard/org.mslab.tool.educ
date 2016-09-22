package org.mslab.tool.educ.client.tool.educ.people.viewer;

import java.util.List;

import org.mslab.tool.educ.client.tool.educ.EducLoader;
import org.mslab.tool.educ.shared.types.ContactSuggestNames;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;

public class EducPeopleSuggestBoxGivenName extends SuggestBox {
	
	EducPeopleSuggestBoxGivenName() {
		super(getGivenNameOracle());
		setWidth("9em");
		Style style = getElement().getStyle();
		style.setBorderStyle(BorderStyle.NONE);
		getElement().setPropertyString("placeholder", "Rechercher un prénom");
	}

	private static MultiWordSuggestOracle getGivenNameOracle() {
		MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
		ContactSuggestNames suggestNames = EducLoader.getInstance().getLoadedSuggestNames();
		oracle.addAll(suggestNames.getMaleGivenNames());
		oracle.addAll(suggestNames.getFemaleGivenNames());
		return oracle;
	}

}
