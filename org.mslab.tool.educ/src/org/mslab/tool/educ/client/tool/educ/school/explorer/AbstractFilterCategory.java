package org.mslab.commons.client.tool.educ.school.explorer;

import java.util.List;

import org.mslab.commons.shared.text.MessageFormat;
import org.mslab.commons.shared.types.PhoneNumber;
import org.mslab.commons.shared.types.PhoneNumber.Category;
import org.mslab.commons.shared.types.educ.Organization;
import org.mslab.commons.shared.types.educ.Person;
import org.mslab.commons.shared.types.educ.Person.Gender;

public class AbstractFilterCategory {

	//
	// abstract
	//
	public static abstract class AbstractCategorizer<T> {
		protected String _name;
		
		protected AbstractCategorizer(String name) {
			_name = name;
		}
		
		public abstract String categorize(T person, List<T> directory);

		public String getName() {
			return _name;
		}
	}
	
	
	

	
}
