package org.mslab.tool.educ.client.tool.educ.school.explorer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.mslab.tool.educ.client.core.ui.panels.GridPanel;
import org.mslab.tool.educ.client.core.ui.theme.AbstractTheme;
import org.mslab.tool.educ.client.tool.educ.AbstractExplorer;
import org.mslab.tool.educ.client.tool.educ.EducContent;
import org.mslab.tool.educ.client.tool.educ.EducContext;
import org.mslab.tool.educ.client.tool.educ.EducTheme;
import org.mslab.tool.educ.client.tool.educ.school.viewer.OrganizationAbstractFilter;
import org.mslab.tool.educ.client.tool.services.ServiceStore;
import org.mslab.tool.educ.shared.text.MessageFormat;
import org.mslab.tool.educ.shared.text.StringExt;
import org.mslab.tool.educ.shared.types.Color;
import org.mslab.tool.educ.shared.types.RegionAdministrative;
import org.mslab.tool.educ.shared.types.educ.OrdreAppartenance;
import org.mslab.tool.educ.shared.types.educ.Organization;
import org.mslab.tool.educ.shared.types.educ.School;
import org.mslab.tool.educ.shared.types.educ.SchoolBoard;

import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

public class TreeExplorer extends AbstractExplorer implements ResizeHandler, OpenHandler<TreeItem>, SelectionHandler<TreeItem>, CloseHandler<TreeItem> {
	private Tree _tree;
	private List<Organization> _organizations; 
	private List<SchoolBoard> _schoolBoards; 
	
	public TreeExplorer(EducContent parent) {
		super(parent);		
		_tree = new Tree();
		_tree.addOpenHandler(this); 
		_tree.addCloseHandler(this);
		_tree.addSelectionHandler(this);
		add(_tree);
		
		TreeItem itemLoader = new ItemLoader();
		_tree.addItem(itemLoader);
	}
	
	@Override
	public void onResize(ResizeEvent event) {
		refresh();
	}
	
	private void refresh() {
	}

	@Override
	public void explore(List<Organization> organizations) {
		_organizations = organizations;
		_schoolBoards = new ArrayList<SchoolBoard>();
		for (Organization org : organizations) {
			if (org instanceof SchoolBoard) {
				_schoolBoards.add((SchoolBoard)org);
			}
		} //end for
		
		List<RegionAdministrative> regions = getListRegions(organizations); 
		_tree.clear();
		
		for (RegionAdministrative region : regions) {
			TreeItem item = new RegionTreeItem(region, organizations);
			_tree.addItem(item);
		}
	}
	
	private List<RegionAdministrative> getListRegions(List<Organization> organizations) {
		List<RegionAdministrative> regions = new ArrayList<RegionAdministrative>(); 
		
		for (Organization org : organizations) {
			RegionAdministrative region = org.getRegionAdministrative();
			String name = region.getName();
			
			if (! regions.contains(region) && name.length() > 1) {
				regions.add(region); 
			}	
		}
		
		Collections.sort(regions);
		
		return regions;
	}

	@Override
	public void onOpen(OpenEvent<TreeItem> event) {
		TreeItem item = event.getTarget();
		List<Organization> organizations = new ArrayList<Organization>();
		
		if (item instanceof ItemLoader) {
			Timer timer = new UpdateItemTimer((ItemLoader)item);
			timer.scheduleRepeating(200);
		} else if (item instanceof RegionTreeItem) {
			RegionTreeItem regionItem = (RegionTreeItem)item;
			loadRegion(regionItem, organizations);
			regionItem.setOpened(true); 
			regionItem.refresh(); 
		} else if (item instanceof SchoolBoardItem) { 
			SchoolBoardItem schoolBoardItem = (SchoolBoardItem)item;
			loadSchoolBoard(schoolBoardItem, organizations);
			schoolBoardItem.setOpened(true); 
			schoolBoardItem.refresh(); 
		} else if (item instanceof OrdreAppartenanceItem) { 
			OrdreAppartenanceItem oaItem = (OrdreAppartenanceItem)item;
			loadOrdreAppartenance(oaItem, organizations);
			oaItem.setOpened(true); 
			oaItem.refresh();
		}
	}
	

