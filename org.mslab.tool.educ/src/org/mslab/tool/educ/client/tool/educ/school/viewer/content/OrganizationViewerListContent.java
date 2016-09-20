package org.mslab.commons.client.tool.educ.school.viewer.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.mslab.commons.client.core.ui.AbstractIconButton;
import org.mslab.commons.client.core.ui.panels.GridPanel;
import org.mslab.commons.client.core.ui.theme.AbstractTheme;
import org.mslab.commons.client.tool.educ.EducTheme;
import org.mslab.commons.client.tool.educ.school.viewer.OrganizationListViewer.OrganizationViewerHeaderContent;
import org.mslab.commons.shared.text.MessageFormat;
import org.mslab.commons.shared.types.Color;
import org.mslab.commons.shared.types.educ.Organization;

import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;

public class OrganizationViewerListContent extends AbstractContentViewer {
	public OrganizationViewerHeaderContent _owner;
	private List<Organization> _organizations;
	public List<Organization> _displayedOrganizations = new ArrayList<Organization>(); 
	private String _filterText = null;
	private OrganizationComparator _organizationComparator; 
	private PageOptionPanel _pageOptionPanel;
	private int _pageSize = PAGE_SIZES[0];
	private int _currentPage = 0;
	private Color _borderColor, _lightBg; 
	
	public OrganizationViewerListContent(OrganizationViewerHeaderContent owner) {
		_owner = owner;
		_grid.setWidth("100%");
		_grid.setCellPadding(0);
		_grid.setCellSpacing(0);
		
		_organizationComparator = new OrganizationComparator(); 
		_pageOptionPanel = new PageOptionPanel();
		_borderColor = EducTheme.getTheme().getPrimaryBgColor();
		_lightBg = EducTheme.getTheme().getPrimaryBgColor().getGrayscale().blendWith(Color.WHITE, 75);
	}
	
	@Override
	public void showOrganizations(String listName, List<Organization> organizations) {
		_organizations = organizations;
		_filterText = null;
		update();
	}
	
	public void sort(OrganizationComparator.Criteria criteria, boolean ascendent) {
		_organizationComparator.setCriteria(criteria);
		_organizationComparator.setOrder(ascendent);
		refresh();
	}

	@Override
	public List<Organization> filterText(String filterText) {
		_filterText = filterText;
		update();
		return _displayedOrganizations; 
	}
	
	private void update() {
		//build list of items to show
		_displayedOrganizations.clear();
		if (_organizations != null) {
			for (Organization org : _organizations) {
				boolean accepted = filter(org);
				if (accepted) {
					_displayedOrganizations.add(org); 
				}
			}
		}
		
		refresh();
	}

	private void refresh() {
		int row = 0;
		_grid.clear();
		int nb = (_displayedOrganizations == null) ? 0 : _displayedOrganizations.size();
		
		if (nb > 0) {
			//sort
			Collections.sort(_displayedOrganizations, _organizationComparator);
			
			//get page
			int start = _currentPage * _pageSize;
			int end = (_currentPage + 1) * _pageSize;
			start = Math.min(start, _displayedOrganizations.size());
			end =  Math.min(end, _displayedOrganizations.size());
			List<Organization> page =  _displayedOrganizations.subList(start, end);
			
			_pageOptionPanel.setNbPages(nb);
			
			//show filtered elements
			for (Organization org : page) { 
				OrganizationViewerListItem itemViewer = new OrganizationViewerListItem(this, org, _filterText);
				boolean even = (row % 2) == 0;
				Color bgColor = even ? Color.WHITE : _lightBg;
				itemViewer.getElement().getStyle().setBackgroundColor(bgColor.toString());
				itemViewer.getElement().getStyle().setBorderStyle(BorderStyle.SOLID);
				itemViewer.getElement().getStyle().setBorderColor(_borderColor.toString());
				itemViewer.getElement().getStyle().setProperty("borderWidth", "0px 0px 1px 0px");
				
				_grid.setWidget(row, 0, itemViewer);
				row++; 
			} //end for
			
			_grid.setWidget(row, 0, _pageOptionPanel);
			row++; 
		}
				
		SimplePanel filler = new SimplePanel();
		_grid.setWidget(row, 0, filler);
		_grid.getFlexCellFormatter().setWidth(row, 0, "95%");
		row++;
	}

	private boolean filter(Organization org) {
		boolean accepted; 
		
		if ((_filterText == null) || (_filterText.length() <= 1)) {
			accepted = true; 
		} else {
			accepted = false;
			
			OrganizationRenderer renderer = new OrganizationRenderer(org); 
			accepted |= (renderer.indexOf(OrganizationRenderer.Field.NAME, _filterText) != -1); 
			accepted |= (renderer.indexOf(OrganizationRenderer.Field.ADDRESS_LINE_1, _filterText) != -1); 
			accepted |= (renderer.indexOf(OrganizationRenderer.Field.ADDRESS_LINE_2, _filterText) != -1); 
			accepted |= (renderer.indexOf(OrganizationRenderer.Field.POSTAL_CODE, _filterText) != -1); 
			
			accepted |= (renderer.indexOf(OrganizationRenderer.Field.PHONE_NUMBER_EXT, _filterText) != -1); 
			accepted |= (renderer.indexOf(OrganizationRenderer.Field.FAX, _filterText) != -1); 
			accepted |= (renderer.indexOf(OrganizationRenderer.Field.EMAIL, _filterText) != -1); 
			accepted |= (renderer.indexOf(OrganizationRenderer.Field.WEB, _filterText) != -1); 
			accepted |= (renderer.indexOf(OrganizationRenderer.Field.DIRECTION, _filterText) != -1); 
			accepted |= (renderer.indexOf(OrganizationRenderer.Field.CIRCONSCRIPTION, _filterText) != -1);
		}
		
		return accepted;
	}


	class PageOptionPanel extends GridPanel {
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
			setVisible(count > 1);
			_pagerPanel.setNbPages(count);
		}
	}
	
	private static final int[] PAGE_SIZES = new int[] {5, 10, 15};
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
			
			label = new HTML("organismes par page"); 
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
} //end OrganizationViewerContent