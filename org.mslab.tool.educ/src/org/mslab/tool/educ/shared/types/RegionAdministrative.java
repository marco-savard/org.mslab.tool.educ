package org.mslab.tool.educ.shared.types;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.mslab.tool.educ.shared.text.DecimalFormat;
import org.mslab.tool.educ.shared.text.MessageFormat;

@SuppressWarnings("serial")
public class RegionAdministrative implements Serializable, Comparable<RegionAdministrative> {
	private static DecimalFormat _formatter = new DecimalFormat("0");
	private int _idx;
	
	//required by GWT
	@SuppressWarnings("unused")
	private RegionAdministrative() {}

	public RegionAdministrative(String name) {
		if (_regionList == null) {
			_regionList = Arrays.asList(REGIONS); 
		}
		
		name = name.replace("--", "–");
		_idx = _regionList.indexOf(name); 
		
		if (_idx == -1) {
			_idx = 0;
		}
	}
	
	public static RegionAdministrative createFromCode(String code) {
		RegionAdministrative region = new RegionAdministrative(); 
		region._idx = Integer.parseInt(code) - 1; 
		
		if (region._idx == -1) {
			region._idx = 0;
		}
		
		return region;
	}

	@Override
	public int compareTo(RegionAdministrative that) {
		int comparison = _idx - that._idx;
		return comparison;
	}
	
	@Override
	public String toString() {	
		String code = getCode();
		String text = MessageFormat.format("{0} {1}", 
			code, REGIONS[_idx]);
		return text;
	}
	
	public String getCode() {
		String code = (_idx < 9) ? "0" + (_idx+1) : Integer.toString(_idx+1); 
		return code;
	}
	
	@Override
	public boolean equals(Object that) {
		boolean equal = false;
		
		if (that instanceof RegionAdministrative) {
			RegionAdministrative other = (RegionAdministrative)that;
			equal = _idx == other._idx; 
		}

		return equal;
	}

	public String getName() {
		if (_idx >= REGIONS.length) {
			_idx = 0;
		}
		
		return REGIONS[_idx];
	}
	
	private static List<String> _regionList;
	private static final String[] REGIONS = new String[] {
		"Bas-Saint-Laurent", 
		"Saguenay–Lac-Saint-Jean",
		"Capitale-Nationale", 
		"Mauricie", 
		"Estrie", 
		"Montréal", 
		"Outaouais", 
		"Abitibi-Témiscamingue", 
		"Côte-Nord", 
		"Nord-du-Québec", 
		"Gaspésie–Îles-de-la-Madeleine", 
		"Chaudière-Appalaches", 
		"Laval",
		"Lanaudière", 
		"Laurentides", 
		"Montérégie", 
		"Centre-du-Québec"
	};




}
