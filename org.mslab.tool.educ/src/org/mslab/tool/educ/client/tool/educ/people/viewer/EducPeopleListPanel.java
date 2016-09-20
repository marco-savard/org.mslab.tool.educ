package org.mslab.commons.client.tool.educ.people.viewer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.mslab.commons.client.core.ui.AbstractIconButton;
import org.mslab.commons.client.core.ui.SelectionButton;
import org.mslab.commons.client.core.ui.SelectionButtonGroup;
import org.mslab.commons.client.core.ui.StyleUtil;
import org.mslab.commons.client.core.ui.panels.GridPanel;
import org.mslab.commons.client.core.ui.theme.AbstractTheme;
import org.mslab.commons.client.tool.educ.EducAnchor;
import org.mslab.commons.client.tool.educ.EducListBox;
import org.mslab.commons.client.tool.educ.EducTheme;
import org.mslab.commons.client.tool.educ.people.viewer.content.PeoplePanel;
import org.mslab.commons.client.tool.educ.people.viewer.content.PersonRenderer;
import org.mslab.commons.client.tool.educ.school.explorer.EntityViewable;
import org.mslab.commons.shared.text.AlphabeticComparator;
import org.mslab.commons.shared.text.MessageFormat;
import org.mslab.commons.shared.text.StringExt;
import org.mslab.commons.shared.types.Color;
import org.mslab.commons.shared.types.educ.Person;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;

public class EducPeopleListPanel extends GridPanel implements EntityViewable<Person> {
	private EducPeopleDeckPanel _owner;
	private EducPeopleViewHeader _header; 
	private PeopleAnimatedTitle _title;
	private SortOptionPanel _sortOptionPanel;
	private PeopleListPanel _peopleListPanel;
	private PageOptionPanel _pageOptionPanel;
	
    private List<Person> _filteredDirectors; 
    private PersonComparator _personComparator; 
    private FilteringOption _filteringOption;
    //private String _filterText = null;
   // private AdvancedSearchPanel.SearchField _searchField; 
    //private String _category;
    private int _pageSize = PAGE_SIZES[0];
    private int _currentPage = 0;
    
    EducPeopleListPanel(EducPeopleDeckPanel owner) {
    	_owner = owner;
		int row = 0;
		_grid.setWidth("100%");
		_grid.setCellPadding(0);
		_grid.setCellSpacing(0);

		_header = new EducPeopleViewHeader(this); 
		_grid.setWidget(row, 0, _header);
		row++;
	
		_peopleListPanel = new PeopleListPanel(this); 
		_grid.setWidget(row, 0, _peopleListPanel);
		row++;
		
		_pageOptionPanel = new PageOptionPanel(); 
		_grid.setWidget(row, 0, _pageOptionPanel);
		row++;
		
		_personComparator = new PersonComparator(); 
		refresh();
	}
	
    @Override
	public void update(String category, List<Person> persons) {
		_owner.update(category, persons);
	}
		
	void refresh() {
		List<Person> persons = _owner.getPersons();
		String category = _owner.getCategory();
		int nb = (persons == null) ? 0 : persons.size(); 
		
		boolean visible = (nb > 0);
		setVisible(visible);
		
		if (! visible) {
			return;
		}
		
		_filteredDirectors = filterDirectors(); 
		int count = _filteredDirectors.size();
		_pageOptionPanel.setNbPages(count);
		
		String text = (_filteringOption == null) ? null : _filteringOption._filteringText;
		String criteria = StringExt.isNullOrWhitespace(text) ? category : text; 
		_title.setCountCriteria(count, criteria); 
		
		int selectedCriteriaIdx = _sortOptionPanel._criteraBox.getSelectedIndex();
		int selectedOrderIdx = _sortOptionPanel._sortDirectionPanel.getSelectedIndex();
		_personComparator.setCriteria(selectedCriteriaIdx);
		_personComparator.setOrder(selectedOrderIdx);
		
		if (_filteredDirectors != null) {
			Collections.sort(_filteredDirectors, _personComparator);
			int start = (_currentPage * _pageSize); 
			int end = ((_currentPage+1) * _pageSize); 
			start = Math.min(start, _filteredDirectors.size());
			end =  Math.min(end, _filteredDirectors.size());
			List<Person> page =  _filteredDirectors.subList(start, end);
			_peopleListPanel.updateList(page, _filteringOption, start, end);
		}
	}
	
