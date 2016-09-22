package org.mslab.tool.educ.client.tool.educ.school.viewer.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mslab.tool.educ.client.core.ui.StyleUtil;
import org.mslab.tool.educ.client.core.ui.panels.GridPanel;
import org.mslab.tool.educ.client.core.ui.theme.ThemeChangeEvent;
import org.mslab.tool.educ.client.core.ui.theme.ThemeChangeHandler;
import org.mslab.tool.educ.client.tool.educ.EducAnchor;
import org.mslab.tool.educ.client.tool.educ.EducContext;
import org.mslab.tool.educ.client.tool.educ.EducRouting;
import org.mslab.tool.educ.client.tool.educ.EducTheme;
import org.mslab.tool.educ.client.tool.educ.people.viewer.content.PersonRenderer;
import org.mslab.tool.educ.shared.text.MessageFormat;
import org.mslab.tool.educ.shared.types.Color;
import org.mslab.tool.educ.shared.types.educ.OrdreAppartenance;
import org.mslab.tool.educ.shared.types.educ.Organization;
import org.mslab.tool.educ.shared.types.educ.Person;
import org.mslab.tool.educ.shared.types.educ.School;
import org.mslab.tool.educ.shared.types.educ.SchoolBoard;

import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;

public class OrganizationInstanceViewer extends GridPanel {
	private SimplePanel _organizationIcon;
	private BasicInfoPanel _basicInfoPanel; 
	private AddressPanel _addressPanel; 
	private DirectionPanel _directionPanel; 
	private SchoolListPanel _schoolsPanel; 
	
	public OrganizationInstanceViewer() {
		int row = 0; 
		_organizationIcon = new SimplePanel(); 
		_organizationIcon.getElement().getStyle().setMargin(24, Unit.PX);
		_grid.setWidget(row, 0, _organizationIcon);
		_grid.getFlexCellFormatter().setRowSpan(row, 0, 6);
		_grid.getFlexCellFormatter().setVerticalAlignment(row, 0, HasVerticalAlignment.ALIGN_TOP);
		
		_basicInfoPanel = new BasicInfoPanel();
		_grid.setWidget(row, 1, _basicInfoPanel);
		row++;
		
		_addressPanel = new AddressPanel();
		_addressPanel.getElement().getStyle().setMarginTop(24, Unit.PX);
		_grid.setWidget(row, 0, _addressPanel);
		row++;
		
		_directionPanel = new DirectionPanel();
		_directionPanel.getElement().getStyle().setMarginTop(24, Unit.PX);
		_grid.setWidget(row, 0, _directionPanel);
		row++;
		
		_schoolsPanel = new SchoolListPanel();
		_schoolsPanel.getElement().getStyle().setMarginTop(24, Unit.PX);
		_grid.setWidget(row, 0, _schoolsPanel);
		row++;
	}

	public void refresh() {
		
	}

	public void update(Organization organization) {
		
		OrganizationIcon icon = new OrganizationIcon(organization);
		_organizationIcon.setWidget(icon);
		
		_basicInfoPanel.update(organization);
		_addressPanel.update(organization);
		_directionPanel.update(organization);
		_schoolsPanel.update(organization);
	}
	
	//
	// inner classes
	//
	private static class OrganizationIcon extends GridPanel implements ThemeChangeHandler {		
		OrganizationIcon(Organization organization) {
			if (organization instanceof SchoolBoard) {
				drawSchoolBoard(); 
			} else {
				drawSchool(); 
			}
			
			getElement().getStyle().setBorderStyle(BorderStyle.SOLID);
			getElement().getStyle().setBorderWidth(1, Unit.PX);
			getElement().getStyle().setPadding(2, Unit.PX);
			StyleUtil.setBorderRadius(this, "5px");
			
			EducTheme.getTheme().addThemeChangeHandler(this);
			refresh();
		}
		
		private void drawSchool() {
			String text = "<i class=\"fa fa-university fa-5x\" aria-hidden=\"true\" ></i>";
			HTML html = new HTML(text);
			html.getElement().getStyle().setFontSize(200, Unit.PCT);
			_grid.setWidget(0, 0, html);
			_grid.getFlexCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
		}

		private void drawSchoolBoard() {
			String text = "<i class=\"fa fa-university fa-4x\" aria-hidden=\"true\" ></i>";
			HTML html = new HTML(text);
			_grid.setWidget(0, 0, html);
			_grid.getFlexCellFormatter().setColSpan(0, 0, 2);
			_grid.getFlexCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
			
			html = new HTML(text);
			_grid.setWidget(1, 0, html);
			
			html = new HTML(text);
			_grid.setWidget(1, 1, html);
		}

		@Override
		public void onThemeChange(ThemeChangeEvent event) {
			refresh();
		}
		
		private void refresh() {
			Color color = EducTheme.getTheme().getPrimaryFgColor().getGrayscale();
			getElement().getStyle().setColor(color.toString());
			getElement().getStyle().setBorderColor(color.toString());
		}
	}
	
	private class BasicInfoPanel extends GridPanel implements ClickHandler {
		private HTML _nameLbl, _levelLbl, _networkLbl, _languageLbl;
		private OrganizationAnchor _schoolBoardLbl;
		
