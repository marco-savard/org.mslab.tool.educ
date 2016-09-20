package org.mslab.commons.client.tool.educ;

import java.util.List;

import org.mslab.commons.shared.types.educ.Organization;

import com.google.gwt.user.client.ui.ScrollPanel;

public abstract class AbstractExplorer extends ScrollPanel {
	protected EducContent _parent;
	
	protected AbstractExplorer(EducContent parent) {
		_parent = parent;
	}
	
	public abstract void explore(List<Organization> organizations);
}
