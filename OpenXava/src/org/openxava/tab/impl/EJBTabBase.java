package org.openxava.tab.impl;

import java.rmi.*;
import java.util.*;

import javax.ejb.*;

import org.openxava.ejbx.*;
import org.openxava.util.*;


/**
 * Clase base para implementar un <code>IEntityTabImpl</code> usando un <i>Statefull SessionBean</i>. <p>
 *
 * Simplemente hay que sobreescribir los siguientes métodos:
 * <ul>
 * <li> {@link #buscarEntidad}
 * <li> {@link #getCabeceras}
 * <li> {@link #getClasesColumnas}
 * <li> {@link #getPropiedadesCondiciones}
 * </ul>
 *
 * En la clase hijo no es necesario escribir un <code>ejbCreate</code>,
 * pero si se hace hay que llamar a super. <br>
 *
 * <h4>Usos típicos</h4>
 * A continucación se exponen algunas sugerencias para resolver algunos
 * problemas comunes. Son solo sugerencias.
 *
 * <h5>Campos cálculados</h5>
 * En primer lugar hay que dejar el hueco al definir los campos en
 * CAMPOS. Por ejemplo:
 * <pre>
 * CAMPOS=CODIGO, DESCRIPCION, IMPORTE, 'TOTAL'
 * </pre>
 * Es de notar que 'TOTAL' está entrecomillado por lo que es una
 * constante puesta solo para reservar el hueco.<br>
 * Lo siguiente es sobreescribir el método {@link #filtrar}:
 * <pre>
 *  protected Object[] filtrar(Object[] fila) {
 *    try {
 *      MiEntidad e = (MiEntidad) getEntidad(fila);
 *      fila[3] = e.calcularTotal();
 *    }
 *    catch (Exception ex) {
 *      fila[3] = null; // Por ejemplo
 *  }
 *  return super.filtrar(fila);
 * </pre>
 * Aquí usamos el método {@link #getEntidad} para obtener la entidad a
 * partir de la fila.<br>
 *
 * <h5>Ocultar columnas</h5>
 * Si nos interesa ocultar columnas en el <tt>TableModel</tt> pero que
 * que sigan estando disponibles (para obtener la pk o para tener algunos
 * datos para hacer cálculos) lo podemos hacer de la siguiente forma:
 * <pre>
 * // Sobreescribe el método de org.openxava.tab.impl.EJBTabBase
 * public IXTableModel getTabla() throws java.rmi.RemoteException {
 *   int [] ocultas = {0, 1};
 *   IXTableModel decorada = new OcultaXTableModel(super.getTabla(), ocultas);
 *   return decorada;
 * }
 * </pre>
 * De esta forma ya no se visualizarían las columnas 0 y 1. Aunque siguen
 * estando disponibles a nivel de conseguir la <i>PrimaryKey</i> y en el
 * método filtrar, y no afecta a ninguno de los demás métodos, el efecto
 * solo es a nivel de visualización.<br>
 * 
 * @author  Javier Paniza
 */

// El implements IEntityTabImpl lo quitamos, se puede descomentar para compilar
abstract public class EJBTabBase extends SessionBase /*implements IEntityTabImpl*/ {

  private ContextTabProvider tabProvider;
  private TableModelBean table;

  /** Constructor por defecto. */
  public EJBTabBase() {
  }
  // Implementa IEntityTab
  public void search(int indice, Object clave) throws FinderException, RemoteException {
		tabProvider.search(indice, clave);
  }
  
  // Implementa IEntityTab
  public void search(String condicion, Object clave) throws FinderException, RemoteException {
		tabProvider.search(condicion, clave);
  }
  
  // Implementa IEntityTab
  /**
   * Crea un objeto de negocio (normalmente un EntityBean) a partir
   * de la clave enviada. <br>
   */
  abstract public Object findEntity(Object [] clave) throws FinderException, RemoteException;
  /**
   * Crea el bean, siempre se ha de llamar.<br>
   * Aquí se configura lo necesario para la gestión de
   * datos. <br>
   */
  public void ejbCreate() throws CreateException {
	tabProvider = new ContextTabProvider();
	table = new TableModelBean();
	iniciar();
  }
  
