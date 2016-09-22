package org.mslab.tool.educ.client.tool.educ.school.viewer.content;

import java.util.Comparator;

import org.mslab.tool.educ.client.tool.educ.EducContext;
import org.mslab.tool.educ.shared.text.AlphabeticComparator;
import org.mslab.tool.educ.shared.types.Address;
import org.mslab.tool.educ.shared.types.GeoLocation;
import org.mslab.tool.educ.shared.types.PostalCode;
import org.mslab.tool.educ.shared.types.educ.City;
import org.mslab.tool.educ.shared.types.educ.Organization;

public class OrganizationComparator implements Comparator<Organization> {
	public enum Criteria {NAME, CITY, POSTAL_CODE, DISTANCE}
	private Criteria _criteria;
	//private int _selectedCriteriaIdx; 
	private boolean _ascendent; 
	private AlphabeticComparator _alphabeticComparator = new AlphabeticComparator();
	
	@Override
	public int compare(Organization o1, Organization o2) {
		int comparison = 0;
		
		if (_criteria == Criteria.NAME) {
			String s1 = o1.getName(); 
			String s2 = o2.getName(); 
			comparison = _alphabeticComparator.compare(s1, s2); 
		} else if (_criteria == Criteria.CITY) {
			Address a1 = o1.getAddress(); 
			Address a2 = o2.getAddress(); 
			GeoLocation l1 = ( a1 == null) ? null : a1.getLocation(); 
			GeoLocation l2 = ( a2 == null) ? null : a2.getLocation(); 
			City c1 = (l1 == null) ? null : l1.getCity(); 
			City c2 = (l2 == null) ? null : l2.getCity(); 
			String s1 = c1 == null ? "" : c1.getName();
			String s2 = c2 == null ? "" : c2.getName();
			comparison = _alphabeticComparator.compare(s1, s2); 
			
		} else if (_criteria == Criteria.POSTAL_CODE) {
			Address a1 = o1.getAddress(); 
			Address a2 = o2.getAddress(); 
			PostalCode pc1 = (a1 == null) ? null : a1.getPostalCode(); 
			PostalCode pc2 = (a2 == null) ? null : a2.getPostalCode(); 
			
			String s1 = pc1 == null ? "" : pc1.toString();
			String s2 = pc2 == null ? "" : pc2.toString();
			comparison = _alphabeticComparator.compare(s1, s2); 
		} else if (_criteria == Criteria.DISTANCE) {
			Address a1 = o1.getAddress(); 
			Address a2 = o2.getAddress(); 
			GeoLocation l1 = a1.getLocation(); 
			GeoLocation l2 = a2.getLocation(); 
			GeoLocation clientLocation = EducContext.getInstance().getClientLocation();
			double d1 = l1.computeDistanceFrom(clientLocation); 
			double d2 = l2.computeDistanceFrom(clientLocation); 	
			comparison = (int)(d1 - d2);
		}
		
		
		comparison *= _ascendent ? 1 : -1;
		
		return comparison;
	}

	public void setOrder(boolean ascendent) {
		_ascendent = ascendent;
	}

	public void setCriteria(OrganizationComparator.Criteria criteria) {
		_criteria = criteria;
		//_selectedCriteriaIdx = selectedCriteriaIdx;
	}
}