
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
		String backendPath = "../" + getProject() + "/build/ejb/META-INF/backends/OPENXAVA";			
		File dirBackend = new File(backendPath);
		dirBackend.mkdirs();
 
		// Generate dbxmi
		String [] argv = {				
			"../OpenXava/xava/controllers.xml", // A XML file is required, but it's not used
			backendPath + "/DB.dbxmi",			
		};
		WebsphereDbxmiPG.main(argv);
		
		// Generate schxmi
		for (Iterator it=ModelMapping.getSchemas().iterator(); it.hasNext();) {
			String schema = (String) it.next();
			String [] argvSch = {				
				"../OpenXava/xava/controllers.xml", // A XML file is required, but it's not used
				backendPath + "/" + schema +".schxmi",
				schema
			};
		
			WebsphereSchxmiPG.main(argvSch);
		}
		
	}
	
	protected void generate(MetaComponent component, String componentsPath, String file) throws Exception {		
	}
	
}
