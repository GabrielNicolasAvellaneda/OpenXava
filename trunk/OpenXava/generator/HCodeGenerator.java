
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
		String modelPath = "../" + getProject() + "/gen-src-xava/" + dirPackage;		
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
			
	}
		
}