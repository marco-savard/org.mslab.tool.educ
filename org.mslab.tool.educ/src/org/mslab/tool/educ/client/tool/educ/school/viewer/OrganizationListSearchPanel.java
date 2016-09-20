package org.mslab.commons.client.tool.educ.school.viewer;

import java.util.List;

import org.mslab.commons.client.core.ui.DecoratedTextBox2;
import org.mslab.commons.client.core.ui.panels.GridPanel;
import org.mslab.commons.client.core.ui.panels.IconTabLayoutPanelOld;
import org.mslab.commons.client.core.ui.panels.IconTabPanelBuilder;
import org.mslab.commons.client.tool.educ.EducContent;
import org.mslab.commons.client.tool.educ.school.explorer.SchoolFilterExplorer;
import org.mslab.commons.client.tool.educ.school.explorer.TreeExplorer;
import org.mslab.commons.client.tool.educ.school.viewer.OrganizationListViewer.OrganizationListViewerContent;
import org.mslab.commons.shared.types.Color;
import org.mslab.commons.shared.types.educ.Organization;

import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.SimplePanel;

public class OrganizationListSearchPanel extends GridPanel implements SelectionHandler<Integer> {
	private OrganizationSearchBoxPanel _seachBoxPanel;
	private OrganizationTagCloudViewer _tagCloud;
	
	OrganizationListSearchPanel(Color selectionColor, OrganizationListViewerContent parent) {
		IconTabPanelBuilder builder = new IconTabPanelBuilder(); 
		
		_seachBoxPanel = new OrganizationSearchBoxPanel(parent);
		String text = "<i class=\"fa fa-search fa-2x\"></i>"; 
		builder.add(_seachBoxPanel, text, "Recherche"); 
	}
	
	@Override
	public void onSelection(SelectionEvent<Integer> event) {
		int index = event.getSelectedItem();
		//selectTab(index);
	}
	
	public void showOrganizations(String listName, List<Organization> organizations) {
		_seachBoxPanel.showOrganizations(organizations);	
		_tagCloud.update(listName, organizations);
	}
	
	private static class OrganizationSearchBoxPanel extends GridPanel {	
		private OrganizationSearchBox _searchBox; 
		public OrganizationSearchBoxPanel(OrganizationListViewerContent parent) {
			_grid.setWidth("100%");
			_searchBox = new OrganizationSearchBox(parent); 
			_grid.setWidget(0, 0, _searchBox);
			_grid.getFlexCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		}
		
		public void showOrganizations(List<Organization> organizations) {
			_searchBox.getTextBox().setText("");
		}
	}
	
	private static class OrganizationSearchBox extends DecoratedTextBox2 {	
		private OrganizationListViewerContent _parent; 
		
		private static final int WIDTH = 300; //px
		private static final int DELAY = 300; //ms
		
		public OrganizationSearchBox(OrganizationListViewerContent parent) {
			super("<span class=\"fa fa-search\"></span>", "Search", Color.GREY_LIGHT.toString(), WIDTH, DELAY);
			//setWidth("100%");
			_parent = parent;
			//setWidth(WIDTH + "px"); 
		}
		
		protected void onTextChanged(String textChanged) {
			textChanged = textChanged.toLowerCase();
			_parent._headerContent.filterText(textChanged);
		}
	} //end SearchTextBox


	
	
}
