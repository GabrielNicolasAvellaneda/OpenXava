package org.openxava.calculators;

import java.rmi.*;


/**
 * Calculador que recibe una entidad sobre que usa de fuente para hacer el c�lculo. 
 * 
 * Este tipo de calculador se usa para generar c�digo de las propiedades c�lculadas
 * al generar <i>EntityBeans</i> y para c�lcular los campos calculados en el tab. Tambi�n
 * puede usarse en el futuro(�presente?) para calcular en la interfaz gr�fica).<br>
 * Dentro de un <i>EntityBean</i> recibir� el propio bean, mientras que en el
 * tab recibe el objeto remoto. As� que si queremos que este calculador se pueda
 * usar en un EntityBean y en el tab lo ideal es hacer una interfaz que sea
 * implementado por el interfaz remoto y por el bean, y usar esa interfaz al
 * moldear. El generador de c�digo Xava crea un interfaz por cada componente
 * que puede ser usado para esto.<p>
 * 
 * 
 * @author Javier Paniza
 */
public interface IEntityCalculator extends ICalculator {
	
	void setEntity(Object entity) throws RemoteException;

}
