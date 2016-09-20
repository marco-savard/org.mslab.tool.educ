package org.mslab.commons.client.tool.educ;

import java.util.ArrayList;
import java.util.List;

import org.mslab.commons.client.core.ui.GradientBackground;
import org.mslab.commons.client.core.ui.panels.AbstractApplicationPanel;
import org.mslab.commons.client.core.ui.panels.GridPanel;
import org.mslab.commons.client.core.ui.theme.AbstractTheme;
import org.mslab.commons.client.core.ui.theme.ThemeChangeEvent;
import org.mslab.commons.client.core.ui.theme.ThemeChangeHandler;
import org.mslab.commons.client.tool.MenuTabPanel;
import org.mslab.commons.client.tool.educ.people.EducPeoplePage;
import org.mslab.commons.client.tool.educ.settings.EducSettingsPage;
import org.mslab.commons.client.tool.snippets.SnippetContext;
import org.mslab.commons.shared.types.Color;
import org.mslab.commons.shared.types.educ.Organization;
import org.mslab.commons.shared.types.educ.Person;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.google.gwt.user.client.Window.ClosingHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.Widget;

public class EducShell extends AbstractApplicationPanel implements CloseHandler<Window>, ClosingHandler {
	public static final int HEADER_HEIGHT_PX = 120; 
	private EducHeader _educHeader;
	private EducStartPanel _startPanel;
	private MenuTabPanel _tabPanel;
	private EducContent _educSchools;
	private EducPeoplePage _educPeople;
	private EducSettingsPage _educSettings;
    private int _contentRow; 

	private EducShell() {
		OnLoadComplete onLoadComplete = new OnLoadComplete();
		EducLoader.load(onLoadComplete); 
	}
	
	private class OnLoadComplete implements Runnable {
		@Override
		public void run() {
			loadShell();
		}
	}
	
	private void loadShell() {
		EducContext.getInstance().getClientLocation(); 
		AbstractTheme.setTheme(new EducTheme());
		_educSchools = new EducContent();
		_educPeople = new EducPeoplePage();
		_educSettings = new EducSettingsPage();
		
		//header
		int row = 0;
		_educHeader = new EducHeader(); 
		_grid.setWidget(row, 0, _educHeader); 
		_grid.getFlexCellFormatter().setWidth(row, 0, "100%");
		row++;
		
		//main
		_startPanel = new EducStartPanel(this); 
		_grid.setWidget(row, 0, _startPanel); 
		_contentRow = row;
		
		_tabPanel = new EducMenuTabPanel();
		Style style = _tabPanel.getElement().getStyle();
		style.setProperty("fontFamily", "Roboto, sans-serif");
		
		_tabPanel.add(_educSchools, new EducTab("fa fa-graduation-cap fa-2x", "Écoles"));
		_tabPanel.add(_educPeople, new EducTab("fa fa-user fa-2x", "Directeurs"));
		_tabPanel.add(_educSettings, new EducTab("fa fa-cog fa-2x", "Paramètres"));
		_tabPanel.selectTab(0);
		
		//_grid.setWidget(row, 0, _tabPanel); 
		_grid.getFlexCellFormatter().setHeight(row, 0, "100%");
		_grid.getFlexCellFormatter().setWidth(row, 0, "100%");
		row++;		
		
		
		//footer
		
		//on closing
		Window.addWindowClosingHandler(this); 
		Window.addCloseHandler(this);
	}
	
	public static EducShell getInstance() {
		if (_instance == null) {
			_instance = new EducShell();
		}
		
		return _instance;
	}
	private static EducShell _instance;

	@Override
	protected void pack() {
	}
	
	public void displayOrganization(Organization organization) {
		List<Organization> organizations = new ArrayList<Organization>();
		organizations.add(organization); 
		displayOrganizations(organizations);
	}
	
	public void displayOrganizations(List<Organization> organizations) {		
		_grid.setWidget(_contentRow, 0, _tabPanel); 
		_tabPanel.selectTab(0);
		_educSchools.update("", organizations);
	}
	
	public void displayPerson(Person person) {
		_grid.setWidget(_contentRow, 0, _tabPanel); 
		_tabPanel.selectTab(1);
		_educPeople.update(person); 
	}
	
	private static class EducTab extends GridPanel {

		public EducTab(String faIcon, String text) {
			_grid.setCellPadding(0);
			_grid.setCellSpacing(0);
			AbstractTheme theme = EducTheme.getTheme(); 
			
			String html = "<i class=\"" + faIcon + "\"></i>";
			HTML icon = new HTML(html); 
			_grid.setWidget(0, 0, icon);
			
			HTML label = new HTML(text);
			Style style = label.getElement().getStyle();
			style.setProperty("fontFamily", theme.getFontFamily());
			style.setFontWeight(FontWeight.LIGHTER);
			style.setFontSize(150, Unit.PCT);
			style.setMarginLeft(0.7, Unit.EM);
			_grid.setWidget(0, 1, label);
		}
		
	}
	
	private class EducMenuTabPanel extends MenuTabPanel implements ThemeChangeHandler {
		EducMenuTabPanel() {
			AbstractTheme theme = AbstractTheme.getTheme();
			theme.addThemeChangeHandler(this);
			refresh();
		}
		
		@Override
		public void onSelection(SelectionEvent<Integer> event) {
			int idx = event.getSelectedItem();
			
			if (idx == 1) {
				_educPeople.refreshOrientation();
			}
			
			super.onSelection(event);
		}

		@Override
		public void onThemeChange(ThemeChangeEvent event) {
			refresh(); 
		}
		
		public void refresh() {
			super.refresh();
			
			TabBar tabBar = getTabBar();
			AbstractTheme theme = AbstractTheme.getTheme();
			Color bg = theme.getPrimaryBgColor();
			tabBar.getElement().getStyle().setBackgroundColor(bg.toString());
			//GradientBackground.setGradient(tabBar, primaryColor);
		}
	} //end EducMenuTabPanel
	

	@Override
	public void onWindowClosing(ClosingEvent event) {
		EducContext.ApplicationMode mode = EducContext.getInstance().getApplicationMode();
		if (mode == EducContext.ApplicationMode.DEPLOYMENT) {
			String msg = "Voulez-vous quitter l'application ?"; 
			event.setMessage(msg);
		}
	}

	@Override
	public void onClose(CloseEvent<Window> event) {
		//save
	}

	public void setOrganizations(List<Organization> loadedOrganizations) {
		_educSchools.setOrganizations(loadedOrganizations);
		_educHeader.update();
		_educHeader.refresh();
	}










}
