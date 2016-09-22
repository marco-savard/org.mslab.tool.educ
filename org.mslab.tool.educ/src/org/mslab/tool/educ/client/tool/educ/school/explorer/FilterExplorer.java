package org.mslab.tool.educ.client.tool.educ.school.explorer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.mslab.tool.educ.client.core.ui.CloseButton2;
import org.mslab.tool.educ.client.core.ui.StyleUtil;
import org.mslab.tool.educ.client.core.ui.panels.GridPanel;
import org.mslab.tool.educ.client.core.ui.theme.ThematicLabel;
import org.mslab.tool.educ.client.tool.educ.AbstractExplorer;
import org.mslab.tool.educ.client.tool.educ.EducContent;
import org.mslab.tool.educ.client.tool.educ.OrganizationCategories;
import org.mslab.tool.educ.shared.text.MessageFormat;
import org.mslab.tool.educ.shared.types.Color;
import org.mslab.tool.educ.shared.types.educ.Organization;
import org.mslab.tool.educ.shared.types.educ.SchoolBoard;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;

class FilterExplorer extends AbstractExplorer {
	private Stack<FilteredCollection> _collections = new Stack<FilteredCollection>();
	
	private ExplorerGrid _explorerGrid;

	private FilterExplorer(EducContent parent) {
		super(parent);
		this.getElement().getStyle().setMarginRight(10, Unit.EM);
		
		_explorerGrid = new ExplorerGrid(this);
		add(_explorerGrid);
	}

	@Override
	public void explore(List<Organization> organizations) {
		FilteredCollection collection = new FilteredCollection("Total", organizations); 
		_collections.clear();
		_collections.add(collection); 
		String listName = collection._name;
		_explorerGrid.explore(organizations, listName);
	}
	
	public void removeLastFilter() {
		_collections.pop();
		FilteredCollection collection = _collections.peek();
		List<Organization> organizations = collection._elements;
		String listName = collection._name;
		_explorerGrid.explore(organizations, listName);
	}
	
	//
	// inner classes
	//
	private static class FilteredCollection {
		private String _name;
		private List<Organization> _elements; 
		
		FilteredCollection(String name, List<Organization> elements) {
			_name = name;
			_elements = elements;
		}	
	}
	
	private static class ExplorerGrid extends GridPanel {
		private FilterExplorer _parent;
		private FilterListOutline _outline;
		private List<CategoryFilter> _filters = new ArrayList<CategoryFilter>();
		
		private ExplorerGrid(FilterExplorer parent) {
			int row = 0;
			_parent = parent;
			_grid.setWidth("95%");
			
			_outline = new FilterListOutline(this);
			_grid.setWidget(row, 0, _outline);
			row++;
			
			/*
			addFilter(new OrganizationCategories.RegionCategorizer());
			addFilter(new OrganizationCategories.CityCategorizer());
			addFilter(new OrganizationCategories.OrganizationTypeCategorizer());
			addFilter(new OrganizationCategories.OrdreAppartenanceCategorizer());
			addFilter(new OrganizationCategories.LanguageCategorizer());
			addFilter(new OrganizationCategories.EnvironmentCategorizer());
			addFilter(new OrganizationCategories.SchoolBoardCategorizer());			
			*/
			
			for (CategoryFilter filter : _filters) {
				_grid.setWidget(row, 0, filter);
				row++;
			}
			
			SimplePanel filler = new SimplePanel();
			_grid.setWidget(row, 0, filler);
			_grid.getFlexCellFormatter().setWidth(row, 0, "95%");
			row++;
		}

		private void addFilter(OrganizationCategories.AbstractCategorizer categorizer) {
			_filters.add(new CategoryFilter(this, categorizer));
		}

		public void explore(List<Organization> organizations, String listName) {
			String line = MessageFormat.format("n = {0}", new Object[] {organizations.size()});
			System.out.println(line);
			
			_outline.explore();
			for (CategoryFilter filter : _filters) {
				filter.explore(organizations); 
			}
			
			line = MessageFormat.format("n = {0}", new Object[] {organizations.size()});
			System.out.println(line);
			
			//CategoryFilter lastFilter = _filters.isEmpty() ? null : _filters.get(_filters.size() - 1);
			//String filterName = (lastFilter == null) ? "" : lastFilter._categoryLabel.getText();
			//OrganizationAbstractFilter filter = OrganizationAbstractFilter.createCategoryFilter(listName); 
			_parent._parent.update(listName, organizations);
			
			line = MessageFormat.format("{0} shown", new Object[] {organizations.size()});
			System.out.println(line);
		}
	}
	
	private static class FilterListOutline extends GridPanel {
		private ExplorerGrid _parent; 
		
		private FilterListOutline(ExplorerGrid parent) {
			 _parent = parent;
			_grid.setCellPadding(0);
			_grid.setCellSpacing(0);
			_grid.setWidth("100%");
			
			//StyleUtil.setBorderRadius(this, "5px");
			Style style = getElement().getStyle();
			//style.setWidth(14, Unit.EM);
		}

