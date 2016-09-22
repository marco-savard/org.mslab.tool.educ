package org.mslab.tool.educ.client.core.ui.slider;

public interface SliderListener2 {
	
	public void onStart(SliderEvent2 ev);
	
	public boolean onSlide(SliderEvent2 ev);
	
	public void onChange(SliderEvent2 ev); 
	
	public void onStop(SliderEvent2 ev);

}
