package org.mslab.tool.educ.shared.educ.util;

import java.io.StringWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.mslab.tool.educ.client.tool.educ.school.viewer.content.OrganizationComparator;
import org.mslab.tool.educ.client.tool.educ.school.viewer.content.OrganizationComparator.Criteria;
import org.mslab.tool.educ.shared.types.educ.Organization;

public class OrganizationWriter {
	private StringBuffer _sw; 
	private int _nbColumns, _nbLines;
	private OrganizationComparator _comparator;
	
	public OrganizationWriter() {
		_comparator = new OrganizationComparator();
	}

	public int write(List<Organization> organizations) {
		_sw = new StringBuffer(); 
		_nbColumns = 0; 
		_nbLines = 1;
		
		int len = writeHeader();
        len += writeOrganizations(organizations);
		return len;
	}

	private int writeHeader() {
		int len = 0;
		
		len += writeColumn("Code");
		len += writeColumn("Nom"); 
		len += writeColumn("Numero civique");
		len += writeColumn("Rue");
		len += writeColumn("Ville");
		len += writeColumn("Code Postale");
		len += writeColumn("Circonscription");
		len += writeColumn("Region Administrative");
		len += writeColumn("Direction");
		len += writeColumn("Telephone"); 
		len += writeColumn("Site Web");
		len += writeColumn("Courriel");
		_sw.append("\n"); 
		
		_nbColumns = (len > _nbColumns) ? len : _nbColumns;
		
		return len;
	}

	private int writeColumn(String column) {
		_sw.append(_delimitor + column + _delimitor + _separator);
		int len = column.length();
		return len;
	}

	private int writeOrganizations(List<Organization> organizations) {
		int len = 0; 
		Collections.sort(organizations, _comparator);
		
		for (Organization organization : organizations) {
			len += writeOrganization(organization); 
			_nbLines++;
		}
		
		return len;
	}

	private int writeOrganization(Organization organization) {
		int len = 0;
	    len += writeField(organization.getCode());
		len += writeField(organization.getName());
		len += writeField(organization.getAddress().getCivicNumber());
		len += writeField(organization.getAddress().getStreet());
		len += writeField(organization.getAddress().getLocation().getCity().toString());
		len += writeField(organization.getAddress().getPostalCode().toDisplayString());
		len += writeField(organization.getCirconscription());
		len += writeField(organization.getRegionAdministrative().getName());
		len += writeField(organization.getDirector().getFullName());
		len += writeField(organization.getPhoneNumbers().get(0).toDisplayString());
		len += writeField(organization.getWeb());
		len += writeField(organization.getEmail());
		
		_sw.append("\n");
		
		_nbColumns = (len > _nbColumns) ? len : _nbColumns;
		return len;
	}

	private int writeField(String field) {
		_sw.append((field == null) ? _delimitor + _delimitor + _separator : _delimitor + field + _delimitor + _separator);
		int len = (field == null) ? 0 : field.length();
		return len;
	}

	public String getText() {
		String text = _sw.toString();
		return text;
	}
	
	public int getNbColumns() {
		return _nbColumns;
	}
	
	public int getNbLines() {
		return _nbLines;
	}

	public void setSortCriteria(Criteria sortCriteria) {
		_comparator.setCriteria(sortCriteria);
	}

	public void setSortDirection(boolean sortAscendent) {
		_comparator.setOrder(sortAscendent);
	}

	public void setDelimitor(String delimitor) {
		_delimitor = delimitor;
	}
	private String _delimitor = "";

	public void setSeparator(String separator) {
		_separator = separator;
	}
	private String _separator = "";

}
