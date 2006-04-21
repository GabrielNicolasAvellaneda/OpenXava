
import org.openxava.util.*;
import org.openxava.component.*;
import org.openxava.model.meta.*;
import java.io.*;
import java.util.*;

/**
 * @author Javier Paniza
 */
public class EJBCodeGenerator extends CodeGenerator{
	
	protected void generate(MetaComponent component, String componentsPath, String file) throws Exception {		
		String dirPackage = toDirPackage(getPackageName());		
		String modelPath = "../" + getProject() + "/gen-src-xava/" + dirPackage;		
		String modelXEjbPath = modelPath + "/xejb";
		// Creataing directories
		File fModelPath = new File(modelPath);
		fModelPath.mkdirs();
		File fModelXEjbPath = new File(modelXEjbPath);
		fModelXEjbPath.mkdirs();			
		// Main entity			
		if (component.getMetaEntity().isEjbGenerated()) {			
			System.out.println(XavaResources.getString("generating_xdoclet_code", component.getName()));			
			String [] argv = {				
				componentsPath  + "/" + file,				
				modelXEjbPath + "/" + component.getName() + "Bean.java",
				getJavaPackage(),
				component.getName()								
			};
			EJBeanPG.main(argv);
			
			String [] argvInterface = {
				componentsPath  + "/" + file,
				modelPath + "/I" + component.getName() + ".java",
				getJavaPackage(),
				component.getName()
			};						
			InterfacePG.main(argvInterface);						
		}				
		// Aggregates in bean format
		Iterator itAggregatesBean = component.getMetaAggregatesBeanGenerated().iterator();
		while (itAggregatesBean.hasNext()) {
			MetaAggregateForReference aggregate = (MetaAggregateForReference) itAggregatesBean.next();
			String aggregateName = aggregate.getName();
			String [] argv = {				
				componentsPath  + "/" + file,
				modelPath + "/" + aggregateName + ".java",
				getJavaPackage(),
				component.getName(),
				aggregateName				
			};
			System.out.println(XavaResources.getString("generating_aggregate_javabean_code", aggregateName));
			BeanPG.main(argv);
		}			
		// Agreggates in EJB format		
		Iterator itAggregatesEjb = component.getMetaAggregatesForCollectionEjbGenerated().iterator();
		while (itAggregatesEjb.hasNext()) {
			MetaAggregateForCollection aggregate = (MetaAggregateForCollection) itAggregatesEjb.next();
			String aggregateName = aggregate.getName();
			
			System.out.println(XavaResources.getString("generating_aggregate_xdoclet_code", aggregateName));
			
			String [] argv = {				
				componentsPath  + "/" + file,
				modelXEjbPath + "/" + aggregateName + "Bean.java",
				getJavaPackage(),
				component.getName(),
				aggregateName				
			};
			
			EJBeanPG.main(argv);
			
			String [] argvInterface = {
				componentsPath  + "/" + file,
				modelPath + "/I" + aggregateName + ".java",
				getJavaPackage(),
				component.getName(),
				aggregateName
			};
			InterfacePG.main(argvInterface);			
		}		
	}
	
	protected String getDNAFile() {
		return "dnas-ejb.properties";
	}

	
	public static void main(String [] argv) {
		if (argv.length != 4) {
			System.err.println(XavaResources.getString("generator_argv_required"));
			System.exit(1);			
		}
		try {									
			EJBCodeGenerator g = new EJBCodeGenerator();			
			g.setProject(argv[0]);			
			g.setDomain(argv[1]);			
			g.setUnqualifiedPackage(argv[2]);			
			g.setModelPackage(argv[3]);
			g.run();			
		}	
		catch (Exception ex) {
			ex.printStackTrace();
			System.exit(2);
		}	
	}

}