		BasicInfoPanel() {
			int row = 0;
			
			_nameLbl = new EducLabel("");
			_nameLbl.getElement().getStyle().setFontSize(200, Unit.PCT);
			_grid.setWidget(row, 0, _nameLbl);
			_grid.getFlexCellFormatter().setColSpan(row, 0, 2);
			row++;
			
			Label boardLbl = new HTML("Fait partie de :");
			boardLbl.setWordWrap(false);
			_grid.setWidget(row, 0, boardLbl);
			
			_schoolBoardLbl = new OrganizationAnchor();
			_schoolBoardLbl.addClickHandler(this); 
			_grid.setWidget(row, 1, _schoolBoardLbl);
			row++;
			
			Label levelLbl = new HTML("Niveau :");
			_grid.setWidget(row, 0, levelLbl);
			
			_levelLbl = new HTML();
			_grid.setWidget(row, 1, _levelLbl);
			row++;
			
			Label networkLbl = new HTML("R&eacute;seau :");
			_grid.setWidget(row, 0, networkLbl);

			_networkLbl = new HTML();
			_grid.setWidget(row, 1, _networkLbl);
			row++;
			
			Label languageLbl = new HTML("Langue :");
			_grid.setWidget(row, 0, languageLbl);

			_languageLbl = new HTML();
			_grid.setWidget(row, 1, _languageLbl);
			row++;
			
			SimplePanel filler = new SimplePanel();
			_grid.getFlexCellFormatter().setHeight(row, 0, "95%");
			_grid.setWidget(row, 0, filler);
			row++;
		}

		public void update(Organization organization) {
			String name = organization.getName();
			_nameLbl.setText(name);
		
			if (organization instanceof School) {
				School school = (School)organization;
				SchoolBoard board = school.getSchoolBoard();
				_schoolBoardLbl.setOrganization(board);
				
				OrdreAppartenance ordre = school.getOrdreAppartenance();
				_levelLbl.setHTML(ordre.getName());
			}

			String network = organization.isPublic() ? "Public" : "Priv&eacute;"; 
			_networkLbl.setHTML(network);
			
			String language = organization.isFrench() ? "Fran&ccedil;ais" : "Anglais"; 
			_languageLbl.setHTML(language);
		}

		@Override
		public void onClick(ClickEvent event) {
			Object src = event.getSource();
			
			if (src.equals(_schoolBoardLbl)) {
				Organization organization = _schoolBoardLbl.getOrganization();
				EducContext context = EducContext.getInstance();
				EducRouting routing = context.getEducRouting();
				routing.routeOrganization(organization);
			}
			
		}
	}
	
	private static class AddressPanel extends GridPanel {
		private HTML _addressLine1, _addressLine2;
		
		AddressPanel() {
			int row = 0;
			
			_addressLine1 = new HTML();
			_addressLine1.getElement().getStyle().setMarginBottom(2, Unit.PX);
			_grid.setWidget(row, 0, _addressLine1);
			row++;
			
			_addressLine2 = new HTML();
			_addressLine2.getElement().getStyle().setMarginBottom(2, Unit.PX);
			_grid.setWidget(row, 0, _addressLine2);
			row++;
		}

		public void update(Organization organization) {
			OrganizationRenderer renderer = new OrganizationRenderer(organization); 
			
			String line1 = renderer.render(OrganizationRenderer.Field.ADDRESS_LINE_1);
			_addressLine1.setHTML(line1);
			
			String line2 = renderer.render(OrganizationRenderer.Field.ADDRESS_LINE_2);
			_addressLine2.setHTML(line2);
			
			//String postalCode = renderer.render(OrganizationRenderer.Field.POSTAL_CODE);
			//_postalCode.setHTML(postalCode);
			
		}
	}
	
	private static class DirectionPanel extends GridPanel {
		private DirectorAndTitle _directorAndTitle;
		private HTML _phoneLbl, _faxLbl;
		
		DirectionPanel() {
			int row = 0;
			_directorAndTitle = new DirectorAndTitle();
			_grid.setWidget(row, 0, _directorAndTitle);
			row++;
			
			_phoneLbl = new HTML();
			_grid.setWidget(row, 0, _phoneLbl);
			row++;
			
			_faxLbl = new HTML();
			_grid.setWidget(row, 0, _faxLbl);
			row++;
		}

		public void update(Organization organization) {
			Person person = organization.getDirector();
			_directorAndTitle.setPerson(person);
			 			
			OrganizationRenderer renderer = new OrganizationRenderer(organization); 
			String phoneNumberAndExtension = renderer.render(OrganizationRenderer.Field.PHONE_NUMBER_EXT);
			_phoneLbl.setHTML(phoneNumberAndExtension);
			
			String fax = renderer.render(OrganizationRenderer.Field.FAX);
			_faxLbl.setHTML(fax);
		}
	}
	
	private static class DirectorAndTitle extends GridPanel implements ClickHandler {
		private PersonAnchor _personAnchor;
		private HTML _title;
		
