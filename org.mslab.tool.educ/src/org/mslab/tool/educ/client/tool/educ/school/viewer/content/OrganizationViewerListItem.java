package org.mslab.commons.client.tool.educ.school.viewer.content;

import java.util.ArrayList;
import java.util.List;

import org.mslab.commons.client.core.ui.RangeHTML;
import org.mslab.commons.client.core.ui.StyleUtil;
import org.mslab.commons.client.core.ui.panels.GridPanel;
import org.mslab.commons.client.core.ui.theme.ThemeChangeEvent;
import org.mslab.commons.client.core.ui.theme.ThemeChangeHandler;
import org.mslab.commons.client.tool.educ.EducTheme;
import org.mslab.commons.client.tool.educ.ui.RangeAnchor;
import org.mslab.commons.shared.text.MessageFormat;
import org.mslab.commons.shared.text.StringExt;
import org.mslab.commons.shared.text.Text;
import org.mslab.commons.shared.types.Address;
import org.mslab.commons.shared.types.Color;
import org.mslab.commons.shared.types.educ.Organization;
import org.mslab.commons.shared.types.educ.SchoolBoard;

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

public class OrganizationViewerListItem extends GridPanel implements ClickHandler {
	private OrganizationViewerListContent _listContent;
	private Organization _organization;
	private String _filterText;
	private RangeAnchor<Organization> _schoolName; 
	private OrganizationIcon _organizationIcon;
	private AddressViewer _addressViewer;
	private PhoneCoordinateViewer _phoneCoordinateViewer; 
	private WebCoordinateViewer _webCoordinateViewer; 
	private DirectionViewer _directionViewer;
	
	OrganizationViewerListItem(OrganizationViewerListContent content, Organization organization, String filterText) {
		_listContent = content;
		_grid.setWidth("100%");
		_grid.setCellPadding(0);
		_grid.setCellSpacing(0);
		
		_organization = organization;
		_filterText = filterText;
		int row = 0, col = 0;
		
		EducTheme theme = (EducTheme)EducTheme.getTheme();
		_schoolName = new RangeAnchor<Organization>(organization, theme.getSelectionColor());
		_schoolName.setWidth("100%");
		_schoolName.getElement().getStyle().setFontSize(160, Unit.PCT);
		_schoolName.getElement().getStyle().setMarginTop(6, Unit.PX);
		_schoolName.addClickHandler(this);
		_grid.setWidget(row, 0, _schoolName);
		_grid.getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_CENTER);
		_grid.getFlexCellFormatter().setWidth(row, 0, "100%");
		_grid.getFlexCellFormatter().setColSpan(row, 0, 5);
		row++;
		
		_organizationIcon = new OrganizationIcon(organization);
		_organizationIcon.getElement().getStyle().setMarginLeft(24, Unit.PX);
		_grid.setWidget(row, col, _organizationIcon);
		_grid.getFlexCellFormatter().setWidth(row, col, "5%");
		col++;
		
		_addressViewer = new AddressViewer(this, organization);
		_addressViewer.getElement().getStyle().setMarginLeft(12, Unit.PX);
		_grid.setWidget(row, col, _addressViewer);
		_grid.getFlexCellFormatter().setWidth(row, col, "25%");
		_grid.getFlexCellFormatter().setVerticalAlignment(row, col, HasVerticalAlignment.ALIGN_TOP);
		col++;
		
		_directionViewer = new DirectionViewer(this, organization); 
		_grid.setWidget(row, col, _directionViewer);
		_grid.getFlexCellFormatter().setWidth(row, col, "25%");
		_grid.getFlexCellFormatter().setVerticalAlignment(row, col, HasVerticalAlignment.ALIGN_TOP);
		col++;
		
		_phoneCoordinateViewer = new PhoneCoordinateViewer(this, organization); 
		_grid.setWidget(row, col, _phoneCoordinateViewer);
		_grid.getFlexCellFormatter().setWidth(row, col, "25%");
		_grid.getFlexCellFormatter().setVerticalAlignment(row, col, HasVerticalAlignment.ALIGN_TOP);
		col++;
		
		_webCoordinateViewer = new WebCoordinateViewer(this, organization); 
		_grid.setWidget(row, col, _webCoordinateViewer);
		_grid.getFlexCellFormatter().setWidth(row, col, "25%");
		_grid.getFlexCellFormatter().setVerticalAlignment(row, col, HasVerticalAlignment.ALIGN_TOP);
		col++;
		
