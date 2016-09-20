package org.mslab.commons.client.tool.educ.people;

import java.util.List;

import org.mslab.commons.client.core.ui.panels.GridPanel;
import org.mslab.commons.client.tool.educ.EducHeader;
import org.mslab.commons.client.tool.educ.EducTheme;
import org.mslab.commons.client.tool.educ.people.explorer.PersonFilterExplorer;
import org.mslab.commons.client.tool.educ.people.viewer.EducPeopleListPanel;
import org.mslab.commons.client.tool.educ.people.viewer.EducPeopleDeckPanel;
import org.mslab.commons.client.tool.educ.school.explorer.AbstractFilterExplorer;
import org.mslab.commons.client.tool.educ.school.explorer.EntityViewable;
import org.mslab.commons.shared.types.Color;
import org.mslab.commons.shared.types.educ.Organization;
import org.mslab.commons.shared.types.educ.Person;

import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;

public class EducPeoplePage extends SimplePanel implements ResizeHandler {
	private SplitLayoutPanel _splitLayoutPanel;
	private ExplorerPane _explorerPane; 
	private ViewerPane _viewerPane;
	private Timer _resizeTimer;
	private boolean _landscape; 
	
	public EducPeoplePage() {
		_splitLayoutPanel = new SplitLayoutPanel(5);
		_splitLayoutPanel.setSize("100%", "100%");
		add(_splitLayoutPanel);
		
		EducTheme theme = (EducTheme)EducTheme.getTheme();
		Color primary = theme.getPrimaryFgColor();
		_viewerPane = new ViewerPane();
		EntityViewable<Person> personViewable = _viewerPane.getPersonViewable(); 
		_explorerPane = new ExplorerPane(personViewable);
		
		//refresh() on window resize
		_resizeTimer = new ResizeTimer(); 
		refreshOrientation();
		refresh();
		Window.addResizeHandler(this); 
		
		/*
		_grid.setWidth("100%");
		int col = 0;
		
		_viewerPane = new ViewerPane(); 
		EntityViewable personViewable = _viewerPane.getPersonViewable(); 
		
		_explorerPane = new ExplorerPane(personViewable);
		_grid.setWidget(0, col, _explorerPane);
		_grid.getFlexCellFormatter().setVerticalAlignment(0, col, HasVerticalAlignment.ALIGN_TOP);
		col++;
		
		_grid.setWidget(0, col, _viewerPane);
		_grid.getFlexCellFormatter().setVerticalAlignment(0, col, HasVerticalAlignment.ALIGN_TOP);
		col++;
		*/
	}
	
	@Override
	public void onResize(ResizeEvent event) {
		_resizeTimer.schedule(200);
	}
	
	private class ResizeTimer extends Timer {
		@Override
		public void run() {
			 refresh();
		}
	}
	
	public void update(Person person) {
		_viewerPane.update(person);
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
			refreshOrientation();
		}
	}
	
	public void refreshOrientation() {
		int width = Window.getClientWidth(); 
		int height = Window.getClientHeight(); 
		_landscape = width >= height;
		_splitLayoutPanel.clear();
		
		if (_landscape) {
			int size = (width / 3) - 12;
			_splitLayoutPanel.addWest(_explorerPane, size);
			_splitLayoutPanel.add(_viewerPane);
		} else {
			int size = (height / 3) - 12;
			_splitLayoutPanel.addNorth(_explorerPane, size);
			_splitLayoutPanel.add(_viewerPane);
		}
		
		_explorerPane.update();
	}
	
	//
	// inner classes
	//
	private static class ExplorerPane extends ScrollPanel {
		private AbstractFilterExplorer _explorer; 
		
		ExplorerPane(EntityViewable personViewable) {
			_explorer = PersonFilterExplorer.create(personViewable); 
			add(_explorer);
			
			getElement().getStyle().setBorderWidth(2, Unit.PX);
			getElement().getStyle().setBorderColor(Color.GREY_SILVER.toString());
			getElement().getStyle().setBorderStyle(BorderStyle.SOLID);
		}

		public void update() {
			_explorer.update();
		}
	} //end ExplorerPanelScroll
	
	private static class ViewerPane extends ScrollPanel {
		private EducPeopleDeckPanel _content; 
		
		ViewerPane() {
			_content = new EducPeopleDeckPanel();
			add(_content);
			
			getElement().getStyle().setBorderWidth(2, Unit.PX);
			getElement().getStyle().setBorderColor(Color.GREY_SILVER.toString());
			getElement().getStyle().setBorderStyle(BorderStyle.SOLID);
		}

		public void update(Person person) {
			_content.update(person);
		}

		public EntityViewable<Person> getPersonViewable() {
			return _content;
		}
	}


}