		DirectorAndTitle() {
			_grid.setCellPadding(0);
			_grid.setCellSpacing(0);
			
			_personAnchor = new PersonAnchor();
			_personAnchor.setWordWrap(false);
			_personAnchor.addClickHandler(this);
			_grid.setWidget(0, 0, _personAnchor);
			
			_title = new HTML();
			_title.setWordWrap(false);
			_grid.setWidget(0, 1, _title);
		}

		public void setPerson(Person person) {
			_personAnchor.setPerson(person);
			
			String html = MessageFormat.format(", {0}", person.getTitle());
			_title.setHTML(html);
		}

		@Override
		public void onClick(ClickEvent event) {
			Object src = event.getSource();
			
			if (src.equals(_personAnchor)) {
				Person person = _personAnchor.getPerson();
				EducContext context = EducContext.getInstance();
				EducRouting routing = context.getEducRouting();
				routing.routePerson(person);
				
				//EducShell shell = EducShell.getInstance(); 
				//shell.displayPerson(person);
			}
		}
	}
	
	private static class PersonAnchor extends EducAnchor {
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
	
	private class SchoolListPanel extends GridPanel implements ClickHandler {
		private static final int NB_COLS = 3;
		
		SchoolListPanel() {
			
		}

		public void update(Organization organization) {
			SchoolBoard board = (organization instanceof SchoolBoard) ? (SchoolBoard)organization : null;
			setVisible(board != null);
			
			if (board != null) {
				List<School> schools = board.getSchools();
				Map<String, List<School>> schoolsByOrdres = new HashMap<String, List<School>>();
				
				for (School school : schools) {
					OrdreAppartenance ordre = school.getOrdreAppartenance();
					String ordreText = ordre.getName();
					
					if (! schoolsByOrdres.containsKey(ordreText)) {
						schoolsByOrdres.put(ordreText, new ArrayList<School>());
					}
					
					List<School> schoolsInOrder = schoolsByOrdres.get(ordreText); 
					schoolsInOrder.add(school); 
				}
				
				addOrdres(schoolsByOrdres); 
			}
		}

		private void addOrdres(Map<String, List<School>> schoolsByOrdres) {
			_grid.clear(true);
			_grid.removeAllRows();
			Color fgColor = EducTheme.getTheme().getPrimaryFgColor();
			
			List<String> ordres = new ArrayList<String>(); 
			ordres.addAll(schoolsByOrdres.keySet());
			Collections.sort(ordres, new OrderComparator(schoolsByOrdres));
			
			int row = 0;
			
			for (String ordre : ordres) {
				String name = ordre;
				Label ordreLbl = new HTML(name); 
				ordreLbl.getElement().getStyle().setFontSize(140, Unit.PCT);
				ordreLbl.getElement().getStyle().setMarginTop(12, Unit.PX);
				_grid.setWidget(row, 0, ordreLbl);
				_grid.getFlexCellFormatter().setColSpan(row, 0, NB_COLS);
				row++; 
				
				List<School> schoolsInOrder = schoolsByOrdres.get(ordre); 
				row = addSchools(row, schoolsInOrder, fgColor);
			}
		}

		private int addSchools(int row, List<School> schoolsInOrder, Color fgColor) {
			int nb = schoolsInOrder.size();
			int nbRows = 1 + (nb / NB_COLS);
			
			for (int i=0; i<nb; i++) {
				School school = schoolsInOrder.get(i); 
				int r = (i / NB_COLS); 
				int c = (i % NB_COLS); 
				String name = school.getName();
				
				OrganizationAnchor schoolLbl = new OrganizationAnchor();
				schoolLbl.setOrganization(school);
				schoolLbl.addClickHandler(this);
				
				_grid.setWidget(row + r, c, schoolLbl);
			}
			
			return row + nbRows;
		}

		@Override
		public void onClick(ClickEvent event) {
			Object src = event.getSource(); 
			
			if (src instanceof OrganizationAnchor) {
				OrganizationAnchor anchor = (OrganizationAnchor)src;
				Organization org = anchor.getOrganization();
				EducContext.getInstance().getEducRouting().routeOrganization(org);
			}
		}
	}
	
	private static class EducLabel extends HTML {
		public EducLabel(String text) {
			super(text);
			refresh();
		}
		
		private void refresh() {
			EducTheme theme = (EducTheme)EducTheme.getTheme(); 
			Color color = theme.getPrimaryFgColor().getGrayscale();
			String family = theme.getFontFamily();
			
			getElement().getStyle().setColor(color.toString());
			getElement().getStyle().setProperty("fontFamily", family);
			getElement().getStyle().setFontSize(300, Unit.PCT);
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
			setText(text);
		}
	}
	
	private class OrderComparator implements Comparator<String> {
		private Map<String, List<School>> _schoolsByOrdres; 

		public OrderComparator(Map<String, List<School>> schoolsByOrdres) {
			_schoolsByOrdres = schoolsByOrdres;
		}

		@Override
		public int compare(String o1, String o2) {
			List<School> l1 = _schoolsByOrdres.get(o1); 
			List<School> l2 = _schoolsByOrdres.get(o2); 
			int comparison = l2.size() - l1.size();
			return comparison;
		}
		
	}
}
