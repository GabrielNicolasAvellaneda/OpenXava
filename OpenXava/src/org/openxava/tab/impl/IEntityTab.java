package org.openxava.tab.impl;




/**
 * Componente capaz de ofrecer datos de objetos de negocio
 * en forma tabular. <p>
 * Normalmente estos objetos de negocio serán <i>EntityBeans</i>,
 * Los datos se manipulan a través de un <@link IXTableModel>,
 * que se obtiene mediante <@link IConXTableModel#getTabla>. Esta tabla
 * puede mantener una conexión al <code>IEntityTab</code> originador para
 * seguir obteniendo datos de él.<br>
 * Lo típico es que este interface se implemente por un
 * <i>Stateful SessionBean</i>. Aunque el uso de tecnología
 * EJB no es requerido.<br>
 *
 * <h4>Ejemplo:</h4>
 * Este ejemplo es de uso de un <code>IEntityTab</code>, no de implementación.
 * <pre>
 * IEntityTab tab = obtenerEntityTab(); // Normalmente mediante JNDI
 * tableModel = tab.getTabla(); // Obtenemos la tabla que maneja los datos
 * jtable.setModel(tableModel); // La asignamos a una tabla de la swing
 *
 * tab.buscar(0, null); // Ejecutamos una consulta, la consulta 0
 * tableModel.refrescar(); // Los datos de la consulta se cargan en tableModel,
 *                         // por lo que se visualizan en jtable
 * </pre>
 *
 * @version 0.2.17
 * @author  Javier Paniza
 */

/*
00.01.27  Creación
00.02.17  Se divide la antigua clase en IEntityTab y IEntityTabImpl
*/


public interface IEntityTab extends IWithXTableModel, ISearch {

}
