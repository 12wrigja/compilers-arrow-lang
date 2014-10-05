package cwru.jjs228.pr03;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;

import cwru.jjs228.pr03.ArrowLangParser.AndExprContext;
import cwru.jjs228.pr03.ArrowLangParser.AndExprRemContext;
import cwru.jjs228.pr03.ArrowLangParser.ArithExprContext;
import cwru.jjs228.pr03.ArrowLangParser.ArithExprRemContext;
import cwru.jjs228.pr03.ArrowLangParser.AssignStmtContext;
import cwru.jjs228.pr03.ArrowLangParser.AtomicContext;
import cwru.jjs228.pr03.ArrowLangParser.BlockContext;
import cwru.jjs228.pr03.ArrowLangParser.BlockStmtContext;
import cwru.jjs228.pr03.ArrowLangParser.BlockStmtsContext;
import cwru.jjs228.pr03.ArrowLangParser.BooleanConstantContext;
import cwru.jjs228.pr03.ArrowLangParser.BooleanExprContext;
import cwru.jjs228.pr03.ArrowLangParser.BooleanExprRemContext;
import cwru.jjs228.pr03.ArrowLangParser.BooleanTermContext;
import cwru.jjs228.pr03.ArrowLangParser.CallContext;
import cwru.jjs228.pr03.ArrowLangParser.CallParamsContext;
import cwru.jjs228.pr03.ArrowLangParser.CallParamsRemContext;
import cwru.jjs228.pr03.ArrowLangParser.CallStmtContext;
import cwru.jjs228.pr03.ArrowLangParser.CmpExprContext;
import cwru.jjs228.pr03.ArrowLangParser.CmpOpContext;
import cwru.jjs228.pr03.ArrowLangParser.ConstantContext;
import cwru.jjs228.pr03.ArrowLangParser.DeclStmtContext;
import cwru.jjs228.pr03.ArrowLangParser.ElseIfStmtContext;
import cwru.jjs228.pr03.ArrowLangParser.ExprContext;
import cwru.jjs228.pr03.ArrowLangParser.ForStmtContext;
import cwru.jjs228.pr03.ArrowLangParser.FuncDefStmtContext;
import cwru.jjs228.pr03.ArrowLangParser.IfStmtContext;
import cwru.jjs228.pr03.ArrowLangParser.LoopControlStmtContext;
import cwru.jjs228.pr03.ArrowLangParser.MulDivContext;
import cwru.jjs228.pr03.ArrowLangParser.MulDivRemContext;
import cwru.jjs228.pr03.ArrowLangParser.NegateContext;
import cwru.jjs228.pr03.ArrowLangParser.NotExprContext;
import cwru.jjs228.pr03.ArrowLangParser.ParamDeclsContext;
import cwru.jjs228.pr03.ArrowLangParser.ParamDeclsRemContext;
import cwru.jjs228.pr03.ArrowLangParser.ReturnStmtContext;
import cwru.jjs228.pr03.ArrowLangParser.StartContext;
import cwru.jjs228.pr03.ArrowLangParser.StmtContext;
import cwru.jjs228.pr03.ArrowLangParser.StmtsContext;
import cwru.jjs228.pr03.ArrowLangParser.SymbolValueExprContext;
import cwru.jjs228.pr03.ArrowLangParser.TStmtContext;
import cwru.jjs228.pr03.ArrowLangParser.TypeNameContext;
import cwru.jjs228.pr03.ArrowLangParser.TypeSpecContext;
import cwru.jjs228.pr03.ArrowLangParser.ValueExprContext;
import cwru.jjs228.pr03.ArrowLangParser.WhileStmtContext;

