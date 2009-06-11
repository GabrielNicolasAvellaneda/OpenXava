package org.openxava.test.tests;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxava.component.MetaComponent;
import org.openxava.model.meta.MetaCollection;
import org.openxava.model.meta.MetaMember;
import org.openxava.view.meta.MetaView;
import org.openxava.view.meta.PropertiesSeparator;

/**
 * Create on 25/05/2009 (12:32:14)
 * @autor Ana Andrés
 */
public class PruebasInstantaneas {
	private static Log log = LogFactory.getLog(PruebasInstantaneas.class);

	private static MetaComponent component = MetaComponent.get("Carrier");
	private static String vista = "CollectionsTogether";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MetaView mv = 
			component.getMetaEntity().getMetaView(vista);
		Collection miembros = mv.getMetaMembers();
		Iterator it = miembros.iterator();
		while(it.hasNext()){
			MetaMember mm = (MetaMember) it.next();
			if (!PropertiesSeparator.INSTANCE.equals(mm)) { 
				String nombre = mm.getName();
				log.info("[PruebasInstantaneas.main] miembro:" + nombre); // tmp
				if (mm instanceof MetaCollection){
					boolean a = isVariousCollectionsInSameLine(nombre);
				}
			}
		}
	}

	public static boolean isVariousCollectionsInSameLine(String propiedad){
		Collection<MetaMember> metaMembersLine = getMetaMembersInTheLine(propiedad);
		if (metaMembersLine.size() <= 1) return false;
		
		Iterator<MetaMember> it = metaMembersLine.iterator();
		boolean allCollection = true;
		while(it.hasNext() && allCollection){
			MetaMember metaMember = it.next();
			if (!(metaMember instanceof MetaCollection)) allCollection = false;
		}
		
		return allCollection;
	}
	
	public static Collection<MetaMember> getMetaMembersInTheLine(String propiedad){
		MetaView metaView = component.getMetaEntity().getMetaView(vista);
		Collection<MetaMember> member = metaView.getMetaMembers();
		Iterator<MetaMember> it = member.iterator();
		boolean found = false;
		Collection<MetaMember> line = new ArrayList<MetaMember>();
		while (it.hasNext() && !found){
			MetaMember metaMember = (MetaMember) it.next();
			if (propiedad.equals(metaMember.getName())) found = true;
			if (found){
				line.add(metaMember);
				boolean finLinea = false;
				while (it.hasNext() && !finLinea){
					MetaMember metaMember2 = (MetaMember) it.next();
					if (PropertiesSeparator.INSTANCE.equals(metaMember2)) finLinea = true;
					else line.add(metaMember2);
				}
			}
			else {
				if (PropertiesSeparator.INSTANCE.equals(metaMember)) line = new ArrayList<MetaMember>();
				else line.add(metaMember);
			}
		}
		return line;
	}
	
	public static boolean isEndInTheLine(String property){
		Collection<MetaMember> members = getMetaMembersInTheLine(property);
		MetaMember last = null;
		for(MetaMember  member : members) last = member;
		return property.equals(last.getName());
	}
}
