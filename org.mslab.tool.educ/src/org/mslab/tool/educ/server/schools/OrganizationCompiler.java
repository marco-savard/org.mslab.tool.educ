package org.mslab.tool.educ.server.schools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.mslab.tool.educ.server.postalcodes.PostalCodeFinder;
import org.mslab.tool.educ.shared.text.MessageFormat;
import org.mslab.tool.educ.shared.types.GeoLocation;
import org.mslab.tool.educ.shared.types.PhoneNumber;
import org.mslab.tool.educ.shared.types.PostalCode;
import org.mslab.tool.educ.shared.types.RegionAdministrative;
import org.mslab.tool.educ.shared.types.educ.Circonscription;
import org.mslab.tool.educ.shared.types.educ.City;
import org.mslab.tool.educ.shared.types.educ.FamilyName;
import org.mslab.tool.educ.shared.types.educ.GivenName;
import org.mslab.tool.educ.shared.types.educ.OrdreAppartenance;
import org.mslab.tool.educ.shared.types.educ.Organization;
import org.mslab.tool.educ.shared.types.educ.OrganizationName;

public class OrganizationCompiler {
	
	public static void main(String[] args) {
		OrganizationCompiler compiler = new OrganizationCompiler(); 
		List<Organization> organizations = compiler.getListOrganizations();
		//for (Organization org : organizations) {
		//	System.out.println(org.toString()); 
		//}
	}
	
