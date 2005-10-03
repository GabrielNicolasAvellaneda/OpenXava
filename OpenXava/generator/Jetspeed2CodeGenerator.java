
import org.openxava.application.meta.*;

import java.util.*;
import org.openxava.util.*;

/**
 * @author Javier Paniza
 */
public class Jetspeed2CodeGenerator {
	
	private String project;
	private String pagesDir;
		
	
	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}
	
	public String getPagesDir() {
		return pagesDir;
	}

	public void setPagesDir(String pagesDir) {
		this.pagesDir = pagesDir;
	}
		
	private void run() throws Exception {		 
		try {
			// portlet.xml
			String [] argvPortlet = {
				"../" + project + "/web/WEB-INF/web.xml",
				"../" + project + "/web/WEB-INF/portlet.xml",
				project
			};
			PortletXmlPG.main(argvPortlet);
			
			// folder.metadata
			String [] argvFolder = {				
				"../" + project + "/web/WEB-INF/web.xml",				
				pagesDir + "/" + project + "/folder.metadata",
				project
			};			
			Jetspeed2FolderPG.main(argvFolder);
			
			// ds
			String [] argvDs = {				
				"../" + project + "/web/WEB-INF/web.xml",				
				pagesDir + "/" + project + "/" + project + ".ds",
				project
			};			
			Jetspeed2DsPG.main(argvDs);
						
			// psml			
			MetaApplication app = MetaApplications.getMetaApplication(project);
			for (Iterator it=app.getMetaModules().iterator(); it.hasNext();) {
				MetaModule module = (MetaModule) it.next();
				String [] argvPsml = {				
					"../" + project + "/web/WEB-INF/web.xml",				
					pagesDir + "/" + project + "/" + module.getName() + ".psml",
					project,
					module.getName()
				};
				Jetspeed2PsmlPG.main(argvPsml);
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
			System.err.println(XavaResources.getString("generation_project_code_error", project)); 
		}
	}	
	
	public static void main(String [] argv) {
		if (argv.length != 2) {
			System.err.println(XavaResources.getString("jetspeed2_generator_argv_required")); 
			System.exit(1);			
		}
		try {									
			Jetspeed2CodeGenerator g = new Jetspeed2CodeGenerator();			
			g.setProject(argv[0]);			
			g.setPagesDir(argv[1]);			
			g.run();			
		}	
		catch (Exception ex) {
			ex.printStackTrace();
			System.exit(2);
		}	
	}
	
}
