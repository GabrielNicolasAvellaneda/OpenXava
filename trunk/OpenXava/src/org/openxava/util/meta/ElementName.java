package org.openxava.util.meta;

/**
 * No se usa para definir el nombre, sino como utilidad
 * para desglosarlo. <p>
 * 
 * @author Javier Paniza
 */
public class ElementName {
	
	private String name;
	private int idxPunto;
	private boolean idxPuntoObtenido;
	private String containerName;	
	private String unqualifiedName;

	public ElementName(String nombre) {
		this.name = nombre==null?"":nombre;
	}
	
	public boolean isQualified() {
		return getIdxPunto() >= 0;
	}
	
	public String getContainerName() {
		if (containerName == null) {
			containerName = isQualified()?name.substring(0, idxPunto):"";
		}
		return containerName;
	}
	
	public String getUnqualifiedName() {
		if (unqualifiedName == null) {
			unqualifiedName = isQualified()?name.substring(idxPunto+1):name;
		}
		return unqualifiedName;		
	}
	
	private int getIdxPunto() {
		if (!idxPuntoObtenido) {
			idxPunto = name.indexOf('.');
			idxPuntoObtenido = true;
		}
		return idxPunto;
	}
	
	public String toString() {
		return name;
	}
	

}

