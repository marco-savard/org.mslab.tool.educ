package org.mslab.tool.educ.client.tool.educ;

import org.mslab.tool.educ.shared.types.educ.Organization;
import org.mslab.tool.educ.shared.types.educ.Person;

public class EducRouting {

	public static EducRouting getInstance() {
		if (_instance == null) {
			_instance = new EducRouting(); 
		}
		return _instance;
	}
	private static EducRouting _instance; 
	
	private EducRouting() {}

	public void routeOrganization(Organization organization) {
		EducShell shell = EducShell.getInstance();
		shell.displayOrganization(organization);
	}

	public void routePerson(Person person) {
		EducShell shell = EducShell.getInstance();
		shell.displayPerson(person);
	}

}
