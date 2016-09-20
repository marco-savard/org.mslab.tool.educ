package org.mslab.commons.client.tool.educ;

import java.util.Date;
import java.util.List;

import org.mslab.commons.client.core.ui.StyleUtil;
import org.mslab.commons.client.core.ui.panels.GridPanel;
import org.mslab.commons.client.core.ui.panels.IconTabLayoutPanelOld;
import org.mslab.commons.client.core.ui.panels.IconTabPanelBuilder;
import org.mslab.commons.client.core.ui.theme.ThematicButton;
import org.mslab.commons.client.tool.educ.school.explorer.EntityViewable;
import org.mslab.commons.client.tool.educ.school.explorer.SchoolFilterExplorer;
import org.mslab.commons.client.tool.educ.school.explorer.TreeExplorer;
import org.mslab.commons.client.tool.educ.school.viewer.AbstractViewer;
import org.mslab.commons.client.tool.educ.school.viewer.OrganizationGraphicViewer;
import org.mslab.commons.client.tool.educ.school.viewer.OrganizationListViewer;
import org.mslab.commons.client.tool.educ.school.viewer.OrganizationMapViewer;
import org.mslab.commons.client.tool.educ.school.viewer.OrganizationListViewer.OrganizationViewerHeaderContent;
import org.mslab.commons.client.tool.services.ServiceStore;
import org.mslab.commons.shared.types.Color;
import org.mslab.commons.shared.types.educ.Organization;

import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

public class EducContent extends SimplePanel implements EntityViewable<Organization>, ClickHandler, ResizeHandler {
	private SplitLayoutPanel _splitLayoutPanel;
	private Timer _resizeTimer;
	private ThematicButton _showTreeBtn; 
	private ExplorerPanelScroll _explorerTabPanel;
	private ViewerPanelScroll _viewerPanel;
	private boolean _landscape; 

	EducContent() {
		_splitLayoutPanel = new EducSplitLayoutPanel();
		_splitLayoutPanel.setSize("100%", "100%");
		add(_splitLayoutPanel);
		
		EducTheme theme = (EducTheme)EducTheme.getTheme();
		Color primary = theme.getPrimaryFgColor();
		_viewerPanel = new ViewerPanelScroll(this, primary);
		_explorerTabPanel = new ExplorerPanelScroll(this, primary, _viewerPanel);
		
		//refresh() on window resize
		_resizeTimer = new ResizeTimer(); 
		update();
		refresh();
		Window.addResizeHandler(this); 
	}
	
	private class ResizeTimer extends Timer {
		@Override
		public void run() {
			 refresh();
		}
	}
	
	@Override
	public void onResize(ResizeEvent event) {
		_resizeTimer.schedule(200);
	}

	private void refresh() {
		int width = Window.getClientWidth(); 
		int height = Window.getClientHeight(); 
		int paneHeight = height; 
		paneHeight -= EducHeader.HEIGHT; //remove header
		paneHeight -= 48; //tab menu height
		setHeight(paneHeight + "px");
		
		boolean landscape = width >= height;
		if (_landscape != landscape) {
			update();
		}
	} //end refresh();

	private void update() {
		int width = Window.getClientWidth(); 
		int height = Window.getClientHeight(); 
		_landscape = width >= height;
		_splitLayoutPanel.clear();
		
		if (_landscape) {
			int size = (width / 4) - 12;
			_splitLayoutPanel.addWest(_explorerTabPanel, size);
			_splitLayoutPanel.add(_viewerPanel);
		} else {
			int size = (height / 3) - 12;
			_splitLayoutPanel.addNorth(_explorerTabPanel, size);
			_splitLayoutPanel.add(_viewerPanel);
		}
	}

	@Override
	public void update(String listName, List<Organization> organizations) {
		_viewerPanel.update(listName, organizations);
	}

	@Override
	public void onClick(ClickEvent event) {
		Object src = event.getSource(); 

		if (src.equals(_showTreeBtn)) {
			callShowTree(); 
		}
	}

	//called on load
	public void setOrganizations(List<Organization> organizations) { 
		_explorerTabPanel.setOrganizations(organizations);
	}

	public void callShowTree() {
		AsyncCallback<List<Organization>> callback = new AsyncCallback<List<Organization>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub	
			}

