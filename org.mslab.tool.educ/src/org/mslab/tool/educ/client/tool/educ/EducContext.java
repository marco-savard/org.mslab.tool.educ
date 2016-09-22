package org.mslab.tool.educ.client.tool.educ;

import java.util.ArrayList;
import java.util.List;

import org.mslab.tool.educ.shared.types.GeoLocation;
import org.mslab.tool.educ.shared.types.educ.Organization;
import org.mslab.tool.educ.shared.types.educ.Person;

public class EducContext {
	private ApplicationMode _applicationMode = ApplicationMode.DEVELOPMENT; 
	private List<Organization> _organizations = null; 
	private List<Person> _persons = new ArrayList<Person>();
	//private EducLoader _loader;
	
	//singleton
	private static EducContext _instance = null;
	public static EducContext getInstance() { 
		if (_instance == null) {
			_instance = new EducContext(); 
		}
		
		return _instance; 
	}
	
	private EducContext() {
		//_loader = EducLoader.getInstance();
	}
	
	//
	// application mode
	//
	public enum ApplicationMode {DEVELOPMENT, DEPLOYMENT, MAINTENANCE}; 
	public ApplicationMode getApplicationMode() { return _applicationMode; }
	
	//
	// client location
	//
	public GeoLocation getClientLocation() {
		return _clientLocation;
	}
	private GeoLocation _clientLocation;
	public void setCurrentLocation(GeoLocation location) { _clientLocation = location; } 
	
	//
	// organizations
	//
	public void init(List<Organization> organizations) {
		_organizations = organizations;
		_persons.clear();
		
		for (Organization org : _organizations) {
			Person director = org.getDirector();
			if (director != null) {
				_persons.add(director);
			}
		}
	}

	public List<Person> getDirectorList() {
		return _persons;
	}

	public List<Organization> getOrganizations() {
		return _organizations;
	}

	public EducRouting getEducRouting() {
		EducRouting routing = EducRouting.getInstance();
		return routing;
	}



	
	
	
}
