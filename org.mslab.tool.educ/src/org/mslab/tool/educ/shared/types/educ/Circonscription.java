package org.mslab.commons.shared.types.educ;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("serial")
public class Circonscription implements Serializable {
    private int _number; 
    
	//GWT required
	@SuppressWarnings("unused")
	private Circonscription() {}
    
	public Circonscription(String value) {
		if (_circonscriptionList == null) {
			_circonscriptionList = Arrays.asList(CIRCONSCRIPTIONS);
		}
		
		String name = value.replace("--", "–"); 
		//_number = Arrays.binarySearch(CIRCONSCRIPTIONS, name); 
		
		_number = _circonscriptionList.indexOf(name);
		if (_number == -1) {
			String cir = _circonscriptionList.get(124);
			boolean equal = cir.equals(name); 
			_number = -1;
		}
	}
	
	@Override 
	public String toString() {
		return Integer.toString(_number); 
	}

	private static List<String> _circonscriptionList;
	private static String[] CIRCONSCRIPTIONS = new String[] {
		"Abitibi-Est",
		"Abitibi-Ouest",
		"Acadie",
		"Anjou–Louis-Riel",
		"Argenteuil",
		"Arthabaska",
		"Beauce-Nord",
		"Beauce-Sud",
		"Beauharnois",
		"Bellechasse",
		"Berthier",
		"Bertrand",
		"Blainville",
		"Bonaventure",
		"Borduas",
		"Bourassa-Sauvé",
		"Bourget",
		"Brome-Missisquoi",
		"Chambly",
		"Champlain",
		"Chapleau",
		"Charlesbourg",
		"Charlevoix–Côte-de-Beaupré",
		"Châteauguay",
		"Chauveau",
		"Chicoutimi",
		"Chomedey",
		"Chutes-de-la-Chaudière",
		"Côte-du-Sud",
		"Crémazie",
		"D'Arcy-McGee",
		"Deux-Montagnes",
		"Drummond–Bois-Francs",
		"Dubuc",
		"Duplessis",
		"Fabre",
		"Gaspé",
		"Gatineau",
		"Gouin",
		"Granby",
		"Groulx",
		"Hochelaga-Maisonneuve",
		"Hull",
		"Huntingdon",
		"Iberville",
		"Îles-de-la-Madeleine",
		"Jacques-Cartier",
		"Jean-Lesage",
		"Jeanne-Mance–Viger",
		"Jean-Talon",
		"Johnson",
		"Joliette",
		"Jonquière",
		"Labelle",
		"Lac-Saint-Jean",
		"LaFontaine",
		"La Peltrie",
		"La Pinière",
		"Laporte",
		"La Prairie",
		"L'Assomption",
		"Laurier-Dorion",
		"Laval-des-Rapides",
		"Laviolette",
		"Lévis",
		"Lotbinière-Frontenac",
		"Louis-Hébert",
		"Marguerite-Bourgeoys",
		"Marie-Victorin",
		"Marquette",
		"Maskinongé",
		"Masson",
		"Matane-Matapédia",
		"Mégantic",
		"Mercier",
		"Mille-Îles",
		"Mirabel",
		"Montarville",
		"Montmorency",
		"Mont-Royal",
		"Nelligan",
		"Nicolet-Bécancour",
		"Notre-Dame-de-Grâce",
		"Orford",
		"Outremont",
		"Papineau",
		"Pointe-aux-Trembles",
		"Pontiac",
		"Portneuf",
		"René-Lévesque",
		"Repentigny",
		"Richelieu",
		"Richmond",
		"Rimouski",
		"Rivière-du-Loup–Témiscouata",
		"Robert-Baldwin",
		"Roberval",
		"Rosemont",
		"Rousseau",
		"Rouyn-Noranda–Témiscamingue",
		"Saint-François",
		"Saint-Henri–Sainte-Anne",
		"Saint-Hyacinthe",
		"Saint-Jean",
		"Saint-Jérôme",
		"Saint-Laurent",
		"Sainte-Marie–Saint-Jacques",
		"Saint-Maurice",
		"Sainte-Rose",
		"Sanguinet",
		"Sherbrooke",
		"Soulanges",
		"Taillon",
		"Taschereau",
		"Terrebonne",
		"Trois-Rivières",
		"Ungava",
		"Vachon",
		"Vanier-Les Rivières",
		"Vaudreuil",
		"Verchères",
		"Verdun",
		"Viau",
		"Vimont",
		"Westmount–Saint-Louis",
	};

	public static Circonscription fromCode(String code) {
		Circonscription circonscription = new Circonscription();
		circonscription._number = Integer.parseInt(code); 
		return circonscription;
	}

	public String getName() {
		String name = (_number == -1) ? null : CIRCONSCRIPTIONS[_number]; 
		return name;
	}
}
