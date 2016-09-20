package org.mslab.commons.client.tool.educ.school.viewer;

import org.mslab.commons.client.core.ui.panels.GridPanel;
import org.mslab.commons.client.tool.educ.school.explorer.EntityViewable;

import com.google.gwt.user.client.ui.SimplePanel;

public abstract class AbstractViewer<T> extends SimplePanel implements EntityViewable<T> {
	
	protected AbstractViewer() {
	}

}
