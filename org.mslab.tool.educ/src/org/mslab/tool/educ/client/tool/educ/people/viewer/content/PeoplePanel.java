package org.mslab.tool.educ.client.tool.educ.people.viewer.content;

import org.mslab.tool.educ.client.core.ui.RangeHTML;
import org.mslab.tool.educ.client.core.ui.panels.GridPanel;
import org.mslab.tool.educ.client.tool.educ.EducShell;
import org.mslab.tool.educ.client.tool.educ.people.viewer.EducPeopleListPanel.PeopleListPanel;
import org.mslab.tool.educ.client.tool.educ.people.viewer.FilteringOption;
import org.mslab.tool.educ.client.tool.educ.people.viewer.content.PersonRenderer.Field;
import org.mslab.tool.educ.client.tool.educ.ui.RangeAnchor;
import org.mslab.tool.educ.shared.text.StringExt;
import org.mslab.tool.educ.shared.types.Color;
import org.mslab.tool.educ.shared.types.educ.Organization;
import org.mslab.tool.educ.shared.types.educ.Person;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.WhiteSpace;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;

public class PeoplePanel extends GridPanel implements ClickHandler {
	private PeopleListPanel _owner;
	private RangeAnchor<Person> _personAnchor; 
	
	public PeoplePanel(PeopleListPanel owner, int pos, Person person, FilteringOption filteringOption, Color colorIcon) {
		_owner = owner;
		_grid.setCellPadding(0);
		_grid.setCellSpacing(0);
		this.getElement().getStyle().setPaddingLeft(24, Unit.PX);
		this.getElement().getStyle().setPaddingTop(6, Unit.PX);
		this.getElement().getStyle().setPaddingBottom(6, Unit.PX);
		
		PersonRenderer renderer = new PersonRenderer(person); 
		Color selectionColor = Color.YELLOW;
		int row = 0, col = 0;
		
		FilteringOption.SearchField searchField = (filteringOption == null) ? 
				FilteringOption.SearchField.ALL_FIELDS : filteringOption._searchField; 
		
		// icon
		PersonIcon icon = new PersonIcon(colorIcon); 
		icon.getElement().getStyle().setMarginRight(12, Unit.PX);
		_grid.setWidget(row, col, icon);
		_grid.getFlexCellFormatter().setRowSpan(row, col, 2);
		_grid.getFlexCellFormatter().setWidth(row, col, "5%");
		col++; row = 0;
		
		// name 
		String text = renderer.render(Field.FULL_NAME); 
		String filteringText = (filteringOption == null) ? null : filteringOption._filteringText;
		int idx = findMatch(renderer, Field.FULL_NAME, filteringOption);
		int len = (filteringText == null) ? 0 : filteringText.length();
		_personAnchor = new RangeAnchor<Person>(person, selectionColor);
		_personAnchor.setRangeHTML(text, idx, len);
		_personAnchor.getElement().getStyle().setFontWeight(FontWeight.BOLD);
		_personAnchor.addClickHandler(this);
		_grid.setWidget(row, col, _personAnchor);
		_grid.getFlexCellFormatter().setWidth(row, col, "45%");
		row++; 
		
		//title
		TitleAndOrganizationPanel titleOrg = new TitleAndOrganizationPanel(person, filteringOption);
		_grid.setWidget(row, col-1, titleOrg);
		col++; row = 0;
		
		//contact
		boolean filterPhone = (searchField == FilteringOption.SearchField.PHONE_NUMBER);
		filterPhone |= (searchField == FilteringOption.SearchField.ALL_FIELDS);
		text = renderer.render(Field.PHONE);  
		RangeHTML line4 = new RangeHTML(selectionColor.toString());
		
		idx = filterPhone ? findMatch(renderer, Field.PHONE, filteringOption) : -1;
		line4.setRangeHTML(text, idx, len);
		_grid.setWidget(row, col, line4);
		_grid.getFlexCellFormatter().setWidth(row, col, "50%");
		row++; 
		
		text = renderer.render(Field.EMAIL);  
		boolean filterEmail = (searchField == FilteringOption.SearchField.ALL_FIELDS);
		
		if (! StringExt.isNullOrWhitespace(text)) {
			idx = filterEmail ? findMatch(renderer, Field.EMAIL, filteringOption) : -1; 
			RangeHTML line5 = new RangeHTML(selectionColor.toString());
			line5.setRangeHTML(text, idx, len);
			_grid.setWidget(row, col-1, line5);
			row++; 
		}
	}
	
	private int findMatch(PersonRenderer renderer, Field field, FilteringOption filteringOption) {
		String text = (filteringOption == null) ? null : filteringOption._filteringText; 
		int idx = (text == null) ? -1 : renderer.indexOf(field, text); 
		return idx;
	}

	@Override
	public void onClick(ClickEvent event) {
		Object src = event.getSource();
		
		if (src.equals(_personAnchor)) {
			Person person = _personAnchor.getItem();
			_owner.update(person);
		}
	}
	
	private static class TitleAndOrganizationPanel extends GridPanel implements ClickHandler {
		private RangeAnchor<Organization> _organizationAnchor; 
		
		TitleAndOrganizationPanel(Person person, FilteringOption filteringOption) {
			_grid.setCellPadding(0);
			_grid.setCellSpacing(0);
			String filterText = (filteringOption == null) ? null : filteringOption._filteringText; 
			
			PersonRenderer renderer = new PersonRenderer(person); 
			Organization organization = person.getOrganization(); 
			Color selectionColor = Color.YELLOW;
			int col = 0; 
			
			boolean filterTitle = (filteringOption != null) && (filteringOption._searchField == FilteringOption.SearchField.ALL_FIELDS);
			String title = renderer.render(Field.TITLE); 
			RangeHTML line2 = new RangeHTML(selectionColor.toString());
			
			if (filterTitle) {
				int idx = (filterText == null) ? -1 : renderer.indexOf(Field.TITLE, filterText); 
				int len = (filterText == null) ? 0 : filterText.length();
				line2.setRangeHTML(title, idx, len);
			} else {
				line2.setRangeHTML(title);
			}
			
			_grid.setWidget(0, col, line2);
			col++;

			String school = renderer.render(Field.SCHOOL); 
			_organizationAnchor = new RangeAnchor<Organization>(organization, selectionColor);
			
			if (filterTitle) {
			    int idx = renderer.indexOf(Field.SCHOOL, filterText); 
				int len = (filterText == null) ? 0 : filterText.length();
				_organizationAnchor.setRangeHTML(school, idx, len);
			} else {
				_organizationAnchor.setRangeHTML(school);
			}
			
			_organizationAnchor.addClickHandler(this);
			_organizationAnchor.setWordWrap(false);
			_grid.setWidget(0, col, _organizationAnchor);
			_grid.getFlexCellFormatter().setWidth(0, col, "90%");
			col++; 
		}

		@Override
		public void onClick(ClickEvent event) {
			Object src = event.getSource();
			
			if (src.equals(_organizationAnchor)) {
				Organization organization = _organizationAnchor.getItem();
				EducShell shell = EducShell.getInstance(); 
				shell.displayOrganization(organization);
			}
		}
	}
	
	


}