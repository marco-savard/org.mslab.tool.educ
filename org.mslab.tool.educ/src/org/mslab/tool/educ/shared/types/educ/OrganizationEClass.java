package org.mslab.commons.shared.types.educ;

import java.util.List;

import org.mslab.commons.shared.emf.EAttribute;
import org.mslab.commons.shared.emf.EClass;

public class OrganizationEClass extends EClass {
	
	public OrganizationEClass() {
		List<EAttribute> attrs = getListEAttributes(); 
		attrs.add(new EAttribute("code")); 
		attrs.add(new EAttribute("nom")); 
		attrs.add(new EAttribute("numero civique")); 
		attrs.add(new EAttribute("rue")); 
		attrs.add(new EAttribute("ville")); 
	}
	
}
