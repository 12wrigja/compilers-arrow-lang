package cwru.jjs228.pr03;

public class ArrowLangTypeCheckerVisitor {
	
	
	public ArrowLangTypeCheckerVisitor(){
		//TODO : Build Symbol table, add native functions to table.
	}
	
	public void visit(TypedNode node) {

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

	public void visitStmts(TypedNode node) throws TypeCheckingException {
		boolean allUnits = true;
		for(TypedNode kid : ((Iterable<TypedNode>)node.kids)){
			if(kid.type != SizedType.UNIT){
				allUnits = false;
			}
		}
		if(allUnits){
			node.type = SizedType.UNIT;
		}else{
			throw new TypeCheckingException("Stmts children are not all unit type. Unable to type-check.");
		}
	}

	public void visitFuncDefStmt(TypedNode node) {

	}

	public void visitReturn(TypedNode node) {

	}

	public void visitDeclStmt(TypedNode node) {

	}

	public void visitShortDeclStmt(TypedNode node) {

	}

	public void visitAssignStmt(TypedNode node) {

	}

	public void visitIfElseStmt(TypedNode node) {

	}

	public void visitForStmt(TypedNode node) {

	}

	public void visitE(TypedNode node) {

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
