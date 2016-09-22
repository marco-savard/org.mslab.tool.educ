package org.mslab.tool.educ.shared.types.educ;

import java.io.Serializable;

import org.mslab.tool.educ.shared.text.Text;

@SuppressWarnings("serial")
public class OrdreAppartenance implements Serializable, Comparable<OrdreAppartenance> {
	private static final int PRESCOLAIRE = 1;
	private static final int PRIMAIRE = 2;
	private static final int SECONDAIRE = 4;
	private static final int FORMATION_PROF = 8;
	private static final int FORMATION_ADULTES = 16;
	private static final int COLLEGIAL = 32;
	private static final int UNIVERSITAIRE = 64;
	
	private static final String[] NAMES = new String[] {
		"préscolaire", "primaire", "secondaire", "formation professionnelle", 
		"adultes", "collégial", "universitaire"
	}; 
	
	private int _value = 0;
	
	//GWT required
	@SuppressWarnings("unused")
	private OrdreAppartenance() {}

	public OrdreAppartenance(String text) {
		text = text.toLowerCase(); 
		_value += (text.indexOf("préscolaire") != -1) ? PRESCOLAIRE : 0;
		_value += (text.indexOf("primaire") != -1) ? PRIMAIRE : 0;
		_value += (text.indexOf("secondaire") != -1) ? SECONDAIRE : 0;
		_value += (text.indexOf("professionnelle") != -1) ? FORMATION_PROF : 0;
		_value += (text.indexOf("adultes") != -1) ? FORMATION_ADULTES : 0;
		_value += (text.indexOf("collégial") != -1) ? COLLEGIAL : 0;
		_value += (text.indexOf("universitaire") != -1) ? UNIVERSITAIRE : 0;
	}
	
	@Override
	public String toString() {
		String s = Integer.toString(_value);
		return s;
	}
	
	@Override
	public boolean equals(Object that) {
		boolean equal = (that instanceof OrdreAppartenance) ? 
			_value == ((OrdreAppartenance)that)._value : 
			false;
		return equal;
	}

	public static OrdreAppartenance fromCode(String ordreApp) {
		OrdreAppartenance ordre = new OrdreAppartenance();
		ordre._value = Integer.parseInt(ordreApp);
		return ordre;
	}

	public String getName() {
		StringBuffer buf = new StringBuffer(); 
		boolean empty = true; 
		
		for (int i=0; i<=7; i++) {
			int match = _value & (1 << i);
			if (match != 0) {
				if (! empty) {
					buf.append(", "); 
				}
				
				String word = Text.capitalizeWords(NAMES[i]);
				buf.append(word);
				empty = false;
			}
		}
		
		return buf.toString();
	}

	@Override
	public int compareTo(OrdreAppartenance that) {
		int comparison = _value - that._value;
		return comparison;
	}

}
