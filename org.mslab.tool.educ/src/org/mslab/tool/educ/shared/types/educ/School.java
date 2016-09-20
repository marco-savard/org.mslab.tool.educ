package org.mslab.commons.shared.types.educ;

import org.mslab.commons.shared.types.Address;
import org.mslab.commons.shared.types.RegionAdministrative;

@SuppressWarnings("serial")
public class School extends Organization {
	private SchoolBoard _schoolBoard; 
	private OrdreAppartenance _ordreApp; 
	
	//required by GWT
	private School() {}

	public School(SchoolBoard schoolBoard, String code, OrganizationName name, 
			Address address, RegionAdministrative regionAdmin, OrdreAppartenance ordreApp) {
		super(code, name, address, regionAdmin);
		_schoolBoard = schoolBoard;
		_ordreApp = ordreApp;
		
		if (_schoolBoard != null) {
			_schoolBoard.addSchool(this);
		}
	}

	public SchoolBoard getSchoolBoard() {
		return _schoolBoard;
	}

	public OrdreAppartenance getOrdreAppartenance() {
		return _ordreApp;
	}

	

	

}
