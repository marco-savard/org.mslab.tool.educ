package org.mslab.commons.client.tool.educ.school.viewer.content;

import java.util.List;

import org.mslab.commons.client.tool.educ.school.viewer.content.OrganizationComparator.Criteria;
import org.mslab.commons.shared.types.educ.Organization;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;

public class OrganizationMapViewer3 extends AbstractContentViewer {
	private MapPanel _mapPanel;
	
	public OrganizationMapViewer3() {
		int row = 0;
		_grid.setWidth("100%");
		
		HTML html = new HTML("Carte"); 
		html.getElement().getStyle().setFontSize(150, Unit.PCT);
		_grid.getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_CENTER);
		_grid.setWidget(row, 0, html);
		row++;
		
		_mapPanel = new MapPanel();
		_grid.setWidget(row, 0, _mapPanel);
		_grid.getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_CENTER);

	}
	
	@Override
	public void showOrganizations(String listName, List<Organization> organizations) {
		_mapPanel.display(organizations);
	}

	@Override
	public void sort(Criteria criteria, boolean ascendent) {
		// TODO Auto-generated method stub
		
	}

}
