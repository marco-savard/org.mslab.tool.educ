package org.mslab.tool.educ.client.core.ui.panels;

import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.ScrollPanel;

public abstract class AbstractApplicationPanel extends ScrollPanel implements ResizeHandler {
	protected FlexTable _grid;
	private Timer _resizeTimer;
	
	protected AbstractApplicationPanel() {		
		_grid = new FlexTable();
		_grid.setSize("100%", "100%");
		_grid.setCellPadding(0);
		_grid.setCellSpacing(0);
		
		this.add(_grid); 
		
		//this.setAlwaysShowScrollBars(true);
		Window.addResizeHandler(this);
		_resizeTimer = new ResizeTimer(); 
		pack();
	}
	
	@Override
	public void onResize(ResizeEvent ev) {
		 _resizeTimer.cancel();
		 _resizeTimer.schedule(200);
	}
	
	protected abstract void pack();
	
	private class ResizeTimer extends Timer {
		@Override
		public void run() {
			 pack();
		}
	}
}
