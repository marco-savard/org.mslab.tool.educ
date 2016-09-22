package org.mslab.tool.educ.client.tool;

import org.mslab.tool.educ.shared.types.Color;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.TabBar.Tab;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;

public class MenuTabPanel extends TabPanel implements SelectionHandler<Integer> {
	
	public MenuTabPanel() {
		setWidth("100%");
		setHeight("100%");
		
		DeckPanel deckPanel = getDeckPanel();
		deckPanel.getElement().getStyle().setBorderStyle(BorderStyle.NONE);
		addSelectionHandler(this); 
	}
	
	
	public void add(Widget child, String tabText) {
		super.add(child, tabText);
		
		TabBar tabBar = getTabBar();
		int count =  tabBar.getTabCount();  
		
		Tab tab = tabBar.getTab(count-1);
		tab.setWordWrap(false);
		refresh();
	}

	@Override
	public void onSelection(SelectionEvent<Integer> event) {
		refresh(); 
	}
	
	protected void refresh() {
		TabBar tabBar = getTabBar();
		int count =  tabBar.getTabCount();  
		int selectedTab = tabBar.getSelectedTab();
		
		for (int i=0; i<count; i++) {
			boolean selected = (i == selectedTab); 
			Color color = selected ? Color.BLACK : Color.GREY_SILVER.darker(1.4);
			Color bgColor = selected ? Color.WHITE : Color.GREY_LIGHT; 
			FontWeight weight = selected ? FontWeight.BOLD : FontWeight.NORMAL; 
			
			Tab tab = tabBar.getTab(i);
			if (tab instanceof Composite) {
				Composite composite = (Composite)tab;
				//Widget widget = composite.
				
				Element element = composite.getElement();
				Style style = element.getStyle(); 
				style.setColor(color.toString());
				style.setBackgroundColor(bgColor.toString());
				style.setFontWeight(weight);
				style.setMarginLeft(24, Unit.PX);
				style.setFontSize(140, Unit.PCT);
				style.setProperty("fontFamily", "Roboto, sans-serif");
			}
		}
	}

}
