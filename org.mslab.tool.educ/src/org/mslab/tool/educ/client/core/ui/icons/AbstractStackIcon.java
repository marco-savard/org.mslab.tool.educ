package org.mslab.tool.educ.client.core.ui.icons;

import java.util.ArrayList;
import java.util.List;

import org.mslab.tool.educ.shared.text.MessageFormat;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTML;

public abstract class AbstractStackIcon extends AbsolutePanel {
	private List<Layer> _layers = new ArrayList<Layer>();
	
	protected void addLayer(String iconName, String color) {
		addLayer(iconName, color, 0, 0);
	}

	protected void addLayer(String iconName, String color, int x, int y) {
		_layers.add(new Layer(iconName, color, x, y));
	}
	
	protected void render() {
		int nb = _layers.size();
		String pat = "<span class=\"fa {0} \" style=\"color:{1}\" />";
		
		for (Layer layer : _layers) {
			String html = MessageFormat.format(pat, 
				new Object[] {layer._iconName, layer._color, layer._x, layer._y});
			super.add(new HTML(html), layer._x, layer._y);
		}
		
		super.setSize("20px", "20px");
	}
	
	class Layer {
		private String _iconName;
		private String _color;
		private int _x, _y;

		public Layer(String iconName, String color, int x, int y) {
			_iconName = iconName;
			_color = color;
			_x = x;
			_y = y;
		}

	}
}
