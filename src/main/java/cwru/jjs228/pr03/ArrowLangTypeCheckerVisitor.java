package cwru.jjs228.pr03;

import java.util.List;

import cwru.jjs228.pr03.SymbolTable.SymbolTableException;

public class ArrowLangTypeCheckerVisitor {

	SymbolTable context;

	public ArrowLangTypeCheckerVisitor() {
		// TODO : Build/Initialize Symbol table, add native functions to table.
		context = new SymbolTable();
		context.push();
		FunctionType[] nativeFunctions = NativeLibrary.nativeFunctions();
		for (FunctionType function : nativeFunctions) {
			try {
				context.put(function.name, function);
			} catch (SymbolTableException e) {
				// This block should never be entered as a new table was just
				// pushed.
				e.printStackTrace();
			}
		}
	}

	public void visit(TypedNode node) throws TypeCheckingException, SymbolTableException {
		visitStmts(node);
	}

	public void visitIntConstant(TypedNode node) {
		node.type = IntType.Int32;
	}

	public void visitFloatConstant(TypedNode node) {
		node.type = SizedType.FLOAT32;
	}

	public void visitBoolConstant(TypedNode node) {
		node.type = SizedType.BOOLEAN;
	}

	public void visitStringConstant(TypedNode node) {
		node.type = SizedType.STRING;
	}

	public void visitSymbol(TypedNode node) {

	}

	public void visitStmts(TypedNode node) throws TypeCheckingException, SymbolTableException {
		boolean allUnits = true;
		for (TypedNode kid : ((Iterable<TypedNode>) node.kids)) {
			visitStmt(kid);
			if (kid.type != SizedType.UNIT) {
				allUnits = false;
			}
		}
		if (allUnits) {
			node.type = SizedType.UNIT;
		} else {
			throw new TypeCheckingException(
					"Stmts children are not all unit type. Unable to type-check.");
		}
	}

	public void visitStmt(TypedNode node) throws TypeCheckingException, SymbolTableException {
		String name = node.label;
		switch (name) {
		case "FuncDef": {
			visitFuncDefStmt(node);
			break;
		}
		case "Decl": {
			visitDeclStmt(node);
			break;
		}
		case "ShortDecl": {
			visitShortDeclStmt(node);
			break;
		}
		case "AssignStmt": {
			visitAssignStmt(node);
			break;
		}
		case "If": {
			visitIfElseStmt(node);
			break;
		}
		case "For": {
			visitForStmt(node);
			break;
		}
		case "Call": {
			visitCall(node);
			break;
		}
		default: {
			throw new TypeCheckingException(
					"Unable to handle statement with label " + name
							+ " at visitStmt call");
		}
		}
	}

	public void visitFuncDefStmt(TypedNode node) {

	}

	public void visitReturn(TypedNode node) {

	}

	public void visitDeclStmt(TypedNode node) {

	}

	public void visitShortDeclStmt(TypedNode node) throws TypeCheckingException, SymbolTableException {
		
		List<TypedNode> children = node.kids;
		
		//Kid 0 should be the name. Check and see if it is undefined.
		TypedNode nameNode = children.get(0);
		String variableName = nameNode.value.toString();
		if(context.isDefined(variableName)){
			throw new TypeCheckingException("Variable " + variableName + "is already defined.");
		}
		
		//Variable is undefined at this point.
		//Compute variable type.
		TypedNode expressionNode = children.get(1);
		visitExpression(expressionNode);
		
		//Add the variable to the symbol table with the defined type.
		context.put(variableName, expressionNode.type);
		
		//Set the type of the name node to the type of the expression
		nameNode.type = expressionNode.type;
		
		//Set the type of the entire short declaration statement to unit.
		node.type = SizedType.UNIT;
	}

	public void visitAssignStmt(TypedNode node) {

	}

	public void visitIfElseStmt(TypedNode node) {

	}

	public void visitForStmt(TypedNode node) {

	}

	public void visitExpression(TypedNode node) {

	}

	public void visitBlock(TypedNode node) {

	}

	public void visitDeclExpr(TypedNode node) {

	}

	public void visitUpdateExpr(TypedNode node) {

	}

	public void visitBooleanExpr(TypedNode node) {

	}

	public void visitAnd(TypedNode node) {

	}

	public void visitOr(TypedNode node) {

	}

	public void visitNegateBoolean(TypedNode node) {

	}

	public void visitCmpOp(TypedNode node) {

	}

	public void visitCall(TypedNode node) {

	}

	public void visitCast(TypedNode node) {

	}

	public void visitArithOp(TypedNode node) {

	}

	public void visitNegateArith(TypedNode node) {

	}
}
