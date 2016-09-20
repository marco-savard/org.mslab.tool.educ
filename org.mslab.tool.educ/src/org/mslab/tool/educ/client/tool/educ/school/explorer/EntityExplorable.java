package org.mslab.commons.client.tool.educ.school.explorer;

import java.util.List;
import java.util.Stack;

public interface EntityExplorable<T> {

	public void init(String title, List<T> entities);
	
	public void update(); 
	
	public Stack<FilteredCollection<T>> getCollectionStack();

	public void removeLastFilter();
	
	

}
