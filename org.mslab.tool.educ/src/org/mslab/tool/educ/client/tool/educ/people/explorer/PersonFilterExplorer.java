package org.mslab.tool.educ.client.tool.educ.people.explorer;

import java.util.List;

import org.mslab.tool.educ.client.tool.educ.EducContext;
import org.mslab.tool.educ.client.tool.educ.school.explorer.AbstractFilterExplorer;
import org.mslab.tool.educ.client.tool.educ.school.explorer.EntityViewable;
import org.mslab.tool.educ.shared.types.educ.Person;

public class PersonFilterExplorer extends AbstractFilterExplorer<Person> {
	
	public static PersonFilterExplorer create(EntityViewable personViewable) {
		EducContext context = EducContext.getInstance();
		List<Person> persons = context.getDirectorList(); 
		
		PersonFilterExplorer explorer = new PersonFilterExplorer(personViewable, persons);
		explorer.init("Total", persons);
		return explorer; 
	}

	private PersonFilterExplorer(EntityViewable personViewable, List<Person> persons) {
		super(personViewable, persons, "directeur", "directeurs");
		
		addFilter(new PersonFilterCategory.GenderCategorizer());
		addFilter(new PersonFilterCategory.TitleCategorizer());
		addFilter(new PersonFilterCategory.FamilyNameCategorizer());
		addFilter(new PersonFilterCategory.GivenNameCategorizer());
		addFilter(new PersonFilterCategory.LanguageCategorizer());
		addFilter(new PersonFilterCategory.AreaCodeCategorizer());
		addFilter(new PersonFilterCategory.EmailDomainCategorizer());
	}



}
