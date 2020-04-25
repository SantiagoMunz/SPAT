package srat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;

public class LocalVariableRenaming extends ASTVisitor{
	Map<IBinding, ArrayList<SimpleName> > bindings2names = new HashMap<>();
	CompilationUnit cu = null;
	Document document = null;
	String outputDirPath = null;
	
	public LocalVariableRenaming(CompilationUnit cu_, Document document_, String outputDirPath_) {
		this.cu = cu_;
		this.document = document_;
		this.outputDirPath = outputDirPath_;
	}
	
	public boolean visit(SimpleName node) {
		IBinding ibd = node.resolveBinding();
		if (ibd==null) {
			return true;
		}
		if (this.bindings2names.containsKey(ibd)) {
			this.bindings2names.get(ibd).add(node);
//			System.out.println("Bingding:	" + ibd.toString() + "update to: " + this.bindings2names.toString());
		}
		else if(node.getParent().getNodeType() == ASTNode.VARIABLE_DECLARATION_FRAGMENT || node.getParent().getNodeType() == ASTNode.SINGLE_VARIABLE_DECLARATION) {
			ArrayList<SimpleName> tmp = new ArrayList<SimpleName>();
			tmp.add(node);
			this.bindings2names.put(ibd, tmp);
//			System.out.println("find a new binding for declaration:" + ibd.toString());
		}
		return true;
	}
	public void endVisit(CompilationUnit node) {
//		System.out.println("Whole file is parsed! begin rewriting");
		AST ast = cu.getAST();
		ASTRewrite rewriter = ASTRewrite.create(ast);
		Set<IBinding> variableBins = this.bindings2names.keySet();
		for (IBinding varBin : variableBins) {
			String newName = Utils.getRandomString(8);
			ArrayList<SimpleName> vars = this.bindings2names.get(varBin);
			for(SimpleName var: vars) {
				rewriter.set(var, SimpleName.IDENTIFIER_PROPERTY, newName, null);
			}
		}
//		System.out.println("begin applying edits");
		TextEdit edits = rewriter.rewriteAST(document, null);
		Utils.applyRewrite(node, edits, document,outputDirPath);
	}
}
