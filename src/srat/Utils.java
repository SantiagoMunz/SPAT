package srat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class Utils {
	//read file content into a string
	public static String readFileToString(String filePath) throws IOException {
		StringBuilder fileData = new StringBuilder(1000);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
 
		char[] buf = new char[10];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			//System.out.println(numRead);
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}
 
		reader.close();
 
		return  fileData.toString();	
	}
	
	//generating random string
	public static String getRandomString(int length){
	     String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	     String head = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	     Random random=new Random();
	     StringBuffer sb=new StringBuffer();
	     int number=random.nextInt(52);
	     sb.append(head.charAt(number));
	     for(int i=1;i<length;i++){
	       number=random.nextInt(62);
	       sb.append(str.charAt(number));
	     }
	     return sb.toString();
	 }
	
	public static String [] SingleStr2priList(String t) {
		ArrayList<String> tmp = new ArrayList<String>();
		tmp.add(t);
		return ArryStr2priStrList(tmp);
	}
	
	
	public static String [] ArryStr2priStrList(ArrayList<String> t) {
		int sizer=t.size();  
		String[] arrString = (String[])t.toArray(new String[sizer]) ;
		return arrString;
	}
	
	
	public static String FromCompilationUnit2ClassName(CompilationUnit node) {
		return ((TypeDeclaration)node.types().get(0)).getName().getIdentifier();
	}

	public static void applyRewrite(CompilationUnit node, String newcode, String outputDirPath) {
		// TODO Auto-generated method stub
		String classname = FromCompilationUnit2ClassName(node);
		String filepath = outputDirPath + "\\" + classname + ".java";
		try {
			FileWriter writer = new FileWriter(filepath);
			writer.write(newcode);
            writer.flush();
            writer.close();
            System.out.println(filepath + "	reWrited successfully!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