	@Override
	public void onClose(CloseEvent<TreeItem> event) {
		TreeItem item = event.getTarget();
		
		if (item instanceof RegionTreeItem) {
			RegionTreeItem regionItem = (RegionTreeItem)item;
			regionItem.setOpened(false);
			regionItem.refresh(); 
		} else if (item instanceof SchoolBoardItem) { 
			SchoolBoardItem schoolBoardItem = (SchoolBoardItem)item;
			schoolBoardItem.setOpened(false); 
			schoolBoardItem.refresh(); 
		} else if (item instanceof OrdreAppartenanceItem) { 
			OrdreAppartenanceItem oaItem = (OrdreAppartenanceItem)item;
			oaItem.setOpened(false); 
			oaItem.refresh(); 
		}
	}

	private void loadRegion(RegionTreeItem item, List<Organization> organizations) {
		item.removeItems();
		RegionAdministrative region = item.getRegion();
		List<SchoolBoard> schoolBoards = getSchoolBoardsForRegion(region); 
		
		for (SchoolBoard schoolBoard : schoolBoards) {
			SchoolBoardItem child = new SchoolBoardItem(schoolBoard);
			item.addItem(child);
			
			organizations.add(schoolBoard); 
			loadSchoolBoard(child, organizations);
		}
		
		EnseignentSuperieurItem esi = new EnseignentSuperieurItem(region);
		item.addItem(esi);
	}

	private void loadSchoolBoard(SchoolBoardItem item, List<Organization> organizations) {
		item.removeItems();
		SchoolBoardItem sbi = (SchoolBoardItem)item;
		SchoolBoard schoolBoard = sbi.getSchoolBoard();
		List<OrdreAppartenance> ordres = getOrdreAppartenanceFor(schoolBoard); 
		for (OrdreAppartenance ordre : ordres) { 
			OrdreAppartenanceItem child = new OrdreAppartenanceItem(ordre, schoolBoard);
			item.addItem(child);	
			loadOrdreAppartenance(child, organizations); 
		}
	}
	
	private void loadOrdreAppartenance(OrdreAppartenanceItem item, List<Organization> organizations) {
		item.removeItems();
		SchoolBoardItem sbi = (SchoolBoardItem)item.getParentItem();
		SchoolBoard schoolBoard = sbi.getSchoolBoard();
		List<School> schools = getSchools(schoolBoard); 
		
		OrdreAppartenanceItem oai = (OrdreAppartenanceItem)item;
		OrdreAppartenance ordre = oai.getOrdre();
		if (ordre != null) {
			List<School> sbSchools = getSchools(schools, ordre);
			for (School school : sbSchools) {  
				SchoolItem child = new SchoolItem(school);
				item.addItem(child);
				loadSchool(child, organizations); 
			}
		}

	}

	private void loadSchool(SchoolItem item, List<Organization> organizations) {
		School school = item.getSchool(); 
		organizations.add(school);
	}

	private void loadItems() {
		AsyncCallback<List<Organization>> callback = new AsyncCallback<List<Organization>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub	
			}

