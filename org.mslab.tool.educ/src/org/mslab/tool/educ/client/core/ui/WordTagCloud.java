package org.mslab.tool.educ.client.core.ui;

import java.util.ArrayList;
import java.util.List;

import org.mslab.tool.educ.shared.types.Color;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;

public abstract class WordTagCloud extends FlowPanel implements ClickHandler {
	private static final int MIN_HEIGHT = 10, MAX_HEIGHT = 25;
	private List<WordTag> _tags = new ArrayList<WordTag>(); 
	
	public WordTagCloud() {
		setWidth("100%");
		Style style = getElement().getStyle();
		Color color = Color.GREY_LIGHT;
		//style.setVerticalAlign(VerticalAlign.MIDDLE);
		//style.setBackgroundColor(color.toString());
	}
	
	public void setColored(boolean colored) {
		// TODO Auto-generated method stub
		
	}
	
	public void addWord(WordTag tag) {
		_tags.add(tag); 
	}

	public List<WordTag> getTags() {
		return _tags;
	}
	
	protected void refresh() {
		clear();
		int higestFrequency = computeHighestFrequency(); 
		
		for (WordTag tag : _tags) {
			tag.setHeight(MAX_HEIGHT + "px");
			int frequency = tag.getNbOccurrences(); 
			//int fontSize = (int)(100 * 5 * Math.log10(nb));
			
			Style style = tag.getElement().getStyle();
			style.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
			style.setMarginRight(12, Unit.PX);
			add(tag);
			
			Label label = tag.getLabel();
			label.addClickHandler(this);
			style = label.getElement().getStyle();
			
			int fontSize = computeFontSize(higestFrequency, frequency);
			style.setFontSize(fontSize, Unit.PX);
		}
	}

	private int computeHighestFrequency() {
		int highestFrequency = 0;
		
		for (WordTag tag : _tags) { 
			int frequency = tag.getNbOccurrences(); 
			highestFrequency = Math.max(highestFrequency, frequency); 
		}
		return highestFrequency;
	}
	
	private int computeFontSize(int higestFrequency, int frequency) {
		double ratio = (frequency / (double)higestFrequency); //in range [0..1]
		ratio = 1 - ((1-ratio) * (1-ratio) * (1-ratio) * (1-ratio)); //log-1
		double fontSize = MIN_HEIGHT + ((MAX_HEIGHT - MIN_HEIGHT) * ratio); //in range [min..max] 
		return (int)fontSize;
	}
}
