package srat;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.Document;

import srat.rules.ChangeFor2While;
import srat.rules.ChangeWhile2For;
import srat.rules.ConditionalExp2SingleIF;
import srat.rules.LocalVariableRenaming;
import srat.rules.ReveseIf_Else;
import srat.rules.SingleIf2ConditionalExp;
import srat.rules.PP2AddAssignment;
import srat.rules.AddAssignemnt2EqualAssignment;
import srat.rules.InfixExpressionDividing;





public class RuleSelector {
	static final int LocalVarRenaming = 0;
	static final int For2While = 1;
	static final int While2For = 2;
	static final int ReverseIfElse = 3;
	static final int SingleIF2ConditionalExp  = 4;
	static final int ConditionalExp2SingleIF  = 5;
	static final int PP2AddAssignment = 6;
	static final int AddAssignemnt2EqualAssignment = 7;
	static final int InfixExpressionDividing = 8;
	
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
		case PP2AddAssignment:
			return new PP2AddAssignment(cu_, document_, outputDirPath_);
		case AddAssignemnt2EqualAssignment:
			return new AddAssignemnt2EqualAssignment(cu_, document_, outputDirPath_);
		case InfixExpressionDividing:
			return new InfixExpressionDividing(cu_, document_, outputDirPath_);
			
			
		default:
			System.out.println("ERROR:" + "No rule belongs to this id!");
			System.exit(5);
			return null;
		}
		
			
	}
}
