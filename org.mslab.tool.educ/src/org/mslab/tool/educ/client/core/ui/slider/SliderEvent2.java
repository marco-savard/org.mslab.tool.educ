package org.mslab.tool.educ.client.core.ui.slider;

public class SliderEvent2 {
	private Slider2 _source; 
	private int[] _values; 

	public SliderEvent2(Slider2 source, int[] values) {
		_source = source;
		_values = values;
	}

	public Slider2 getSource() {
		return _source;
	}

	public int[] getValues() {
		return _values;
	}

}
