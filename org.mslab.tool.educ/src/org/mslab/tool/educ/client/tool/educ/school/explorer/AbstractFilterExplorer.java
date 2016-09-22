package org.mslab.tool.educ.client.tool.educ.school.explorer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.mslab.tool.educ.client.core.ui.CloseButton2;
import org.mslab.tool.educ.client.core.ui.StyleUtil;
import org.mslab.tool.educ.client.core.ui.panels.GridPanel;
import org.mslab.tool.educ.client.core.ui.theme.ThematicAnchor;
import org.mslab.tool.educ.client.core.ui.theme.ThematicBadge;
import org.mslab.tool.educ.client.tool.educ.school.explorer.AbstractFilterCategory.AbstractCategorizer;
import org.mslab.tool.educ.shared.types.Color;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.SimplePanel;

public abstract class AbstractFilterExplorer<T> extends GridPanel implements EntityExplorable<T> {
	private String _nameSingular, _namePlural; 
	private EntityViewable<T> _viewable;
	private Stack<FilteredCollection<T>> _collections = new Stack<FilteredCollection<T>>();
	private FilterListOutline<T> _outline; 
	private FilterCategoryListPanel<T> _categoryListPanel;
	
	protected AbstractFilterExplorer(EntityViewable<T> entityViewable, 
			List<T> entities,
			String nameSingular,
			String namePlural) {
		_viewable = entityViewable;
		_nameSingular = nameSingular;
		_namePlural = namePlural;
		int row = 0;
		_outline = new FilterListOutline<T>(this);
		_grid.setWidget(row, 0, _outline);
		_grid.getElement().getStyle().setWidth(100, Unit.PCT);
		row++;
		
		_categoryListPanel = new FilterCategoryListPanel<T>(this);
		_grid.setWidget(row, 0, _categoryListPanel);
		_grid.getElement().getStyle().setWidth(100, Unit.PCT);
		row++;
		
		init("Total", entities);
	}
	
	@Override
	public void init(String title, List<T> entities) {
		FilteredCollection<T> collection = new FilteredCollection<T>("Total", entities); 
		_collections.clear();
		_collections.add(collection); 
		update();
	}
	
	protected void addFilter(AbstractCategorizer<T> categorizer) {
		_categoryListPanel.addFilter(categorizer);
	}
	
	@Override
	public void update() {
		_outline.update();
		_categoryListPanel.update();
		
		FilteredCollection<T> collection = _collections.peek();
		List<T> entities = collection.getElements(); 
		_viewable.update(collection.getName(), entities);
	}
	
	@Override
	public Stack<FilteredCollection<T>> getCollectionStack() {
		return _collections;
	}
	
	@Override
	public void removeLastFilter() {
		_collections.pop();
		//FilteredCollection<T> collection = _collections.peek();
		//List<T> entities = collection.getElements(); 
		//String listName = collection.getName();
		update();
	}
	
	//
	// inner classes
	//
	
	private class FilterListOutline<T> extends GridPanel {
		private EntityExplorable<T> _parent; 
		
		FilterListOutline(EntityExplorable<T> parent) {
			_parent = parent;
			_grid.setCellPadding(0);
			_grid.setCellSpacing(0);
			_grid.setWidth("100%");
		}
		
	    void update() {
			_grid.clear();
			Stack<FilteredCollection<T>> collections = _parent.getCollectionStack();
			int row = 0;
			
			for (FilteredCollection<T> collection : collections) {
				boolean isLast = (row == collections.size() -1);
				explore(row, isLast, collection);
				row++;
			}
		}
		
		private void explore(int row, boolean isLast, FilteredCollection<T> collection) {
			String name = collection.getName();
			List<T> elements = collection.getElements();
			if (elements != null) {
				int count = collection.getElements().size();
				
				boolean isFirst = (row == 0); 
				CategoryOutline<T> outline = new CategoryOutline<T>(_parent, name, count, isFirst, isLast);
				outline.setWidth("100%");
				_grid.setWidget(row, 0, outline);
			}
		}
	} 
	
