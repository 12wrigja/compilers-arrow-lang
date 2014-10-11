package cwru.jjs228.pr03;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

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
		Node symbol = visit(ctx.NAME());
		if (symbol != null) {
			symbol.label = "Symbol";
		}
		Node callParams = visit(ctx.callParams());
		return minimize("Call", symbol, callParams);
	}

	@Override
	public Node visitAndExpr(AndExprContext ctx) {
		Node notExpr = visit(ctx.notExpr());
		Node andExprRem = visit(ctx.andExprRem());
		return minimize(null, notExpr, andExprRem);
	}

	@Override
	public Node visitFuncDefStmt(FuncDefStmtContext ctx) {
		Node name = visit(ctx.NAME());
		name.label = "Name";
		Node paramDecls = visit(ctx.paramDecls());
		Node typeSpec = wrap("ReturnType", visit(ctx.typeSpec()));
		Node block = visit(ctx.block());
		return minimize("FuncDef", name, paramDecls, typeSpec, block);
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
		Node expr = visit(ctx.expr());
		return minimize("Return", expr);
	}

	@Override
	public Node visitBlock(BlockContext ctx) {
		Node blockStmts = visit(ctx.blockStmts());
		Node returnStmt = visit(ctx.returnStmt());
		return minimize("Block", blockStmts, returnStmt);
	}

	@Override
	public Node visitExpr(ExprContext ctx) {
		return minimize(null, visit(ctx.arithExpr()));
	}

	@Override
	public Node visitBooleanTerm(BooleanTermContext ctx) {
		Node cmpExpr = visit(ctx.cmpExpr());
		Node booleanConstant = visit(ctx.booleanConstant());
		Node booleanExpr = visit(ctx.booleanExpr());
		return minimize(null, cmpExpr, booleanConstant, booleanExpr);
	}

	@Override
	public Node visitCallParamsRem(CallParamsRemContext ctx) {
		Node comma = visit(ctx.COMMA());
		Node expr = visit(ctx.expr());
		Node callParamsRem = visit(ctx.callParamsRem());
		return minimize(null, comma, expr, callParamsRem);
	}

	@Override
	public Node visitAtomic(AtomicContext ctx) {
		Node valueExpr = visit(ctx.valueExpr());
		Node lparen = visit(ctx.LPAREN());
		Node arithExpr = visit(ctx.arithExpr());
		Node rparen = visit(ctx.RPAREN());
		return minimize(null, valueExpr, lparen, arithExpr, rparen);
	}

	@Override
	public Node visitCmpOp(CmpOpContext ctx) {
		if (ctx.LT() != null) {
			if (ctx.EQ(0) != null) {
				return new Node("<=");
			} else {
				return new Node("<");
			}
		} else if (ctx.EXCL() != null) {
			if (ctx.EQ(0) != null) {
				return new Node("!=");
			}
		} else if (ctx.GT() != null) {
			if (ctx.EQ(0) != null) {
				return new Node(">=");
			} else {
				return new Node(">");
			}
		} else if (ctx.EQ(0) != null && ctx.EQ(1) != null) {
			return new Node("==");
		}
		return null;
	}

	@Override
	public Node visitBooleanExpr(BooleanExprContext ctx) {
		Node andExpr = visit(ctx.andExpr());
		Node booleanExprRem = visit(ctx.booleanExprRem());
		return minimize("BooleanExpr", andExpr, booleanExprRem);
	}

	@Override
	public Node visitMulDivRem(MulDivRemContext ctx) {
		String symbol = null;
		Node mult = visit(ctx.MULT());
		Node div = visit(ctx.DIV());
		Node mod = visit(ctx.MOD());
		Node negate = visit(ctx.negate());
		Node mulDivRem = visit(ctx.mulDivRem());
		if (mult != null) {
			return minimize("*",mulDivRem,negate);
		} else if (div != null) {
			return minimize("/",mulDivRem,negate);
		} else if (mod != null) {
			return minimize("%",mulDivRem,negate);
		} else {
			return null;
		}
	}

	@Override
	public Node visitParamDecls(ParamDeclsContext ctx) {
		Node name = visit(ctx.NAME());
		name.label = "Name";
		Node typeSpec = visit(ctx.typeSpec());
		Node paramDecl = new Node("ParamDecl");
		paramDecl.addkid(name);
		paramDecl.addkid(typeSpec);
		Node paramDeclsRem = visit(ctx.paramDeclsRem());
		return minimize("ParamDecls", paramDecl, paramDeclsRem);
	}

	@Override
	public Node visitCallParams(CallParamsContext ctx) {
		Node expr = visit(ctx.expr());
		Node callParamsRem = visit(ctx.callParamsRem());
		return minimize("Params", expr, callParamsRem);
	}

	@Override
	public Node visitDeclStmt(DeclStmtContext ctx) {
		Node name = visit(ctx.NAME());
		name.label = "Name";
		Node typeSpec = visit(ctx.typeSpec());
		Node expr = visit(ctx.expr());
		if (typeSpec == null) {
			return minimize("ShortDecl", name, expr);
		} else {
			return minimize("Decl", name, typeSpec, expr);
		}
	}

	@Override
	public Node visitIfStmt(IfStmtContext ctx) {
		Node booleanExpr = visit(ctx.booleanExpr());
		Node block = visit(ctx.block());
		Node elseIfStmt = visit(ctx.elseIfStmt());
		return minimize("If", booleanExpr, block, elseIfStmt);
	}

	@Override
	public Node visitNotExpr(NotExprContext ctx) {
		Node excl = visit(ctx.EXCL());
		Node booleanTerm = visit(ctx.booleanTerm());
		if (excl != null) {
			return minimize("Not", booleanTerm);
		} else {
			return minimize(null, booleanTerm);
		}
	}

	@Override
	public Node visitNegate(NegateContext ctx) {
		Node dash = visit(ctx.DASH());
		Node atomic = visit(ctx.atomic());
		if (dash != null) {
			return minimize("Negate", atomic);
		} else {
			return minimize(null, atomic);
		}
	}

	@Override
	public Node visitConstant(ConstantContext ctx) {
		if (ctx.INT_CONST() != null) {
			return new Node(Integer.parseInt(ctx.getText()), "Int");
		} else if (ctx.FLOAT_CONST() != null) {
			return new Node(Float.parseFloat(ctx.getText()), "Float");
		} else if (ctx.STRING_CONST() != null) {
			return new Node(ctx.getText(), "String");
		}
		return null;
	}

	@Override
	public Node visitValueExpr(ValueExprContext ctx) {
		Node constant = visit(ctx.constant());
		Node symbolValueExpr = visit(ctx.symbolValueExpr());
		return minimize(null, constant, symbolValueExpr);
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
		if(mulDivRem == null){
			return negate;
		}
		Node lastChild = mulDivRem;
		while(lastChild != null && lastChild.kids.size() != 1 && lastChild.kids.size()!= 0){
			lastChild = (Node)lastChild.kids.get(0);
		}
		lastChild.kids.add(0, negate);
		return mulDivRem;
	}

	@Override
	public Node visitForStmt(ForStmtContext ctx) {
		Node declStmt = visit(ctx.declStmt());
		declStmt = wrap("DeclExpr",declStmt);
		Node booleanExpr = visit(ctx.booleanExpr());
		Node assignmentStmt = visit(ctx.assignStmt());
		if (assignmentStmt != null) {
			assignmentStmt = wrap("UpdateExpr", assignmentStmt);
		}
		Node block = visit(ctx.block());
		return minimize("For", declStmt, booleanExpr, assignmentStmt, block);
	}

	@Override
	public Node visitStmt(StmtContext ctx) {
		Node callStmt = visit(ctx.callStmt());
		Node declStmt = visit(ctx.declStmt());
		Node assignStmt = visit(ctx.assignStmt());
		Node ifStmt = visit(ctx.ifStmt());
		Node whileStmt = visit(ctx.whileStmt());
		Node forStmt = visit(ctx.forStmt());
		return minimize(null, callStmt, declStmt, assignStmt, ifStmt,
				whileStmt, forStmt);
	}

	@Override
	public Node visitBooleanExprRem(BooleanExprRemContext ctx) {
		Node andExpr = visit(ctx.andExpr());
		Node booleanExprRem = visit(ctx.booleanExprRem());
		return minimize("BooleanExprRem", andExpr, booleanExprRem);
	}

	@Override
	public Node visitTStmt(TStmtContext ctx) {
		Node stmt = visit(ctx.stmt());
		Node funcDefStmt = visit(ctx.funcDefStmt());
		return minimize(null, stmt, funcDefStmt);
	}

	@Override
	public Node visitAndExprRem(AndExprRemContext ctx) {
		Node notExpr = visit(ctx.notExpr());
		Node andExprRem = visit(ctx.andExprRem());
		return minimize("AndExprRem", notExpr, andExprRem);
	}

	@Override
	public Node visitSymbolValueExpr(SymbolValueExprContext ctx) {
		Node call = visit(ctx.call());
		Node name = visit(ctx.NAME());
		if(name != null){
			name.label = "Symbol";
		}
		return minimize(null, call, name);
	}

	@Override
	public Node visitTypeName(TypeNameContext ctx) {
		Node typeName = visit(ctx.NAME());
		typeName.label = "TypeName";
		return typeName;
	}

	@Override
	public Node visitWhileStmt(WhileStmtContext ctx) {
		Node whileNode = visit(ctx.WHILE());
		Node booleanExpr = visit(ctx.booleanExpr());
		Node block = visit(ctx.block());
		return minimize("While", whileNode, booleanExpr, block);
	}

	@Override
	public Node visitAssignStmt(AssignStmtContext ctx) {
		Node name = visit(ctx.NAME());
		name.label = "Name";
		Node expr = visit(ctx.expr());
		return minimize("AssignStmt", name, expr);
	}

	@Override
	public Node visitParamDeclsRem(ParamDeclsRemContext ctx) {
		Node name = visit(ctx.NAME());
		if(name != null){
			name.label = "Name";
		}
		Node typeSpec = visit(ctx.typeSpec());
		Node paramDeclsRem = visit(ctx.paramDeclsRem());
		return minimize("ParamDecl", name, typeSpec, paramDeclsRem);
	}

	@Override
	public Node visitStart(StartContext ctx) {
		if (ctx.stmts() != null) {
			Node result = visit(ctx.stmts());
			pullup("Stmts", result);
			return result;
		}
		return null;
	}

	@Override
	public Node visitElseIfStmt(ElseIfStmtContext ctx) {
		Node block = visit(ctx.block());
		Node ifStmt = visit(ctx.ifStmt());
		return minimize("ElseIf", block, ifStmt);
	}

	@Override
	public Node visitCallStmt(CallStmtContext ctx) {
		return minimize(null, visit(ctx.call()));
	}

	@Override
	public Node visitBlockStmts(BlockStmtsContext ctx) {
		Node blockStmt = visit(ctx.blockStmt());
		Node blockStmts = visit(ctx.blockStmts());
		return minimize(null, blockStmt, blockStmts);
	}

	@Override
	public Node visitCmpExpr(CmpExprContext ctx) {
		Node arithExpr1 = visit(ctx.arithExpr(0));
		Node cmpOp = visit(ctx.cmpOp());
		Node arithExpr2 = visit(ctx.arithExpr(1));
		return minimize(cmpOp.label, arithExpr1, arithExpr2);
	}

	@Override
	public Node visitArithExprRem(ArithExprRemContext ctx) {
		Node plus = visit(ctx.PLUS());
		Node minus = visit(ctx.DASH());
		Node mulDiv = visit(ctx.mulDiv());
		Node arithExprRem = visit(ctx.arithExprRem());
		if (plus != null) {
			if(arithExprRem == null){
				return minimize("+", arithExprRem, mulDiv);
			}else{
				Node lastOp = arithExprRem;
				while(lastOp != null && lastOp.kids.size() != 1){
					lastOp = (Node)lastOp.kids.get(0);
				}
				lastOp.kids.add(0, wrap("+",mulDiv));
				return arithExprRem;
			}
		} else if (minus != null) {
			if(arithExprRem == null){
				return minimize("-", arithExprRem, mulDiv);
			}else{
				Node lastOp = arithExprRem;
				while(lastOp != null && lastOp.kids.size() != 1){
					lastOp = (Node)lastOp.kids.get(0);
				}
				lastOp.kids.add(0, wrap("-",mulDiv));
				return arithExprRem;
			}
		} else {
			return null;
		}

	}

	@Override
	public Node visitBlockStmt(BlockStmtContext ctx) {
		Node blockStmt = visit(ctx.stmt());
		Node loopCtrlStmt = visit(ctx.loopControlStmt());
		return minimize(null, blockStmt, loopCtrlStmt);
	}

	@Override
	public Node visitTypeSpec(TypeSpecContext ctx) {
		return minimize("Type", visit(ctx.typeName()));
	}

	@Override
	public Node visitArithExpr(ArithExprContext ctx) {
		Node mulDiv = visit(ctx.mulDiv());
		Node arithExprRem = visit(ctx.arithExprRem());
		if(arithExprRem == null){
			return mulDiv;
		}
		Node lastChild = arithExprRem;
		while(lastChild != null && lastChild.kids.size() != 1){
			lastChild = (Node)lastChild.kids.get(0);
		}
		lastChild.kids.add(0, mulDiv);
		return arithExprRem;
	}

	@Override
	public Node visitTerminal(TerminalNode node) {
		Node returnNode = new Node(node.getText(),
				ArrowLangLexer.ruleNames[node.getSymbol().getType() - 1]);
		return (Node) returnNode;
	}

	private Node wrap(String nodeLabel, Node childNode) {
		Node newNode = new Node(nodeLabel);
		newNode.addkid(childNode);
		return newNode;
	}

	private Node minimize(String nodeLabel, Node... childNodes) {
		Node newNode = new Node(nodeLabel);
		for (Node n : childNodes) {
			if (n != null) {
				if (n.label == null) {
					for (Node n2 : ((Iterable<Node>) n.kids)) {
						newNode.addkid(n2);
					}
				} else {
					newNode.addkid(n);
				}
			}
		}
		if (newNode.kids.size() == 0) {
			return null;
		} else if (nodeLabel == null & newNode.kids.size() == 1) {
			return (Node) newNode.kids.get(0);
		}
		return newNode;
	}

	private void pullup(String label, Node node) {
		int index = 0;
		while (index < node.kids.size()) {
			Node currentKid = (Node) node.kids.get(index);
			if (currentKid.label.equals(label)) {
				for (int j = 0; j < currentKid.kids.size(); j++) {
					if (j == 0) {
						node.kids.set(index, currentKid.kids.get(j));
					} else {
						node.kids.add(j, currentKid.kids.get(j));
					}
				}
			} else {
				index++;
			}
		}
	}

	@Override
	public Node visit(ParseTree tree) {
		if (tree == null) {
			return null;
		}
		return super.visit(tree);
	}
}
