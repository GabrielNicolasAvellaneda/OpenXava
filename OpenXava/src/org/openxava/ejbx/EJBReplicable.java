package org.openxava.ejbx;

import javax.ejb.*;

import org.openxava.util.*;


/**
 * Interfaz remota de un <i>EntityBean</i> que puede ser replicado. <p>
 *
 * Básicamente permite actualizar y obtener un cojunto de propiedades
 * de un solo golpe.<br>
 * 
 * @author  Javier Paniza
 */

public interface EJBReplicable extends EJBObject, IPropertiesContainer {

}
