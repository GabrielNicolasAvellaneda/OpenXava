
import org.openxava.util.*;
import org.openxava.component.*;
import org.openxava.model.meta.*;

import java.io.*;
import java.util.*;

/**
 * To generate hibernate code. <P>
 * 
 * @author Mª Carmen Gimeno
 */
public class HCodeGenerator extends CodeGenerator {
	
	public static void main(String [] argv) {
		if (argv.length != 4) {
			System.err.println(XavaResources.getString("generator_argv_required"));
			System.exit(1);			
		}
		try {									
			HCodeGenerator g = new HCodeGenerator();			
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
		
	protected void generate(MetaComponent component, String componentsPath, String file) throws Exception {
		if (!component.getMetaEntity().isGenerateXDocLet()) return; //by now, better isGeneratePojo() or so
		String dirPackage = toDirPackage(getPackageName());		
		String modelPath = "../" + getProject() + "/gen-src-xava/" + dirPackage; //tmp ¿una sola vez?		
		// Creataing directories
		File fModelPath = new File(modelPath);
		fModelPath.mkdirs();
		
		// Main entity			
		System.out.println(XavaResources.getString("generating_pojo_code", component.getName()));			
		String [] argv = {				
			componentsPath  + "/" + file,				
			modelPath + "/" + component.getName() + ".java",
			getJavaPackage(),
			component.getName()								
		};
		PojoPG.main(argv);
		
		//	 Aggregates in bean format
		Iterator itAggregatesBean = component.getMetaAggregatesBeanGenerated().iterator();
		while (itAggregatesBean.hasNext()) {
			MetaAggregateBean aggregate = (MetaAggregateBean) itAggregatesBean.next();
			String aggregateName = aggregate.getName();
			String [] argvAg = {				
				componentsPath  + "/" + file,
				modelPath + "/" + aggregateName + ".java",
				getJavaPackage(),
				component.getName(),
				aggregateName				
			};
			System.out.println(XavaResources.getString("generating_aggregate_javabean_code", aggregateName));
			BeanPG.main(argvAg);
		}
		
		// Hibernate mapping
		String mappingPath = "../" + getProject() + "/build/hibernate/"; //tmp ¿una sola vez?
		System.out.println(XavaResources.getString("generating_hibernate_mapping", component.getName()));			
		String [] argvMap = {				
			componentsPath  + "/" + file,				
			mappingPath + component.getName() + ".hbm.xml",
			getJavaPackage(),
			component.getName()								
		};
		HibernatePG.main(argvMap);
		
	}
		
}