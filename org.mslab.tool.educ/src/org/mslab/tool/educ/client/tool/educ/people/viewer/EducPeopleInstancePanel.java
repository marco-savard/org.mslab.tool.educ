package org.mslab.commons.client.tool.educ.people.viewer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Comparator;

import org.mslab.commons.client.core.ui.StyleUtil;
import org.mslab.commons.client.core.ui.panels.GridPanel;
import org.mslab.commons.client.tool.educ.EducAnchor;
import org.mslab.commons.client.tool.educ.EducContext;
import org.mslab.commons.client.tool.educ.EducRouting;
import org.mslab.commons.client.tool.educ.EducShell;
import org.mslab.commons.client.tool.educ.EducTheme;
import org.mslab.commons.client.tool.educ.people.viewer.content.PersonRenderer;
import org.mslab.commons.shared.types.Color;
import org.mslab.commons.shared.types.educ.Organization;
import org.mslab.commons.shared.types.educ.Person;
import org.mslab.commons.shared.types.educ.School;
import org.mslab.commons.shared.types.educ.SchoolBoard;

import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.FontStyle;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.SimplePanel;

public class EducPeopleInstancePanel extends GridPanel {
	private EducPeopleDeckPanel _owner;
	private PersonInfoPane _infoPane;
	private HTML _personName, _title, _phone, _email, _superiorLbl; 
	private OrganizationAnchor _school;
	private PersonAnchor _supervisor;
	private TeamPanel _teamPanel;
	
	EducPeopleInstancePanel(EducPeopleDeckPanel owner) {
		_owner = owner;
		int col = 0;
		getElement().getStyle().setMarginTop(12, Unit.PX);
		getElement().getStyle().setMarginLeft(12, Unit.PX);
		getElement().getStyle().setMarginRight(12, Unit.PX);
		
		EducTheme theme = (EducTheme)EducTheme.getTheme();
		Color fgColor = theme.getPrimaryFgColor();
		
		PersonLargeIcon icon = new PersonLargeIcon(fgColor.getGrayscale()); 
		_grid.setWidget(0, col, icon);
		_grid.getFlexCellFormatter().setVerticalAlignment(0, col, HasVerticalAlignment.ALIGN_TOP);
		col++;
		
		_infoPane = new PersonInfoPane(); 
		_grid.setWidget(0, col, _infoPane);
		col++;
	}

	public void update(Person person) {
	}
	
	public void refresh() {
		//get data
		List<Person> persons = _owner.getPersons();
		Person person = persons.get(0);
		Organization organization = person.getOrganization();
		
		//renderer
		PersonRenderer renderer = new PersonRenderer(person); 
		String fullName = renderer.render(PersonRenderer.Field.FULL_NAME); 
		_personName.setHTML(fullName);
		
		String title = renderer.render(PersonRenderer.Field.TITLE); 
		_title.setHTML(title);
		
		_school.setOrganization(organization);
		
		String phone = renderer.render(PersonRenderer.Field.PHONE);
		_phone.setHTML(phone);
		
		String email = renderer.render(PersonRenderer.Field.EMAIL);
		_email.setHTML(email);
		
		String supervisorName = ""; 
		
		if (organization instanceof School) {
			School school = (School)organization;
			SchoolBoard board = school.getSchoolBoard();
			Person supervisor = board.getDirector();
			_supervisor.setPerson(supervisor);
		} else if (organization instanceof SchoolBoard) {
			SchoolBoard board = (SchoolBoard)organization;
			_teamPanel.setSchoolBoard(board); 
		}
		
		_superiorLbl.setVisible(organization instanceof School);
		_supervisor.setVisible(organization instanceof School);
		_teamPanel.setVisible(organization instanceof SchoolBoard);
	}
	
	private static class PersonLargeIcon extends SimplePanel {
		PersonLargeIcon(Color iconColor) {
			String text = "<i class=\"fa fa-user fa-5x\" aria-hidden=\"true\" ></i>";
			HTML html = new HTML(text);
			html.getElement().getStyle().setFontSize(150, Unit.PCT);
			html.getElement().getStyle().setColor(iconColor.toString());
			html.getElement().getStyle().setBorderStyle(BorderStyle.SOLID);
			html.getElement().getStyle().setBorderWidth(1, Unit.PX);
			html.getElement().getStyle().setBorderColor(iconColor.toString());
			html.getElement().getStyle().setPadding(2, Unit.PX);
			StyleUtil.setBorderRadius(html, "5px");
			add(html);
		}
	}
	
	private class PersonInfoPane extends GridPanel implements ClickHandler {
		