	private List<Person> filterDirectors() {
		List<Person> filteredDirectors = new ArrayList<Person>(); 
		List<Person> persons = _owner.getPersons();
	
		if (persons != null) {
			for (Person director : persons) {
				boolean accepted = filterDirector(director); 
				if (accepted) {
					filteredDirectors.add(director); 
				}
			}
		}
		
		return filteredDirectors;
	}

	private boolean filterDirector(Person person) {
		boolean accepted = StringExt.isNullOrWhitespace(person.getFamilyName()) ? 
			false : true; 
		
		if (accepted) {
			if (_filteringOption != null) {
				accepted = false;
				PersonRenderer renderer = new PersonRenderer(person); 
				
				//full name
				if (_filteringOption._searchField == FilteringOption.SearchField.ALL_FIELDS) {
					accepted = renderer.filter(PersonRenderer.Field.FULL_NAME, _filteringOption._filteringText);
				} else if (_filteringOption._searchField == FilteringOption.SearchField.GIVEN_NAME) {
					accepted = renderer.filter(PersonRenderer.Field.GIVEN_NAME, _filteringOption._filteringText);
				} else if (_filteringOption._searchField == FilteringOption.SearchField.FAMILY_NAME) {
					accepted = renderer.filter(PersonRenderer.Field.FAMILY_NAME, _filteringOption._filteringText);
				}
				
				if (_filteringOption._searchField == FilteringOption.SearchField.ALL_FIELDS) {
					accepted |= renderer.filter(PersonRenderer.Field.TITLE, _filteringOption._filteringText);
				}
				
				//filter phone
				boolean filterPhone = (_filteringOption._searchField == FilteringOption.SearchField.PHONE_NUMBER) ||
					(_filteringOption._searchField == FilteringOption.SearchField.ALL_FIELDS) ;
				if (filterPhone) {
				    accepted |= renderer.filter(PersonRenderer.Field.PHONE, _filteringOption._filteringText);
				}
			}
		}
		
		return accepted;
	}

	public void filterText(FilteringOption filteringOption) {
		_filteringOption = filteringOption;
		refresh();
	}
	
	private static class EducPeopleViewHeader extends GridPanel implements ClickHandler {
		private EducPeopleListPanel _parent; 
		private PeopleSearchBoxPanel _searchBoxPanel; 
		private EducAnchor _advancedSearchAnchor;
		private AdvancedSearchPanel _advancedSearchPanel;
		
		EducPeopleViewHeader(EducPeopleListPanel parent) {
			_parent = parent;
			_grid.setWidth("100%");
			int row = 0;
			Color bgColor = EducTheme.getTheme().getPrimaryBgColor();
			StyleUtil.setLinearGradient(this, 90, bgColor.getGrayscale(), Color.WHITE);
			
			_parent._title = new PeopleAnimatedTitle(parent); 
			_parent._title.getElement().getStyle().setPaddingTop(12, Unit.PX);
			_parent._title.getElement().getStyle().setPaddingLeft(12, Unit.PX);
			_grid.setWidget(row, 0, _parent._title);
			
			SimplePanel filler = new SimplePanel();
			_grid.setWidget(row, 1, filler);
			_grid.getFlexCellFormatter().setWidth(row, 1, "95%");
			
			_searchBoxPanel = new PeopleSearchBoxPanel(parent, null); 
			_grid.setWidget(row, 2, _searchBoxPanel);
			_grid.getFlexCellFormatter().setHorizontalAlignment(row, 2, HasHorizontalAlignment.ALIGN_RIGHT);
			row++;
			
			_advancedSearchAnchor = new EducAnchor("Recherche avanc&eacute;e"); 
			_advancedSearchAnchor.setWordWrap(false);
			_advancedSearchAnchor.addClickHandler(this); 
			_grid.setWidget(row, 2, _advancedSearchAnchor);
			_grid.getFlexCellFormatter().setHorizontalAlignment(row, 2, HasHorizontalAlignment.ALIGN_RIGHT);
			row++;
			
			_advancedSearchPanel = new AdvancedSearchPanel(_parent); 
			_advancedSearchPanel.setVisible(false);
			_grid.setWidget(row, 0, _advancedSearchPanel);
			_grid.getFlexCellFormatter().setColSpan(row, 0, 2);
			row++;
			
			_parent._sortOptionPanel = new SortOptionPanel(_parent); 
			_parent._sortOptionPanel.getElement().getStyle().setPaddingLeft(12, Unit.PX);
			_grid.setWidget(row, 1, _parent._sortOptionPanel);
			_grid.getFlexCellFormatter().setHorizontalAlignment(row, 1, HasHorizontalAlignment.ALIGN_RIGHT);
			_grid.getFlexCellFormatter().setColSpan(row, 1, 2);
			row++;
		}
		
