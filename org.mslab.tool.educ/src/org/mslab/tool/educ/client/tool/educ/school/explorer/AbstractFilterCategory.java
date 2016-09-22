package org.mslab.tool.educ.client.tool.educ.school.explorer;

import java.util.List;

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
