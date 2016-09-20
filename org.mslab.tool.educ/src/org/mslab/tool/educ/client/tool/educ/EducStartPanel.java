package org.mslab.commons.client.tool.educ;

import java.util.Date;
import java.util.List;

import org.mslab.commons.client.core.ui.ProgressBar;
import org.mslab.commons.client.core.ui.panels.GridPanel;
import org.mslab.commons.client.core.ui.theme.ThematicButton;
import org.mslab.commons.client.tool.services.ServiceStore;
import org.mslab.commons.shared.text.MessageFormat;
import org.mslab.commons.shared.types.Color;
import org.mslab.commons.shared.types.educ.Organization;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;

public class EducStartPanel extends GridPanel implements ClickHandler {
	private EducShell _owner; 
	private ThematicButton _readSchoolButton, _continueButton;
	private RemainingSchoolLabel _remainingSchoolLabel;
	private ProgressBar _progressBar;
	private LoadTimer _loadTimer; 
	private boolean _timerDone = false, _schoolsRead = false;
	private List<Organization> _loadedOrganizations; 
	
	EducStartPanel(EducShell owner) {
		_owner = owner;
		int row = 0;
		_grid.setWidth("100%");
		EducTheme theme = (EducTheme)EducTheme.getTheme();
		
		_readSchoolButton = new ThematicButton("Lire les &eacute;coles");
		_readSchoolButton.getElement().getStyle().setMargin(24, Unit.PX);
		_readSchoolButton.addClickHandler(this);
		_grid.setWidget(row, 1, _readSchoolButton);
		_grid.getFlexCellFormatter().setHorizontalAlignment(row, 1, HasHorizontalAlignment.ALIGN_CENTER);
		row++;
		
		_remainingSchoolLabel = new RemainingSchoolLabel(); 
		_remainingSchoolLabel.setVisible(false);
		_grid.setWidget(row, 1, _remainingSchoolLabel);
		_grid.getFlexCellFormatter().setHorizontalAlignment(row, 1, HasHorizontalAlignment.ALIGN_CENTER);
		row++;
		
		Label left = new Label(); 
		left.setWidth("10em");
		_grid.setWidget(row, 0, left);
		
		_progressBar = new ProgressBar();
		_progressBar.setColors(theme.getPrimaryFgColor(), theme.getPrimaryBgColor());
		_progressBar.setSize("100%", "20px");
		_progressBar.getElement().getStyle().setMarginTop(12, Unit.PX); 
		_progressBar.setVisible(false);
		_grid.setWidget(row, 1, _progressBar);
		//_grid.getFlexCellFormatter().setColSpan(row, 1, 3);
		
		Label right = new Label(); 
		right.setWidth("10em");
		_grid.setWidget(row, 2, right);
		row++;
		
		_continueButton = new ThematicButton("Continuer");
		_continueButton.getElement().getStyle().setMargin(24, Unit.PX);
		_continueButton.addClickHandler(this);
		_continueButton.setEnabled(false);
		_grid.setWidget(row, 1, _continueButton);
		_grid.getFlexCellFormatter().setHorizontalAlignment(row, 1, HasHorizontalAlignment.ALIGN_RIGHT);
		row++;
		
		_loadTimer = new LoadTimer(_remainingSchoolLabel, _progressBar); 
	}

	@Override
	public void onClick(ClickEvent event) {
		Object src = event.getSource(); 
		
		if (_readSchoolButton.equals(src)) {
			readSchools(); 
		} else if (_continueButton.equals(src)) {
			displaySchools(); 
		}
	}



	private void readSchools() {
		_readSchoolButton.setHTML("Lecture des &eacute;coles..");
		_readSchoolButton.setEnabled(false);
		_remainingSchoolLabel.setVisible(true);
		_progressBar.setVisible(true);
		
		_loadTimer.setStart(0); 
		_loadTimer.scheduleRepeating(100);
		callGetOrganizations();
		_progressBar.setValue(0);
		
	}
	
	public void callGetOrganizations() {
		AsyncCallback<List<Organization>> callback = new AsyncCallback<List<Organization>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub	
			}

			@Override
			public void onSuccess(List<Organization> organizations) {
				displayOrganizations(organizations); 
			}
		};


		ServiceStore.getService().getListOrganizations(callback);
	}
	
	private void displayOrganizations(List<Organization> organizations) {
		_loadedOrganizations = organizations;
		_schoolsRead = true; 
		refreshContinueButton();
		
		Date now = new Date();
		System.out.println(now.toString());
	}
	
	private void refreshContinueButton() {
		if (_schoolsRead && _timerDone) {
			_remainingSchoolLabel.setDone();
			_continueButton.setEnabled(true);
		}
	}
	
	private void displaySchools() {
		EducContext context = EducContext.getInstance();
		context.init(_loadedOrganizations);
		_owner.setOrganizations(_loadedOrganizations);
		
		_owner.displayOrganizations(_loadedOrganizations);
	}
	
	//
	// inner classes
	//
	private static class RemainingSchoolLabel extends GridPanel {
		private static final int EXPECTED_NB_SCHOOLS = 	2474; 
		private int _remaining = EXPECTED_NB_SCHOOLS; 
		private HTML _icon, _text;
		
		RemainingSchoolLabel() {
			String icon = "<i class=\"fa fa-refresh fa-spin\"></i>"; 
			_icon = new HTML(icon); 
			EducTheme theme = (EducTheme)EducTheme.getTheme();
			_icon.getElement().getStyle().setColor(theme.getPrimaryFgColor().toString());
			_icon.getElement().getStyle().setFontSize(160, Unit.PCT);
			_grid.setWidget(0, 0, _icon);
			
			_text = new HTML(); 
			_text.getElement().getStyle().setFontSize(160, Unit.PCT);
			_grid.setWidget(0, 1, _text);
			
			refresh();
		}

		public void refresh() {
			String html = MessageFormat.format("Il reste {0} &eacute;coles &agrave; lire", _remaining); 
			_text.setHTML(html);
		}

		public void setRemainingSchool(int percentRemaining) {
			_remaining =  (int)(EXPECTED_NB_SCHOOLS * (percentRemaining / 100.0));  
			refresh();
		}

		public void setDone() {
			String icon = "<i class=\"fa fa-check\"></i>"; 
			_icon.setHTML(icon);
			_icon.getElement().getStyle().setColor(Color.GREEN_DARK.toString());
			
			String html = MessageFormat.format("{0} &eacute;coles lues", EXPECTED_NB_SCHOOLS); 
			_text.setHTML(html);
		}
	}
	
	private class LoadTimer extends Timer {
		private static final int EXPECTED_DURATION = 4000; //4 sec
		private static final int STEP_DURATION = 200; //200 ms
		private int _time;
		private RemainingSchoolLabel _label;
		private ProgressBar _progressBar;
		
		LoadTimer(RemainingSchoolLabel label, ProgressBar progressBar) {
			_label = label;
			_progressBar = progressBar; 
		}

		public void setStart(int time) {
			_time = time;
		}

		@Override
		public void run() {
			_time += STEP_DURATION;
			
			if (_time > EXPECTED_DURATION) {
				cancel();
				_timerDone = true; 
				refreshContinueButton();
			} else {
				refresh();
			}
		}

		private void refresh() {
			int percentDone = (int)((_time / (double)EXPECTED_DURATION) * 100); 
			percentDone = Math.min(percentDone, 100);
			_label.setRemainingSchool(100 - percentDone); 
			_progressBar.setValue(percentDone);
		}
	}
	

}