		@Override
		public void onClick(ClickEvent event) {
			Object src = event.getSource();
			if (_advancedSearchAnchor.equals(src)) {
				_searchBoxPanel.setVisible(false);
				_advancedSearchAnchor.setVisible(false);
				_advancedSearchPanel.setVisible(true);
			}
		}
	}
	
	private static class PeopleSearchBoxPanel extends GridPanel implements ChangeHandler {	
		private EducPeopleListPanel _parent;
		private AdvancedSearchPanel _advancedSearchPanel; 
		private SearchBox _searchBox; 
		private static final int DELAY = 300; //ms
		
		PeopleSearchBoxPanel(EducPeopleListPanel parent, AdvancedSearchPanel advancedSearchPanel) {
			_parent = parent;
			_advancedSearchPanel = advancedSearchPanel;
			_grid.setCellPadding(0);
			_grid.setCellSpacing(0);
			
			_searchBox = new SearchBox(DELAY); 
			_searchBox.addChangeHandler(this);
			_grid.setWidget(0, 0, _searchBox);
		}
		
		@Override
		public void onChange(ChangeEvent event) {
			Object src = event.getSource();
			if (_searchBox.equals(src)) {
				filterText();
			}
		}
		
		void filterText() {
			FilteringOption.SearchField searchField = (_advancedSearchPanel != null) ? 	
				_advancedSearchPanel.getSearchField() : 
				FilteringOption.SearchField.ALL_FIELDS; 
			
			_searchBox.setSearchField(searchField); 
			String textChanged = _searchBox._text; 
			textChanged = (textChanged == null) ? null : textChanged.toLowerCase();
			FilteringOption filteringOption = new FilteringOption(textChanged, searchField);
			_parent.filterText(filteringOption);
			_parent._currentPage = 0;
		}
	}
	
	private static class PeopleAnimatedTitle extends HTML {
		private UpdateTitleAnimation _animation; 
		private int _startCount; 
		
		PeopleAnimatedTitle(EducPeopleListPanel owner) {
			setWordWrap(false);
			_animation = new UpdateTitleAnimation(owner, this);
			
			AbstractTheme theme = EducTheme.getTheme();
			
			getElement().getStyle().setFontSize(200, Unit.PCT);
			getElement().getStyle().setFontWeight(FontWeight.LIGHTER);
			getElement().getStyle().setProperty("fontFamily", theme.getFontFamily());
		}

		public void setCountCriteria(int endCount, String criteria) {
			_animation.updateCount(600, _startCount, endCount, criteria);
		}
	}
	
	private static class UpdateTitleAnimation extends Animation { 
		private int _startCount, _endCount;
		private String _criteria; 
		private EducPeopleListPanel _owner;
		private PeopleAnimatedTitle _animatedPanel; 
		
		UpdateTitleAnimation(EducPeopleListPanel owner, PeopleAnimatedTitle animatedPanel) {
			_owner = owner;
			_animatedPanel = animatedPanel;
		}

		@Override
		protected void onUpdate(double progress) {
			int delta = _endCount - _startCount;
			int count = (int)(_startCount + (progress * delta)); 
			String persons = (count <= 1) ? "personne trouvée" : "personnes trouvées";

			String text = (_owner._filteringOption == null) ? null : _owner._filteringOption._filteringText;
			String pattern = StringExt.isNullOrWhitespace(text) ? 
					"{0} {1}" : 
					"{0} {1} contenant &laquo;{2}&raquo;"; 
			String html = MessageFormat.format(pattern, new Object[] {count, persons, text});
			_animatedPanel.setHTML(html);
		}
		
		@Override
		protected void onComplete() {
			_startCount = _endCount;
		    super.onComplete();
		}
		
		public void updateCount(int duration, int startCount, int endCount, String criteria) {
			_endCount = endCount;
			_criteria = criteria;
			run(duration);
		}
	}
	
	private static class AdvancedSearchPanel extends GridPanel implements ClickHandler, ChangeHandler {
		private EducPeopleListPanel _parent;
		private PeopleSearchBoxPanel _searchBoxPanel; 
		private ListBox _fieldListBox;
		private EducAnchor _simpleSearchAnchor;
			
