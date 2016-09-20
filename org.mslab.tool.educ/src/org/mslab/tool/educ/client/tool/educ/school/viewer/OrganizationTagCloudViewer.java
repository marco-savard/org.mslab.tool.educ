package org.mslab.commons.client.tool.educ.school.viewer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mslab.commons.client.core.ui.StyleUtil;
import org.mslab.commons.client.core.ui.WordTag;
import org.mslab.commons.client.core.ui.WordTagCloud;
import org.mslab.commons.client.core.ui.panels.GridPanel;
import org.mslab.commons.client.core.ui.theme.AbstractTheme;
import org.mslab.commons.client.tool.educ.EducTheme;
import org.mslab.commons.client.tool.educ.school.viewer.OrganizationListViewer.OrganizationListViewerContent;
import org.mslab.commons.client.tool.educ.school.viewer.OrganizationListViewer.OrganizationViewerHeaderContent;
import org.mslab.commons.shared.text.AlphabeticComparator;
import org.mslab.commons.shared.text.CharacterExt;
import org.mslab.commons.shared.text.MessageFormat;
import org.mslab.commons.shared.text.Text;
import org.mslab.commons.shared.types.Address;
import org.mslab.commons.shared.types.CityOld;
import org.mslab.commons.shared.types.Color;
import org.mslab.commons.shared.types.GeoLocation;
import org.mslab.commons.shared.types.educ.City;
import org.mslab.commons.shared.types.educ.Organization;

import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.TextDecoration;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;

public class OrganizationTagCloudViewer extends AbstractViewer<Organization> implements ResizeHandler {
	private static TagCloudPanel _tagCloudPanel; 
	private OrganizationViewerHeaderContent _parent;
	
	public OrganizationTagCloudViewer(OrganizationViewerHeaderContent parent) {
		_parent = parent;
		_tagCloudPanel = new TagCloudPanel(this); 
		add(_tagCloudPanel);
		
		AbstractTheme theme = EducTheme.getTheme();
		Color bgColor = theme.getPrimaryBgColor();
		Color fgColor = theme.getPrimaryFgColor();
		StyleUtil.setLinearGradient(this, 90, bgColor, Color.WHITE);
		getElement().getStyle().setBorderColor(fgColor.toString());
		getElement().getStyle().setBorderWidth(1, Unit.PX);
		getElement().getStyle().setBorderStyle(BorderStyle.SOLID);
	}
	
	@Override
	public void update(String listName, List<Organization> organizations) {
		Dictionary dic = new Dictionary(); 
		if (organizations != null) {
			for (Organization organization : organizations) {
				dic.addWords(organization); 
			}
			
			_tagCloudPanel.showCloud(dic); 
		}
	}
	
	@Override
	public void onResize(ResizeEvent event) {
		// TODO Auto-generated method stub	
	}
	
	private static class TagCloudPanel extends GridPanel {
		private OrganizationTagCloudViewer _parent;
		private OrganizationWordTagCloud _tagCloud;
		
		TagCloudPanel(OrganizationTagCloudViewer parent) {
			_parent = parent;
			_tagCloud = new OrganizationWordTagCloud(this);
			_grid.setWidth("100%");
	        _grid.setWidget(0, 0, _tagCloud);
	        _grid.getFlexCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER); 
		}

