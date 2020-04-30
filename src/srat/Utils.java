package srat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.text.edits.UndoEdit;

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
	
	public static Statement father2AStatement(ASTNode node) {
		while(!(node.getParent() instanceof  Statement)) {
			return father2AStatement(node.getParent());
		}
		return (Statement) node.getParent();
		
	}
	

	public static void applyRewrite(CompilationUnit node, TextEdit edits, Document document, String outputDirPath) {
		//Code below is to reformat the transfered codes.
		@SuppressWarnings("unused")
		UndoEdit undo = null;
		 try {
		     undo = edits.apply(document);
		 } catch(MalformedTreeException e) {
		     e.printStackTrace();
		 } catch(BadLocationException e) {
		     e.printStackTrace();
		 }
		 
		 CodeFormatter codeFormatter = ToolFactory.createCodeFormatter(null, ToolFactory.M_FORMAT_EXISTING);
		 String code = document.get();
		 TextEdit textEdit = codeFormatter.format(CodeFormatter.K_COMPILATION_UNIT, code, 0, code.length(), 0, "\n");
		 IDocument formatteddoc = new Document(code);
		 try {
				textEdit.apply(formatteddoc);
			} catch (MalformedTreeException e) {
				e.printStackTrace();
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		String newcode = formatteddoc.get();
		
		
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

	public static InfixExpression father2AInfixExpression(Expression node) {
		if(!(node.getParent() instanceof  Expression)) {
			return null;
		}
		if(!(node.getParent() instanceof InfixExpression)) {
			return father2AInfixExpression( (Expression) node.getParent());
		}
		return (InfixExpression) node.getParent();
	}

	public static Statement father2AListRewriterForStatementInserting(ASTNode node, ASTRewrite rewriter) {
		if(node == null) {
			return null;
		}
		if(! (node instanceof Statement)) {
			return father2AListRewriterForStatementInserting(node.getParent(), rewriter);
		}
		try {
			@SuppressWarnings("unused")
			ListRewrite lrt = rewriter.getListRewrite(node.getParent(), (ChildListPropertyDescriptor) node.getLocationInParent());
			return (Statement) node;
		}catch (Exception e) {
			return father2AListRewriterForStatementInserting(node.getParent(), rewriter);
		}
		
	}
}
