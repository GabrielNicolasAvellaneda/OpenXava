package org.openxava.calculators;

import org.openxava.util.*;


/**
 * @author Javier Paniza
 */
public interface IJDBCCalculator extends ICalculator {
	
	/**
	 * Este metodo es llamado desde el sistema para proveer
	 * un proveedor de conexiones JDBC. <p>
	 * 
	 * Cuando se ejecuta calcular el sistema ya ha llamado
	 * a este metodo con un proveedor de conexiones valido.<br>
	 */
	public void setConnectionProvider(IConnectionProvider proveedor);

}
