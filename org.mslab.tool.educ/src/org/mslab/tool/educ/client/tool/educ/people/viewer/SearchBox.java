package org.mslab.tool.educ.client.tool.educ.people.viewer;

import java.util.ArrayList;
import java.util.List;

import org.mslab.tool.educ.client.core.ui.StyleUtil;
import org.mslab.tool.educ.client.core.ui.panels.GridPanel;
import org.mslab.tool.educ.client.tool.educ.people.viewer.FilteringOption.SearchField;
import org.mslab.tool.educ.shared.text.StringExt;
import org.mslab.tool.educ.shared.types.Color;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class SearchBox extends GridPanel implements MouseOverHandler, MouseOutHandler {
	private ResizeAnimation _animation; 
	private SuggestBoxContainer _suggestBoxContainer;
	String _text;
	private int _delayInMs; 
	private Timer _updateTimer;
	private List<ChangeHandler> _changeHandlers = new ArrayList<ChangeHandler>(); 
	
	public SearchBox(int delayInMs) {
		_delayInMs = delayInMs; 
		getElement().getStyle().setBackgroundColor(Color.WHITE.toString());
		
		Color color = Color.GREY_SILVER; 
		setHeight("2em"); 
		_grid.setCellPadding(0);
		_grid.setCellSpacing(0);
		StyleUtil.setBorderRadius(this, "5px");
		Style style = getElement().getStyle();
		style.setBorderWidth(2, Unit.PX);
		style.setBorderStyle(BorderStyle.SOLID);
		style.setBorderColor(color.toString());
		
		//the decorating icon
		int col = 0; 
		HorizontalPanel prefix = new HorizontalPanel();
		prefix.setSize("2em", "2em");
		prefix.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		Color bgColor = color.blendWith(Color.WHITE, 70); 
		style = prefix.getElement().getStyle();
		style.setBackgroundColor(bgColor.toString());
		StyleUtil.setBorderRadius(prefix, "4px 0px 0px 4px");
		_grid.setWidget(0, col, prefix);
		
		HTML icon = new HTML("<i class=\"fa fa-search\"></i>");
		icon.getElement().getStyle().setTextAlign(TextAlign.CENTER);
		prefix.add(icon);
		col++;
		
		//the textbox
		_suggestBoxContainer = new SuggestBoxContainer();
		_grid.setWidget(0, col, _suggestBoxContainer);
		
		_updateTimer = new Timer() {
			@Override
			public void run() {
				String text = _suggestBoxContainer.getText();
				if (! text.equals(_text)) {
					_text = text;
					
					if (text.length() > 1) {
						fireEvent();	
					}
				}
			}
		};
		
		_animation = new ResizeAnimation(this);
		addMouseOverHandler(this); 
		addMouseOutHandler(this); 
		refresh();		
	}

	public void addChangeHandler(ChangeHandler handler) {
		_changeHandlers.add(handler); 
	}
	
	private void fireEvent() {
		ChangeEvent event = new TextChangeEvent(this);
		for (ChangeHandler handler : _changeHandlers) {
			handler.onChange(event);
		}
	}	

	@Override
	public void onMouseOver(MouseOverEvent event) {
		_animation.resize(300, 12);
	}
	
	@Override
	public void onMouseOut(MouseOutEvent event) {
		refresh();
	}
	
	private void refresh() {
		boolean visible = ! StringExt.isNullOrWhitespace(_suggestBoxContainer.getText());
		int targetSize = visible ? 12 : 5; 
		_animation.resize(200, targetSize);
		_suggestBoxContainer.setVisible(visible);
	}
	
	public String getText() {
		return _suggestBoxContainer.getText();
	}
	
	public void setSearchField(SearchField searchField) {
		_suggestBoxContainer.setSearchField(searchField);
	}
	
	//
	// inner classes
	//
	private class SuggestBoxContainer extends DeckPanel implements KeyUpHandler, SelectionHandler<Suggestion> {
		private SuggestBox _allFieldsSuggestBox, _givenNameSuggestBox, _familyNameSuggestBox;
		private EducPhoneTextBox _phoneTextBox;
		private int _selectedIdx = 0;
		
		SuggestBoxContainer() {
			_allFieldsSuggestBox = new EducPeopleSuggestBoxAllFields();
			_givenNameSuggestBox = new EducPeopleSuggestBoxGivenName();
			_familyNameSuggestBox = new EducPeopleSuggestBoxFamilyName();
			_phoneTextBox = new EducPhoneTextBox();
			
			_allFieldsSuggestBox.addKeyUpHandler(this);
			_givenNameSuggestBox.addKeyUpHandler(this);
			_familyNameSuggestBox.addKeyUpHandler(this);
			_phoneTextBox.addKeyUpHandler(this);
			
			_allFieldsSuggestBox.addSelectionHandler(this); 
			_givenNameSuggestBox.addSelectionHandler(this); 
			_familyNameSuggestBox.addSelectionHandler(this); 
			
			add(_allFieldsSuggestBox);
			add(_givenNameSuggestBox);
			add(_familyNameSuggestBox);
			add(_phoneTextBox);
			
			refresh();
		}
		
		public void setSearchField(SearchField searchField) {
			if (searchField == SearchField.GIVEN_NAME) {
				_selectedIdx = 1;
			} else if (searchField == SearchField.FAMILY_NAME) {
				_selectedIdx = 2;
			} else if (searchField == SearchField.PHONE_NUMBER) {
				_selectedIdx = 3;
			} else if (searchField == SearchField.ALL_FIELDS) {
				_selectedIdx = 0;
			}
			
			refresh();
		}

		public String getText() {
			Widget widget = getWidget(_selectedIdx); 
			String text = null; 
			
			if (widget instanceof TextBox) {
				text = ((TextBox)widget).getText();
			} else if (widget instanceof SuggestBox) {
				text = ((SuggestBox)widget).getText();
			}
			return text;
		}

		public void refresh() {
			showWidget(_selectedIdx);
		}

		@Override
		public void onKeyUp(KeyUpEvent event) {
			_updateTimer.cancel();
			_updateTimer.schedule(_delayInMs);
		}

		@Override
		public void onSelection(SelectionEvent<Suggestion> event) {
			_updateTimer.cancel();
			_updateTimer.schedule(_delayInMs);
		}
	}
	
	class ResizeAnimation extends Animation {
		private SearchBox _widget; 
		private int _startSize = 5, _endSize;
		
		ResizeAnimation(SearchBox widget) {
			_widget = widget;
		}
		
		@Override
		protected void onUpdate(double progress) {
			int delta = _endSize - _startSize;
			int size = (int)(_startSize + (progress * delta)); 
			_widget.setWidth(size + "em");
		}
		
		@Override
		protected void onComplete() {
			if (_endSize > _startSize) {
				_widget._suggestBoxContainer.setVisible(true);
			}
			
			_startSize = _endSize;
		    super.onComplete();
		}
		
		public void resize(int duration, int targetSize) {
			_endSize = targetSize;
			run(duration);
		}
	}
	
	class TextChangeEvent extends ChangeEvent {
		private SearchBox _src; 
		
		TextChangeEvent(SearchBox src) {
			_src = src;
		}
		
		@Override
		public Object getSource() {
		    return _src;
		  }
	}




}