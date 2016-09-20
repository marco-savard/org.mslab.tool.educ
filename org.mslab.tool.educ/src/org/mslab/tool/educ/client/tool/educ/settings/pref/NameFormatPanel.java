package org.mslab.commons.client.tool.educ.settings.pref;

import org.mslab.commons.shared.text.MessageFormat;

public class NameFormatPanel extends AbstractFormatPanel {
	public static final String[] BUILTIN_FORMATS = new String[] {
		"{0} {1}",  //Prenom nom
		"{1}, {0}"  //Nom, Prenom
	}; 
	
	private static final String[] VARIABLE_NAMES = new String[] {
		"{Prénom}", 
		"{Nom}"
	}; 
	
	private static final String[] VARIABLE_DEFAULT_VALUES = new String[] {
		"Pierre", 
		"Tremblay"
	}; 
	
	public NameFormatPanel() {
		super(NameFormatPanel.class.getName(),
			"Format des noms :", 
			BUILTIN_FORMATS, 
			VARIABLE_NAMES, 
			VARIABLE_DEFAULT_VALUES);
	}

	@Override
	protected String getFormat() {
		String nameFormat = Preferences.getInstance().getNameFormat();
		return nameFormat;
	}

	@Override
	protected void setFormat(String nameFormat) {
		Preferences.getInstance().setNameFormat(nameFormat);
	}
	


}