		AdvancedSearchPanel(EducPeopleListPanel parent) {
			_parent = parent;
			int row = 0;
			
			_searchBoxPanel = new PeopleSearchBoxPanel(parent, this);
			_grid.setWidget(row, 0, _searchBoxPanel);
			
			HTML in = new HTML("dans"); 
			in.getElement().getStyle().setMarginLeft(12, Unit.PX);
			_grid.setWidget(row, 1, in);
			
			_fieldListBox = new PeopleFieldListBox();
			_fieldListBox.addChangeHandler(this);
			_grid.setWidget(row, 2, _fieldListBox);
			row++; 
			
			_simpleSearchAnchor = new EducAnchor("Retour &agrave; la recherche simple"); 
			_simpleSearchAnchor.addClickHandler(this);
			_grid.setWidget(row, 0, _simpleSearchAnchor);
			_grid.getFlexCellFormatter().setColSpan(row, 0, 3);
			row++;
		}
		
		public FilteringOption.SearchField getSearchField() {
			int idx = _fieldListBox.getSelectedIndex();
			FilteringOption.SearchField field; 
			
			if (idx == 1) {
				field = FilteringOption.SearchField.GIVEN_NAME; 
			} else if (idx == 2) {
				field = FilteringOption.SearchField.FAMILY_NAME; 
			} else if (idx == 3) {
				field = FilteringOption.SearchField.PHONE_NUMBER; 
			} else {
				field = FilteringOption.SearchField.ALL_FIELDS; 
			}

			return field;
		}

		@Override
		public void onClick(ClickEvent event) {
			Object src = event.getSource();
			
			if (src.equals(_simpleSearchAnchor)) {
				_parent._header._searchBoxPanel.setVisible(true);
				_parent._header._advancedSearchAnchor.setVisible(true);
				setVisible(false);
			}
		}

		@Override
		public void onChange(ChangeEvent event) {
			Object src = event.getSource();
			
			if (src.equals(_fieldListBox)) {
				_searchBoxPanel.filterText();
			}
		}
	}
	
	private static class PeopleFieldListBox extends EducListBox {
		PeopleFieldListBox() {
			addItem("tous les champs");
			addItem("le prénom");
			addItem("le nom de famille");
			addItem("le numéro de téléphone");
		}
	}
	
	private static class SortOptionPanel extends GridPanel implements ChangeHandler {
		private EducPeopleListPanel _parent; 
		private ListBox _criteraBox;
		private SortDirectionPanel _sortDirectionPanel;
		
		public SortOptionPanel(EducPeopleListPanel parent) {
			_parent = parent;
			_grid.setWidth("100%");
			int col = 0;
			
			SimplePanel filler = new SimplePanel(); 
			_grid.setWidget(0, col, filler);
			_grid.getFlexCellFormatter().setWidth(0, col, "90%");
			col++;
			
			HTML html = new HTML("Trier selon"); 
			html.setWordWrap(false);
			_grid.setWidget(0, col, html);
			col++;
						
			_criteraBox = new ListBox(); 
			_criteraBox.addItem("le prénom");
			_criteraBox.addItem("le nom");
			_criteraBox.addChangeHandler(this);
			_grid.setWidget(0, col, _criteraBox);
			col++;
			
			Color selectionColor = AbstractTheme.getTheme().getPrimaryFgColor();
			_sortDirectionPanel = new SortDirectionPanel(parent, selectionColor); 
			_grid.setWidget(0, col, _sortDirectionPanel);
			col++;
		}

		@Override
		public void onChange(ChangeEvent event) {
			Object src = event.getSource();
			if (_criteraBox.equals(src)) {
				_parent.refresh();
			}
		}
	}
	
	private static class SortDirectionPanel extends SelectionButtonGroup  {
		private EducPeopleListPanel _parent; 
		private SelectionButton _sortAsc, _sortDesc; 
		
		public SortDirectionPanel(EducPeopleListPanel parent, Color selectionColor) {
			super(selectionColor);
			_parent = parent;
			
			_sortAsc = new SelectionButton(this, "<i class=\"fa fa-sort-alpha-asc fa-lg\"></i>");
			_sortDesc = new SelectionButton(this, "<i class=\"fa fa-sort-alpha-desc fa-lg\"></i>");
			addItem(_sortAsc);
			addItem(_sortDesc);
			setSelectedItem(0);
		}

		@Override
		public void onSelectionChanged() {
			_parent.refresh();
		}  
	}
	
	private class Separator extends HTML {
		Separator() {
			super("<hr>"); 
		}
	}
	
