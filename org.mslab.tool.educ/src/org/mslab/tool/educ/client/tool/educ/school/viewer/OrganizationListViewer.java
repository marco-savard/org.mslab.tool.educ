package org.mslab.tool.educ.client.tool.educ.school.viewer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.mslab.tool.educ.client.core.ui.AbstractIconButton;
import org.mslab.tool.educ.client.core.ui.SelectionButton;
import org.mslab.tool.educ.client.core.ui.SelectionButtonGroup;
import org.mslab.tool.educ.client.core.ui.SelectionButtonGroup.SelectionChangeHandler;
import org.mslab.tool.educ.client.core.ui.StyleUtil;
import org.mslab.tool.educ.client.core.ui.panels.GridPanel;
import org.mslab.tool.educ.client.core.ui.panels.IconTabPanelBuilder;
import org.mslab.tool.educ.client.core.ui.theme.AbstractTheme;
import org.mslab.tool.educ.client.core.ui.theme.ThematicAnchor;
import org.mslab.tool.educ.client.tool.educ.EducTheme;
import org.mslab.tool.educ.client.tool.educ.people.viewer.SearchBox;
import org.mslab.tool.educ.client.tool.educ.school.explorer.EntityViewable;
import org.mslab.tool.educ.client.tool.educ.school.viewer.content.AbstractContentViewer;
import org.mslab.tool.educ.client.tool.educ.school.viewer.content.OrganizationComparator;
import org.mslab.tool.educ.client.tool.educ.school.viewer.content.OrganizationComparator.Criteria;
import org.mslab.tool.educ.client.tool.educ.school.viewer.content.OrganizationExportViewer;
import org.mslab.tool.educ.client.tool.educ.school.viewer.content.OrganizationGraphicViewer4;
import org.mslab.tool.educ.client.tool.educ.school.viewer.content.OrganizationInstanceViewer;
import org.mslab.tool.educ.client.tool.educ.school.viewer.content.OrganizationMapViewer3;
import org.mslab.tool.educ.client.tool.educ.school.viewer.content.OrganizationRenderer;
import org.mslab.tool.educ.client.tool.educ.school.viewer.content.OrganizationViewerListContent;
import org.mslab.tool.educ.shared.text.MessageFormat;
import org.mslab.tool.educ.shared.types.Color;
import org.mslab.tool.educ.shared.types.educ.Organization;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.TextDecoration;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;

public class OrganizationListViewer extends AbstractViewer<Organization> implements ResizeHandler {
	private OrganizationContent _content;
    int _pageSize = PAGE_SIZES[0];
    int _currentPage = 0;
	
	public OrganizationListViewer() {
		_content = new OrganizationContent(); 
		add(_content);
		Window.addResizeHandler(this);
		refresh();
	}
	
	@Override
	public void onResize(ResizeEvent event) {
		refresh();
	}
	
	private void refresh() {
		_content.refresh();
	}
	
	public void onResize() {
		_content.onResize(); 
	}
	

	@Override
	public void update(String listName, List<Organization> organizations) {
		_content.update(listName, organizations);
	}
	
	//
	// inner classes
	//
	public class OrganizationContent extends DeckPanel {
		private OrganizationListViewerContent _listViewer; 
		private OrganizationInstanceViewer _instanceViewer;
		
		OrganizationContent() {
			_listViewer = new OrganizationListViewerContent(this); 
			_instanceViewer = new OrganizationInstanceViewer(); 
			
			add(_listViewer);
			add(_instanceViewer);
			showWidget(0);
		}

		public void update(String listName, List<Organization> organizations) {
			int nb = (organizations == null) ? 0 : organizations.size();
			int idx = (nb > 1) ? 0 : 1;
			showWidget(idx);
			
			_listViewer.update(listName, organizations);
			
			if (nb == 1) {
				Organization organization = organizations.get(0); 
				_instanceViewer.update(organization);
			}
			
		}