	private class CategoryOutline<T> extends GridPanel implements ClickHandler { 
		private HTML _countLbl;
		private CloseButton2 _closeBtn; 
		private EntityExplorable<T> _explorable; 
		
		CategoryOutline(EntityExplorable<T> explorable, String name, int count, boolean first, boolean last) {
			int row = 0;
			_explorable = explorable;
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
			
			_closeBtn = new CloseButton2();
			_closeBtn.getElement().getStyle().setMarginLeft(12, Unit.PX);
			_closeBtn.setVisible(!first && last);
			_closeBtn.addClickHandler(this);
			_grid.setWidget(row, 1, _closeBtn);
			row++; 
			
			String itemName = (count <= 1) ? _nameSingular : _namePlural;
			_countLbl = new HTML(Integer.toString(count) + " " + itemName); 
			_countLbl.getElement().getStyle().setFontSize(80, Unit.PCT);
			_grid.setWidget(row, 0, _countLbl);
			_grid.getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_RIGHT);
			_grid.getFlexCellFormatter().setColSpan(row, 0, 2);
			row++;
		}
		
		/*
		public void update() {
			
		} //end update()*/
		
		@Override
		public void onClick(ClickEvent event) {
			Object src = event.getSource(); 
			
			if (src.equals(_closeBtn)) {
				close();
			}
		}
		