	public static class PeopleListPanel extends GridPanel {
		private EducPeopleListPanel _owner;
		private HTML _title; 
		private Color _fgColor, _bgColor; 
		
		PeopleListPanel(EducPeopleListPanel owner) {
			_owner = owner;
			_grid.setCellPadding(0);
			_grid.setCellSpacing(0);
			_grid.setWidth("100%");
			getElement().getStyle().setMarginTop(12, Unit.PX);
			getElement().getStyle().setBackgroundColor("yellow");
			
			//
			_title = new HTML();
			_title.getElement().getStyle().setFontSize(140, Unit.PCT);
			EducTheme theme = (EducTheme)EducTheme.getTheme(); 
			_fgColor = theme.getPrimaryFgColor();
			_bgColor = theme.getPrimaryBgColor();
		}

		public void updateList(List<Person> page, FilteringOption option, int start, int end) {
			_grid.clear();
			int row = 0;
			Color bgColor = _bgColor.getGrayscale().blendWith(Color.WHITE, 75);
			 
			int i = 0;
			for (Person person : page) {
				PeoplePanel peoplePanel = new PeoplePanel(this, start+i, person, option, _fgColor.getGrayscale());
				Color color = (i % 2) == 0 ? Color.WHITE : bgColor;
				peoplePanel.getElement().getStyle().setBackgroundColor(color.toString());
				peoplePanel.getElement().getStyle().setBorderStyle(BorderStyle.SOLID);
				peoplePanel.getElement().getStyle().setBorderColor(_bgColor.toString());
				peoplePanel.getElement().getStyle().setProperty("borderWidth", "0px 0px 1px 0px");
				_grid.setWidget(row, 0, peoplePanel);
				row++;
				i++;
			}
		}

		public void update(Person person) {
			List<Person> persons = new ArrayList<Person>();
			persons.add(person);
			_owner.update("", persons);
		}
	}
	
	private class PageOptionPanel extends GridPanel {
		private PagerPanel _pagerPanel; 
		
		PageOptionPanel() {
			int col = 0;
			_grid.setWidth("100%"); 
			
			PageSizeOptionPanel pageSizeOptionPanel = new PageSizeOptionPanel(); 
			_grid.setWidget(0, col, pageSizeOptionPanel);
			col++; 
			
			SimplePanel filler = new SimplePanel();
			_grid.setWidget(0, col, filler);
			_grid.getFlexCellFormatter().setWidth(0, col, "90%");
			col++; 
			
			_pagerPanel = new PagerPanel(); 
			_grid.setWidget(0, col, _pagerPanel);
			col++; 
		}

		public void setNbPages(int count) {
			_pagerPanel.setNbPages(count);
		}
	}
	
	private static final int[] PAGE_SIZES = new int[] {5, 10, 15, 25, 50};
	private class PageSizeOptionPanel extends GridPanel implements ChangeHandler {
		
		private ListBox _nbItemsPerPage; 
		
		PageSizeOptionPanel() {
			int col = 0;
			
			HTML label = new HTML("Afficher"); 
			label.getElement().getStyle().setFontSize(140, Unit.PCT);
			_grid.setWidget(0, col, label);
			col++; 
			
			_nbItemsPerPage = new ListBox(); 
			for (int size : PAGE_SIZES) {
				_nbItemsPerPage.addItem(Integer.toString(size));
			}
			
			_nbItemsPerPage.getElement().getStyle().setFontSize(140, Unit.PCT);
			_nbItemsPerPage.addChangeHandler(this); 
			_grid.setWidget(0, col, _nbItemsPerPage);
			col++; 
			
			label = new HTML("directeurs par page"); 
			label.setWordWrap(false);
			label.getElement().getStyle().setFontSize(140, Unit.PCT);
			_grid.setWidget(0, col, label);
			col++; 
		}

		@Override
		public void onChange(ChangeEvent event) {
			int idx = _nbItemsPerPage.getSelectedIndex(); 
			_pageSize = PAGE_SIZES[idx]; 
			refresh();
		}
	}
	
	private class PagerPanel extends GridPanel implements ChangeHandler, ClickHandler {
		private Button _firstBtn, _previousBtn, _nextBtn, _lastBtn;
		private ListBox _currentPageBox; 
		private int _nbPages;
		