		public void onResize() {
			_listViewer.onResize();
			
		}

		public void refresh() {
			_listViewer.refresh();
			_instanceViewer.refresh();
		}
	}
	
	public class OrganizationListViewerContent extends GridPanel {
		public OrganizationContent _owner;
		OrganizationViewerHeaderContent _headerContent;
		
		OrganizationListViewerContent(OrganizationContent owner) {
			_owner = owner;
			_grid.setCellPadding(0);
			_grid.setCellSpacing(0);
			_grid.setWidth("100%");
			
			//create content & header
			SimplePanel organizationViewerContentPanel = new SimplePanel(); 
			_headerContent = new OrganizationViewerHeaderContent(this, organizationViewerContentPanel); 
			
			//add header
			int row = 0;
			Color leftGradient = Color.GREY_LIGHT;
			StyleUtil.setLinearGradient(_headerContent, 90, leftGradient, Color.WHITE);
			_grid.setWidget(row, 0, _headerContent);
			row++;

			//add content
			_grid.setWidget(row, 0, organizationViewerContentPanel);
			row++;
		}

		public void onResize() {
			_headerContent.onResize();
		}

		public void update(String listName, List<Organization> organizations) {
			_headerContent.update(listName, organizations);
		}

		public void refresh() {
		}
	}

	public class OrganizationViewerHeaderContent extends GridPanel implements EntityViewable<Organization> {
		public OrganizationListViewerContent _owner;
		private OrganizationAnimatedTitlePanel _viewerTitle;
		private AdvancedSearchPanel _advancedSearchPanel;
		//private SearchPanel _searchPanel;
		//private SearchOrTagCloudPanel _searchOrTagCloudPanel;
		private ViewerTabPanel _viewerTabPanel;
		private String _listName;
		private List<Organization> _organizations;
		private SorterPanel _sorterPanel;

		OrganizationViewerHeaderContent(OrganizationListViewerContent owner, SimplePanel organizationViewerContentPanel) {
			_owner = owner;
			int row = 0;
			_grid.setCellPadding(0);
			_grid.setCellSpacing(0);
			_grid.setWidth("100%");
			EducTheme theme = (EducTheme)EducTheme.getTheme();
			
			_viewerTitle = new OrganizationAnimatedTitlePanel(this); 
			_viewerTitle.getElement().getStyle().setPaddingTop(12, Unit.PX);
			_viewerTitle.getElement().getStyle().setPaddingLeft(12, Unit.PX);
			_grid.setWidget(row, 0, _viewerTitle);
			_grid.getFlexCellFormatter().setColSpan(row, 0, 2);
			row++;
			
			_advancedSearchPanel = new AdvancedSearchPanel(this); 
			_grid.setWidget(row, 0, _advancedSearchPanel);
			_grid.getFlexCellFormatter().setColSpan(row, 0, 2);
			_grid.getFlexCellFormatter().setWidth(row, 0, "100%");
			row++; 
			
			_viewerTabPanel = new ViewerTabPanel(this, theme.getPrimaryFgColor(), organizationViewerContentPanel);
			_viewerTabPanel.setWidth("50%");
			_viewerTabPanel.getElement().getStyle().setPaddingLeft(6, Unit.PX);
			_grid.setWidget(row, 0, _viewerTabPanel);
			_grid.getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_LEFT);
			
			_sorterPanel = new SorterPanel(this); 
			_sorterPanel.setWidth("50%");
			_grid.setWidget(row, 1, _sorterPanel);
			_grid.getFlexCellFormatter().setHorizontalAlignment(row, 1, HasHorizontalAlignment.ALIGN_RIGHT);
			row++;
			
			//_viewerTabPanel = new ViewerTabPanel(this, theme.getPrimaryFgColor(), organizationViewerContentPanel);
			//_viewerTabPanel.getElement().getStyle().setPaddingLeft(6, Unit.PX);
			//_grid.setWidget(row, 0, _viewerTabPanel);
			//_grid.getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_LEFT);
			//row++;
			
