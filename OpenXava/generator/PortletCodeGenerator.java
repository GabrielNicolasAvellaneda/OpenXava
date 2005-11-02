
import org.openxava.application.meta.*;

import java.io.*;
import java.util.*;
import org.openxava.util.*;

/**
 * @author Javier Paniza
 */
public class PortletCodeGenerator {
	
	private final static Locale [] locales = {
		new Locale("ca"), new Locale("de"), new Locale("en"), 
		new Locale("es"), new Locale("fr"), new Locale("in"), 
		new Locale("it"), new Locale("ja"), new Locale("ko"), 
		new Locale("nl"), new Locale("pt"), new Locale("sv"), 
		new Locale("zh")
	};
	
	private final static Locale defaultLocale = new Locale("en");
		
	private String project;
	private String pagesDir;
	private boolean generateJetspeed2Files;
		
	
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
	
	public boolean isGenerateJetspeed2Files() {
		return generateJetspeed2Files;
	}

	public void setGenerateJetspeed2Files(boolean generateJetspeed2Files) {
		this.generateJetspeed2Files = generateJetspeed2Files;
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
			
			if (isGenerateJetspeed2Files()) {
			
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
			
			// i18n resource files							
			File f = new File("../" + project + "/i18n/portlets/");
			f.mkdir();
			Locale.setDefault(defaultLocale);			
			MetaApplication app = MetaApplications.getMetaApplication(project);
			for (Iterator it=app.getMetaModules().iterator(); it.hasNext();) {
				MetaModule module = (MetaModule) it.next();
				createI18nFiles(module);			
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
			System.err.println(XavaResources.getString("generation_project_code_error", project)); 
		}
	}	
	
	private void createI18nFiles(MetaModule module) throws Exception {		
		for (int i = 0; i < locales.length; i++) {
			Properties i18n = new Properties();			
			i18n.put("javax.portlet.title", module.getDescription(locales[i]));
			i18n.put("javax.portlet.short-title", module.getLabel(locales[i]));
			i18n.store(new FileOutputStream("../" + project + "/i18n/portlets/" + module.getName() + "_" + locales[i].getLanguage() + ".properties"), null);
		}
	}
	
	public static void main(String [] argv) {
		if (argv.length < 2 || argv.length > 3) {
			System.err.println(XavaResources.getString("jetspeed2_generator_argv_required")); 
			System.exit(1);			
		}
		try {									
			PortletCodeGenerator g = new PortletCodeGenerator();			
			g.setProject(argv[0]);			
			g.setPagesDir(argv[1]);
			g.setGenerateJetspeed2Files(Boolean.valueOf(argv[2]).booleanValue()); // optional
			g.run();			
		}	
		catch (Exception ex) {
			ex.printStackTrace();
			System.exit(2);
		}	
	}
	
}
