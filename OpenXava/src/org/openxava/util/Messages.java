package org.openxava.util;

import java.text.*;
import java.util.*;

import javax.servlet.*;

import org.openxava.application.meta.*;
import org.openxava.model.meta.*;


/**
 * Set of messages. <p>
 * 
 * @author Javier Paniza
 */

public class Messages implements java.io.Serializable {
		
	class Message implements java.io.Serializable {
		private String id;
		private Object [] argv;
		
		public Message(String id, Object [] argv) {
			this.id = id;
			this.argv = argv;
		}
		
		public Message(String id) {
			this(id, null);
		}
		
		
		public String toString() {
			return toString(Locale.getDefault());
		}
		
		public boolean equals(Object otro) {
			Message m = (Message) otro;
			return id.equals(m.id);
		}
		
		public String toString(Locale locale) {
			try {
				String m = getMessage(id, locale);
				if (argv == null || argv.length == 0) return m;
				return formatear(m, traducir(argv, locale), locale);
			}
			catch (Exception ex) {
				System.err.println("¡ADVERTENCIA! No es posible obtener el mensaje " + id);
				return id;
			}
		}
				
		private Object[] traducir(Object[] argv, Locale locale) {			
			Object [] result = new Object[argv.length];
			for (int i = 0; i < argv.length; i++) {
				Object v = argv[i];
				if (v instanceof String) {
					try {
						try {
							result[i] = getMessage((String)v, locale);
						}
						catch (MissingResourceException ex) {						
							result[i] = Labels.removeUnderlined(Labels.get((String)v, locale)); 
						}
					}
					catch (Exception ex) {
						result[i] = v;
					}
				}
				else {
					result[i] = v;
				}
			}			
			return result;
		}

		private String getMessage(String id, Locale locale) throws MissingResourceException, XavaException {
			Iterator it = MetaApplications.getApplicationsNames().iterator();
			while (it.hasNext()) {
				String name = (String) it.next();
				try {
					ResourceBundle rb = ResourceBundle.getBundle(name + "-messages", locale);
					return rb.getString(id);
				}
				catch (MissingResourceException ex) {
				}							
				try {
					ResourceBundle rb = ResourceBundle.getBundle("Mensajes" + name, locale);
					return rb.getString(id);
				}
				catch (MissingResourceException ex) {
				}			
			}		
			ResourceBundle rb = ResourceBundle.getBundle("Messages", locale);
			return rb.getString(id);
		}
		
		private String formatear(String mensaje, Object [] argv, Locale locale) {
			MessageFormat formateador = new MessageFormat("");
			formateador.setLocale(locale);
			formateador.applyPattern(mensaje);
			return formateador.format(argv);		
		}
		
	}
	
	private Collection mensajes = new ArrayList();
	private Collection miembros;
		
	public void add(String idMensaje) {
		mensajes.add(new Message(idMensaje));		
	}
	
	public boolean contains(String idMensaje) {
		return mensajes.contains(new Message(idMensaje));
	}
	
	public void remove(String idMensaje) {
		mensajes.remove(new Message(idMensaje));		
	}
	public void removeAll() {
		mensajes.clear();
		if (miembros != null) miembros.clear();
	}
	
	public void add(String idMensaje, Object [] ids) {		
		if (ids != null) {
			if (ids.length == 1) addMember(ids[0]);
			else if (ids.length > 1) {
				addMember(ids[0], ids[1]);
			} 
		}
		mensajes.add(new Message(idMensaje, ids));
	}
	

	private void addMember(Object miembro) {
		addMember(miembro, null);
	}

	private void addMember(Object miembro, Object modelo) {		
		if (miembro instanceof String) {
			Object id = null;
			if (modelo instanceof String) id = miembro; 
			else id = modelo + "." + miembro;
			if (miembros == null) miembros = new ArrayList();		
			miembros.add(id); 
		}				
	}

	public void add(String idMensaje, Object id0) {
		add(idMensaje, new Object [] { id0 });
	}
	
	public void add(String idMensaje, Object id0, Object id1) {
		add(idMensaje, new Object [] { id0, id1 });
	}
	
	public void add(String idMensaje, Object id0, Object id1, Object id2) {
		add(idMensaje, new Object [] { id0, id1, id2 });
	}
	
	public void add(String idMensaje, Object id0, Object id1, Object id2, Object id3) {
		add(idMensaje, new Object [] { id0, id1, id2, id3 });
	}
	
	public void add(String idMensaje, Object id0, Object id1, Object id2, Object id3, Object id4) {
		add(idMensaje, new Object [] { id0, id1, id2, id3, id4 });
	}
	
	public void add(String idMensaje, Object id0, Object id1, Object id2, Object id3, Object id4, Object id5) {
		add(idMensaje, new Object [] { id0, id1, id2, id3, id4, id5 });
	}
	
	
	
	
	public boolean contains() {
		return !mensajes.isEmpty();
	}
	
	public boolean isEmpty() {
		return mensajes.isEmpty();
	}
	
	public String toString() {
		Iterator it = mensajes.iterator();
		StringBuffer r = new StringBuffer();
		while (it.hasNext()) {			
			r.append(it.next());
			r.append('\n');
		}
		return r.toString();
	}
	
	public void add(Messages mensajes) {		
		this.mensajes.addAll(mensajes.mensajes);		
		if (mensajes.miembros != null) {
			if (this.miembros == null) this.miembros = new ArrayList();
			this.miembros.addAll(mensajes.miembros);
		}
	}
	
	public Collection getStrings() {
		return getStrings(Locale.getDefault());
	}
	
	public Collection getStrings(ServletRequest request) {
		return getStrings(XavaResources.getLocale(request));
	}
	
	public Collection getStrings(Locale locale) {
		Iterator it = mensajes.iterator();
		Collection r = new ArrayList();
		while (it.hasNext()) {
			r.add(((Message) it.next()).toString(locale));
		}
		return r;
	}
	
	public boolean memberHas(MetaMember m) {
		if (miembros == null) return false;		
		if (m.getMetaModel() != null && miembros.contains(m.getMetaModel().getName() + "." + m.getName())) return true;
		return miembros.contains(m.getName());
	}

}