		public void explore() {
			_grid.clear();
			Stack<FilteredCollection> collections = _parent._parent._collections;
			int row = 0;
			
			for (FilteredCollection collection : collections) {
				boolean last = (row == collections.size() -1);
				explore(row, last, collection);
				row++;
			}
		}

		private void explore(int row, boolean last, FilteredCollection collection) {
			String name = collection._name;
			int count = collection._elements.size();
			
			CategoryOutline outline = new CategoryOutline(this, name, count, (row == 0), last);
			outline.setWidth("100%");
			_grid.setWidget(row, 0, outline);
		}
	} 
	
	private static class CategoryOutline extends GridPanel implements ClickHandler {
		private FilterListOutline _parent;
		private HTML _count;
		
		CategoryOutline(FilterListOutline parent, String name, int count, boolean first, boolean last) {
			int row = 0;
			_parent = parent;
			_grid.setWidth("100%");
			
			Style style = getElement().getStyle();
			style.setWidth(14, Unit.EM);
			
			if (! first) {
				StyleUtil.setBorderRadius(this, "5px 5px 0px 0px");
				style.setBorderStyle(BorderStyle.SOLID);
				style.setProperty("borderWidth", "1px 0px 0px 0px");
			}
			
			if (! last) {
				style.setBackgroundColor(Color.GREY_LIGHT.toString());
				
				StyleUtil.setBorderRadius(this, "5px 5px 0px 0px");
				style = getElement().getStyle();
				style.setBorderStyle(BorderStyle.SOLID);
				style.setProperty("borderWidth", "1px 1px 0px 1px");
			} else {
				StyleUtil.setBorderRadius(this, "5px 5px 5px 5px");
				style.setBorderStyle(BorderStyle.SOLID);
				style.setProperty("borderWidth", "1px 1px 1px 1px");
			}
			
			HTML categoryName = new HTML(name); 
			categoryName.setWordWrap(false);
			categoryName.setWidth("12em");
			_grid.setWidget(row, 0, categoryName);
			_grid.getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_CENTER);
			_grid.getFlexCellFormatter().setWidth(row, 0, "95%");
			
			CloseButton2 btn = new CloseButton2();
			btn.getElement().getStyle().setMarginLeft(12, Unit.PX);
			btn.setVisible(!first && last);
			btn.addClickHandler(this);
			_grid.setWidget(row, 1, btn);
			row++; 
			
