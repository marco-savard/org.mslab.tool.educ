package org.mslab.tool.educ.server.services;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletOutputStream;

import org.mslab.tool.educ.server.schools.OrganizationFinder;
import org.mslab.tool.educ.server.schools.OrganizationFinder.OrganizationRepository;
import org.mslab.tool.educ.shared.types.educ.Organization;

public class OrganizationsCsvWriter {
	private OutputStream _output;
	private String _sep;
	private PrintWriter _pw; 

	private OrganizationsCsvWriter(OutputStream output, String sep) {
		_output = output;
		_sep = sep;
		 OutputStreamWriter osw = new OutputStreamWriter(output); 
	     _pw = new PrintWriter(osw);    
	}

	public int write(String codes) throws IOException {
		int len = writeHeader(_pw);
        len += writeOrganizations(_pw, codes);
        _pw.close();
        _output.close();
        return len;
	}
	
	private int writeHeader(PrintWriter pw) {
		int len = 0;
		
		len += writeColumn(pw, "Code");
		len += writeColumn(pw, "Nom"); 
		len += writeColumn(pw, "Numero civique");
		len += writeColumn(pw, "Rue");
		len += writeColumn(pw, "Ville");
		len += writeColumn(pw, "Code Postale");
		len += writeColumn(pw, "Circonscription");
		len += writeColumn(pw, "Region Administrative");
		len += writeColumn(pw, "Direction");
		len += writeColumn(pw, "Telephone"); 
		len += writeColumn(pw, "Site Web");
		len += writeColumn(pw, "Courriel");
		pw.println();
		
		return len;
	}
	
	private int writeColumn(PrintWriter pw, String name) {
		pw.print(name + _sep); 
		return name.length();
	}

	private int writeOrganizations(PrintWriter pw, String codeList) {
		OrganizationFinder finder = new OrganizationFinder(); 
		OrganizationRepository repository = finder.getOrganizationRepository(); 
		List<Organization> organizations = repository._organizations;
		int len = 0;
		String[] codes = codeList.split(";"); 
		
		for (String code : codes) {
			Organization org = findOrganization(organizations, code);
			if (org != null) {
				len += writeOrganization(pw, org);
			}
		}
		
		return len;
	}
	
	private Organization findOrganization(List<Organization> organizations,
			String code) {
		Organization foundOrg = null; 
		
		for (Organization org : organizations) {
			if (code.equals(org.getCode())) {
				foundOrg = org;
				break;
			}
		}
		return foundOrg;
	}
	
	private int writeOrganization(PrintWriter pw, Organization org) {
		int len = 0; 
	
		len += writeField(pw, org.getCode());
		len += writeField(pw, org.getName());
		len += writeField(pw, org.getAddress().getCivicNumber());
		len += writeField(pw, org.getAddress().getStreet());
		len += writeField(pw, org.getAddress().getLocation().getCity().toString());
		len += writeField(pw, org.getAddress().getPostalCode().toDisplayString());
		len += writeField(pw, org.getCirconscription());
		len += writeField(pw, org.getRegionAdministrative().getName());
		len += writeField(pw, org.getDirector().getFullName());
		len += writeField(pw, org.getPhoneNumbers().get(0).toDisplayString());
		len += writeField(pw, org.getWeb());
		len += writeField(pw, org.getEmail());
		
		pw.println();
		return len;
	}

	private int writeField(PrintWriter pw, String field) {
		pw.print(field + _sep); 
		int len = (field == null) ? 0 : field.length();
		return len;
	}

}
