package org.mslab.commons.client.tool.educ;

import java.util.List;

import org.mslab.commons.client.tool.educ.OrganizationCategories.AbstractCategorizer;
import org.mslab.commons.shared.types.Address;
import org.mslab.commons.shared.types.CityOld;
import org.mslab.commons.shared.types.GeoLocation;
import org.mslab.commons.shared.types.PostalCode;
import org.mslab.commons.shared.types.RegionAdministrative;
import org.mslab.commons.shared.types.educ.City;
import org.mslab.commons.shared.types.educ.OrdreAppartenance;
import org.mslab.commons.shared.types.educ.Organization;
import org.mslab.commons.shared.types.educ.School;
import org.mslab.commons.shared.types.educ.SchoolBoard;

public class OrganizationCategories {
	
	//
	// abstract
	//
	public static abstract class AbstractCategorizer {
		protected String _name;
		
		protected AbstractCategorizer(String name) {
			_name = name;
		}
		
		public abstract String categorize(Organization org, List<SchoolBoard> schoolBoards);

		public String getName() {
			return _name;
		}
	}

	//
	// region
	//
	public static class RegionCategorizer extends AbstractCategorizer {
		public RegionCategorizer() {
			super("Régions");
		}
		
		@Override
		public String categorize(Organization org, List<SchoolBoard> schoolBoards) {
			RegionAdministrative region = org.getRegionAdministrative();
			return region.toString();
		}
	}
	
	//
	// city
	//
	public static class CityCategorizer extends AbstractCategorizer {
		public CityCategorizer() {
			super("Villes");
		}
		
		@Override
		public String categorize(Organization org, List<SchoolBoard> schoolBoards) {
			Address address = org.getAddress();
			GeoLocation location = (address == null) ? null : address.getLocation();
			City city = (location == null) ? null : location.getCity();
			String cityName = (city == null) ? null : city.getName();
			return cityName;
		}
	}
	
	//
	// type
	//
	public static class OrganizationTypeCategorizer extends AbstractCategorizer {
		public OrganizationTypeCategorizer() {
			super("Type d'organisme");
		}
		
		@Override
		public String categorize(Organization org, List<SchoolBoard> schoolBoards) {
			String category = null;
			if (org instanceof School) {
				category = "École";
			} else if (org instanceof SchoolBoard) {
				category = "Commission scolaire";
			}
			
			return category;
		}
	}
	
	//
	// niveau
	//
	public static class OrdreAppartenanceCategorizer extends AbstractCategorizer {
		public OrdreAppartenanceCategorizer() {
			super("Niveau");
		}
		
		@Override
		public String categorize(Organization org, List<SchoolBoard> schoolBoards) {
			String category = null;
			if (org instanceof School) {
				School school = (School)org;
				OrdreAppartenance ordre = school.getOrdreAppartenance();
				category = ordre.getName();
			}
			
			return category;
		}
	}
	
	//
	// reseau
	//
	public static class ReseauCategorizer extends AbstractCategorizer {
		public ReseauCategorizer() {
			super("Réseau");
		}
		
		@Override
		public String categorize(Organization org, List<SchoolBoard> schoolBoards) {
			String category = null; 
			
			if (org.isPublic()) {
				category = "Public"; 
			} else if (org.isEnglish()) {
				category = "Privé"; 
			}
			
			return category;
		}
	}
	
	//
	// langue
	//
	public static class LanguageCategorizer extends AbstractCategorizer {
		public LanguageCategorizer() {
			super("Langue");
		}
		
		@Override
		public String categorize(Organization org, List<SchoolBoard> schoolBoards) {
			String category = null; 
			
			if (org.isFrench()) {
				category = "Français"; 
			} else if (org.isEnglish()) {
				category = "Anglais"; 
			}
			
			return category;
		}
	}
	
	//
	// environnement
	//
	public static class EnvironmentCategorizer extends AbstractCategorizer {
		public EnvironmentCategorizer() {
			super("Environnement");
		}
		
		@Override
		public String categorize(Organization org, List<SchoolBoard> schoolBoards) {
			String category = "Inconnu"; 
			
			Address address = org.getAddress();
			if (address != null) {
				PostalCode postalCode = address.getPostalCode();
				category = postalCode.isRural() ? "Rural" : "Urbain";
			}
			
			return category;
		}
	}
	
	//
	// school board
	//
	public static class SchoolBoardCategorizer extends AbstractCategorizer {		
		public SchoolBoardCategorizer() {
			super("Commissions scolaires");
		}
		
		@Override
		public String categorize(Organization organization, List<SchoolBoard> schoolBoards) {
			String category = null;
			if (organization instanceof School) {
				School school = (School)organization;
				SchoolBoard sb = school.getSchoolBoard(); 
				category = (sb == null) ? null : sb.getName();
			}
			
			return category;
		}
	}
	
	

}


