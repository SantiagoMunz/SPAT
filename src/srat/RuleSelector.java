package srat;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.Document;

public class RuleSelector {
	static ASTVisitor create(String ruleId, CompilationUnit cu_, Document document_, String outputDirPath_) {
		int ider = Integer.parseInt(ruleId);
		switch (ider) {
		case 0://local var Renaming
			return new LocalVariableRenaming(cu_, document_, outputDirPath_);
		case 1://for statement trans to while statement
			return new ChangeFor2While(cu_, document_, outputDirPath_);
		default:
			System.out.println("ERROR:" + "No rule corresponds to this id!");
			System.exit(5);
			return null;
		}
		
			
	}
}