public class ArrowLangASTVisitor extends AbstractParseTreeVisitor<Node>
		implements ArrowLangVisitor<Node> {

	@Override
	public Node visitCall(CallContext ctx) {
		Node name = visit(ctx.NAME());
		Node callParams = visit(ctx.callParams());
		return minimize("Call", name,callParams);
	}

	@Override
	public Node visitAndExpr(AndExprContext ctx) {
		Node notExpr = visit(ctx.notExpr());
		Node andExprRem = visit(ctx.andExprRem());
		return minimize("AndExpr",notExpr,andExprRem);
	}

	@Override
	public Node visitFuncDefStmt(FuncDefStmtContext ctx) {
		Node name = visit(ctx.NAME());
		Node paramDecls = visit(ctx.paramDecls());
		Node typeSpec = visit(ctx.typeSpec());
		Node block = visit(ctx.block());
		return minimize("Func", name,paramDecls,typeSpec,block);
	}

	@Override
	public Node visitLoopControlStmt(LoopControlStmtContext ctx) {
		if (ctx.CONTINUE() != null) {
			return new Node("Continue");
		} else if (ctx.BREAK() != null) {
			return new Node("Break");
		}
		return null;
	}

	@Override
	public Node visitStmts(StmtsContext ctx) {
		Node tstmt = visit(ctx.tStmt());
		Node stmts = visit(ctx.stmts());
		return minimize("Stmts", tstmt, stmts);
	}

	@Override
	public Node visitReturnStmt(ReturnStmtContext ctx) {
		Node retNode = new Node("Return");
		Node expr = visit(ctx.expr());
		return minimize("Return", retNode, expr);
	}

	@Override
	public Node visitBlock(BlockContext ctx) {
		Node blockStmts = visit(ctx.blockStmts());
		Node returnStmt = visit(ctx.returnStmt());
		return minimize("Block", blockStmts,returnStmt);
	}

	@Override
	public Node visitExpr(ExprContext ctx) {
		return minimize("Expr",visit(ctx.arithExpr()));
	}

	@Override
	public Node visitBooleanTerm(BooleanTermContext ctx) {
		Node cmpExpr = visit(ctx.cmpExpr());
		Node booleanConstant = visit(ctx.booleanConstant());
		Node lparen = visit(ctx.LPAREN());
		Node booleanExpr = visit(ctx.booleanExpr());
		Node rparen = visit(ctx.RPAREN());
		return minimize("BooleanTerm", cmpExpr,booleanConstant,lparen,booleanExpr,rparen);
	}

	@Override
	public Node visitCallParamsRem(CallParamsRemContext ctx) {
		Node comma = visit(ctx.COMMA());
		Node expr = visit(ctx.expr());
		Node callParamsRem = visit(ctx.callParamsRem());
		return minimize("CallParamsRem", comma,expr,callParamsRem);
	}

	@Override
	public Node visitAtomic(AtomicContext ctx) {
		Node valueExpr = visit(ctx.valueExpr());
		Node lparen = visit(ctx.LPAREN());
		Node arithExpr = visit(ctx.arithExpr());
		Node rparen = visit(ctx.RPAREN());
		return minimize("Atomic", valueExpr,lparen,arithExpr,rparen);
	}

	@Override
	public Node visitCmpOp(CmpOpContext ctx) {
		Node less = visit(ctx.LT());
		Node more = visit(ctx.GT());
		Node eq1 = visit(ctx.EQ(1));
		Node eq2 = visit(ctx.EQ(2));
		Node excl = visit(ctx.EXCL());
		return minimize("CmpOp", less,more,eq1,excl,eq2);
	}

	@Override
	public Node visitBooleanExpr(BooleanExprContext ctx) {
		Node andExpr = visit(ctx.andExpr());
		Node booleanExprRem = visit(ctx.booleanExprRem());
		return minimize("BooleanExpr",andExpr,booleanExprRem);
	}

	@Override
	public Node visitMulDivRem(MulDivRemContext ctx) {
		Node mult = visit(ctx.MULT());
		Node div = visit(ctx.DIV());
		Node mod = visit(ctx.MOD());
		Node negate = visit(ctx.negate());
		Node mulDivRem = visit(ctx.mulDivRem());
		return minimize("MulDivRem", mult,div,mod,negate,mulDivRem);
	}

	@Override
	public Node visitParamDecls(ParamDeclsContext ctx) {
		Node name = visit(ctx.NAME());
		Node typeSpec = visit(ctx.typeSpec());
		Node paramDeclsRem = visit(ctx.paramDeclsRem());
		return minimize("ParamDecls",name,typeSpec,paramDeclsRem);
	}

	@Override
	public Node visitCallParams(CallParamsContext ctx) {
		Node expr = visit(ctx.expr());
		Node callParamsRem = visit(ctx.callParamsRem());
		return minimize("CallParams", expr,callParamsRem);
	}

	@Override
	public Node visitDeclStmt(DeclStmtContext ctx) {
		Node var = visit(ctx.VAR());
		Node name = visit(ctx.NAME());
		Node typeSpec = visit(ctx.typeSpec());
		Node eq = visit(ctx.EQ());
		Node expr = visit(ctx.expr());
		return minimize("Decl Stmt", var,name,typeSpec,eq,expr);
	}

	@Override
	public Node visitIfStmt(IfStmtContext ctx) {
		Node ifPt = visit(ctx.IF());
		Node booleanExpr = visit(ctx.booleanExpr());
		Node block = visit(ctx.block());
		Node elseIfStmt = visit(ctx.elseIfStmt());
		return minimize("IfStmt", ifPt,booleanExpr,block,elseIfStmt);
	}

	@Override
	public Node visitNotExpr(NotExprContext ctx) {
		Node excl = visit(ctx.EXCL());
		Node booleanTerm = visit(ctx.booleanTerm());
		return minimize("Not",excl,booleanTerm);
	}

	@Override
	public Node visitNegate(NegateContext ctx) {
		Node dash = visit(ctx.DASH());
		Node atomic = visit(ctx.atomic());
		return minimize("Negate", dash,atomic);
	}

	@Override
	public Node visitConstant(ConstantContext ctx) {
		if (ctx.INT_CONST() != null) {
			return new Node(Integer.parseInt(ctx.getText()));
		} else if (ctx.FLOAT_CONST() != null) {
			return new Node(Float.parseFloat(ctx.getText()));
		} else if (ctx.STRING_CONST() != null) {
			return new Node(ctx.getText());
		}
		return null;
	}

	@Override
	public Node visitValueExpr(ValueExprContext ctx) {
		Node constant = visit(ctx.constant());
		Node symbolValueExpr = visit(ctx.symbolValueExpr());
		return minimize("ValueExpr", constant,symbolValueExpr);
	}

	@Override
	public Node visitBooleanConstant(BooleanConstantContext ctx) {
		if (ctx.TRUE() != null) {
			return new Node("TRUE");
		} else if (ctx.FALSE() != null) {
			return new Node("FALSE");
		}
		return null;
	}

	@Override
	public Node visitMulDiv(MulDivContext ctx) {
		Node negate = visit(ctx.negate());
		Node mulDivRem = visit(ctx.mulDivRem());
		return minimize("MulDiv", negate,mulDivRem);
	}

	@Override
	public Node visitForStmt(ForStmtContext ctx) {
		Node forNode = visit(ctx.FOR());
		Node declStmt = visit(ctx.declStmt());
		Node booleanExpr = visit(ctx.booleanExpr());
		Node assignmentStmt = visit(ctx.assignStmt());
		Node block = visit(ctx.block());
		return minimize("For", forNode,declStmt,booleanExpr,assignmentStmt,block);
	}

	@Override
	public Node visitStmt(StmtContext ctx) {
		Node callStmt = visit(ctx.callStmt());
		Node declStmt = visit(ctx.declStmt());
		Node assignStmt = visit(ctx.assignStmt());
		Node ifStmt = visit(ctx.ifStmt());
		Node whileStmt = visit(ctx.whileStmt());
		Node forStmt = visit(ctx.forStmt());
		return minimize("Stmt", callStmt,declStmt,assignStmt,ifStmt,whileStmt,forStmt);
	}

	@Override
	public Node visitBooleanExprRem(BooleanExprRemContext ctx) {
		Node andExpr = visit(ctx.andExpr());
		Node booleanExprRem = visit(ctx.booleanExprRem());
		return minimize("BooleanExprRem",andExpr,booleanExprRem);
	}

	@Override
	public Node visitTStmt(TStmtContext ctx) {
		Node stmt = visit(ctx.stmt());
		Node funcDefStmt = visit(ctx.funcDefStmt());
		return minimize("TStmt", stmt,funcDefStmt);
	}

	@Override
	public Node visitAndExprRem(AndExprRemContext ctx) {
		Node notExpr = visit(ctx.notExpr());
		Node andExprRem = visit(ctx.andExprRem());
		return minimize("AndExprRem",notExpr,andExprRem);
	}

	@Override
	public Node visitSymbolValueExpr(SymbolValueExprContext ctx) {
		Node call = visit(ctx.call());
		Node name = visit(ctx.NAME());
		return minimize("Symbol", call,name);
	}

	@Override
	public Node visitTypeName(TypeNameContext ctx) {
		return minimize("TypeName", visit(ctx.NAME()));
	}

	@Override
	public Node visitWhileStmt(WhileStmtContext ctx) {
		Node whileNode = visit(ctx.WHILE());
		Node booleanExpr = visit(ctx.booleanExpr());
		Node block = visit(ctx.block());
		return minimize("While", whileNode,booleanExpr,block);
	}

	@Override
	public Node visitAssignStmt(AssignStmtContext ctx) {
		Node name = visit(ctx.NAME());
		Node eq = visit(ctx.EQ());
		Node expr = visit(ctx.expr());
		return minimize("Assign", name,eq,expr);
	}

	@Override
	public Node visitParamDeclsRem(ParamDeclsRemContext ctx) {
		Node name = visit(ctx.NAME());
		Node typeSpec = visit(ctx.typeSpec());
		Node paramDeclsRem = visit(ctx.paramDeclsRem());
		return minimize("ParamDeclsRem",name,typeSpec,paramDeclsRem);
	}

	@Override
	public Node visitStart(StartContext ctx) {
		if (ctx.stmts() != null) {
			return visit(ctx.stmts());
		}
		return null;
	}

	@Override
	public Node visitElseIfStmt(ElseIfStmtContext ctx) {
		Node elseNode = visit(ctx.ELSE());
		Node block = visit(ctx.block());
		Node ifStmt = visit(ctx.ifStmt());
		return minimize("ElseIf", elseNode,block,ifStmt);
	}

	@Override
	public Node visitCallStmt(CallStmtContext ctx) {
		return minimize("Call",visit(ctx.call()));
	}

	@Override
	public Node visitBlockStmts(BlockStmtsContext ctx) {
		Node blockStmt = visit(ctx.blockStmt());
		Node blockStmts = visit(ctx.blockStmts());
		return minimize("BlockStmts", blockStmt,blockStmts);
	}

	@Override
	public Node visitCmpExpr(CmpExprContext ctx) {
		//TODO figure out why there is not a simply ctx.arithExpr() method that visit will accept.
		Node arithExpr1 = visit(ctx.arithExpr(0));
		Node cmpOp = visit(ctx.cmpOp());
		Node arithExpr2 = visit(ctx.arithExpr(1));
		return minimize("CmpExpr", arithExpr1,cmpOp,arithExpr2);
	}

	@Override
	public Node visitArithExprRem(ArithExprRemContext ctx) {
		Node plus = visit(ctx.PLUS());
		Node minus = visit(ctx.DASH());
		Node mulDiv = visit(ctx.mulDiv());
		Node arithExprRem = visit(ctx.arithExprRem());
		return minimize("ArithExprRem", plus,minus,mulDiv,arithExprRem);
	}

	@Override
	public Node visitBlockStmt(BlockStmtContext ctx) {
		Node blockStmt = visit(ctx.stmt());
		Node loopCtrlStmt = visit(ctx.loopControlStmt());
		return minimize("Block", blockStmt,loopCtrlStmt);
	}

	@Override
	public Node visitTypeSpec(TypeSpecContext ctx) {
		return minimize("TypeSpec", visit(ctx.typeName()));
	}

	@Override
	public Node visitArithExpr(ArithExprContext ctx) {
		Node mulDiv = visit(ctx.mulDiv());
		Node arithExprRem = visit(ctx.arithExprRem());
		return minimize("ArithExpr", mulDiv,arithExprRem);
	}

	private Node minimize(String nodeLabel, Node... childNodes) {
		Node newNode = new Node(nodeLabel);
		for (Node n : childNodes) {
			if (n != null) {
				newNode.addkid(n);
			}
		}
		if (newNode.kids.size() == 1) {
			// Grab value from node below, set it as the value of this node and
			// remove the child node.
			Node child = ((Node) newNode.kids.get(0));
			newNode.label = child.label;
			newNode.kids.remove(child);
		} else if (newNode.kids.size() == 0) {
			return null;
		}
		return newNode;
	}
	
	@Override
	public Node visit(ParseTree tree) {
		if(tree == null){
			return null;
		}
		return super.visit(tree);
	}
}
