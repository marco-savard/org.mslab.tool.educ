package org.mslab.commons.client.tool.educ.school.explorer;

import java.util.List;

import org.mslab.commons.client.tool.educ.OrganizationCategories.AbstractCategorizer;

public interface EntityViewable<T> {

	void update(String listName, List<T> persons);

}
