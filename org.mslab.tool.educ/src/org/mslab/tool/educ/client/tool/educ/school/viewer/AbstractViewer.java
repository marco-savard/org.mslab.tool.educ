package org.mslab.tool.educ.client.tool.educ.school.viewer;

import org.mslab.tool.educ.client.tool.educ.school.explorer.EntityViewable;

import com.google.gwt.user.client.ui.SimplePanel;

public abstract class AbstractViewer<T> extends SimplePanel implements EntityViewable<T> {
	
	protected AbstractViewer() {
	}

}