			@Override
			public void onSuccess(List<Organization> organizations) {
				//explore(organizations); 
			}
		};

		Date now = new Date();
		System.out.println(now.toString());
		ServiceStore.getService().getListOrganizations(callback);
	}

	//
	// inner classes
	//
	private class EducSplitLayoutPanel extends SplitLayoutPanel {
		EducSplitLayoutPanel() {
			super(5);
		}
		
		@Override
	    public void onResize() {
			_viewerPanel.onResize();
	    }        
	}
	
	private static class ExplorerPanelScroll extends ScrollPanel {
		private ExplorerPanel _explorerPanel; 

		public ExplorerPanelScroll(EducContent educContent, Color primary, EntityViewable<Organization> organizationViewable) {
			_explorerPanel = new ExplorerPanel(educContent, primary, organizationViewable);
			add(_explorerPanel);

			getElement().getStyle().setBorderWidth(2, Unit.PX);
			getElement().getStyle().setBorderColor(Color.GREY_SILVER.toString());
			getElement().getStyle().setBorderStyle(BorderStyle.SOLID);
		}

		public void setOrganizations(List<Organization> organizations) {
			_explorerPanel.setOrganizations(organizations);
		}
	}
	
	private static class ExplorerPanel extends GridPanel {
		private TreeExplorer _treeExplorer; 
		private SchoolFilterExplorer _filterExplorer;
		
		ExplorerPanel(EducContent educContent, Color primary, EntityViewable<Organization> organizationViewable) {
			IconTabPanelBuilder builder = new IconTabPanelBuilder(); 
			
			_treeExplorer = new TreeExplorer(educContent); 
			String text = "<i class=\"fa fa-tree fa-2x\"></i>"; 
			builder.add(_treeExplorer, text, "Arboresrence"); 
			
			_filterExplorer = SchoolFilterExplorer.create(organizationViewable); 
			text = "<i class=\"fa fa-filter fa-2x\"></i>";
			builder.add(_filterExplorer, text, "Filtres"); 
			
			SimplePanel menuPanel = builder.buildMenuPanel(); 
			SimplePanel contentPanel = builder.getContentPanel(); 
			_grid.setWidth("100%");
			int row = 0; 
			
			_grid.setWidget(row, 0, menuPanel);
			row++;
			
			_grid.setWidget(row, 0, contentPanel);
			row++;
		}
		
		public void setOrganizations(List<Organization> organizations) { 
			_treeExplorer.explore(organizations); 
			
			if (_filterExplorer != null) {
				_filterExplorer.init("Total", organizations);
			}
		}
	}

	/*
	private static class ExplorerPanelOld extends IconTabLayoutPanelOld implements ResizeHandler, SelectionHandler<Integer> {
		private TreeExplorer _te; 
		private SchoolFilterExplorer _fe; 

		ExplorerPanelOld(EducContent parent, Color selectionColor, EntityViewable<Organization> organizationViewable) {
			super(25, Unit.PX, selectionColor); 
			
			_te = new TreeExplorer(parent); 
			String text = "<i class=\"fa fa-tree fa-2x\"></i>"; //"Tree Explorer";
			add(_te, text, true);

			_fe = SchoolFilterExplorer.create(organizationViewable); 
			//_fe = new SchoolFilterExplorer(parent); 
			//_fe.setWidth("100%");
			text = "<i class=\"fa fa-filter fa-2x\"></i>"; //"Filter Explorer";
			add(_fe, text, true);

			selectTab(0);

			addSelectionHandler(this);
			//Window.addResizeHandler(this);
			//refresh();
		}

		@Override
		public void onResize(ResizeEvent event) {
			//refresh();
		}

		@Override
		public void onSelection(SelectionEvent<Integer> event) {
		}

		private void refresh() {
			//int height = Window.getClientHeight() - EducShell.HEADER_HEIGHT_PX - 24; 
			//int width = (Window.getClientWidth() / 3) - 24; 

			//setHeight(height + "px");
			//setWidth(width + "px");
		}

		public void setOrganizations(List<Organization> organizations) { 
			_te.explore(organizations); 
			
			if (_fe != null) {
				_fe.init("Total", organizations);
			}
		}
	}*/

	private static class ViewerPanelScroll extends ScrollPanel implements EntityViewable<Organization> {
		//private ViewerPanel _content; 
		private OrganizationListViewer _schoolListViewer;

		public ViewerPanelScroll(EducContent educContent, Color primary) {
			_schoolListViewer = new OrganizationListViewer();
			//_content = new ViewerPanel(educContent, primary);
			_schoolListViewer.setVisible(false);
			add(_schoolListViewer);

			getElement().getStyle().setBorderWidth(2, Unit.PX);
			getElement().getStyle().setBorderColor(Color.GREY_SILVER.toString());
			getElement().getStyle().setBorderStyle(BorderStyle.SOLID);
		}
		
		@Override
	    public void onResize() {
			_schoolListViewer.onResize();
	    }   

		@Override
		public void update(String listName, List<Organization> organizations) {
			boolean visible = (organizations != null) && organizations.size() > 0;
			_schoolListViewer.update(listName, organizations);
			_schoolListViewer.setVisible(visible);
		}
	}
	
	/*
	private static class ViewerPanel extends GridPanel {
		private ViewerTabPanel _tabPanel;
		
		ViewerPanel(EducContent parent, Color selectionColor) {
			_grid.setWidth("100%");
			
			_tabPanel = new ViewerTabPanel(parent, selectionColor);
			_grid.setWidget(1, 0, _tabPanel);
		}

		public void update(String listName, List<Organization> organizations) {
			_tabPanel.update(listName, organizations);
		}
	}*/

	/*
	private static class ViewerTabPanel extends IconTabLayoutPanelOld implements EntityViewable<Organization>, ResizeHandler, SelectionHandler<Integer> { 
		private AbstractViewer<Organization> _schoolListViewer, _schoolMapViewer, _schoolGraphicViewer;
		private String _listName;
		private List<Organization> _organizations; 

		ViewerTabPanel(EducContent parent, Color selectionColor) {
			super(25, Unit.PX, selectionColor); 
			IconTabLayoutPanelContent content = getContentPanel();
			SimplePanel header = content.getHeaderPanel(); 
			Color leftGradient = Color.GREY_LIGHT;
			StyleUtil.setLinearGradient(header, 90, leftGradient, Color.WHITE);

			_schoolListViewer = new OrganizationListViewer();
			_schoolListViewer.setWidth("100%");
			String text = "<i class=\"fa fa-list fa-2x\"></i>"; //List
			add(_schoolListViewer, text, true);

			_schoolGraphicViewer = new OrganizationGraphicViewer();
			_schoolGraphicViewer.setWidth("100%");
			text = "<i class=\"fa fa-pie-chart fa-2x\"></i>"; //Graphic
			add(_schoolGraphicViewer, text, true);

			_schoolMapViewer = new OrganizationMapViewer();
			_schoolMapViewer.setWidth("100%");
			text = "<i class=\"fa fa-globe fa-2x\"></i>"; //Map
			add(_schoolMapViewer, text, true);

			selectTab(0);
			addSelectionHandler(this);
			Window.addResizeHandler(this);
			refresh();
		}

		@Override
		public void onResize(ResizeEvent event) {
			refresh();
		}

		private void refresh() {
			/ *
			int height = Window.getClientHeight() - EducShell.HEADER_HEIGHT_PX - 24; 
			int width = ((2 * Window.getClientWidth()) / 3) - 48; 

			setHeight(height + "px");
			setWidth(width + "px");
			* /
		}

		@Override
		public void update(String listName, List<Organization> organizations) {
			_listName = listName;
			_organizations = organizations;
			int idx = getSelectedIndex(); 

			if (idx == 0) {
				_schoolListViewer.update(listName, organizations);
			} else if (idx == 1) {
				_schoolGraphicViewer.update(listName, organizations);
			} else if (idx == 2) {
				_schoolMapViewer.update(listName, organizations);
			}
		}

		@Override
		public void onSelection(SelectionEvent<Integer> event) {
			int index = event.getSelectedItem();
			selectTab(index);
		}

		@Override
		public void selectTab(int idx) {
			super.selectTab(idx);
			update(_listName, _organizations);
		}
	}
  */




}
