package org.mslab.tool.educ.client.tool.educ.people.viewer;

import java.util.ArrayList;
import java.util.List;

import org.mslab.tool.educ.client.tool.educ.school.explorer.EntityViewable;
import org.mslab.tool.educ.shared.types.educ.Person;

import com.google.gwt.user.client.ui.DeckPanel;

public class EducPeopleDeckPanel extends DeckPanel implements EntityViewable<Person> {
	private List<Person> _persons = null;
	private String _listName; 
	private EducPeopleListPanel _listPanel;
	private EducPeopleInstancePanel _instancePanel;
	
	public EducPeopleDeckPanel() {
		_listPanel = new EducPeopleListPanel(this);
		_instancePanel = new EducPeopleInstancePanel(this); 
		
		add(_listPanel);
		add(_instancePanel);
		showWidget(0);
	}
	
	public List<Person> getPersons() {
		return _persons;
	}
	
	public void update(Person person) {
		List<Person> persons = new ArrayList<Person>();
		persons.add(person);
		update("", persons); 
	}
	
	@Override
	public void update(String listName, List<Person> persons) {
		_persons = persons;
		_listName = listName;
		int nb = persons.size();
		
		if (nb == 1) {
			_instancePanel.refresh();
			showWidget(1);
		} else {
			_listPanel.refresh();
			showWidget(0);
		}
	}

	public String getCategory() {
		return _listName;
	}


	
}
