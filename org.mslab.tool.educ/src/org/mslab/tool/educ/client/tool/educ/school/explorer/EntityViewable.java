package org.mslab.tool.educ.client.tool.educ.school.explorer;

import java.util.List;

public interface EntityViewable<T> {

	void update(String listName, List<T> persons);

}
