package org.openxava.calculators;

import java.rmi.*;


/**
 * Calculador que recibe una entidad sobre que usa de fuente para hacer el cálculo. 
 * 
 * Este tipo de calculador se usa para generar código de las propiedades cálculadas
 * al generar <i>EntityBeans</i> y para cálcular los campos calculados en el tab. También
 * puede usarse en el futuro(¿presente?) para calcular en la interfaz gráfica).<br>
 * Dentro de un <i>EntityBean</i> recibirá el propio bean, mientras que en el
 * tab recibe el objeto remoto. Así que si queremos que este calculador se pueda
 * usar en un EntityBean y en el tab lo ideal es hacer una interfaz que sea
 * implementado por el interfaz remoto y por el bean, y usar esa interfaz al
 * moldear. El generador de código Xava crea un interfaz por cada componente
 * que puede ser usado para esto.<p>
 * 
 * 
 * @author Javier Paniza
 */
public interface IEntityCalculator extends ICalculator {
	
	void setEntity(Object entity) throws RemoteException;

}
