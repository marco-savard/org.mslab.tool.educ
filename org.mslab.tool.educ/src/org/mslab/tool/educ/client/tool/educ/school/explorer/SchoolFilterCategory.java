package org.mslab.tool.educ.client.tool.educ.school.explorer;

import java.util.List;

import org.mslab.tool.educ.client.tool.educ.EducContext;
import org.mslab.tool.educ.shared.types.Address;
import org.mslab.tool.educ.shared.types.GeoLocation;
import org.mslab.tool.educ.shared.types.PostalCode;
import org.mslab.tool.educ.shared.types.RegionAdministrative;
import org.mslab.tool.educ.shared.types.educ.City;
import org.mslab.tool.educ.shared.types.educ.OrdreAppartenance;
import org.mslab.tool.educ.shared.types.educ.Organization;
import org.mslab.tool.educ.shared.types.educ.School;
import org.mslab.tool.educ.shared.types.educ.SchoolBoard;

import com.google.gwt.i18n.client.NumberFormat;

public class SchoolFilterCategory extends AbstractFilterCategory {
	
	//
	// region
	//
	public static class RegionCategorizer extends AbstractCategorizer<Organization> {
		public RegionCategorizer() {
			super("Région");
		}
		
		@Override
		public String categorize(Organization org, List<Organization> schoolBoards) {
			RegionAdministrative region = org.getRegionAdministrative();
			String name = region.getName(); 
			return name;
		}
	}
	
	//
	// city
	//
	public static class CityCategorizer extends AbstractCategorizer<Organization> {
		public CityCategorizer() {
			super("Ville");
		}
		
		@Override
		public String categorize(Organization org, List<Organization> schoolBoards) {
			Address address = org.getAddress();
			GeoLocation location = (address == null) ? null : address.getLocation();
			City city = (location == null) ? null : location.getCity();
			String cityName = (city == null) ? null : city.getName();
			return cityName;
		}
	}
	
	//
	// circonscription
	//
	public static class CirconscriptionCategorizer extends AbstractCategorizer<Organization> {
		public CirconscriptionCategorizer() {
			super("Circonscription");
		}
		
		@Override
		public String categorize(Organization org, List<Organization> schoolBoards) {
			String circonscription = org.getCirconscription(); 
			return circonscription;
		}
	}
	
	//
	// distance
	//
	public static class DistanceCategorizer extends AbstractCategorizer<Organization> {
		public DistanceCategorizer() {
			super("Distance");
		}
		
		@Override
		public String categorize(Organization org, List<Organization> schools) {
			Address address = org.getAddress();
			GeoLocation location = (address == null) ? null : address.getLocation();
			GeoLocation clientLocation = EducContext.getInstance().getClientLocation(); 
			double km = (location == null) || (clientLocation == null) ? -1 : 
				location.computeDistanceFrom(clientLocation); 
			String category; 
			
			if (km == -1) {
				category = "Inconnue"; 
			} else if (km < 25) {
				category = "Moins de 25 km"; 
			} else if (km < 100) {
				category = "De 25 à 100 km"; 
			} else {
				category = "Plus de 100 km"; 
			}
			
			return category;
		}
	}
	
	//
	// type
	//
	public static class OrganizationTypeCategorizer extends AbstractCategorizer<Organization> {
		public OrganizationTypeCategorizer() {
			super("Type d'organisme");
		}
		
		@Override
		public String categorize(Organization org, List<Organization> schoolBoards) {
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
	public static class OrdreAppartenanceCategorizer extends AbstractCategorizer<Organization> {
		public OrdreAppartenanceCategorizer() {
			super("Niveau");
		}
		
		@Override
		public String categorize(Organization org, List<Organization> schoolBoards) {
			String category = null;
			if (org instanceof School) {
				School school = (School)org;
				OrdreAppartenance ordre = school.getOrdreAppartenance();
				category = (ordre == null) ? null : ordre.getName();
			}
			
			return category;
		}
	}
	
	//
	// reseau
	//
	public static class ReseauCategorizer extends AbstractCategorizer<Organization> {
		public ReseauCategorizer() {
			super("Réseau");
		}
		
		@Override
		public String categorize(Organization org, List<Organization> schoolBoards) {
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
	public static class LanguageCategorizer extends AbstractCategorizer<Organization> {
		public LanguageCategorizer() {
			super("Langue");
		}
		
		@Override
		public String categorize(Organization org, List<Organization> schoolBoards) {
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
	public static class EnvironmentCategorizer extends AbstractCategorizer<Organization> {
		public EnvironmentCategorizer() {
			super("Environnement");
		}
		
		@Override
		public String categorize(Organization org, List<Organization> schoolBoards) {
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
	public static class SchoolBoardCategorizer extends AbstractCategorizer<Organization> {		
		public SchoolBoardCategorizer() {
			super("Commission scolaire");
		}
		
		@Override
		public String categorize(Organization organization, List<Organization> schoolBoards) {
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