		PagerPanel() {
			int col = 0;
			
			_firstBtn = new PagerButton("<i class=\"fa fa-step-backward\"></i>", "Premier");
			_firstBtn.getElement().getStyle().setFontSize(100, Unit.PCT);
			_firstBtn.addClickHandler(this);
			_grid.setWidget(0, col, _firstBtn);
			col++; 
			
			_previousBtn = new PagerButton("<i class=\"fa fa-play fa-rotate-180\"></i>", "Précédent");
			_previousBtn.getElement().getStyle().setFontSize(100, Unit.PCT);
			_previousBtn.addClickHandler(this);
			_grid.setWidget(0, col, _previousBtn);
			col++; 
			
			_currentPageBox = new ListBox();
			_currentPageBox.getElement().getStyle().setFontSize(120, Unit.PCT);
			_currentPageBox.addChangeHandler(this);
			_grid.setWidget(0, col, _currentPageBox);
			col++; 
			
			_nextBtn = new PagerButton("<i class=\"fa fa-play\"></i>", "Suivant");
			_nextBtn.getElement().getStyle().setFontSize(100, Unit.PCT);
			_nextBtn.addClickHandler(this);
			_grid.setWidget(0, col, _nextBtn);
			col++; 
			
			_lastBtn = new PagerButton("<i class=\"fa fa-step-forward\"></i>", "Dernier");
			_lastBtn.getElement().getStyle().setFontSize(100, Unit.PCT);
			_lastBtn.addClickHandler(this);
			_grid.setWidget(0, col, _lastBtn);
			col++; 
		}

		public void setNbPages(int count) {
			int nbPages = (int)Math.ceil(count / (double)_pageSize);
			
			if (nbPages != _nbPages) {
				_nbPages = nbPages;
				_currentPageBox.clear();
				
				for (int i=0; i<nbPages; i++) {
					String item = MessageFormat.format("Page {0}", i+1);
					String value = Integer.toString(i);
					_currentPageBox.addItem(item, value);
				}
			}
		}

		@Override
		public void onChange(ChangeEvent event) {
			int idx = _currentPageBox.getSelectedIndex();
			_currentPage = idx;
			refresh();
		}

		@Override
		public void onClick(ClickEvent event) {
			Object src = event.getSource();
			int idx = _currentPageBox.getSelectedIndex();
			int len = _currentPageBox.getItemCount();
			
			if (src.equals(_firstBtn)) {
				idx = 0;
			} else if (src.equals(_previousBtn)) {
				idx = (idx == 0) ? 0 : idx-1;
			} else if (src.equals(_nextBtn)) {
				idx = (idx >= len-1) ? len-1 : idx+1;
			} else if (src.equals(_lastBtn)) {
				idx = len -1;
			}
			
			_currentPageBox.setItemSelected(idx, true);
			_currentPage = idx;
			refresh();
		}
	}
	
	private static class PagerButton extends AbstractIconButton {
		private Color _primaryColor; 

		public PagerButton(String html, String title) {
			super(html, title); 
			
			_primaryColor = AbstractTheme.getTheme().getPrimaryFgColor();
			getElement().getStyle().setBorderWidth(2, Unit.PX);
			getElement().getStyle().setBorderStyle(BorderStyle.SOLID);
			refresh();
		}
		
		@Override
		public void onMouseOver(MouseOverEvent event) {
			super.onMouseOver(event);
			getElement().getStyle().setBackgroundColor(_primaryColor.toString());
			getElement().getStyle().setColor(Color.WHITE.toString());
			getElement().getStyle().setBorderColor(_primaryColor.toString());
		}
		
		@Override
		public void onMouseOut(MouseOutEvent event) {
			super.onMouseOut(event);
			refresh();
		}
		
		private void refresh() {
			getElement().getStyle().setBackgroundColor(Color.WHITE.toString());
			getElement().getStyle().setColor(Color.BLACK.toString());
			getElement().getStyle().setBorderColor(Color.BLACK.toString());
		}
	}
	
	private static class PersonComparator implements Comparator<Person> {
		private AlphabeticComparator _comparator = new AlphabeticComparator();
		private int _criteria, _order; 

		@Override
		public int compare(Person p1, Person p2) {
			String s1 = (_criteria == 0) ? p1.getGivenName() : p1.getFamilyName();
			String s2 = (_criteria == 0) ? p2.getGivenName() : p2.getFamilyName();
			int comparison = _comparator.compare(s1, s2); 
			comparison = (_order == 0) ? comparison : -comparison;
			return comparison;
		}

		public void setOrder(int order) {
			_order = order;
		}

		public void setCriteria(int criteria) {
			_criteria = criteria;
		}		
	}

}
