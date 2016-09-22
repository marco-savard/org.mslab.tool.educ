package org.mslab.tool.educ.shared.types.educ;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("serial")
public class City implements Serializable {
    private String _name;  
	
	public static City create(String cityName) {
		City city = new City(cityName); 
		return city; 
	}
	
	//GWT required
	@SuppressWarnings("unused")
	private City() {}
	
	public City(String name) {
		if (_majorCityList == null) {
			_majorCityList = Arrays.asList(MAJOR_CITIES);
		}
		
		int idx = _majorCityList.indexOf(name);
		_name = (idx == -1) ? name : Integer.toString(idx); 
		_name = _name.replace("Montréal-", "MTL");
		_name = _name.replace("Mont-", "MT");
		_name = _name.replace("Notre-Dame-", "ND");
		_name = _name.replace("Sainte-", "STE");
		_name = _name.replace("Saint-", "ST");
	}
		
	@Override
	public String toString() {
		return _name;
	}
	
	public String getName() {
		String name; 
		
		try {
			int idx = Integer.parseInt(_name); 
			name = MAJOR_CITIES[idx];
		} catch (NumberFormatException ex) {
			name = _name;
			name = name.replace("MTL","Montréal-");
			name = name.replace("MT", "Mont-");
			name = name.replace("ND", "Notre-Dame-");
			name = name.replace("STE", "Sainte-");
			name = name.replace("ST", "Saint-");
			name = name.replace("montreal", "Montréal"); 
			name = name.replace("quebec", "Québec"); 
		}
		
		return name;
	} 
	
	private static List<String> _majorCityList = null;
	private static final String[] MAJOR_CITIES = new String[] {
		//most common
		"Montréal",
		"Québec",
		"Laval",
		"Gatineau",
		"Longueuil",
		"Sherbrooke", 
		"Trois-Rivières", 
		"Saguenay", 
		"Lévis",
		"Trois-Rivières", 
		"Terrebonne",
		"Saint-Jean-sur-Richelieu", 
		"Repentigny", 
		"Brossard", 
		"Drummondville", 
		"Saint-Jérôme", 
		"Granby",
		"Blanville", 
		"Saint-Hyacinthe", 
		"Shawinigan", 
		"Dollard-Des Ormeaux", 
		"Rimouski", 
		"Châteauguay", 
		"Saint-Eustache", 
		"Victoriaville",
		"Mascouche", 
		"Rouyn-Noranda", 
		"Boucherville", 
		"Salaberry-de-Valleyfield",
		
		//most frequent
		"Alma",
		"Amos",
		"Beaconsfield",
		"Baie-Comeau", 
		"Blainville",
		"Boisbriand",
		"Bonne-Espérance",
		"Carleton-sur-Mer",
		"Chibougamau",
		"Côte-Saint-Luc",
		"Deux-Montagnes",
		"Dolbeau-Mistassini",
		"Gaspé", 
		"Joliette",
		"La Pocatière",
		"La Prairie", 
		"Lachute", 
		"Lasalle", 
		"Magog", 
		"Matane", 
		"Mirabel",
		"Mont-Royal",
		"Montmagny",
		"Pointe-Claire", 
		"Rivière-du-Loup",
		"Saint-Georges",
		"Saint-Lambert", 
		"Sainte-Thérèse",
		"Sept-Îles", 
		"Sorel-Tracy", 
		"Thetford Mines", 
		"Val-d'Or", 
		"Vaudreuil-Dorion", 
		"Westmount", 
		
		//longer names
		"Côte-Nord-du-Golfe-du-Saint-Laurent", 
		"Saint-Bruno-de-Montarville", 
		"Sainte-Agathe-des-Monts",
		"Saint-Augustin-de-Desmaures",
		"Sainte-Anne-de-Bellevue", 
		"Saint-Paul-de-Montminy", 
		"Les Îles-de-la-Madeleine", 
		"Témiscouata-sur-le-Lac",
		
	};

	


}
