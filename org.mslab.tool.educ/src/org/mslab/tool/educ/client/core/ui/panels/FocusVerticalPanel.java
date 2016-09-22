package org.mslab.tool.educ.client.core.ui.panels;

import java.util.Iterator;

import com.google.gwt.event.dom.client.HasAllKeyHandlers;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class FocusVerticalPanel extends FocusPanel implements HasAlignment {

	private VerticalPanel _verticalPanel;
	
	public FocusVerticalPanel() {
	    _verticalPanel = new VerticalPanel();
	    _verticalPanel.setSize("100%", "100%");
	    _verticalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
	    setWidget(_verticalPanel);
	}
	
	@Override
	public void add(Widget widget) {
		_verticalPanel.add(widget);
	}
	
	@Override
	public boolean remove(Widget w) {
	    return _verticalPanel.remove(w);
	}
	
	@Override
	public void clear() {
	    _verticalPanel.clear();
	}
	
	@Override
	public Iterator<Widget> iterator() {
	    return _verticalPanel.iterator();
	}
	
	@Override
	public HorizontalAlignmentConstant getHorizontalAlignment() {
	    return _verticalPanel.getHorizontalAlignment();
	}

	@Override
	public void setHorizontalAlignment(HorizontalAlignmentConstant align) {
		_verticalPanel.setHorizontalAlignment(align);

	}

	@Override
	public VerticalAlignmentConstant getVerticalAlignment() {
	    return _verticalPanel.getVerticalAlignment();
	}

	@Override
	public void setVerticalAlignment(VerticalAlignmentConstant align) {
		_verticalPanel.setVerticalAlignment(align);
	}

}
