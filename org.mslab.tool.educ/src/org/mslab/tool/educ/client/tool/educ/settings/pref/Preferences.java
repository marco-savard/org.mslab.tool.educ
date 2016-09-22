package org.mslab.tool.educ.client.tool.educ.settings.pref;

public class Preferences {
	
	private static Preferences _instance;
	private Preferences() {}
	public static Preferences getInstance() {
		if (_instance == null) {
			_instance = new Preferences(); 
		}
		
		return _instance;
	}
	
	private String _nameFormat = NameFormatPanel.BUILTIN_FORMATS[0]; 
	public void setNameFormat(String nameFormat) { _nameFormat = nameFormat; }
	public String getNameFormat() { return  _nameFormat; }
	
	
	private String _phoneFormat = PhoneFormatPanel.BUILTIN_FORMATS[0]; 
	public void setPhoneFormat(String phoneFormat) { _phoneFormat = phoneFormat; }
	public String getPhoneFormat() { return  _phoneFormat; }
	
	private String _cityFormat = CityAndProvinceFormatPanel.BUILTIN_FORMATS[0];
	public void setCityFormat(String cityFormat) { _cityFormat = cityFormat; }
	public String getCityFormat() { return _cityFormat; }
	


}