		refresh(); 
	}
	
	private void refresh() {
		OrganizationRenderer renderer = new OrganizationRenderer(_organization); 
		
		String text = renderer.render(OrganizationRenderer.Field.NAME);
		int idx = renderer.indexOf(OrganizationRenderer.Field.NAME, _filterText); 
		int len = (_filterText == null) ? 0 : _filterText.length();
		_schoolName.setRangeHTML(text, idx, len);
	}
	
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
			String text = "<i class=\"fa fa-university fa-3x\" aria-hidden=\"true\" ></i>";
			HTML html = new HTML(text);
			_grid.setWidget(0, 0, html);
			_grid.getFlexCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
		}

		private void drawSchoolBoard() {
			String text = "<i class=\"fa fa-university fa-lg\" aria-hidden=\"true\" ></i>";
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
	
	static class AddressViewer extends GridPanel {
		private OrganizationViewerListItem _parent;
		private Organization _organization;
		private RangeHTML _addressLine1, _addressLine2, _distanceLbl;
		
		AddressViewer(OrganizationViewerListItem parent, Organization organization) {
			int row = 0;
			_grid.setCellPadding(0);
			_grid.setCellSpacing(0);
			_parent = parent;
			_organization = organization;

			EducTheme theme = (EducTheme)EducTheme.getTheme();
			String selectionColor = theme.getSelectionColor().toString();
			
			_addressLine1 = new RangeHTML(selectionColor);
			_addressLine1.getElement().getStyle().setMarginBottom(2, Unit.PX);
			_grid.setWidget(row, 0, _addressLine1);
			row++;
			
			_addressLine2 = new RangeHTML(selectionColor);
			_addressLine2.getElement().getStyle().setMarginBottom(2, Unit.PX);
			_grid.setWidget(row, 0, _addressLine2);
			row++;
			
			/*
			_distanceLbl = new RangeHTML(selectionColor);
			_distanceLbl.getElement().getStyle().setMarginBottom(2, Unit.PX);
			_grid.setWidget(row, 0, _distanceLbl);
			row++;
			
			
			SimplePanel filler = new SimplePanel();
			_grid.setWidget(row, 0, filler);
			_grid.getFlexCellFormatter().setHeight(row, 0, "100%");
			row++;
			*/
			
			refresh();
		}
		
		private void refresh() {
			OrganizationRenderer renderer = new OrganizationRenderer(_organization); 
			int len = (_parent._filterText == null) ? 0 : _parent._filterText.length();
			
			String line1 = renderer.render(OrganizationRenderer.Field.ADDRESS_LINE_1);
			int idx = renderer.indexOf(OrganizationRenderer.Field.ADDRESS_LINE_1, _parent._filterText); 
			_addressLine1.setRangeHTML(line1, idx, len);

			String line2 = renderer.render(OrganizationRenderer.Field.ADDRESS_LINE_2);
			idx = renderer.indexOf(OrganizationRenderer.Field.ADDRESS_LINE_2, _parent._filterText); 
			_addressLine2.setRangeHTML(line2, idx, len);
			
			/*
			String line4 = renderer.render(OrganizationRenderer.Field.DISTANCE);
			idx = renderer.indexOf(OrganizationRenderer.Field.DISTANCE, _parent._filterText); 
			//_distanceLbl.setRangeHTML(line4, idx, len);
		
			 */
		}
	}
	
	static class PhoneCoordinateViewer extends GridPanel {
		private OrganizationViewerListItem _parent;
		private Organization _organization;
		private RangeHTML _phoneNumber, _fax;
		
		PhoneCoordinateViewer(OrganizationViewerListItem parent, Organization organization) {
			int row = 0;
			_parent = parent;
			_organization = organization;
			_grid.setCellPadding(0);
			_grid.setCellSpacing(0);
			
			EducTheme theme = (EducTheme)EducTheme.getTheme();
			String selectionColor = theme.getSelectionColor().toString();
			
			_phoneNumber = new RangeHTML(selectionColor);
			_phoneNumber.getElement().getStyle().setMarginBottom(2, Unit.PX);
			_grid.setWidget(row, 0, _phoneNumber);
			row++;
			
			_fax = new RangeHTML(selectionColor);
			_fax.getElement().getStyle().setMarginBottom(2, Unit.PX);
			_grid.setWidget(row, 0, _fax);
			row++;
			
			refresh(); 
		}
		
		private void refresh() {
			OrganizationRenderer renderer = new OrganizationRenderer(_organization); 
			int len = (_parent._filterText == null) ? 0 : _parent._filterText.length();
			
			String phoneNumberAndExtension = renderer.render(OrganizationRenderer.Field.PHONE_NUMBER_EXT);
			int idx = renderer.indexOf(OrganizationRenderer.Field.PHONE_NUMBER_EXT, _parent._filterText); 
			_phoneNumber.setRangeHTML(phoneNumberAndExtension, idx, len);
			_phoneNumber.setVisible(! StringExt.isNullOrWhitespace(phoneNumberAndExtension));
			
			String fax = renderer.render(OrganizationRenderer.Field.FAX);
		    idx = renderer.indexOf(OrganizationRenderer.Field.FAX, _parent._filterText); 
		    _fax.setRangeHTML(fax, idx, len);
		    _fax.setVisible(! StringExt.isNullOrWhitespace(fax));
		}
	} //end CoordinateViewer
	
	static class WebCoordinateViewer extends GridPanel {
		private OrganizationViewerListItem _parent;
		private Organization _organization;
		private RangeHTML _email, _web;
		
		WebCoordinateViewer(OrganizationViewerListItem parent, Organization organization) {
			int row = 0;
			_parent = parent;
			_organization = organization;
			_grid.setCellPadding(0);
			_grid.setCellSpacing(0);
			
			EducTheme theme = (EducTheme)EducTheme.getTheme();
			String selectionColor = theme.getSelectionColor().toString();
			
			_email = new RangeHTML(selectionColor);
			_email.getElement().getStyle().setMarginBottom(2, Unit.PX);
			_grid.setWidget(row, 0, _email);
			row++;
			
			_web = new RangeHTML(selectionColor);
			_web.getElement().getStyle().setMarginBottom(2, Unit.PX);
			_grid.setWidget(row, 0, _web);
			row++;
			
			refresh(); 
		}
		
		private void refresh() {
			OrganizationRenderer renderer = new OrganizationRenderer(_organization); 
			int len = (_parent._filterText == null) ? 0 : _parent._filterText.length();
			
		    String email = renderer.render(OrganizationRenderer.Field.EMAIL);
			int idx = renderer.indexOf(OrganizationRenderer.Field.EMAIL, _parent._filterText); 
			_email.setRangeHTML(email, idx, len);
			_email.setVisible(! StringExt.isNullOrWhitespace(email));
			
			String web = renderer.render(OrganizationRenderer.Field.WEB);
			idx = renderer.indexOf(OrganizationRenderer.Field.WEB, _parent._filterText); 
			_web.setRangeHTML(web, idx, len);
			_web.setVisible(! StringExt.isNullOrWhitespace(web));
		}
	} //end CoordinateViewer
	
	static class DirectionViewer extends GridPanel {
		private OrganizationViewerListItem _parent;
		private Organization _organization;
		private RangeHTML _direction, _circonscription;
		
		DirectionViewer(OrganizationViewerListItem parent, Organization organization) {
			int row = 0;
			_parent = parent;
			_organization = organization;
			_grid.setCellPadding(0);
			_grid.setCellSpacing(0);
			
			EducTheme theme = (EducTheme)EducTheme.getTheme();
			String selectionColor = theme.getSelectionColor().toString();
			
			_direction = new RangeHTML(selectionColor);
			_direction.getElement().getStyle().setMarginBottom(2, Unit.PX);
			_direction.setWordWrap(false);
			_grid.setWidget(row, 0, _direction);
			row++; 
			
			_circonscription = new RangeHTML(selectionColor);
			_circonscription.getElement().getStyle().setMarginBottom(2, Unit.PX);
			_circonscription.setWordWrap(false);
			_grid.setWidget(row, 0, _circonscription);
			row++;
			
			refresh();
		}
		
		private void refresh() {
			OrganizationRenderer renderer = new OrganizationRenderer(_organization); 
			int len = (_parent._filterText == null) ? 0 : _parent._filterText.length();
			
			String direction = renderer.render(OrganizationRenderer.Field.DIRECTION);
			int idx = renderer.indexOf(OrganizationRenderer.Field.DIRECTION, _parent._filterText); 
			_direction.setRangeHTML(direction, idx, len);
			_direction.setVisible(! StringExt.isNullOrWhitespace(direction));
			
			String circonscription = renderer.render(OrganizationRenderer.Field.CIRCONSCRIPTION);
			idx = renderer.indexOf(OrganizationRenderer.Field.CIRCONSCRIPTION, _parent._filterText); 
			_circonscription.setRangeHTML(circonscription, idx, len);
			_circonscription.setVisible(! StringExt.isNullOrWhitespace(circonscription));
		}
	}

  

	@Override
	public void onClick(ClickEvent event) {
		Object src = event.getSource();
		
		if (src.equals(_schoolName)) {
			Organization organization = _schoolName.getItem();
			List<Organization> organizations = new ArrayList<Organization>();
			organizations.add(organization);
			
			_listContent._owner._owner._owner.update("", organizations);
		}
	}
}