  public void ejbPassivate() throws java.rmi.RemoteException {
	table.setEntityTab(null);
	super.ejbPassivate();
  }
  /**
   * Filtra todas y cada una de las filas antes de enviarse al cliente. <p>
   *
   * Por defecto hace un <tt>trim()</tt> sobre los campos de tipo <tt>String</tt>,
   * pero se puede refinar para hacer filtraje de datos personalizado.<br>
   * Normalmente no se deben producir errores al filtra, pero si ocurre se
   * puede optar por tres soluciones:
   * <ul>
   * <li> Devolver un valor que indique un error al filtrar, p. ej. "¡Dato erroneo!".
   * <li> Devolver <tt>null</tt>. En caso de que esto no confunda demasiado al usuario.
   * <li> Lanzar una <tt>EJBException</tt>. Esto es demasiado brusco, teniendo en
   *                                        cuenta el propósito inocente de este método.
   * </ul>
   */
  protected Object[] filter(Object [] fila) {
	for (int i=0; i<fila.length; i++) {
	  if (fila[i] instanceof String) {
		fila[i] = ((String) fila[i]).trim();
	  }
	}
	return fila;
  }
  /** Cabeceras que aparecerán en la tabla. */
  abstract protected String [] getHeading();
  /**
   * Clase asociada a cada columna de la tabla. <br>
   * Se especifica el nombre de la clase completo en
   * un <code>String</code>.<br>
   */
  abstract protected String [] getColumnsClasses();
  /**
   * Devuelve la entidad correspondiente a la fila enviada. <p>
   *
   * Util para usar en {@link #filtrar}.<br>
   *
   * @param fila  Fila íntegra con los datos tabulares.
   * @exception  FinderException  Si no se consigue localizar la fila.
   * @exception  NullPointerException  Si <tt>fila == null</tt>.
   */
  protected Object getEntity(Object [] fila) throws FinderException {
	// assert(fila)
	int [] indicesPK = tabProvider.getIndexesPK();
	Object [] clave = new Object[indicesPK.length];
	for (int i=0; i< clave.length; i++) {
	  clave[i] = fila[indicesPK[i]];
	}
	try {
	  return findEntity(clave);
	}
	catch (RemoteException ex) {
		ex.printStackTrace();
	  throw new FinderException(XavaResources.getString("tab_entity_find_error"));
	}
  }
  /**
   * Nombres de las propiedades del EJB que almacenan las
   * condiciones de busqueda. <p>
   *
   * Las condiciones son una sentencia SELECT de SQL,
   * íntegra o partir del WHERE. Para más información
   * ver {@link JDBCTabProvider#getCondiciones}. <br>
   */
  abstract protected String [] getConditionsProperty();
  // Implementa IEntityTab
  public IXTableModel getTable() throws RemoteException {
	table.setEntityTab((IEntityTabImpl) getSessionContext().getEJBObject());
	return table;
  }
  /**
   * Inicialización del bean.
   */
  private void iniciar() throws CreateException {
	try {
	  table.setHeading(getHeading());
	  table.setColumnsClasses(getColumnsClasses());
	  tabProvider.setConditionsProperties(getConditionsProperty());
	  tabProvider.setContext(createEJBContext());
	  table.setPKIndexes(tabProvider.getIndexesPK());
	  table.invariant();
	  tabProvider.invariant();
	}
	catch (Exception ex) {
	  ex.printStackTrace();
	  throw new CreateException(XavaResources.getString("init_error"));
	}
  }
  // Implementa IEntityTabImpl
  public DataChunk nextChunk() throws RemoteException {
	// Filtramos todas las filas
	DataChunk tv = tabProvider.nextChunk();
	List data = tv.getData();
	System.out.println("[EJBTabBase.siguienteTrozo] v.size="+data.size());
	int l = data.size();
	for (int i=0; i<l; i++) {
	  data.set(i, filter((Object []) data.get(i)));
	}
	return tv;
  }

	/**
	 * 
	 */
	protected IEJBContext createEJBContext() throws InitException {
		return EJBContextFactory.create(getSessionContext());
	}
	
	public int getResultSize() {
		try {
			return tabProvider.getResultSize();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("tab_result_size_error"));
		}		 
	}
	
	public void reset() {
		try {
			tabProvider.reset();
		}
		catch (RemoteException e) {			
			e.printStackTrace();
			throw new EJBException(XavaResources.getString("tab_reset_error"));
		}					
	}

}
