package org.mslab.commons.client.tool.educ.school.explorer;

import java.util.List;

import org.mslab.commons.client.core.ui.theme.AbstractTheme;
import org.mslab.commons.client.tool.educ.EducContext;
import org.mslab.commons.client.tool.educ.school.explorer.AbstractFilterExplorer;
import org.mslab.commons.shared.types.Color;
import org.mslab.commons.shared.types.educ.Organization;
import org.mslab.commons.shared.types.educ.Person;

public class SchoolFilterExplorer extends AbstractFilterExplorer<Organization> {
	
	public static SchoolFilterExplorer create(EntityViewable<Organization> organizationViewable) {
		EducContext context = EducContext.getInstance();
		List<Organization> organizations = context.getOrganizations(); 
		
		SchoolFilterExplorer explorer = new SchoolFilterExplorer(organizationViewable, organizations);
		explorer.init("Total", organizations);
		return explorer; 
	}

	private SchoolFilterExplorer(EntityViewable<Organization> organizationViewable, List<Organization> organizations) {
		super(organizationViewable, organizations, "organisme", "organismes");
		
		addFilter(new SchoolFilterCategory.RegionCategorizer());
		addFilter(new SchoolFilterCategory.CityCategorizer());
		addFilter(new SchoolFilterCategory.CirconscriptionCategorizer());
		addFilter(new SchoolFilterCategory.DistanceCategorizer());
		addFilter(new SchoolFilterCategory.OrganizationTypeCategorizer());
		addFilter(new SchoolFilterCategory.OrdreAppartenanceCategorizer());
		addFilter(new SchoolFilterCategory.ReseauCategorizer());
		addFilter(new SchoolFilterCategory.LanguageCategorizer());
		addFilter(new SchoolFilterCategory.EnvironmentCategorizer());
		addFilter(new SchoolFilterCategory.SchoolBoardCategorizer());
	}



}
