package srat.rules;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;

import srat.Utils;

public class ChangeFor2While extends ASTVisitor{
	CompilationUnit cu = null;
	Document document = null;
	String outputDirPath = null;
	ArrayList<ForStatement> fors = new ArrayList<ForStatement>();
	
	public ChangeFor2While(CompilationUnit cu_, Document document_, String outputDirPath_) {
		this.cu = cu_;
		this.document = document_;
		this.outputDirPath = outputDirPath_;
	}
	
	
	public boolean visit(ForStatement node) {
		fors.add(node);

		return true;
	}
	
	@SuppressWarnings("unchecked")
	public void endVisit(CompilationUnit node) {
		
		if (fors.size() == 0) {
			return;
		}
		
		AST ast = cu.getAST();
		ASTRewrite rewriter = ASTRewrite.create(ast);
		for(ForStatement forer: fors){
			WhileStatement whiler = ast.newWhileStatement();
			Expression theexp = (Expression) ASTNode.copySubtree(ast, forer.getExpression());
			if (theexp == null) {
				theexp = ast.newBooleanLiteral(true);
			}
			whiler.setExpression(theexp);
			Statement bodystatement = forer.getBody();
			Statement whilebody = (Statement) ASTNode.copySubtree(ast, bodystatement);
			if(whilebody.getNodeType() != ASTNode.BLOCK) {
//				System.out.println("single statement for, let's change it!");
				Block blocker = ast.newBlock();
				ListRewrite lrt = rewriter.getListRewrite(blocker, Block.STATEMENTS_PROPERTY);
				lrt.insertFirst(whilebody, null);
				whilebody = blocker;
			}
			whiler.setBody(whilebody);
			
			List<Expression> updexpressions = forer.updaters();
			ListRewrite lrt = rewriter.getListRewrite(whilebody, Block.STATEMENTS_PROPERTY);
			for(Expression upd: updexpressions) {
				ExpressionStatement updsta = ast.newExpressionStatement((Expression) rewriter.createCopyTarget(upd));
				lrt.insertLast(updsta, null);
			}
			
			
			List<Expression> initexpressions = forer.initializers();
			Block blockoutsideWhile = ast.newBlock();
			ListRewrite lrt2 = rewriter.getListRewrite(blockoutsideWhile, Block.STATEMENTS_PROPERTY);// This place is dangerous
			for(Expression ini: initexpressions) {
				ExpressionStatement inista = ast.newExpressionStatement((Expression) rewriter.createCopyTarget(ini));
				lrt2.insertLast(inista, null);
			}
			lrt2.insertLast(whiler, null);
			
			rewriter.replace(forer, blockoutsideWhile, null);
		}
		TextEdit edits = rewriter.rewriteAST(document, null);
		Utils.applyRewrite(node, edits, document,outputDirPath);
	}
}
