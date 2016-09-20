package org.mslab.commons.client.tool.educ.settings.pref;

import java.util.ArrayList;
import java.util.List;

import org.mslab.commons.client.core.ui.panels.GridPanel;
import org.mslab.commons.shared.text.MessageFormat;
import org.mslab.commons.shared.types.PostalCode;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;

public abstract class AbstractFormatPanel extends GridPanel {
	private String _btnGroupName; 
	private String _title; 
	private String[] _builtinFormats; 
	protected String[] _variableNames;
	protected String[] _variableDefaultValues;
	
	private List<FormatRadioButton> _buttons = new ArrayList<FormatRadioButton>(); 
	protected RadioButton _rb2;
	protected HTML _preview; 
	protected TextBox _formatBox; 
	private NameFormatOptionPanel _optionPanel; 
	private NameFormatPreviewPanel _previewPanel; 
	
	protected AbstractFormatPanel(String btnGroupName, String title, String[] builtinFormats, String[] variableNames, String[] variableDefaultValues) {
		_btnGroupName = btnGroupName;
		_title = title;
		_builtinFormats = builtinFormats;
		_variableNames = variableNames;
		_variableDefaultValues = variableDefaultValues;
		int col = 0;
		
		_optionPanel = new NameFormatOptionPanel(); 
		_grid.setWidget(0, col, _optionPanel);
		col++; 
		
		_previewPanel = new NameFormatPreviewPanel(); 
		_previewPanel.getElement().getStyle().setMarginLeft(100, Unit.PX);
		_grid.setWidget(0, col, _previewPanel);
		_grid.getFlexCellFormatter().setVerticalAlignment(0, col, HasVerticalAlignment.ALIGN_TOP);
		col++; 
	}
	
	protected void refreshOptions() {
		String nameFormat = getFormat(); 
		String formatted = MessageFormat.format(nameFormat, _variableDefaultValues);
		String html = MessageFormat.format("Ex: <i>{0}</i>", formatted);
		_preview.setHTML(html);
	}
	
	protected abstract String getFormat(); 
	protected abstract void setFormat(String nameFormat);
	
	protected void refreshPreview() {
		String nameFormat = getFormat(); 
		int nb = _variableNames.length; 
		
		for (int i=0; i<nb; i++) {
			nameFormat = nameFormat.replace("{" + i + "}", _variableNames[i]); 
		}
		
		_formatBox.setText(nameFormat);
		_formatBox.setEnabled(_rb2.getValue());
	}
	
	private class NameFormatOptionPanel extends GridPanel implements ClickHandler {
		public NameFormatOptionPanel() {
			int row = 0;
			
			Label title = new Label(_title); 
			_grid.setWidget(row, 0, title);
			row++; 
			
			for (String builtinFormat : _builtinFormats) {
				FormatRadioButton rb = new FormatRadioButton(_btnGroupName, builtinFormat);
				String html = MessageFormat.format(builtinFormat, _variableDefaultValues); 
				rb.setHTML(html);
				rb.addClickHandler(this);
				_buttons.add(rb); 
				_grid.setWidget(row, 0, rb);
				row++; 
			}
			
			_rb2 = new RadioButton(_btnGroupName, "Format libre");
			_rb2.addClickHandler(this);
			_grid.setWidget(row, 0, _rb2);
			row++;
			
			_preview = new HTML(); 
			_preview.getElement().getStyle().setMarginLeft(18, Unit.PX);
			_grid.setWidget(row, 0, _preview);
			row++;
			
			RadioButton rb = _buttons.get(0); 
			rb.setValue(true);
			refreshOptions();
		}
		
		@Override
		public void onClick(ClickEvent event) {
			Object src = event.getSource(); 
			
			for (FormatRadioButton btn : _buttons) {
				if (btn.getValue()) {
					String nameFormat = btn.getNameFormat(); 
					setFormat(nameFormat); 
				}
			}
			
			refreshPreview();
			refreshOptions();
		}
	}

	private static class FormatRadioButton extends RadioButton {
		private String _builtinFormat; 
		
		FormatRadioButton(String grpName, String builtinFormat) {
			super(grpName); 
			_builtinFormat = builtinFormat;
		}

		public String getNameFormat() {
			return _builtinFormat;
		}
	}
	
	private class NameFormatPreviewPanel extends GridPanel implements KeyUpHandler {
 		
		public NameFormatPreviewPanel() {
			int row = 0;
			
			Label formatLbl = new Label("Chaîne de formattage :"); 
			formatLbl.getElement().getStyle().setFontSize(80, Unit.PCT);
			_grid.setWidget(row, 0, formatLbl);
			row++;
			
			_formatBox = new TextBox();
			_formatBox.setWidth("15em");
			_formatBox.addKeyUpHandler(this); 
			_grid.setWidget(row, 0, _formatBox);
			row++;
			
			HTML hintLbl = new HTML(); 
			hintLbl.getElement().getStyle().setMarginLeft(12, Unit.PX);
			hintLbl.getElement().getStyle().setFontSize(80, Unit.PCT);
			_grid.setWidget(row, 0, hintLbl);
			row++;
			
			String variables = getVariableList();
			String html = "<i>Variables permises: " + variables + "</i>"; 
			hintLbl.setHTML(html); 
			refreshPreview();  
		}

		private String getVariableList() {
			StringBuffer variables = new StringBuffer();
			boolean first = true; 
			
			for (String name : _variableNames) {
				variables.append(first ? "" : ", ");
				variables.append(name); 
				first = false;
			}
			
			return variables.toString();
		}

		@Override
		public void onKeyUp(KeyUpEvent event) {
			String nameFormat = _formatBox.getText();
			int nb = _variableNames.length; 
			
			for (int i=0; i<nb; i++) {
				nameFormat = nameFormat.replace(_variableNames[i], "{" + i + "}"); 
			}
			
			setFormat(nameFormat); 
			refreshOptions();
		}
	}
	
	
}
