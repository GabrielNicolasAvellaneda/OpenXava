
import org.openxava.util.*;
import org.openxava.mapping.*;
import org.openxava.component.*;
import org.openxava.model.meta.*;

import java.io.*;
import java.util.*;

/**
 * @author Javier Paniza
 */
public class WebsphereCodeGenerator extends CodeGenerator {
	
	private String backendPath;
	
	public static void main(String [] argv) {
		if (argv.length != 4) {
			// tmp: Revisar mensaje i18n
			System.err.println(XavaResources.getString("generator_argv_required"));
			System.exit(1);			
		}
		try {									
			WebsphereCodeGenerator g = new WebsphereCodeGenerator();			
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
		
	public void generate(String componentsPath, String [] components) throws Exception {		
		backendPath = "../" + getProject() + "/build/ejb/META-INF/backends/OPENXAVA";			
		File dirBackend = new File(backendPath);
		dirBackend.mkdirs();
 
		// Generate dbxmi
		String [] argv = {				
			"../OpenXava/xava/controllers.xml", // A XML file is required, but it's not used
			backendPath + "/DB.dbxmi",			
		};
		WebsphereDbxmiPG.main(argv);
		
		// Generate schxmi
		for (Iterator it=Mapping.getSchemas().iterator(); it.hasNext();) {
			String schema = (String) it.next();
			String [] argvSch = {				
				"../OpenXava/xava/controllers.xml", // A XML file is required, but it's not used
				backendPath + "/" + schema +".schxmi",
				schema
			};
		
			WebsphereSchxmiPG.main(argvSch);
		}
		super.generate(componentsPath, components);
	}
	
	protected void generate(MetaComponent component, String componentsPath, String file) throws Exception {		
		String tableId = Strings.change(component.getEntityMapping().getTable(), ".", "_");
		String [] argv = {				
			componentsPath  + "/" + file,				
			backendPath + "/" + tableId + ".tblxmi",			
			component.getName()								
		};
		WebsphereTblxmiPG.main(argv);
		
		Iterator itAggregatesBean = component.getMetaAggregatesBeanGenerated().iterator();
		while (itAggregatesBean.hasNext()) {
			MetaAggregateBean aggregate = (MetaAggregateBean) itAggregatesBean.next();
			String aggregateName = aggregate.getName();
			String aggregateTableId = Strings.change(aggregate.getMapping().getTable(), ".", "_");
			String [] argvAg = {				
				componentsPath  + "/" + file,
				backendPath + "/" + aggregateTableId + ".tblxmi",				
				component.getName(),
				aggregateName				
			};			
			WebsphereTblxmiPG.main(argvAg);
		}			
		
	}
	
}
