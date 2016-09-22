package org.mslab.tool.educ.client.tool.educ.school.viewer;

public abstract class OrganizationAbstractFilter {
	protected String _name; 
	protected OrganizationAbstractFilter(String name) {_name = name;}

	public static OrganizationCategoryFilter createCategoryFilter(String name) {
		OrganizationCategoryFilter filter = new OrganizationCategoryFilter(name);
		return filter;
	}
	
	public static OrganizationSearchFilter createSearchFilter(String text) {
		OrganizationSearchFilter filter = new OrganizationSearchFilter(text);
		return filter;
	}
	
	public String getName() {
		return _name;
	}

	
	public static class OrganizationSearchFilter extends OrganizationAbstractFilter {
		OrganizationSearchFilter(String name) {
			super(name);
		}
	}
	
	public static class OrganizationCategoryFilter extends OrganizationAbstractFilter {
		OrganizationCategoryFilter(String name) {
			super(name);
		}
	}




}
