package org.openxava.calculators;

/**
 * Concatena cadenas. <p>
 * 
 * @author Javier Paniza
 */ 
public class ConcatCalculator implements ICalculator {
	
	private String separator="";
	private Object string1="";
	private Object string2="";
	private Object string3=null;
	private Object string4=null;
	
	/**
	 * @see org.openxava.calculators.ICalculator#calculate()
	 */
	public Object calculate() throws Exception {
		StringBuffer r = new StringBuffer(string1==null?"":string1.toString());
		r.append(separator);
		r.append(string2);
		if (string3 != null) {
			r.append(separator);
			r.append(string3);
		}
		if (string4 != null) {
			r.append(separator);
			r.append(string4);
		}
		return r.toString();
	}

	/**
	 * Returns the cadena1.
	 * @return String
	 */
	public Object getString1() {
		return string1;
	}

	/**
	 * Returns the cadena2.
	 * @return String
	 */
	public Object getString2() {
		return string2;
	}

	/**
	 * Returns the separador.
	 * @return String
	 */
	public String getSeparator() {
		return separator;
	}

	/**
	 * Sets the cadena1.
	 * @param cadena1 The cadena1 to set
	 */
	public void setString1(Object cadena1) {
		this.string1 = cadena1;
	}

	/**
	 * Sets the cadena2.
	 * @param cadena2 The cadena2 to set
	 */
	public void setString2(Object cadena2) {
		this.string2 = cadena2;
	}

	/**
	 * Sets the separador.
	 * @param separador The separador to set
	 */
	public void setSeparator(String separador) {
		this.separator = separador;
	}

	public Object getString3() {
		return string3;
	}

	public void setString3(Object string) {
		string3 = string;
	}

	/**
	 * @return
	 */
	public Object getString4() {
		return string4;
	}

	/**
	 * @param object
	 */
	public void setString4(Object object) {
		string4 = object;
	}

	/**
	 * 
	 */
	public int getInt1() {
		return Integer.parseInt((String)string1);
	}
	
	/**
	 * 
	 */
	public void setInt1(int int1) {
		this.string1 = String.valueOf(int1);
	}

	/**
	 * 
	 */
	public int getInt2() {
		return Integer.parseInt((String)string2);
	}
	
	/**
	 * 
	 */
	public void setInt2(int int2) {
		this.string2 = String.valueOf(int2);
	}

	/**
	 * 
	 */
	public int getInt3() {
		return Integer.parseInt((String)string3);
	}
	
	/**
	 * 
	 */
	public void setInt3(int int3) {
		this.string3 = String.valueOf(int3);
	}

	/**
	 * 
	 */
	public int getInt4() {
		return Integer.parseInt((String)string4);
	}
	
	/**
	 * 
	 */
	public void setInt4(int int4) {
		this.string4 = String.valueOf(int4);
	}
}
