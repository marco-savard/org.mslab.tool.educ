package org.mslab.tool.educ.client.tool.educ.settings.pref;

public class CityAndProvinceFormatPanel extends AbstractFormatPanel {
	public static final String[] BUILTIN_FORMATS = new String[] {
		"{0}, {2} {3}",  //Ville, QC A1A 1A1
		"{0} ({1}) {3}"  //Ville (Québec) A1A 1A1
	}; 
	
	private static final String[] VARIABLE_NAMES = new String[] {
		"{Ville}", 
		"{NomProvince}",
		"{CodeProvince}",
		"{CodePostal}"
	}; 
	
	private static final String[] VARIABLE_DEFAULT_VALUES = new String[] {
		"Montréal", 
		"Québec", 
		"QC",
		"A1A 1A1"
	}; 
	
	public CityAndProvinceFormatPanel() {
		super(CityAndProvinceFormatPanel.class.getName(),
			"Format de l'adresse :", 
			BUILTIN_FORMATS, 
			VARIABLE_NAMES, 
			VARIABLE_DEFAULT_VALUES);
	}

	@Override
	protected String getFormat() {
		String nameFormat = Preferences.getInstance().getCityFormat();
		return nameFormat;
	}

	@Override
	protected void setFormat(String nameFormat) {
		Preferences.getInstance().setCityFormat(nameFormat);
	}
}
