package org.mslab.commons.shared.types.educ;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("serial")
public class FamilyName implements Serializable {
	private String _name;

	//GWT required
	@SuppressWarnings("unused")
	private FamilyName() {}
	
	public FamilyName(String text) {
		if (_familyNameList == null) {
			_familyNameList = Arrays.asList(FAMILY_NAMES); 
		}
		
		int idx = _familyNameList.indexOf(text);
		_name = (idx == -1) ? text : Integer.toString(idx); 
	}
	
	public static FamilyName fromString(String value) {
		FamilyName name = new FamilyName(); 
		name._name = value;
		return name;
	} 
	
	@Override
	public String toString() {
		return _name;
	}

	public String toDisplayString() {
		String displayString; 
		
		try {
			int idx = Integer.parseInt(_name); 
			displayString = FAMILY_NAMES[idx]; 
		} catch (NumberFormatException ex) {
			displayString = _name;
		}
		
		return displayString;
	}
	
	private static List<String> _familyNameList = null;
	private static final String[] FAMILY_NAMES = new String[] {
		//most common
		"Tremblay",
		"Gagnon",
		"Roy",
		"Côté",
		"Bouchard",
		"Gauthier",
		"Morin",
		"Lavoie",
		"Fortin",
		"Gagné",
		"Ouellet",
		"Pelletier",
		"Bélanger",
		"Lévesque",
		"Bergeron",
		"Leblanc",
		"Paquette",
		"Girard",
		"Simard",
		"Boucher",
		"Caron",
		"Beaulieu",
		"Cloutier",
		"Dubé",
		"Poirier",
		"Fournier",
		"Lapointe",
		"Leclerc",
		"Lefebvre",
		"Poulin",
		"Thibault",
		"St-Pierre",
		"Nadeau",
		"Martin",
		"Landry",
		"Martel",
		"Bédard",
		"Grenier",
		"Lessard",
		"Bernier",
		"Richard",
		"Michaud",
		"Hébert",
		"Desjardins",
		"Couture",
		"Turcotte",
		"Lachance",
		"Parent",
		"Blais",
		"Gosselin",
	};




}
