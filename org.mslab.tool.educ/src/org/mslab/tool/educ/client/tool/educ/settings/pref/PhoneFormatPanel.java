package org.mslab.tool.educ.client.tool.educ.settings.pref;

public class PhoneFormatPanel extends AbstractFormatPanel {
	public static final String[] BUILTIN_FORMATS = new String[] {
		"{0} {1}-{2}",    //CodeRégional, BlocDébut, BlocFin
		"{0}-{1}-{2}",    //CodeRégional, BlocDébut, BlocFin
		"({0}) {1}-{2}",  //CodeRégional, BlocDébut, BlocFin
		"{0}.{1}.{2}",    //CodeRégional, BlocDébut, BlocFin
	}; 
	
	private static final String[] VARIABLE_NAMES = new String[] {
		"{CodeRégional}", 
		"{Début}",
		"{Fin}"
	}; 
	
	private static final String[] VARIABLE_DEFAULT_VALUES = new String[] {
		"514", 
		"123", 
		"4567"
	}; 
	
	public PhoneFormatPanel() {
		super(PhoneFormatPanel.class.getName(), 
			"Format des numéro de téléphone :", 
			BUILTIN_FORMATS, 
			VARIABLE_NAMES, 
			VARIABLE_DEFAULT_VALUES);
	}
	
	@Override
	protected String getFormat() {
		String nameFormat = Preferences.getInstance().getPhoneFormat();
		return nameFormat;
	}

	@Override
	protected void setFormat(String nameFormat) {
		Preferences.getInstance().setPhoneFormat(nameFormat);
	}
	
	
}
