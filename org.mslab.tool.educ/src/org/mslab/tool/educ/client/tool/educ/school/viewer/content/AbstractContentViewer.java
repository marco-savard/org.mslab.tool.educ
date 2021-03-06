package org.mslab.tool.educ.client.tool.educ.school.viewer.content;

import java.util.ArrayList;
import java.util.List;

import org.mslab.tool.educ.client.core.ui.panels.GridPanel;
import org.mslab.tool.educ.client.tool.educ.school.viewer.content.OrganizationComparator.Criteria;
import org.mslab.tool.educ.shared.types.educ.Organization;

public abstract class AbstractContentViewer extends GridPanel {

	public List<Organization> filterText(String filterText) {
		List<Organization> orgs = new ArrayList<Organization>();
		return orgs;
	}

	public abstract void sort(Criteria criteria, boolean ascendent);

	public abstract void showOrganizations(String listName, List<Organization> organizations);

	public void onResize() {
	}
}