		private void close() {
			_explorable.removeLastFilter();
		}
	} //end CategoryOutline
	
	private class FilterCategoryListPanel<T> extends GridPanel {
		private List<FilterCategoryPanel> _filterPanels = new ArrayList<FilterCategoryPanel>();
		private EntityExplorable<T> _explorable; 
		
		FilterCategoryListPanel(EntityExplorable<T> explorable) {
			_explorable = explorable;
		}
		
		private void addFilter(AbstractFilterCategory.AbstractCategorizer<T> categorizer) {
			_filterPanels.add(new FilterCategoryPanel(this, categorizer));
			int row = 0; 
			
			for (FilterCategoryPanel filterPanel : _filterPanels) {
				_grid.setWidget(row, 0, filterPanel);
				_grid.getElement().getStyle().setWidth(100, Unit.PCT);
				row++;
			}
			
			SimplePanel filler = new SimplePanel();
			_grid.setWidget(row, 0, filler);
			_grid.getFlexCellFormatter().setWidth(row, 0, "95%");
			row++;
		}

		public void update() {
			for (FilterCategoryPanel filterPanel : _filterPanels) {
				filterPanel.update();
			}
		}

		public EntityExplorable<T> getPersonExplorable() {
			return _explorable;
		}
	} //end FilterCategoryListPanel
	
	private class FilterCategoryPanel extends GridPanel implements ClickHandler { 
		private FilterCategoryListPanel<T> _parent; 
		private AbstractFilterCategory.AbstractCategorizer _categorizer;
		protected Map<String, List<T>> _personByCategories = new HashMap<String, List<T>>(); 
		protected List<String> _categoriesCounts = new ArrayList<String>();
		private HTML _categoryLbl;
		private int _nbDisplay; 
		
		FilterCategoryPanel(FilterCategoryListPanel parent, AbstractFilterCategory.AbstractCategorizer categorizer) {
			_parent = parent;
			_categorizer = categorizer;
			_grid.setWidth("100%");
		}

		@Override
		public void onClick(ClickEvent event) {
			Object src = event.getSource();
			if (src instanceof CategoryAnchor) {
				CategoryAnchor anchor = (CategoryAnchor)src;
				String text = anchor.getText();
				addFilter(text);
			} else if (src instanceof MoreAnchor) {
				_nbDisplay += 3;
				_nbDisplay = Math.min(_nbDisplay, _categoriesCounts.size());
				refresh();
			}
		}
		
		private void addFilter(String value) {
			EntityExplorable explorable = _parent.getPersonExplorable(); 
			Stack<FilteredCollection> stack = explorable.getCollectionStack(); 
			FilteredCollection collection = stack.peek();
			List<T> persons = collection.getElements();
			List<T> filteredElements = new ArrayList<T>();
			
			for (T person : persons) {
				String category = _categorizer.categorize(person, persons); 
				if (value.equals(category)) {
					filteredElements.add(person); 
				}
			}
			
			FilteredCollection filteredCollection = new FilteredCollection(value, filteredElements); 
			stack.add(filteredCollection);
			explorable.update(); 
		}
		
		public void update() {
			EntityExplorable explorable = _parent.getPersonExplorable(); 
			Stack<FilteredCollection> stack = explorable.getCollectionStack(); 
			FilteredCollection current = stack.peek(); 
			
			List<T> persons = current.getElements();
			_personByCategories.clear();
			_categoriesCounts.clear();
			
			if (persons != null) {
				for (T person : persons) {
					String category = _categorizer.categorize(person, persons);
					
					if (category != null) {
						List<T> personsInCategory = _personByCategories.get(category);
						if (personsInCategory == null) {
							personsInCategory = new ArrayList<T>();
							_personByCategories.put(category, personsInCategory);
							_categoriesCounts.add(category);
						}
						personsInCategory.add(person);
					}
				} 
			}

			_grid.clear();
			_grid.removeAllRows();
			
			String categoryName = _categorizer.getName();
			_categoryLbl = new HTML(categoryName);
			_categoryLbl.getElement().getStyle().setFontWeight(FontWeight.BOLD);
			_grid.setWidth("100%");
			_grid.setWidget(0, 0, _categoryLbl);
			_grid.getFlexCellFormatter().setColSpan(0, 0, 2);
			_grid.getFlexCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
			
			//sort and display categories
			Collections.sort(_categoriesCounts, new Comparator<String>() {
				@Override
				public int compare(String k1, String k2) {
					return _personByCategories.get(k2).size() - _personByCategories.get(k1).size();
				}
			});
			
			_nbDisplay = Math.min(3, _categoriesCounts.size());
			refresh(); 
		} //end update()
		
		private void refresh() {
			setVisible(_nbDisplay > 1);
			
			for (int i=0; i<_nbDisplay; i++) {
				displayLine(i);
			}
			
			if (_nbDisplay < _categoriesCounts.size()) {
				displayMore();
			}
		}
		
		private void displayLine(int idx) {
			String count = _categoriesCounts.get(idx);
			List<T> persons = _personByCategories.get(count);
			
			int row = idx+1;
			CategoryAnchor categoryAnchor = new CategoryAnchor(count);
			categoryAnchor.setWordWrap(false);
			categoryAnchor.addClickHandler(this);
			_grid.setWidget(row, 0, categoryAnchor);
			_grid.getFlexCellFormatter().setWidth(row, 0, "95%");
			_grid.getFlexCellFormatter().setColSpan(row, 0, 1);
			_grid.getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_LEFT);
			
			HTML badge = new ThematicBadge(Integer.toString(persons.size())); 
			_grid.setWidget(row, 1, badge);
			_grid.getFlexCellFormatter().setHorizontalAlignment(row, 1, HasHorizontalAlignment.ALIGN_RIGHT);
		}
		
		private void displayMore() {
			int row = _nbDisplay+1;
			Anchor anchor = new MoreAnchor();
			anchor.addClickHandler(this);
			_grid.setWidget(row, 0, anchor);
			_grid.getFlexCellFormatter().setColSpan(row, 0, 2);
			_grid.getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		}
	} //end FilterCategoryPanel

	private static class CategoryAnchor extends ThematicAnchor {
		CategoryAnchor(String text) {
			super(text);
			setWordWrap(false);
		}
	}
	
	private static class MoreAnchor extends ThematicAnchor {
		MoreAnchor() {
			super("En montrer plus");
			setWordWrap(false);
		}
	}

	






}
