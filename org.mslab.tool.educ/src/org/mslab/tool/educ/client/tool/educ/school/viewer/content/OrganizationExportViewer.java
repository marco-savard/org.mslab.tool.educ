package org.mslab.commons.client.tool.educ.school.viewer.content;

import java.util.List;

import org.mslab.commons.client.core.ui.Refreshable;
import org.mslab.commons.client.core.ui.panels.CommonDisclosurePanel;
import org.mslab.commons.client.core.ui.panels.GridPanel;
import org.mslab.commons.client.core.ui.theme.ThematicButton;
import org.mslab.commons.client.core.ui.theme.ThematicLabel;
import org.mslab.commons.client.tool.educ.school.viewer.content.OrganizationComparator.Criteria;
import org.mslab.commons.shared.educ.util.OrganizationWriter;
import org.mslab.commons.shared.services.DownloadService;
import org.mslab.commons.shared.text.MessageFormat;
import org.mslab.commons.shared.text.Text;
import org.mslab.commons.shared.types.educ.Organization;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.FontStyle;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class OrganizationExportViewer extends AbstractContentViewer {
	private OptionSection _optionPanel; 
	private PreviewSection _previewPanel; 
	private ButtonPanel _buttonPanel;
	private String _listName;
	private List<Organization> _organizations; 
	private int _fileSize, _nbLines;
	private String _quote = "";
	private Criteria _sortCriteria;
	private boolean _sortAscendent;
	
	private RadioButton _delim0, _delim1, _delim2; 
	private RadioButton _separ0, _separ1; 
	private TextBox _delimitorTB, _separatorTB;
	
	public OrganizationExportViewer() {
		_grid.setWidth("100%");
		int row = 0;
		
		SimplePanel margin = new SimplePanel(); 
		_grid.setWidget(row, 0, margin);
		_grid.getFlexCellFormatter().setWidth(row, 0, "45%");
		
		_optionPanel = new OptionSection(); 
		SectionDisclosurePanel optionSection = new SectionDisclosurePanel(null, _optionPanel, "Cacher les param&egrave;tres de formattage", "Afficher les param&egrave;tres de formattage");  
		optionSection.getElement().getStyle().setMarginTop(36, Unit.PX);
		optionSection.setExpanded(false);
		_grid.setWidget(row, 1, optionSection);
		
		margin = new SimplePanel(); 
		_grid.setWidget(row, 2, margin);
		_grid.getFlexCellFormatter().setWidth(row, 2, "45%");
		row++;
		
		_previewPanel = new PreviewSection(); 
		SectionDisclosurePanel previewSection = new SectionDisclosurePanel(null, _previewPanel, "Cacher l'aper&ccedil;u", "Afficher l'aper&ccedil;u");  
		previewSection.getElement().getStyle().setMarginTop(36, Unit.PX);
		previewSection.setExpanded(false);
		_grid.setWidget(row, 1, previewSection);
		row++;
		
		_buttonPanel = new ButtonPanel();
		_buttonPanel.getElement().getStyle().setMarginTop(36, Unit.PX);
		_grid.setWidget(row, 1, _buttonPanel);
		_grid.getFlexCellFormatter().setHorizontalAlignment(row, 1, HasHorizontalAlignment.ALIGN_CENTER);
	}
	
	@Override
	public void showOrganizations(String listName, List<Organization> organizations) {
		_listName = listName;
		_organizations = organizations;
		_previewPanel.refresh();
		_buttonPanel.refresh();
	}
	
	@Override
	public void sort(Criteria criteria, boolean ascendent) {
		_sortCriteria = criteria;
		_sortAscendent = ascendent;
		_previewPanel.refresh();
	}
	
	private static class SectionDisclosurePanel extends CommonDisclosurePanel {
		private String _hideText, _showText; 
		
		public SectionDisclosurePanel(String name, Widget childWidget, String hideText, String showText) {
			super(name, childWidget);
			this.getElement().getStyle().setFontSize(120, Unit.PCT);
			
			_hideText = hideText;
			_showText = showText;
			setExpanded(false); 
			refresh();
		}
		
		@Override
		public void onOpen(OpenEvent<DisclosurePanel> event) {
			super.onOpen(event);
			refresh();
		}
		
		@Override
		public void onClose(CloseEvent<DisclosurePanel> event) {
			super.onClose(event);
			refresh();
		}
		
		private void refresh() {
			if (_childWidget instanceof Refreshable) {
				Refreshable refreshable = (Refreshable)_childWidget;
				refreshable.refresh();
			}
			
			String html = _expanded ? _hideText : _showText;
			HTML label = (HTML)_showHeader.getWidget(0); 
			label.setHTML(html); 
		}
	}

	private class OptionSection extends GridPanel implements ClickHandler, KeyUpHandler {		
		OptionSection() {
			int row = 0, col = 0;
			
			DelimitorOptionSection delimitorOptionSection = new DelimitorOptionSection(this);
			_grid.setWidget(row, col, delimitorOptionSection);
			col++;

			SeparatorOptionSection separatorOptionSection = new SeparatorOptionSection(this);
			separatorOptionSection.getElement().getStyle().setMarginLeft(24, Unit.PX);
			_grid.setWidget(row, col, separatorOptionSection);
			_grid.getFlexCellFormatter().setVerticalAlignment(row, col, HasVerticalAlignment.ALIGN_TOP);
			col++;
					
			refresh();
		}

		@Override
		public void onClick(ClickEvent event) {
			Object src = event.getSource(); 
			
			if (src.equals(_delim0)) {
				_delimitorTB.setText("");
			} else if (src.equals(_delim1)) {
				_delimitorTB.setText("\"");
			} else if (src.equals(_separ0)) {
				_separatorTB.setText(";");
			}
			
			refresh(); 
		}
		
		@Override
		public void onKeyUp(KeyUpEvent event) {
			refresh(); 
		}

		private void refresh() {
			_delimitorTB.setEnabled(_delim2.getValue());
			_separatorTB.setEnabled(_separ1.getValue());
			
			if (_previewPanel != null) {
				_previewPanel.refresh();
			}
		}
	}
	
	private class DelimitorOptionSection extends GridPanel implements ClickHandler, KeyUpHandler {
		private OptionSection _owner; 
		
		DelimitorOptionSection(OptionSection owner) {
			_owner = owner;
			int row = 0; 
			
			HTML delimitorLbl = new HTML("D&eacute;limiteur : ");
			_grid.setWidget(row, 0, delimitorLbl);
			row++;
			
			_delim0 = new RadioButton("delimitor", "Aucun");
			_delim0.setValue(true);
			_delim0.addClickHandler(this); 
			_grid.setWidget(row, 0, _delim0);
			row++;
			
			_delim1 = new RadioButton("delimitor", "Guillemets droits");
			_delim1.addClickHandler(this); 
			_grid.setWidget(row, 0, _delim1);
			row++;
			
			_delim2 = new RadioButton("delimitor", "Libre");
			_delim2.addClickHandler(this); 
			_grid.setWidget(row, 0, _delim2);
			
			_delimitorTB = new TextBox();
			_delimitorTB.setWidth("2em");
			_delimitorTB.addKeyUpHandler(this);
			_grid.setWidget(row, 1, _delimitorTB);
			row++;
		}
		
		@Override
		public void onClick(ClickEvent event) {
			Object src = event.getSource(); 
			
			if (src.equals(_delim0)) {
				_delimitorTB.setText("");
			} else if (src.equals(_delim1)) {
				_delimitorTB.setText("\"");
			}
			
			_owner.refresh(); 
		}
		
		@Override
		public void onKeyUp(KeyUpEvent event) {
			_owner.refresh(); 
		}
	}
	
	private class SeparatorOptionSection extends GridPanel implements ClickHandler, KeyUpHandler {		
		private OptionSection _owner; 
		
		SeparatorOptionSection(OptionSection owner) {
			_owner = owner;
			int row = 0;
			
			HTML separatorLbl = new HTML("S&eacute;parateur : ");
			_grid.setWidget(row, 0, separatorLbl);
			row++;

			_separ0 = new RadioButton("separator", "Point-virgule");
			_separ0.setValue(true);
			_separ0.addClickHandler(this); 
			_grid.setWidget(row, 0, _separ0);
			row++;
			
			_separ1 = new RadioButton("separator", "Libre");
			_separ1.addClickHandler(this); 
			_grid.setWidget(row, 0, _separ1);
			
			_separatorTB = new TextBox();
			_separatorTB.setText(";");
			_separatorTB.setWidth("2em");
			_separatorTB.addKeyUpHandler(this);
			_grid.setWidget(row, 1, _separatorTB);
			row++;
			
			_owner.refresh();
		}

		@Override
		public void onClick(ClickEvent event) {
			Object src = event.getSource(); 
			
			if (src.equals(_delim0)) {
				_delimitorTB.setText("");
			} else if (src.equals(_delim1)) {
				_delimitorTB.setText("\"");
			} else if (src.equals(_separ0)) {
				_separatorTB.setText(";");
			}
			
			_owner.refresh(); 
		}
		
		@Override
		public void onKeyUp(KeyUpEvent event) {
			_owner.refresh(); 
		}
	}
	
	private class PreviewSection extends GridPanel implements ClickHandler {
		private ScrollPanel _scrollPanel; 
		private TextArea _area;
		private SelectAllButton _selectAllBtn;
		private OrganizationWriter _csvWriter;
		
		PreviewSection() {
			int row = 0;
			_csvWriter = new OrganizationWriter();
			
			_scrollPanel = new ScrollPanel(); 
			_scrollPanel.setAlwaysShowScrollBars(true);
			_scrollPanel.setWidth("500px");
			_scrollPanel.setHeight("300px");
			_grid.setWidget(row, 0, _scrollPanel);
			_grid.getFlexCellFormatter().setWidth(row, 0, "100%");
			_grid.getFlexCellFormatter().setColSpan(row, 0, 3);
			
			_area = new TextArea(); 
			_area.setReadOnly(true);
			_scrollPanel.add(_area);
			row++;
			
			_selectAllBtn = new SelectAllButton(); 
			_selectAllBtn.addClickHandler(this);
			_grid.setWidget(row, 0, _selectAllBtn);
			
			HTML instr = new HTML("puis copier et coller dans un fichier texte"); 
			instr.setWordWrap(false);
			instr.getElement().getStyle().setFontStyle(FontStyle.ITALIC);
			_grid.setWidget(row, 1, instr);
			_grid.getFlexCellFormatter().setHorizontalAlignment(row, 1, HasHorizontalAlignment.ALIGN_LEFT);
			
			SimplePanel filler = new SimplePanel();
			_grid.setWidget(row, 2, filler);
			_grid.getFlexCellFormatter().setWidth(row, 2, "90%");
		}

		public void refresh() {
			String delimitor = _delimitorTB.getText();
			String separator = _separatorTB.getText();

			_csvWriter.setSortCriteria(_sortCriteria); 
			_csvWriter.setSortDirection(_sortAscendent);
			_csvWriter.setDelimitor(delimitor);
			_csvWriter.setSeparator(separator);
		 	_fileSize = _csvWriter.write(_organizations);
			String text = _csvWriter.getText(); 
			int nbColumns = _csvWriter.getNbColumns(); 
			_nbLines = _csvWriter.getNbLines();
			
			_area.setText(text);
			_area.setCharacterWidth(nbColumns + 10);
			_area.setVisibleLines(_nbLines + 5);
		}

		@Override
		public void onClick(ClickEvent event) {
			Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
		        @Override
		        public void execute() {
		            // your commands here
		           // myText.setVisible(true);
		            _area.setFocus(true);
		            _area.selectAll();
		            _area.getSelectionLength();
		        }
		});
		}
		
	}
	
	private class ButtonPanel extends GridPanel implements ClickHandler {
		private TextBox _filenameBox, _fileExtBox;
		private DownloadButton _exportButton;
		private HTML _detailLabel;
		
		ButtonPanel() {
			_grid.setCellPadding(0);
			_grid.setCellSpacing(0);
			_grid.setWidth("100%");
			int row = 0, col = 0;
			
			HTML title = new HTML("T&eacute;l&eacute;charger sous le nom : "); 
			title.setWordWrap(false);
			Element element = title.getElement();
			Style style = element.getStyle(); 
			style.setProperty("fontFamily", "Roboto, sans-serif");
			style.setColor("black");
			style.setMarginRight(24, Unit.PX);
			style.setFontSize(100, Unit.PCT);
			
			_grid.setWidget(row, col, title);
			_grid.getFlexCellFormatter().setWidth(0, col, "45%");
			_grid.getFlexCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_RIGHT);
			col++; 
			
		     _filenameBox = new TextBox();
		     _filenameBox.getElement().getStyle().setFontSize(120, Unit.PCT);
			_grid.setWidget(row, col, _filenameBox);
			col++;
			
			Label period = new Label("."); 
			period.getElement().getStyle().setMarginLeft(6, Unit.PX);
			period.getElement().getStyle().setMarginRight(6, Unit.PX);
			period.getElement().getStyle().setFontSize(120, Unit.PCT);
			period.getElement().getStyle().setColor("black");
			_grid.setWidget(row, col, period);
			col++;
			
			_fileExtBox = new TextBox();
			_fileExtBox.getElement().getStyle().setWidth(2, Unit.EM);
			_fileExtBox.getElement().getStyle().setFontSize(120, Unit.PCT);
			_grid.setWidget(row, col, _fileExtBox);
			col++;
			
			_exportButton = new DownloadButton();
			_exportButton.getElement().getStyle().setMarginLeft(30, Unit.PX);
			_exportButton.addClickHandler(this);
			_grid.setWidget(row, col, _exportButton);
			col++;
			
			SimplePanel rightFiller = new SimplePanel(); 
			_grid.setWidget(row, col, rightFiller);
			_grid.getFlexCellFormatter().setWidth(0, col, "45%");
			row++; col = 0;
			
			col++;
			_detailLabel = new HTML(); 
			_detailLabel.getElement().getStyle().setFontStyle(FontStyle.ITALIC);
			_grid.setWidget(row, col, _detailLabel);
			_grid.getFlexCellFormatter().setColSpan(row, col, 4);
			
			refresh();
		}
		
		public void refresh() {
			String filename = Text.toUnaccentued(_listName);
			_filenameBox.setText(filename);
			_fileExtBox.setText("cvs");
			
			String html = MessageFormat.format("Fichier de {0} KO, {1} lignes", (_fileSize/1024), _nbLines); 
			_detailLabel.setHTML(html);
		}

		@Override
		public void onClick(ClickEvent event) {
			exportFile(); 
		}
		
		private void exportFile() {
			StringBuffer codes = new StringBuffer(); 
			
			for (Organization organization : _organizations) {
				String code = organization.getCode();
				codes.append(code + ";"); 
			}
			
			String filename = _filenameBox.getText();
			String fileExt = "csv";
			String sep = ";"; 
			
			String url = GWT.getModuleBaseURL() + "downloadService"; 
			url += "?" + DownloadService.FILENAME + "=" + filename;
			url += "&" + DownloadService.FILEEXT + "=" + fileExt;
			url += "&" + DownloadService.SEP + "=" + sep;
			url += "&" + DownloadService.CODES + "=" + codes;
			//url += "&" + "end=1";
			Window.open(url, "_blank", "status=0,toolbar=0,menubar=0,location=0");
		}
	}
	
	private static class SelectAllButton extends ThematicButton {
		SelectAllButton() {
			super("Tout s&eacute;lectionner"); 
		}
	}
	
	private static class DownloadButton extends ThematicButton {
		DownloadButton() {
			super("<i class=\"fa fa-download\" aria-hidden=\"true\"></i> T&eacute;l&eacute;charger"); 
		}
	}


	
	
}