	public List<Organization> getListOrganizations() {
		//read sources
		List<Organization> organizations = null; 
		String resname = "RechercheOrganisme2014_11_25_10h49_15.csv"; 
		URL url = OrganizationFinderOld.class.getResource(resname); 
		try {
			//read
			InputStream input = url.openStream();
			CsvReader reader = new CsvReader(input, "iso-8859-1");
			CsvStructure structure = reader.read(); 
			
			//add location
			Localizator localizator = new Localizator(structure);
			localizator.processRows(); 
			CsvStructure newStructure = localizator.getNewStructure();
			
			//write
			PrintStream output = getOutput(); 
			//PrintStream output = System.out;
			CsvProcessor compiler = new CsvCompiler(newStructure, output);
			compiler.addColumn("Code d'organisme");
			compiler.addColumn("Nom d'organisme officiel");
			compiler.addColumn("Code organisme responsable");
			
			compiler.addColumn("Adresse de courrier electronique");
			compiler.addColumn("Adresse de site WEB");
			compiler.addColumn("Ordre d'appartenance");
			compiler.addColumn("Réseau d'enseignement");
			compiler.addColumn("Type d'organisme"); 
			
			compiler.addColumn("Numéro de téléphone");
			compiler.addColumn("Numéro de poste téléphone");
			compiler.addColumn("Numéro de télécopieur");
			
			compiler.addColumn("Nom du directeur"); 
			compiler.addColumn("Prénom du directeur"); 
			compiler.addColumn("Appellation"); 
			compiler.addColumn("Titre de l'intervenant"); 
			compiler.addColumn("Circonscription électorale provinciale");

			compiler.addColumn("Code postal de l'adresse géographique");
			compiler.addColumn("Numéro - nom de rue et casier postal de l'adresse postale");
			compiler.addColumn("Région administrative"); 
			compiler.addColumn("Latitude");
			compiler.addColumn("Longitude");
			compiler.addColumn("Municipalité de l'adresse géographique");
			
			compiler.addColumn("Français");
			compiler.addColumn("Anglais");

			//write
			compiler.processHeader();
			compiler.processRows(); 
			output.close();
			
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		//summary
		System.out.println("Success");
		return organizations;
	}
	
	private PrintStream getOutput() throws IOException {
		File folder = new File("D:\\Users\\Marco_Home\\My Workspaces\\WorkspaceBenchmark\\org.mslab.commons\\src\\org\\mslab\\commons\\server\\schools"); 
		File file = new File(folder, "Organismes.csv"); 
		PrintStream output = new PrintStream(file, "iso-8859-1");
		return output;
	}
	
	private static class Localizator extends CsvProcessor {
		private CsvStructure _newStructure; 
		private PostalCodeFinder _finder;
		
		public Localizator(CsvStructure structure) {
			super(structure);
			int idx = 0; 
			
			_newStructure = new CsvStructure(); 
			_finder = new PostalCodeFinder();
			
			List<String> columns = _structure.getColumns(); 
			for (String column : columns) {
				_newStructure.addColumn(column, idx++);
			}
			
			_newStructure.addColumn("Latitude", idx++);
			_newStructure.addColumn("Longitude", idx++);
		}
		
		public CsvStructure getNewStructure() {
			return _newStructure;
		}

		@Override
		public void processHeader() {
		} 
		
		@Override
		protected void processRow(String row) {
			String[] fields = row.split(";"); 	
			String postalCode = getValue(fields, "Code postal de l'adresse géographique"); 
			GeoLocation location = _finder.find(postalCode); 
			String newRow = MessageFormat.format("{0}\"{1}\";\"{2}\"", 
				new Object[] {row, location.getLatitude(), location.getLongitude()});
			_newStructure.getRows().add(newRow); 
		}


	}

	private static class CsvCompiler extends CsvProcessor {
		protected PrintStream _output; 
		
		public CsvCompiler(CsvStructure structure, PrintStream output) {
			super(structure);
			_output = output;
		}
		
		public void processHeader() {
			for (String column : _columns) {
				_output.print("\"" + column + "\";"); 
			}
			_output.println();
		}
		
		@Override
		protected void processRow(String row) {
			String[] fields = row.split(";"); 	
			List<String> columns = getColumns(); 
			
			for (String column : columns) {
				String value = getValue(fields, column); 
				processCell(value, column); 
			}
			_output.println(); 
		}

		private void processCell(String value, String column) {
			String cell; 
			
			if ("Code postal de l'adresse géographique".equals(column)) {
				PostalCode postalCode = new PostalCode(value); 
				cell = "\"" + postalCode.toString() + "\";"; 
			} else if ("Municipalité de l'adresse géographique".equals(column)) {
				City city = City.create(value); 
				cell = "\"" + city.toString() + "\";"; 
			} else if ("Région administrative".equals(column)) {
				RegionAdministrative region = new RegionAdministrative(value); 
				cell = "\"" + region.toString() + "\";"; 
			} else if ("Circonscription électorale provinciale".equals(column)) {
				Circonscription circon = new Circonscription(value); 
				cell = "\"" + circon.toString() + "\";"; 
			} else if ("Numéro de téléphone".equals(column)) {
				PhoneNumber phone = new PhoneNumber(PhoneNumber.Category.OFFICE, value); 
				cell = "\"" + phone.toString() + "\";"; 
			} else if ("Nom du directeur".equals(column)) {
				FamilyName name = new FamilyName(value); 
				cell = "\"" + name.toString() + "\";"; 
			} else if ("Prénom du directeur".equals(column)) { 
				GivenName name = new GivenName(value); 
				cell = "\"" + name.toString() + "\";"; 
			} else if ("Ordre d'appartenance".equals(column)) { 
				OrdreAppartenance order = new OrdreAppartenance(value); 
				cell = "\"" + order.toString() + "\";"; 
			} else if ("Nom d'organisme officiel".equals(column)) {
				OrganizationName name = new OrganizationName(value); 
				cell = "\"" + name.toString() + "\";"; 
			} else if ("Adresse de site WEB".equals(column)) {
				if (value.startsWith("http://")) {
					value = value.substring(7); 
				}
				
				cell = "\"" + value + "\";"; 
			} else {
				cell = "\"" + value + "\";"; 
			}

			_output.print(cell);
		}


		

		
	}
	

}
