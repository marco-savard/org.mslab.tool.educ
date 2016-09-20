package org.mslab.commons.shared.types.educ;

import java.util.ArrayList;
import java.util.List;

import org.mslab.commons.shared.types.Address;
import org.mslab.commons.shared.types.RegionAdministrative;

@SuppressWarnings("serial")
public class SchoolBoard extends Organization {
	
	//required by GWT
	private SchoolBoard() {}

	public SchoolBoard(String code, OrganizationName name, Address address,
			RegionAdministrative regionAdmin) {
		super(code, name, address, regionAdmin);
	}
	
	public void addSchool(School school) { _schools.add(school); }
	public List<School> getSchools() { return _schools; }
	private List<School> _schools = new ArrayList<School>();

	public static List<SchoolBoard> getList(List<Organization> organizations) {
		List<SchoolBoard> schoolBoards = new ArrayList<SchoolBoard>(); 
		
		for (Organization organization : organizations) {
			if (organization instanceof SchoolBoard) {
				SchoolBoard schoolBoard = (SchoolBoard)organization;
				if (! schoolBoards.contains(schoolBoard)) {
					schoolBoards.add(schoolBoard);
				}
			}
		}
		
		return schoolBoards;
	}



	

	

	



}