		PersonInfoPane() {
			int row = 0;
			_personName = new HTML(); 
			_personName.getElement().getStyle().setFontSize(150, Unit.PCT);
			_grid.setWidget(row, 0, _personName);
			_grid.getFlexCellFormatter().setColSpan(row, 0, 2);
			row++;
			
			_title = new HTML();
			_title.setWordWrap(false);
			_grid.setWidget(row, 0, _title);
			
			_school = new OrganizationAnchor();
			_school.addClickHandler(this); 
			_grid.getFlexCellFormatter().setWidth(row, 1, "90%");
			_grid.setWidget(row, 1, _school);
			row++;
			
			_phone = new HTML();
			_phone.getElement().getStyle().setMarginTop(12, Unit.PX);
			_grid.setWidget(row, 0, _phone);
			_grid.getFlexCellFormatter().setColSpan(row, 0, 2);
			row++;
			
			_email = new HTML();
			_email.getElement().getStyle().setMarginBottom(12, Unit.PX);
			_grid.setWidget(row, 0, _email);
			_grid.getFlexCellFormatter().setColSpan(row, 0, 2);
			row++;
			
			_superiorLbl = new HTML("Rel&egrave;ve de :");
			_superiorLbl.setWordWrap(false);
			_grid.setWidget(row, 0, _superiorLbl);
			
			_supervisor = new PersonAnchor();
			_supervisor.addClickHandler(this); 
			_grid.setWidget(row, 1, _supervisor);
			row++;
			
			_teamPanel = new TeamPanel();
			_grid.setWidget(row, 0, _teamPanel);
			_grid.getFlexCellFormatter().setColSpan(row, 0, 2);
			row++;
		}

		@Override
		public void onClick(ClickEvent event) {
			Object src = event.getSource();
			
			if (src.equals(_school)) { 
				Organization organization = _school.getOrganization();
				EducContext context = EducContext.getInstance(); 
				EducRouting routing = context.getEducRouting();
				routing.routeOrganization(organization);

			} else if (src.equals(_supervisor)) {
				Person person = _supervisor.getPerson();
				EducContext context = EducContext.getInstance(); 
				EducRouting routing = context.getEducRouting();
				routing.routePerson(person); 
				
				//List<Person> persons = new ArrayList<Person>();
				//persons.add(person);
				//_owner.update("", persons);
			}
		}
	}
	
	private class TeamPanel extends GridPanel {
		private static final int NB_COLS = 3; 
		
		TeamPanel() {
			
		}

		public void setSchoolBoard(SchoolBoard board) {
			_grid.clear(true);
			int i = 0; 
			
			HTML title = new HTML("En charge de:"); 
			title.getElement().getStyle().setFontSize(120, Unit.PCT);
			_grid.setWidget(i, 0, title);
			_grid.getFlexCellFormatter().setColSpan(i, 0, NB_COLS);
			i += NB_COLS;
			
			List<School> schools = board.getSchools();
			SchoolComparator schoolComparator = new SchoolComparator(); 
			Collections.sort(schools, schoolComparator);
			
			for (School school : schools) {
				PersonPanel directorPanel = new PersonPanel(); 
				directorPanel.setSchool(school);		
				
				int row = i / NB_COLS;
				int col = i % NB_COLS;
				_grid.setWidget(row, col, directorPanel);
				i++;
			}
			
		}
	}
	
	private class OrganizationAnchor extends EducAnchor {
		private Organization _organization; 
		
		public OrganizationAnchor() {
		}

		public Organization getOrganization() {
			return _organization;
		}

		public void setOrganization(Organization organization) {
			_organization = organization;
			
			String text = (organization == null) ? "" : organization.getName();
			setHTML(text);
		}
	}
	
	private class PersonPanel extends GridPanel implements ClickHandler {
		private PersonAnchor _personAnchor;
		private HTML _school;
		
		PersonPanel() {
			_personAnchor = new PersonAnchor(); 
			_personAnchor.addClickHandler(this);
			_school = new HTML();
			_school.getElement().getStyle().setFontStyle(FontStyle.ITALIC);
			
			_grid.setWidget(0, 0, _personAnchor);
			_grid.setWidget(1, 0, _school);
		}

		public void setSchool(School school) {
			Person director = school.getDirector();
			_personAnchor.setPerson(director);
			
			String html = school.getName();
			_school.setHTML(html);
		}

		@Override
		public void onClick(ClickEvent event) {
			Object src = event.getSource();
			
			if (src.equals(_personAnchor)) {
				Person person = _personAnchor.getPerson();
				EducRouting routing = EducContext.getInstance().getEducRouting();
				routing.routePerson(person);
			}
		}
	}
	
	private class PersonAnchor extends EducAnchor {
		private Person _person; 
		
		public PersonAnchor() {
		}

		public Person getPerson() {
			return _person;
		}

		public void setPerson(Person person) {
			_person = person;
			
			PersonRenderer supervisorRenderer = new PersonRenderer(person); 
			String personName = supervisorRenderer.render(PersonRenderer.Field.FULL_NAME); 
			setHTML(personName); 
		}
	}
	
	private class SchoolComparator implements Comparator<School> {

		@Override
		public int compare(School s1, School s2) {
			Person p1 = s1.getDirector(); 
			Person p2 = s2.getDirector(); 
			int comparison = p1.getFamilyName().compareTo(p2.getFamilyName()); 
			return comparison;
		}
		
	}
	
	






}
