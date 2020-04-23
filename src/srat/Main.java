package srat;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.Document;
public class Main {
	//use ASTParse to parse string
	public static void parse(String str, String dirPath,String outputdir,String[] arrString) {
		Document document = new Document(str);
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setResolveBindings(true);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
 
		parser.setBindingsRecovery(true);
 
		Map<String, String> options = JavaCore.getOptions();
		parser.setCompilerOptions(options);
 
		String unitName = "Apple.java";
		parser.setUnitName(unitName);
 
		String[] sources = Utils.SingleStr2priList(dirPath); 
		String[] classpath = arrString;
 
		parser.setEnvironment(classpath, sources, new String[] { "UTF-8"}, true);
		parser.setSource(str.toCharArray());
 
		CompilationUnit cu = (CompilationUnit) parser.createAST(null);
//		
//		if (cu.getAST().hasBindingsRecovery()) {
//			System.out.println("Binding activated.");
//		}
//		else {
//			System.out.println("Binding is not activated.");
//		}
		
		cu.accept(new LocalVariableRenaming(cu, document, outputdir));
			
 
	}
	 
	
 
	//loop directory to get file list
	public static void ParseFilesInDir(String dirPath,String outputdir,String[] arrString) throws IOException{
//		SOURCES = new ArrayList<String>();
		File root = new File(dirPath);
		//System.out.println(rootDir.listFiles());
		File[] files = root.listFiles( );
		String filePath = null;
 
		 for (File f : files ) {
			 filePath = f.getAbsolutePath();
			 if(f.isFile()){
				 System.out.println("Current File is: " + filePath);
				 parse(Utils.readFileToString(filePath), dirPath,outputdir, arrString);
			 }
		 }
	}
 
	public static void main(String[] args) throws IOException {
		if(args.length != 3) {
			System.out.println("SRAT needs three arguments to run properly: "
					+ "[DirPathOftheSourcefiles] [OutputDir] [PathoftheJre(rt.jar)] & [PathofotherDependentJar] "
					+ "for example \"C:\\Program Files\\Java\\jre1.8.0_211\\lib\\rt.jar\"");
			System.exit(4);
		}
		String dirOfthefiles = args[0];
		String outputdir = args[1];
		ArrayList <String> jre_rtPath = new ArrayList<String>();
		for(int i = 2; i < args.length;i++) {
			jre_rtPath.add(args[i]);
		}
		
		ParseFilesInDir(dirOfthefiles, outputdir, Utils.ArryStr2priStrList(jre_rtPath));
	}
}