			/*
			_searchPanel = new SearchPanel(this);
			_grid.setWidget(row, 0, _searchPanel);
			_grid.getFlexCellFormatter().setColSpan(row, 0, 2);
			row++;
*/
			//_searchOrTagCloudPanel = new SearchOrTagCloudPanel(this); 
			
			
			
			/*
			
			_chooser.getElement().getStyle().setPaddingLeft(12, Unit.PX);
			_grid.setWidget(row, 0, _chooser);
			_grid.getFlexCellFormatter().setColSpan(row, 0, 2);
			row++;
			
			
			_searchOrTagCloudPanel.getElement().getStyle().setPaddingLeft(12, Unit.PX);
			_grid.setWidget(row, 0, _searchOrTagCloudPanel);
			_grid.getFlexCellFormatter().setColSpan(row, 0, 2);
			_grid.getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_LEFT);
			row++;
			
			
			
			
			*/
		}
		
		public void onResize() {
			_viewerTabPanel.onResize();
		}

		public void refresh() {
			_owner.refresh();
		}

		@Override
		public void update(String listName, List<Organization> organizations) {
			int nb = (organizations == null) ? 0 : organizations.size();
			
			if (nb > 0) {
				_listName = listName;
				_organizations = organizations;
				setVisible(nb > 1);
				
				_viewerTitle.update(listName, organizations.size()); 		
				_advancedSearchPanel.update(listName, organizations);
			    _sorterPanel.update(listName, organizations);
			    
				_viewerTabPanel.showOrganization(listName, organizations);
			}
		}
	
		public void filterText(String filterText) {
			//update header
			//OrganizationAbstractFilter filter = OrganizationAbstractFilter.createSearchFilter(filterText); 
			//_owner._listContent.filterText(filterText);
			//int nbFound = _owner._listContent._displayedOrganizations.size();

			
			//update title
			List<Organization> orgs = _viewerTabPanel.filterText(filterText);
			_viewerTitle.update(filterText, orgs.size());
			
			//update content
			_viewerTabPanel.showOrganization(filterText, orgs);
		}

		/*
		public void displaySearchOrCloud(boolean search) {
			if (_searchOrTagCloudPanel != null) {
				int idx = search ? 0 : 1;
				_searchOrTagCloudPanel.showWidget(idx);
			}
		}*/