			_count = new HTML(Integer.toString(count) + " items"); 
			_count.getElement().getStyle().setFontSize(80, Unit.PCT);
			_grid.setWidget(row, 0, _count);
			_grid.getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_RIGHT);
			_grid.getFlexCellFormatter().setColSpan(row, 0, 2);
			row++;
		}

		@Override
		public void onClick(ClickEvent event) {
			Object src = event.getSource(); 
			
			if (src instanceof CloseButton2) {
				close();
			}
		}

		private void close() {
			FilterListOutline outline = _parent;
			ExplorerGrid grid = outline._parent;
			FilterExplorer explorer = grid._parent;
			explorer.removeLastFilter();
		}
	}

	private static class CategoryFilter extends GridPanel implements ClickHandler {
		protected ExplorerGrid _parent;
		private OrganizationCategories.AbstractCategorizer _categorizer;
		private Label _categoryLabel;
		protected Map<String, List<Organization>> _categories = new HashMap<String, List<Organization>>(); 
		protected List<String> _categoriesCounts = new ArrayList<String>();
		protected int nbDisplay = 0;
		
		@Override
		public String toString() {
			String text = _categoryLabel.getText();
			return text;
		}
		
		public CategoryFilter(ExplorerGrid parent, OrganizationCategories.AbstractCategorizer categorizer) {
			_parent = parent;
			_categorizer = categorizer;
		}
		
		//public abstract String categorize(Organization org);
		
		public void explore(List<Organization> organizations) {
			_categories.clear();
			_categoriesCounts.clear();
			List<SchoolBoard> schoolBoards = getListSchoolBoards();
			
			for (Organization org : organizations) {
				String category = _categorizer.categorize(org, schoolBoards);
				if (category != null) {
					List<Organization> categoryOrganizations = _categories.get(category);
					if (categoryOrganizations == null) {
						categoryOrganizations = new ArrayList<Organization>();
						_categories.put(category, categoryOrganizations);
						_categoriesCounts.add(category);
					}
					categoryOrganizations.add(org);
				}
			}
			
		
			_grid.clear();
			_grid.removeAllRows();
			
			String categoryName = _categorizer.getName();
			_categoryLabel = new ThematicLabel(categoryName);
			_categoryLabel.getElement().getStyle().setFontWeight(FontWeight.BOLD);
			_grid.setWidth("100%");
			_grid.setWidget(0, 0, _categoryLabel);
			_grid.getFlexCellFormatter().setColSpan(0, 0, 2);
			_grid.getFlexCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
			
			Collections.sort(_categoriesCounts, new Comparator<String>() {
				@Override
				public int compare(String k1, String k2) {
					return _categories.get(k2).size() - _categories.get(k1).size();
				}
			});
			
			nbDisplay = Math.min(3, _categoriesCounts.size());
			setVisible(nbDisplay > 1);
			refresh();
		}
		
		private List<SchoolBoard> getListSchoolBoards() {
			List<SchoolBoard> schoolBoards = new ArrayList<SchoolBoard>();
			for (Organization org : _parent._parent._collections.get(0)._elements) {
				if (org instanceof SchoolBoard) {
					schoolBoards.add((SchoolBoard)org);
				}
			}
			
			return schoolBoards;
		}

		private void refresh() {
			for (int i=0; i<nbDisplay; i++) {
				displayLine(i);
			}
			
			if (nbDisplay < _categoriesCounts.size()) {
				displayMore();
			}
		}

		private void displayLine(int idx) {
			String count = _categoriesCounts.get(idx);
			List<Organization> orgs = _categories.get(count);
			
			int row = idx+1;
			CategoryAnchor categoryAnchor = new CategoryAnchor(count);
			categoryAnchor.setWordWrap(false);
			categoryAnchor.addClickHandler(this);
			_grid.setWidget(row, 0, categoryAnchor);
			_grid.getFlexCellFormatter().setWidth(row, 0, "95%");
			_grid.getFlexCellFormatter().setColSpan(row, 0, 1);
			_grid.getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_LEFT);
			
			Badge badge = new Badge(Integer.toString(orgs.size())); 
			_grid.setWidget(row, 1, badge);
			_grid.getFlexCellFormatter().setHorizontalAlignment(row, 1, HasHorizontalAlignment.ALIGN_RIGHT);
		}
		
		private void displayMore() {
			int row = nbDisplay+1;
			Anchor anchor = new MoreAnchor();
			anchor.addClickHandler(this);
			_grid.setWidget(row, 0, anchor);
			_grid.getFlexCellFormatter().setColSpan(row, 0, 2);
			_grid.getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_RIGHT);
			
		}
		
		@Override
		public void onClick(ClickEvent event) {
			Object src = event.getSource();
			if (src instanceof CategoryAnchor) {
				CategoryAnchor anchor = (CategoryAnchor)src;
				String text = anchor.getText();
				addFilter(text);
			} else if (src instanceof MoreAnchor) {
				nbDisplay += 3;
				nbDisplay = Math.min(nbDisplay, _categoriesCounts.size());
				refresh();
			}
		}

		private void addFilter(String value) {
			ExplorerGrid explorerGrid = _parent;
			Stack<FilteredCollection> stack = explorerGrid._parent._collections;
			FilteredCollection collection = stack.peek();
			List<Organization> organizations = collection._elements;
			List<Organization> filteredElements = new ArrayList<Organization>();
			List<SchoolBoard> schoolBoards = getListSchoolBoards();
			
			for (Organization organization : organizations) {
				String category = _categorizer.categorize(organization, schoolBoards); 
				if (value.equals(category)) {
					filteredElements.add(organization); 
				}
			}
			
			FilteredCollection filteredCollection = new FilteredCollection(value, filteredElements); 
			stack.add(filteredCollection);
			String listName = filteredCollection._name;
			explorerGrid.explore(filteredElements, listName);
		}
	}
	
	/*

	private static class SchoolBoardFilter extends CategoryFilter {
		SchoolBoardFilter(ExplorerGrid parent) {
			super(parent, );
		}
		
		@Override
		public String categorize(Organization organization) {
			String category = null;
			if (organization instanceof School) {
				School school = (School)organization;
				String parentCode = school.getParentCode();
				ExplorerGrid grid = _parent;
				FilterExplorer explorer =  grid._parent; 
				FilteredCollection collection = explorer._collections.get(0);
				List<Organization> organizations = collection._elements;
				for (Organization org : organizations) {
					if (org.getCode().equals(parentCode)) {
						category = org.getName();
						break;
					}
				}
			}
			
			return category;
		}
	}
	
	*/
	
	private static class CategoryAnchor extends Anchor {
		CategoryAnchor(String text) {
			super(text);
			setWordWrap(false);
		}
	}
	
	private static class MoreAnchor extends Anchor {
		MoreAnchor() {
			super("En montrer plus");
			setWordWrap(false);
		}
	}
	
	private static class Badge extends HTML {
		public Badge(String html) {
			super(html); 
			StyleUtil.setBorderRadius(this, "3px");
			Style style = getElement().getStyle();
			style.setBackgroundColor(Color.BLUE.toString());
			style.setColor(Color.WHITE.toString());
			style.setPaddingLeft(3, Unit.PX);
			style.setPaddingRight(3, Unit.PX);
			style.setTextAlign(TextAlign.CENTER);
		}
	}


}