		public void showCloud(Dictionary dic) {
			_tagCloud.showCloud(dic);
		}
	}
	
	private static class OrganizationWordTagCloud extends WordTagCloud  {
		private TagCloudPanel _parent;
		
		public OrganizationWordTagCloud(TagCloudPanel parent) {
			_parent = parent;
			sinkEvents(Event.ONCLICK);
			setColored(true);
			setWidth("100%");
		}
		
		private void showCloud(Dictionary dic) {
			getTags().clear();
			List<String> mostFrequentWords = dic.getMostFrequentWords(50); 
			Comparator<String> alphabeticComparator = new AlphabeticComparator(); 
			Collections.sort(mostFrequentWords, alphabeticComparator);
			
			for (String word : mostFrequentWords) {
				int frequency = dic.getFrequency(word); 
				WordTag tag = new OrganizationWordTag(this, word, frequency);
				addWord(tag);
			}
			refresh();
		}

		@Override
		public void onClick(ClickEvent event) {
			Object src = event.getSource();
			
			if (src instanceof Label) {
				Label label = (Label)src;
				search(label.getText()); 
			}
		}

		private void search(String text) {
			_parent._parent._parent.filterText(text);
		}
	} //end OrganizationWordTagCloud
	
	private static class OrganizationWordTag extends WordTag implements MouseOverHandler, MouseOutHandler, ClickHandler {
		private OrganizationWordTagCloud _tagCloud; 
		
		public OrganizationWordTag(OrganizationWordTagCloud tagCloud, String text, int nbOccurrences) {
			super(text, nbOccurrences);
			_tagCloud = tagCloud;
			addMouseOverHandler(this);
			addMouseOutHandler(this);
			addClickHandler(this);
			refresh();
		}
		
		@Override
		public void onMouseOver(MouseOverEvent event) {
			Color color = AbstractTheme.getTheme().getPrimaryFgColor();
			getElement().getStyle().setCursor(Cursor.POINTER);
			getElement().getStyle().setColor(color.toString());
			getElement().getStyle().setTextDecoration(TextDecoration.UNDERLINE);
		}
		
		@Override
		public void onMouseOut(MouseOutEvent event) {
			refresh();
		}

		private void refresh() {
			getElement().getStyle().setCursor(Cursor.DEFAULT);
			getElement().getStyle().setColor(Color.BLACK.toString());
			getElement().getStyle().setTextDecoration(TextDecoration.NONE);
		}

		@Override
		public void onClick(ClickEvent event) {
			String text = _html.getText();
			_tagCloud.search(text);
		}




	}
	
	private static class Dictionary {
		private Map<String, Integer> _words = new HashMap<String, Integer>();

		public void addWords(Organization organization) {
			String name = organization.getName(); 
			addName(name); 
			
			Address address = organization.getAddress();
			String street = (address == null) ? null : address.getStreet(); 
			addName(street); 
			
			GeoLocation location = (address == null) ? null : address.getLocation();
			City city = (location == null) ? null : location.getCity(); 
			
			if (city != null) {
				addName(city.getName());
			}
		}

		private void addName(String text) {
			if (text != null) {
				String parsed = text.replaceAll("-|[/?:!.,;\'\"]", " ");
				String[] words = parsed.split(" "); 
				for (String word : words) {
					if (hasMinimalNumberOfCharacters(word, 3)) {
						addKey(word); 
					}
					//word = word.toLowerCase();
				}
			}
		}

		private boolean hasMinimalNumberOfCharacters(String word, int nbCharsMin) {
			boolean hasMinimal = false;
			int nb = word.length();
			int nbChars = 0; 
			
			for (int i=0; i<nb; i++) {
				char ch = word.charAt(i); 
				nbChars += CharacterExt.isAlphabetic(ch) ? 1 : 0;
				if (nbChars >= nbCharsMin) {
					hasMinimal = true;
					break;
				}
			}
			
			return hasMinimal;
		}

		private void addKey(String key) {
			if (key != null) {
				key = new Text(key).capitalize().toString();
				
				if (_words.containsKey(key)) {
					int count = _words.get(key); 
					_words.put(key, 1+count); 
				} else {
					_words.put(key, 1); 
				}
			}
		}

		public List<String> getMostFrequentWords(int nbWords) {
			List<String> wordList = getWordList(); 
			Comparator<String> comparator = new FrequencyComparator(); 
			Collections.sort(wordList, comparator);
			nbWords = Math.min(nbWords, wordList.size()); 
			List<String> mostFrequentWordList = wordList.subList(0, nbWords);
			return mostFrequentWordList;
		}

		private List<String> getWordList() {
			Set<String> keys = _words.keySet();
			List<String> wordList = new ArrayList<String>();
			wordList.addAll(keys);
			return wordList;
		}

		public int getFrequency(String word) {
			return _words.get(word);
		}
		
		private class FrequencyComparator implements Comparator<String> {
			@Override
			public int compare(String w1, String w2) {
				int f1 = _words.get(w1);
				int f2 = _words.get(w2);
				return f2 - f1;
			}
		}
		
		@Override
		public String toString() {
			String msg = MessageFormat.format("{0} words", new Object[] {_words.size()});
			return msg;
		}
	} //end Dictionary


	
}
