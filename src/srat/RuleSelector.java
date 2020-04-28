package srat;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.Document;





public class RuleSelector {
	static final int LocalVarRenaming = 0;
	static final int For2While = 1;
	static final int While2For = 2;
	static final int ReverseIfElse = 3;
	static final int SingleIF2ConditionalExp  = 4;
	static final int ConditionalExp2SingleIF  = 5;
	
	static ASTVisitor create(String ruleId, CompilationUnit cu_, Document document_, String outputDirPath_) {
		int ider = Integer.parseInt(ruleId);
		switch (ider) {
		case LocalVarRenaming://local var Renaming
			return new LocalVariableRenaming(cu_, document_, outputDirPath_);
		case For2While://for statement trans to while statement
			return new ChangeFor2While(cu_, document_, outputDirPath_);
		case While2For://while statement trans to for statement
			return new ChangeWhile2For(cu_, document_, outputDirPath_);
		case ReverseIfElse:
			return new ReveseIf_Else(cu_, document_, outputDirPath_);
		case SingleIF2ConditionalExp:
			return new SingleIf2ConditionalExp(cu_, document_, outputDirPath_);
		case ConditionalExp2SingleIF:
			return new ConditionalExp2SingleIF(cu_, document_, outputDirPath_);
			
			
		default:
			System.out.println("ERROR:" + "No rule corresponds to this id!");
			System.exit(5);
			return null;
		}
		
			
	}
}