		public void sort(OrganizationComparator.Criteria criteria, boolean ascendent) {
			_viewerTabPanel.sort(criteria, ascendent);
		}
	}
	
	private class OrganizationAnimatedTitlePanel extends GridPanel implements ChangeHandler {
		private static final int DELAY = 300; //ms	
		private OrganizationViewerHeaderContent _owner;
		private OrganizationAnimatedTitle _viewerTitle;
		private SearchBox _searchBox;
		
		OrganizationAnimatedTitlePanel(OrganizationViewerHeaderContent owner) {
			_owner = owner;
			_grid.setWidth("100%");
			
			_viewerTitle = new OrganizationAnimatedTitle();
			_grid.setWidget(0, 0, _viewerTitle);
			
			SimplePanel filler = new SimplePanel();
			_grid.setWidget(0, 1, filler);
			_grid.getFlexCellFormatter().setWidth(0, 1, "95%");
			
			_searchBox = new SearchBox(DELAY); 
			_searchBox.getElement().getStyle().setMarginRight(12, Unit.PX);
			_searchBox.addChangeHandler(this);
			_grid.setWidget(0, 2, _searchBox);
		}

		public void update(String listName, int size) {
			_viewerTitle.update(listName, size);
		}

		@Override
		public void onChange(ChangeEvent event) {
			Object src = event.getSource(); 
			
			if (_searchBox.equals(src)) {
				String textChanged = _searchBox.getText(); 
				textChanged = (textChanged == null) ? null : textChanged.toLowerCase();
				_owner.filterText(textChanged);
			}
		}
	}
	
	private class SearchPanel extends GridPanel {
		
	}
	
	private class AdvancedSearchPanel extends GridPanel implements ClickHandler, SelectionChangeHandler {
		private OrganizationViewerHeaderContent _owner;
		private SearchModeToggle _anchor; 
		SearchTypeSelectionButtonGroup _btnGroup;
		SearchOrTagCloudPanel _searchOrTagCloudPanel;
		
		AdvancedSearchPanel(OrganizationViewerHeaderContent owner) {
			_owner = owner;
			int row = 0;
			_grid.setWidth("100%");
			
			_anchor = new SearchModeToggle();
			_anchor.refresh();
			_anchor.addClickHandler(this);
			_anchor.getElement().getStyle().setMarginLeft(12, Unit.PX);
			_anchor.getElement().getStyle().setMarginRight(12, Unit.PX);
			_grid.setWidget(row, 0, _anchor);
			_grid.getFlexCellFormatter().setColSpan(row, 0, 2);
			row++; 
			
			AbstractTheme theme = EducTheme.getTheme();
			_btnGroup = new SearchTypeSelectionButtonGroup( theme.getPrimaryFgColor()); 
			_btnGroup.getElement().getStyle().setMarginLeft(12, Unit.PX);
			_btnGroup.addSelectionChangeHandler(this);
			_grid.setWidget(row, 0, _btnGroup);
			_grid.getFlexCellFormatter().setVerticalAlignment(row, 0, HasVerticalAlignment.ALIGN_TOP);
			
			_searchOrTagCloudPanel = new SearchOrTagCloudPanel(owner);
			_grid.setWidget(row, 1, _searchOrTagCloudPanel);
			_grid.getFlexCellFormatter().setWidth(row, 1, "99%");
			refresh();
		}

		@Override
		public void onClick(ClickEvent event) {
			Object src = event.getSource();
			
			if (src.equals(_anchor)) {
				_anchor.toggle();
				refresh();
			}			
		}
		
		public void update(String listName, List<Organization> organizations) {
			_searchOrTagCloudPanel.update(listName, organizations);
		}
		
		public void refresh() {
			boolean advanced = _anchor.isAdvanced();
			_owner._viewerTitle._searchBox.setVisible(! advanced); 
			
			HorizontalAlignmentConstant alignment = advanced ? HasHorizontalAlignment.ALIGN_LEFT : HasHorizontalAlignment.ALIGN_RIGHT; 
			_grid.getFlexCellFormatter().setHorizontalAlignment(0, 0, alignment);
			_btnGroup.setVisible(advanced);
			_searchOrTagCloudPanel.setVisible(advanced);
		}

		@Override
		public void handleSelectionChange() {
			int idx = _btnGroup.getSelectedIndex();
			_searchOrTagCloudPanel.showWidget(idx);
		}
	}
	
	private class SearchModeToggle extends ThematicAnchor {
		private boolean _advanced = false;
		
		SearchModeToggle() {
			refresh();
		}
		
		public boolean isAdvanced() {
			return _advanced;
		}

		public boolean toggle() {
			_advanced = ! _advanced;
			refresh();
			return _advanced; 
		}

		public void refresh() {
			String html = _advanced ? "Retour &agrave; la recherche simple" : "Recherche avanc&eacute;e";
			setHTML(html);
		}
	}
	
	private class SearchTypeSelectionButtonGroup extends SelectionButtonGroup {
		private SelectionButton _searchBtn, _cloudBtn; 
		
		SearchTypeSelectionButtonGroup(Color color) {
			super(color);
			_searchBtn = new SelectionButton(this, "<i class=\"fa fa-search fa-lg\"></i>");
			_cloudBtn = new SelectionButton(this, "<i class=\"fa fa-cloud fa-lg\"></i>");
			
			addItem(_searchBtn);
			addItem(_cloudBtn);
		}

		@Override
		public void onSelectionChanged() {
			// TODO Auto-generated method stub
		}
	}
	
	private class SearchOrTagCloudPanel extends DeckPanel implements ChangeHandler  {
		private static final int DELAY = 300; //ms	
		private OrganizationViewerHeaderContent _header; 
		private SearchBox _searchBox;
		private OrganizationTagCloudViewer _tagCloud;
		
		public SearchOrTagCloudPanel(OrganizationViewerHeaderContent header) {
			_header = header;
			
			_searchBox = new SearchBox(DELAY); 
			_searchBox.addChangeHandler(this);
			add(_searchBox);
			
			//add cloud
			_tagCloud = new OrganizationTagCloudViewer(header);
			_tagCloud.setWidth("98%");
			add(_tagCloud);
			
			showWidget(0);
		}

		public void update(String listName, List<Organization> organizations) {
			_tagCloud.update(listName, organizations);
		}

		@Override
		public void onChange(ChangeEvent event) {
			Object src = event.getSource(); 
			
			if (_searchBox.equals(src)) {
				String textChanged = _searchBox.getText(); 
				textChanged = (textChanged == null) ? null : textChanged.toLowerCase();
				_header.filterText(textChanged);
			}
		}
	}
	
	private class OrganizationAnimatedTitle extends HTML {
		private UpdateTitleAnimation _animation; 
		private int _startCount; 
		
		OrganizationAnimatedTitle() {
			AbstractTheme theme = EducTheme.getTheme();
			_animation = new UpdateTitleAnimation(this);
			this.setWordWrap(false);
			getElement().getStyle().setFontSize(200, Unit.PCT);
			getElement().getStyle().setFontWeight(FontWeight.LIGHTER);
			getElement().getStyle().setProperty("fontFamily", theme.getFontFamily());
		
		}

		public void update(String listName, int endCount) {
			_animation.updateCount(600, _startCount, endCount, listName);
		}
	}
	
	private class UpdateTitleAnimation extends Animation { 
		private int _startCount, _endCount;
		private String _listName; 
		private OrganizationAnimatedTitle _animatedPanel; 
		
		UpdateTitleAnimation(OrganizationAnimatedTitle animatedPanel) {
			_animatedPanel = animatedPanel;
		}
		
		@Override
		protected void onUpdate(double progress) {
			int delta = _endCount - _startCount;
			int count = (int)(_startCount + (progress * delta)); 
			String schools = (count <= 1) ? "organisme trouvé" : "organismes trouvés";
			String patt = "{0} {1} dans &laquo;{2}&raquo;"; 
			String html = MessageFormat.format(patt, new Object[] 
				{count, schools, _listName});
			_animatedPanel.setHTML(html);
		}
		
		@Override
		protected void onComplete() {
			_startCount = _endCount;
		    super.onComplete();
		}
		
		public void updateCount(int duration, int startCount, int endCount, String listName) {
			_endCount = endCount;
			_listName = listName;
			run(duration);
		}
	}
	
	private class ViewerTabPanel extends GridPanel implements SelectionButtonGroup.SelectionChangeHandler {
		private OrganizationViewerHeaderContent _owner;
		private SelectionButtonGroup _menuPanel;
		private List<AbstractContentViewer> _viewerList = new ArrayList<AbstractContentViewer>(); 

		public ViewerTabPanel(OrganizationViewerHeaderContent owner, Color selectionColor, SimplePanel organizationViewerContentPanel) {
			_owner = owner;
			IconTabPanelBuilder builder = new IconTabPanelBuilder(); 
			
			AbstractContentViewer listViewer = new OrganizationViewerListContent(owner); 
			String text = "<i class=\"fa fa-list fa-2x\"></i>"; 
			builder.add(listViewer, text, "Liste"); 
			_viewerList.add(listViewer); 
			
			AbstractContentViewer mapViewer = new OrganizationMapViewer3();
		    text = "<i class=\"fa fa-globe fa-2x\"></i>"; 
			builder.add(mapViewer, text, "Carte"); 
			_viewerList.add(mapViewer);
			
			AbstractContentViewer graphicViewer = new OrganizationGraphicViewer4();
		    text = "<i class=\"fa fa-pie-chart fa-2x\"></i>"; 
			builder.add(graphicViewer, text, "Graphique"); 
			_viewerList.add(graphicViewer);
						
			AbstractContentViewer downloadViewer = new OrganizationExportViewer();
			text = "<i class=\"fa fa-download fa-2x\"></i>"; 
			builder.add(downloadViewer, text, "T&eacute;l&eacute;chargement"); 
			_viewerList.add(downloadViewer);
			
			_menuPanel = builder.buildMenuPanel(); 
			_menuPanel.addSelectionChangeHandler(this);
			SimplePanel contentPanel = builder.getContentPanel(); 
			organizationViewerContentPanel.setWidget(contentPanel);
			_grid.setWidth("100%");
			int row = 0; 
			
			_grid.setWidget(row, 0, _menuPanel);
			row++;
		}

		public void onResize() {
			for (AbstractContentViewer viewer : _viewerList) {
				viewer.onResize();
			}
		}

		public List<Organization> filterText(String filterText) {
			int idx = 0; //_menuPanel.getSelectedIndex(); 
			AbstractContentViewer viewer = _viewerList.get(idx);
			return viewer.filterText(filterText);
		}

		public void sort(OrganizationComparator.Criteria criteria, boolean ascendent) {
			int idx = _menuPanel.getSelectedIndex(); 
			AbstractContentViewer viewer = _viewerList.get(idx);
			viewer.sort(criteria, ascendent);
		}

		public void showOrganization(String listName, List<Organization> organizations) {
			int nb = organizations.size();
			
			
			int idx = _menuPanel.getSelectedIndex(); 
			AbstractContentViewer viewer = _viewerList.get(idx);
			viewer.showOrganizations(listName, organizations);			
		}

		@Override
		public void handleSelectionChange() {
			int idx = _menuPanel.getSelectedIndex(); 
			AbstractContentViewer viewer = _viewerList.get(idx);
			viewer.showOrganizations(_owner._listName, _owner._organizations);
		}
	}
	
	class SorterPanel extends GridPanel implements ChangeHandler {
		private OrganizationViewerHeaderContent _parent;
		OrganizationListBox _criteraBox;
		SortDirectionPanel _sortDirectionPanel;
		
		SorterPanel(OrganizationViewerHeaderContent parent) {
			_parent = parent;
			int row = 0, col = 0;
			
			SimplePanel filler = new SimplePanel(); 
			_grid.setWidget(row, col, filler);
			_grid.getFlexCellFormatter().setWidth(row, col, "90%");
			col++;
			
			HTML html = new HTML("Trier selon"); 
			html.setWordWrap(false);
			_grid.setWidget(row, col, html);
			col++;
			
			_criteraBox = new OrganizationListBox(); 
			_criteraBox.addChangeHandler(this);
			_grid.setWidget(row, col, _criteraBox);
			col++;
			
			Color selectionColor = AbstractTheme.getTheme().getPrimaryFgColor();
			_sortDirectionPanel = new SortDirectionPanel(this, selectionColor); 
			_grid.setWidget(row, col, _sortDirectionPanel);
			row++; col = 0;
		}

		public void update(String listName, List<Organization> organizations) {
		}

		@Override
		public void onChange(ChangeEvent event) {
			Object src = event.getSource();
				
			if (_criteraBox.equals(src)) {
				sort();
			}
		}
		
		public void sort() {
			int idx = _criteraBox.getSelectedIndex();
			boolean numeric = (idx == 3); 
			_sortDirectionPanel.setNumeric(numeric);
			//_parent.refresh();
			
			OrganizationComparator.Criteria criteria = _criteraBox.getSelectedCriteria(); 
			int orderIdx = _sortDirectionPanel.getSelectedIndex();
			_parent.sort(criteria, orderIdx == 0);
		}
	}
	
	private class OrganizationListBox extends ListBox {
		private List<Criteria> _criterias = new ArrayList<Criteria>();
		
		OrganizationListBox() {
			OrganizationRenderer r; 
			
			addItem("le nom de l'école", Criteria.NAME);
			addItem("la ville", Criteria.CITY);
			addItem("le code postal", Criteria.POSTAL_CODE);
			addItem("la distance", Criteria.DISTANCE);
		}

		public Criteria getSelectedCriteria() {
			int idx = super.getSelectedIndex();
			Criteria criteria = _criterias.get(idx);
			return criteria;
		}

		private void addItem(String item, Criteria criteria) {
			super.addItem(item);
			_criterias.add(criteria);
		}
	}
	
	private class SortDirectionPanel extends SelectionButtonGroup  {
		private SorterPanel _sortPanel; 
		private SelectionButton _sortAsc, _sortDesc; 
		
		public SortDirectionPanel(SorterPanel panel, Color selectionColor) {
			super(selectionColor);
			_sortPanel = panel;
			
			_sortAsc = new SelectionButton(this, "<i class=\"fa fa-sort-alpha-asc fa-lg\"></i>");
			_sortDesc = new SelectionButton(this, "<i class=\"fa fa-sort-alpha-desc fa-lg\"></i>");
			addItem(_sortAsc);
			addItem(_sortDesc);
			setSelectedItem(0);
		}

		public void setNumeric(boolean numeric) {
			_sortAsc.setHTML(numeric ? "<i class=\"fa fa-sort-numeric-asc fa-lg\"></i>" : "<i class=\"fa fa-sort-alpha-asc fa-lg\"></i>");
			_sortDesc.setHTML(numeric ? "<i class=\"fa fa-sort-numeric-desc fa-lg\"></i>" : "<i class=\"fa fa-sort-alpha-desc fa-lg\"></i>");
		}

		@Override
		public void onSelectionChanged() {
			_sortPanel.sort();
		}  
	}
	
	private static class SearchOrCloudChooser extends GridPanel implements ClickHandler {
		private OrganizationViewerHeaderContent _parent;
		private boolean _search = true;
		private HTML _label, _sep;
		private Anchor _anchor; 
		
		SearchOrCloudChooser(OrganizationViewerHeaderContent parent) {
			_parent = parent;
			_label = new HTML();
			_label.getElement().getStyle().setFontWeight(FontWeight.BOLD);
			_label.getElement().getStyle().setTextDecoration(TextDecoration.UNDERLINE);
			_sep = new HTML("|"); 
			_anchor = new ThematicAnchor();
			_anchor.addClickHandler(this);
			refresh();
 		}
		
		private void refresh() {
			int col = 0; 
			_grid.clear();
			//_parent.displaySearchOrCloud(_search);
			
			if (_search) {
				_label.setHTML("<i class=\"fa fa-search\"></i> Recherche");
				_grid.setWidget(0, col++, _label);
				
				_grid.setWidget(0, col++, _sep);
				
				_anchor.setHTML("<i class=\"fa fa-cloud\"></i> Etiquettes");
				_grid.setWidget(0, col++, _anchor);
			} else {
				_anchor.setHTML("<i class=\"fa fa-search\"></i> Recherche");
				_grid.setWidget(0, col++, _anchor);
				
				_grid.setWidget(0, col++, _sep);
				
				_label.setHTML("<i class=\"fa fa-cloud\"></i> Etiquettes");
				_grid.setWidget(0, col++, _label);
			}
		}

		@Override
		public void onClick(ClickEvent event) {
			_search = !_search;
			refresh();
		}
	}
	
	static class Separator extends HTML {
		Separator() {
			super("<hr>"); 
		}
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
			_pagerPanel.setNbPages(count);
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
	


}