			@Override
			public void onSuccess(List<Organization> organizations) {
				EducContext context = EducContext.getInstance();
				context.init(organizations);
				_parent.setOrganizations(organizations);
			}
		};
		ServiceStore.getService().getListOrganizations(callback);
	}

	@Override
	public void onSelection(SelectionEvent<TreeItem> event) {
		TreeItem item = event.getSelectedItem();
		
		if (item instanceof RegionTreeItem) {  
			RegionTreeItem ri = (RegionTreeItem)item;
			showRegion(ri);
		} else if (item instanceof SchoolBoardItem) { 
			 SchoolBoardItem sbi = (SchoolBoardItem)item;
			 SchoolBoard schoolboard = sbi.getSchoolBoard();
			 showSchoolBoard(schoolboard);
		} else if (item instanceof EnseignentSuperieurItem) { 
			EnseignentSuperieurItem esi = (EnseignentSuperieurItem)item;
			RegionAdministrative region = esi.getRegion();
			showEnseignentSuperieurForRegion(region); 
		} else if (item instanceof OrdreAppartenanceItem) { 
			OrdreAppartenanceItem oai = (OrdreAppartenanceItem)item;
			SchoolBoardItem sbi = (SchoolBoardItem)oai.getParentItem();
			SchoolBoard schoolboard = sbi.getSchoolBoard();
			OrdreAppartenance ordre = oai._ordre;
			showSchools(schoolboard, ordre);
		} else if (item instanceof SchoolItem) { 
			 SchoolItem si = (SchoolItem)item;
			 School school = si.getSchool();
			 showOrganization(school);
		 }
	}
	
	private void showEnseignentSuperieurForRegion(RegionAdministrative region) {
		List<Organization> schools = getEnseignementSuperieurForRegion(region);
		OrganizationAbstractFilter filter = OrganizationAbstractFilter.createCategoryFilter(region.getName());
		String listName = filter.getName();
		_parent.update(listName, schools);
	}

	private void showRegion(RegionTreeItem ri) {
		List<Organization> organizations = new ArrayList<Organization>(); 
		
		int nb = ri.getChildCount(); 
		for (int i=0; i<nb; i++) {
			TreeItem child = ri.getChild(i);
			if (child instanceof UnloadedItem) { 
				loadRegion(ri, organizations);
			} else if (child instanceof SchoolBoardItem) { 
				 SchoolBoardItem sbi = (SchoolBoardItem)child;
				 SchoolBoard schoolboard = sbi.getSchoolBoard();
				 organizations.add(schoolboard); 
				 
				 List<School> schools = getSchools(schoolboard); 
				 for (School school : schools) {
					 organizations.add(school);
				 }
			} else if (child instanceof EnseignentSuperieurItem) { 
				EnseignentSuperieurItem esi = (EnseignentSuperieurItem)child;
				RegionAdministrative region = ri._region;
				showEnseignentSuperieurItem(esi, region);
			}
		}
		
		RegionAdministrative region = ri.getRegion();
		OrganizationAbstractFilter filter = OrganizationAbstractFilter.createCategoryFilter(region.getName());
		String listName = filter.getName();
		_parent.update(listName, organizations);
	}

	private void showEnseignentSuperieurItem(EnseignentSuperieurItem esi, RegionAdministrative region) {
		List<Organization> schools = getEnseignementSuperieurForRegion(region); 
		List<Organization> schoolsToShow = new ArrayList<Organization>();
		
		for (Organization school : schools) {
			if (region.equals(school.getRegionAdministrative())) {
				schoolsToShow.add(school); 
			}
		}
		
		OrganizationAbstractFilter filter = OrganizationAbstractFilter.createCategoryFilter(region.getName());
		String listName = filter.getName(); 
		_parent.update(listName, schoolsToShow);
	}

	private void showSchoolBoard(SchoolBoard schoolboard) {
		List<Organization> schoolsToShow = new ArrayList<Organization>();
		schoolsToShow.add(schoolboard); 
		
		List<School> schools = getSchools(schoolboard); 
		for (School school : schools) {
			schoolsToShow.add(school);
		}
		
		OrganizationAbstractFilter filter = OrganizationAbstractFilter.createCategoryFilter(schoolboard.getName());
		String listName = filter.getName(); 
		_parent.update(listName, schoolsToShow);
	}

	private void showSchools(SchoolBoard schoolboard, OrdreAppartenance ordre) {
		List<Organization> schoolsToShow = new ArrayList<Organization>();
		List<School> schools = getSchools(schoolboard); 
		for (School school : schools) {
			if (ordre.equals(school.getOrdreAppartenance())) {
				schoolsToShow.add(school);
			}
		}
		
		OrganizationAbstractFilter filter = OrganizationAbstractFilter.createCategoryFilter(schoolboard.getName());
		String listName = filter.getName(); 
		_parent.update(listName, schoolsToShow);
	}

	private void showOrganization(Organization organization) {
		List<Organization> organizations = new ArrayList<Organization>(); 
		organizations.add(organization);
		
		OrganizationAbstractFilter filter = OrganizationAbstractFilter.createCategoryFilter(organization.getName());
		String listName = filter.getName(); 
		_parent.update(listName, organizations);
	}

	private List<School> getSchools(SchoolBoard schoolBoard) {
		String schoolBoardCode = schoolBoard.getCode();
		List<School> schools = new ArrayList<School>();
		
		for (Organization organization : _organizations) {
			if (organization instanceof School) {
				School school = (School)organization;
				SchoolBoard sb = school.getSchoolBoard(); 
				String parentCode = (sb == null) ? null : sb.getCode(); 
				if (schoolBoardCode.equals(parentCode)) {
					schools.add(school);
				}
			}
		}
		
		return schools;
	}
	
	private List<School> getSchools(List<School> schools, OrdreAppartenance ordre) {
		List<School> oaSchools = new ArrayList<School>();
		for (School school : schools) { 
			OrdreAppartenance oa = school.getOrdreAppartenance();
			if (ordre.equals(oa)) {
				oaSchools.add(school);
			}
		}
		
		return oaSchools;
	}
	
	private List<SchoolBoard> getSchoolBoardsForRegion(
			RegionAdministrative region) {
		List<SchoolBoard> schoolBoards = new ArrayList<SchoolBoard>(); 
		
		for (Organization organization : _organizations) {
			if (organization instanceof SchoolBoard) {
				SchoolBoard schoolBoard = (SchoolBoard)organization;
				RegionAdministrative r = schoolBoard.getRegionAdministrative();
				if (r.equals(region)) {
					schoolBoards.add(schoolBoard);
				}
			}
		}
		
		return schoolBoards;
	}
	
	private List<Organization> getEnseignementSuperieurForRegion(RegionAdministrative region) {
		List<Organization> enseignmentsSuperieurSchools = new ArrayList<Organization>(); 
		
		for (Organization organization : _organizations) {
			if (organization instanceof School) {
				School school = (School)organization;
				if (region.equals(school.getRegionAdministrative())) {
					SchoolBoard sb = school.getSchoolBoard(); 
					String parent = (sb == null) ? null : sb.getCode();
					if (StringExt.isNullOrWhitespace(parent)) {
						enseignmentsSuperieurSchools.add(school); 
					}
				}
			}
		}
		
		return enseignmentsSuperieurSchools; 
	}
	
	private List<OrdreAppartenance> getOrdreAppartenanceFor(SchoolBoard schoolBoard) {
		List<OrdreAppartenance> ordres = new ArrayList<OrdreAppartenance>();
		String schoolBoardCode = schoolBoard.getCode();
		
		for (Organization organization : _organizations) {
			if (organization instanceof School) {
				School school = (School)organization;
				SchoolBoard sb = school.getSchoolBoard(); 
				String parentCode = (sb == null) ? null : sb.getCode();

				if (schoolBoardCode.equals(parentCode)) {
					OrdreAppartenance ordre = school.getOrdreAppartenance();
					if (! ordres.contains(ordre)) {
						ordres.add(ordre);
					}
				}
			}
		}
		Collections.sort(ordres);
		
		return ordres;
	}
	
	//
	// inner classes
	//
	private static class ItemLoader extends TreeItem { 
		ItemLoader() {
			setHTML("Lire les établissements");
			addTextItem(""); //temporarily add an item so we can expand this node
		}
	}
	
	private static class RegionTreeItem extends TreeItem {
		private RegionTreeWidget _widget;
		private RegionAdministrative _region; 
		private int _count; 
		
		public RegionTreeItem(RegionAdministrative region, List<Organization> organizations) {
			_region = region;
			_count = 0; 
			
			for (Organization org : organizations) {
				_count += region.equals(org.getRegionAdministrative()) ? 1 : 0; 
			}
						
			//temporarily add an item so we can expand this node
			UnloadedItem unloaded = new UnloadedItem();
			addItem(unloaded);
			
			_widget = new RegionTreeWidget(region, _count);
			setWidget(_widget);
			refresh();
		}

		public void setOpened(boolean opened) {
			_widget.setOpened(opened); 
		}

		public void refresh() {
			_widget.refresh();
		}

		public RegionAdministrative getRegion() {
			return _region;
		}
	}
	
	private static class RegionTreeWidget extends AbstractTreeWidget {
		private RegionAdministrative _region; 
		
		RegionTreeWidget(RegionAdministrative region, int count) {
			super();
			_region = region;
			String code = _region.getCode();
			String name = _region.getName(); 
			String text = MessageFormat.format("{0} {1} ({2})", new Object[] {code, name, count});
			 _treeLabel.setHTML(text);
		}
	}
	
	private static abstract class AbstractTreeWidget extends GridPanel {
		private AbsolutePanel _iconPanel;
		protected HTML _folder, _folderOutline, _treeLabel;
		private boolean _opened;
		
		protected AbstractTreeWidget() {
			_iconPanel = new AbsolutePanel();
			_grid.setWidget(0, 0, _iconPanel);
			
			_folder = new HTML();
			_iconPanel.add(_folder);
			
			_folderOutline = new HTML();
			_iconPanel.add(_folderOutline, 0, 0);

			_treeLabel = new HTML();
			 _grid.setWidget(0, 1, _treeLabel);
			
			refresh();
		}

		public void setOpened(boolean opened) {
			_opened = opened;
			refresh();
		}

		public void refresh() {
			EducTheme theme = (EducTheme)EducTheme.getTheme();
			Color fg = theme.getPrimaryFgColor(); 
			Color bg = theme.getPrimaryBgColor(); 
			
			String icon = _opened ? "folder-open" : "folder";
			String text = MessageFormat.format("<i class=\"fa fa-{0}\">", new Object[] {icon});
			_folder.setHTML(text);
			text = MessageFormat.format("<i class=\"fa fa-{0}-o\">", new Object[] {icon});
			_folderOutline.setHTML(text);
			
			_folder.getElement().getStyle().setColor(bg.toString());
			_folderOutline.getElement().getStyle().setColor(fg.toString());
		}
	}
	
	private class EnseignentSuperieurItem extends TreeItem {
		private EnseignentSuperieurTreeWidget _widget;
		private RegionAdministrative _region;
		
		public EnseignentSuperieurItem(RegionAdministrative region) {
			_region = region;
			_widget = new EnseignentSuperieurTreeWidget();
			setWidget(_widget);
			
			 List<Organization> organizations = getEnseignementSuperieurForRegion(region);
			 for (Organization organization : organizations) {
				 if (organization instanceof School) {
					 SchoolItem item = new SchoolItem((School)organization);
					 addItem(item);
				 }
			 }
		}

		public RegionAdministrative getRegion() {
			return _region;
		}
	} //end EnseignentSuperieurItem
	
	private static class EnseignentSuperieurTreeWidget extends AbstractTreeWidget {
		private RegionAdministrative _region; 
		
		EnseignentSuperieurTreeWidget() {
			 _treeLabel.setHTML("Enseignement supérieur");
		}
	}
	
	private static class SchoolBoardItem extends TreeItem {
		private SchoolBoardTreeWidget _treeWidget;
		private SchoolBoard _schoolBoard; 
		
		public SchoolBoardItem(SchoolBoard schoolBoard) {
			_treeWidget = new SchoolBoardTreeWidget(schoolBoard);
			_schoolBoard = schoolBoard;
			setWidget(_treeWidget);
			
			//temporarily add an item so we can expand this node
			UnloadedItem unloaded = new UnloadedItem();
			addItem(unloaded);
			
			refresh();
		}

		public void setOpened(boolean opened) {
			_treeWidget.setOpened(opened);
		}

		public void refresh() {
			_treeWidget.refresh();
		}

		public SchoolBoard getSchoolBoard() {
			return _schoolBoard;
		}
	}
	
	private static class SchoolBoardTreeWidget extends AbstractTreeWidget {
		private RegionAdministrative _region; 
		
		SchoolBoardTreeWidget(SchoolBoard schoolBoard) {
			super();
			String name = schoolBoard.getName();
			int count = schoolBoard.getSchools().size();
			String text = MessageFormat.format("{0} ({1})", 
				name, count); 
			_treeLabel.setHTML(text);
		}
	}
	
	private static class SchoolItem extends TreeItem {
		private School _school; 
		
		public SchoolItem(School school) {
			_school = school;
			String name = school.getName();
			setHTML(name);
			
			//temporarily add an item so we can expand this node
			UnloadedItem unloaded = new UnloadedItem();
			addItem(unloaded);
		}

		public School getSchool() {
			return _school;
		}
	}
	
	private static class OrdreAppartenanceItem extends TreeItem {
		private OrdreAppartenanceTreeWidget _treeWidget;
		private OrdreAppartenance _ordre; 
		
		public OrdreAppartenanceItem(OrdreAppartenance ordre, SchoolBoard schoolBoard) {
			List<School> schools = schoolBoard.getSchools(); 
			List<School> schoolsInOrdre = getSchoolsInOrder(schools, ordre);
			int count = schoolsInOrdre.size();
			
			_treeWidget = new OrdreAppartenanceTreeWidget(ordre, count);
			_ordre = ordre;
			
			
			setWidget(_treeWidget);
			
			//temporarily add an item so we can expand this node
			UnloadedItem unloaded = new UnloadedItem();
			addItem(unloaded);
			
			refresh();
		}

		public void setOpened(boolean opened) {
			_treeWidget.setOpened(opened);
		}

		public void refresh() {
			_treeWidget.refresh();
		}

		private List<School> getSchoolsInOrder(List<School> schools, OrdreAppartenance ordre) {
			List<School> schoolsInOrder = new ArrayList<School>(); 
			for (School s : schools) {
				OrdreAppartenance o = s.getOrdreAppartenance(); 
				if (ordre.equals(o)) {
					schoolsInOrder.add(s); 
				}
			}
			return schoolsInOrder;
		}

		public OrdreAppartenance getOrdre() {
			return _ordre;
		}

		public String getName() {
			return _ordre.getName();
		}
	}
	
	private static class OrdreAppartenanceTreeWidget extends AbstractTreeWidget {
		private RegionAdministrative _region; 
		
		OrdreAppartenanceTreeWidget(OrdreAppartenance ordre, int count) {
			super();
			String name = ordre.getName();
			String text = MessageFormat.format("{0} ({1})", 
				name, count); 
			_treeLabel.setHTML(text);
		}
	}
	
	private static class UnloadedItem extends TreeItem {
	}
	
	private class UpdateItemTimer extends Timer {
		private ItemLoader _item;
		private int _totalInitial;
		private String _spinner, _check; 
		
		UpdateItemTimer(ItemLoader item) {
			_item = item;
			_totalInitial = 2760;
			AbstractTheme theme = AbstractTheme.getTheme();
			_check = MessageFormat.format("<span style=\"color:{0}\"><i class=\"fa fa-check\"></i></span>", new Object[] {Color.GREEN.toString()});
			_spinner = MessageFormat.format("<span style=\"color:{0}\"><i class=\"fa fa-refresh fa-spin\"></i></span>", new Object[] {theme.getPrimaryFgColor().toString()}); 
		}
		
		@Override
		public void run() {
			_totalInitial -= 295; 
			_totalInitial = Math.max(_totalInitial, 0);
			
			if (_totalInitial > 0) {
				String text = MessageFormat.format("{0} Lecture de {1} établissements..", new Object[] {_spinner, _totalInitial}); 
				_item.setHTML(text);
			} else {
				String text = MessageFormat.format("{0} Initialisation de l'arbre avec {1} établissements..", new Object[] {_spinner, 2760}); 
				_item.setHTML(text);
				cancel();
				loadItems();
			}
		}
	}








}
