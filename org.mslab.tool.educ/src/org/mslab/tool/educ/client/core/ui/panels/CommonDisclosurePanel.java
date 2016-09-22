package org.mslab.tool.educ.client.core.ui.panels;

import java.util.HashMap;
import java.util.Map;

import org.mslab.tool.educ.client.core.ui.ExpandWidget;
import org.mslab.tool.educ.client.core.ui.StyleUtil;
import org.mslab.tool.educ.client.core.ui.interfaces.Buildable;
import org.mslab.tool.educ.shared.text.MessageFormat;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

public class CommonDisclosurePanel extends GridPanel implements OpenHandler<DisclosurePanel>, CloseHandler<DisclosurePanel>, ClickHandler {
	private static Map<String, CommonDisclosurePanel> _allPanels = new HashMap<String, CommonDisclosurePanel>();
	protected boolean _expanded;
	private DisclosurePanel _disclosurePanel;
	protected HorizontalPanel _showHeader, _hideHeader;
	private ExpandWidget _expandSymbol;
	private HTML _verticalBar;
	protected Widget _childWidget; 
	private String _name;

	public CommonDisclosurePanel(String name, Widget childWidget) {
		_name = name;
		_allPanels.put(name, this); 
		
		int row = 0;
		_childWidget = childWidget;
		_grid.setCellPadding(0);
		_grid.setCellSpacing(0);
		
		String showText = MessageFormat.format("Show {0}", new Object[] {name}); 
		String hideText = MessageFormat.format("Hide {0}", new Object[] {name}); 
		
		_expandSymbol = new ExpandWidget(); 
		_grid.setWidget(row, 0, _expandSymbol);
		_grid.getFlexCellFormatter().setVerticalAlignment(row, 0, HasVerticalAlignment.ALIGN_TOP);
		
		_disclosurePanel = new DisclosurePanel();
		_disclosurePanel.setWidth("100%");
		_showHeader = new HorizontalPanel();
		_showHeader.getElement().getStyle().setPaddingLeft(6, Unit.PX);
		_showHeader.add(new HTML(showText));
		
		_hideHeader = new HorizontalPanel();
		_hideHeader.getElement().getStyle().setPaddingLeft(6, Unit.PX);
		_hideHeader.add(new HTML(hideText));
		
		_disclosurePanel.setHeader(_showHeader);
		_disclosurePanel.setAnimationEnabled(true);
		_disclosurePanel.addOpenHandler(this); 
		_disclosurePanel.addCloseHandler(this); 
		
		childWidget.getElement().getStyle().setMarginTop(12, Unit.PX);
		_disclosurePanel.add(childWidget);
		StyleUtil.transition(_disclosurePanel, "all 0.4s ease-in-out");	
		_grid.setWidget(row, 1, _disclosurePanel);
		_grid.getFlexCellFormatter().setWidth(row, 1, "99%");
		row++;
		
		_verticalBar = new VerticalBar();
		_verticalBar.getElement().getStyle().setWidth(100, Unit.PCT);
		_verticalBar.getElement().getStyle().setHeight(100, Unit.PCT);
		_grid.setWidget(row, 0, _verticalBar);
		_grid.getFlexCellFormatter().setColSpan(row, 0, 2);
		
		_expanded = false;
		refresh();
	}

	@Override
	public void onOpen(OpenEvent<DisclosurePanel> event) {
		
		if (_childWidget instanceof OpenHandler) {
			OpenHandler handler = (OpenHandler)_childWidget;
			handler.onOpen(event);
		}
		
		//_childWidget.o
		
		if (_childWidget instanceof Buildable) {
			((Buildable)_childWidget).build();
		}
		
		_expanded = true;
		refresh();
	}
	
	@Override
	public void onClose(CloseEvent<DisclosurePanel> event) {
		_expanded = false;
		refresh();
	}
	
	public void setExpanded(boolean expanded) {
		_expanded = expanded;
		_disclosurePanel.setOpen(_expanded);
		refresh();
	}
	
	private void refresh() {
		_expandSymbol.setExpanded(_expanded);
		//StyleUtil.rotate(_expandSymbol, _expanded ? 90 : 0);
		//_disclosurePanel.setHeader(_expanded ? _hideHeader : _showHeader);
	}
	
	
	private class VerticalBar extends HTML {
		VerticalBar() {
			super("<hr style=\"width:100%;\" />"); 
		}
	}
	
	/*
	@Override
	public void onMouseOver(MouseOverEvent event) {
		_expandSymbol.getElement().getStyle().setCursor(Cursor.POINTER);
	}

	@Override
	public void onMouseOut(MouseOutEvent event) {
		_expandSymbol.getElement().getStyle().setCursor(Cursor.DEFAULT);
	}
	*/

	@Override
	public void onClick(ClickEvent event) {
		_disclosurePanel.setOpen(! _expanded);
	}

	public String getName() {
		return _name;
	}

	public static CommonDisclosurePanel findPanelWhoseNameIs(String name) {
		CommonDisclosurePanel panel = _allPanels.get(name); 
		return panel;
	}




}
