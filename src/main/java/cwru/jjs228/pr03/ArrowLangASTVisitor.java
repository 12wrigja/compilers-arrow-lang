package cwru.jjs228.pr03;

import java.util.ArrayList;
import java.util.List;

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
	/**
	 * Visits a call node in a parse tree for Arrowlang
	 * If the call has a name token associated with it, labels that name as a Symbol
	 * If the call has a parameter tree, that node is added as well by visiting it.
	 * If the call has no parameters, a null node is created for processing
	 */
	public Node visitCall(CallContext ctx) {
		Node symbol = visit(ctx.NAME());
		if (symbol != null) {
			symbol.label = "Symbol";
		}
		Node callParams = visit(ctx.callParams());
		if (callParams == null) {
			callParams = new Node("Params");
		}
		return minimize("Call", symbol, callParams);
	}

	@Override
	/**
	 * Visits an AndExpr node
	 * If the AndExpr* in the production produces null,
	 * 	then this returns the notExpr
	 * Otherwise, the NotExpr is added as a child to
	 * 	the last node of the tree containing the AndExpr*
	 * 	and the AndExpr* node is returned.
	 */
	public Node visitAndExpr(AndExprContext ctx) {
		Node notExpr = visit(ctx.notExpr());
		Node andExprRem = visit(ctx.andExprRem());
		if (andExprRem == null) {
			return notExpr;
		}
		Node lastOp = andExprRem;
		while (lastOp != null && lastOp.kids.size() != 1) {
			lastOp = (Node) lastOp.kids.get(0);
		}
		lastOp.kids.add(0, notExpr);
		return andExprRem;
	}

	@Override
	public Node visitFuncDefStmt(FuncDefStmtContext ctx) {
		Node name = visit(ctx.NAME());
		name.label = "Name";
		Node paramDecls = visit(ctx.paramDecls());
		if (paramDecls == null) {
			paramDecls = new Node("ParamDecls");
		}
		Node typeSpec = visit(ctx.typeSpec());
		if (typeSpec == null) {
			typeSpec = wrap("Type", new Node("unit", "TypeName"));
		}
		typeSpec = wrap("ReturnType", typeSpec);
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
		if (expr != null) {
			return minimize("Return", expr);
		} else {
			return new Node("Return");
		}
	}

	@Override
	public Node visitBlock(BlockContext ctx) {
		Node blockStmts = visit(ctx.blockStmts());
		Node returnStmt = visit(ctx.returnStmt());
		return minimize("Block", blockStmts, returnStmt);
	}

	@Override
	/**
	 * Visits an expression node on the parse tree.
	 * Passes a null label to minimize so that the expression node
	 * 	does not occur in the AST, in accordance with the tree grammar.
	 */
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
	/**
	 * Visits a CallParamRem node on the parse tree
	 * First creates the branch for the expression call by visiting it
	 * Then creates the branch of the tree for remainder of the parameters by visiting 
	 * 	their respective nodes using the same method.
	 * This stops when no more parameters are remaining
	 * No label is assigned to this call so that minimize can reduce all the way up the tree
	 * 	so that all parameters are on the level directly below Params
	 */
	public Node visitCallParamsRem(CallParamsRemContext ctx) {
		Node expr = visit(ctx.expr());
		Node callParamsRem = visit(ctx.callParamsRem());
		return minimize(null, expr, callParamsRem);
	}

	@Override
	/**
	 * Visits a node on the parse tree for an atomic expression
	 * In normal tree operations, either valueExpr or arithExpr will
	 * 	be null, so minimize will only add one child.
	 * In accordance with the AST grammar, this node will be removed in minimize
	 * 	due to the null label.
	 */
	public Node visitAtomic(AtomicContext ctx) {
		Node valueExpr = visit(ctx.valueExpr());
		Node arithExpr = visit(ctx.arithExpr());
		return minimize(null, valueExpr, arithExpr);
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
	/**
	 * Visits a BooleanExpr node
	 * If the BooleanExpr* in the production goes to null,
	 * 	then returns only the AndExpr from the production.
	 * Otherwise, this finds the bottom of the tree from the
	 * 	BooleanExpr* and adds the andExpr as a child to that.
	 * 	It then returns the BooleanExpr*
	 */
	public Node visitBooleanExpr(BooleanExprContext ctx) {
		Node andExpr = visit(ctx.andExpr());
		Node booleanExprRem = visit(ctx.booleanExprRem());
		if (booleanExprRem == null) {
			return andExpr;
		}
		Node lastChild = booleanExprRem;
		while (lastChild != null && lastChild.kids.size() != 1) {
			lastChild = (Node) lastChild.kids.get(0);
		}
		lastChild.kids.add(0, andExpr);
		return booleanExprRem;
	}

	@Override
	/**
	 * Visits a MulDiv* node.
	 * First gets the operation of the node
	 * If there is no operation, returns null because
	 * 	the node produced an epsilon.
	 * Otherwise, if the MulDiv* in the production
	 * 	is null, returns a node with a label of the 
	 * 	operator and a visited negate child.
	 * If the MulDiv* is not null, finds the bottom of
	 * 	the tree from that node and adds a node with the
	 * 	operator as a label and the visited negate as a child.
	 */
	public Node visitMulDivRem(MulDivRemContext ctx) {
		Node mult = visit(ctx.MULT());
		Node div = visit(ctx.DIV());
		Node mod = visit(ctx.MOD());
		Node negate = visit(ctx.negate());
		Node mulDivRem = visit(ctx.mulDivRem());
		if (mult != null) {
			if (mulDivRem == null) {
				return minimize("*", mulDivRem, negate);
			} else {
				Node lastOp = mulDivRem;
				while (lastOp != null && lastOp.kids.size() != 1) {
					lastOp = (Node) lastOp.kids.get(0);
				}
				lastOp.kids.add(0, wrap("*", negate));
				return mulDivRem;
			}
		} else if (div != null) {
			if (mulDivRem == null) {
				return minimize("/", mulDivRem, negate);
			} else {
				Node lastOp = mulDivRem;
				while (lastOp != null && lastOp.kids.size() != 1) {
					lastOp = (Node) lastOp.kids.get(0);
				}
				lastOp.kids.add(0, wrap("/", negate));
				return mulDivRem;
			}
		} else if (mod != null) {
			if (mulDivRem == null) {
				return minimize("%", mulDivRem, negate);
			} else {
				Node lastOp = mulDivRem;
				while (lastOp != null && lastOp.kids.size() != 1) {
					lastOp = (Node) lastOp.kids.get(0);
				}
				lastOp.kids.add(0, wrap("%", negate));
				return mulDivRem;
			}
		} else {
			return null;
		}
	}

	@Override
	public Node visitParamDecls(ParamDeclsContext ctx) {
		Node name = visit(ctx.NAME());
		name.label = "Name";
		Node typeSpec = visit(ctx.typeSpec());
		Node paramDeclsRem = visit(ctx.paramDeclsRem());
		Node paramDecl = new Node("ParamDecl");
		paramDecl.addkid(name);
		paramDecl.addkid(typeSpec);
		List<Node> allParamDecls = new ArrayList<Node>();
		if(null != paramDeclsRem){
		paramDecl.addkid(paramDeclsRem);
		allParamDecls = pullup("ParamDecl", paramDecl);
		}else{
			allParamDecls.add(paramDecl);
		}
		return minimize("ParamDecls", allParamDecls.toArray(new Node[0]));
	}

	@Override
	/**
	 * Visits a CallParams node on the parse tree
	 * One child is the associated expression of the call
	 * The other child is the remaining parameters, which will be 
	 * 	reduced to the same level.
	 * This returns the node produced by the minimize method with a
	 * 	label of Params and children, all of the parameter expressions.
	 */
	public Node visitCallParams(CallParamsContext ctx) {
		Node expr = visit(ctx.expr());
		Node callParamsRem = visit(ctx.callParamsRem());
		return minimize("Params", expr, callParamsRem);
	}

	@Override
	/**
	 * Visits a varible declaration statement on the parse tree
	 * Because DeclStmt is replaced by Decl and ShortDecl on the
	 * 	AST grammar, this checks to see if the type is specified.
	 * If it is, a Decl node is minimized, and if not, a ShortDecl
	 * 	node is minimized.
	 */
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
	/**
	 * Visits an IfStmt node
	 * 
	 */
	public Node visitIfStmt(IfStmtContext ctx) {
		Node booleanExpr = visit(ctx.booleanExpr());
		booleanExpr = wrap("BooleanExpr", booleanExpr);
		Node block = visit(ctx.block());
		Node elseIfStmt = visit(ctx.elseIfStmt());
		if (elseIfStmt == null) {
			elseIfStmt = new Node("ElseIf");
		}
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
	/**
	 * Visits a Negate node on the parse tree
	 * If it takes the production that adds the dash,
	 * 	passes a Negate label to minimize so that the negate
	 * 	node will be present on the AST.
	 * Otherwise, passes a null label to the minimize method
	 * 	so that the atomic node will be reduced upwards, removing
	 * 	this node from the AST.
	 */
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
	/**
	 * Visits a constant node in the parse tree
	 * First checks if the constant is an int. If so,
	 * 	returns a node with the value of the given integer
	 * 	and a label of In
	 * Then checks if the constant is a float. In order to remove
	 * 	any unneeded decimals from the number, the float is compared
	 * 	to an integer representation of itself. If the two are the same,
	 * 	the integer value is given as the value of the resulting node. Otherwise,
	 * 	the float value is given. In either case, the node is given the label Float.
	 * Then checks to see if the node is a string. If it is, the string is the value
	 * 	and String is the label.
	 * If none of these cases are true, returns null. 
	 */
	public Node visitConstant(ConstantContext ctx) {
		if (ctx.INT_CONST() != null) {
			return new Node(ctx.getText(), "Int");
		} else if (ctx.FLOAT_CONST() != null) {
			String floatAsString = ctx.getText();
			float floatVal = Float.parseFloat(floatAsString);
			int intVal = (int) floatVal;
			if (floatVal == (float) intVal) {
				return new Node(intVal, "Float");
			} else {
				return new Node(floatVal, "Float");
			}
		} else if (ctx.STRING_CONST() != null) {
			return new Node(ctx.getText(), "String");
		}
		return null;
	}

	@Override
	/**
	 * Visits a ValueExpr node on the parse tree
	 * Because the grammar specifies that this produces
	 * 	either a constant or a SymbolValueExpr, one of the
	 * 	two terms will be null. The null term will not be added
	 * 	as a child to the node produced by minimize.
	 * Because null is passed through to minimize, the child nodes will
	 * 	take the place of this node in the tree, conforming to the tree
	 * 	grammar.
	 */
	public Node visitValueExpr(ValueExprContext ctx) {
		Node constant = visit(ctx.constant());
		Node symbolValueExpr = visit(ctx.symbolValueExpr());
		return minimize(null, constant, symbolValueExpr);
	}

	@Override
	/**
	 * Visits a boolean constant.
	 * Returns a node with a label of boolean and a value
	 * 	matching the value in the tree.
	 */
	public Node visitBooleanConstant(BooleanConstantContext ctx) {
		if (ctx.TRUE() != null) {
			return new Node("true", "Boolean");
		} else if (ctx.FALSE() != null) {
			return new Node("false", "Boolean");
		}
		return null;
	}

	@Override
	/**
	 * Visits a MulDiv node, mimicking the production 
	 * 	MulDiv -> Negate MulDiv*
	 * First visits the Negate expression.
	 * 
	 */
	public Node visitMulDiv(MulDivContext ctx) {
		Node negate = visit(ctx.negate());
		Node mulDivRem = visit(ctx.mulDivRem());
		if (mulDivRem == null) {
			return negate;
		}
		Node lastChild = mulDivRem;
		while (lastChild != null && lastChild.kids.size() != 1) {
			lastChild = (Node) lastChild.kids.get(0);
		}
		lastChild.kids.add(0, negate);
		return mulDivRem;
	}

	@Override
	public Node visitForStmt(ForStmtContext ctx) {
		Node declStmt = visit(ctx.declStmt());
		declStmt = wrap("DeclExpr", declStmt);
		Node booleanExpr = visit(ctx.booleanExpr());
		booleanExpr = wrap("BooleanExpr", booleanExpr);
		Node assignmentStmt = visit(ctx.assignStmt());
		if (assignmentStmt != null) {
			assignmentStmt = wrap("UpdateExpr", assignmentStmt);
		}
		Node block = visit(ctx.block());
		return minimize("For", declStmt, booleanExpr, assignmentStmt, block);
	}

	@Override
	/**
	 * Visits a Stmt node on the parse tree
	 * Because each production only produces one new non-terminal,
	 * 	only one of the varibles will be non-null.
	 * The minimize call will return the node of the non-null production
	 * 	because Stmt is not in the AST grammar.
	 */
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
	/**
	 * Visits a BooleanExpr* node
	 * If the || is not present at the start of the token, 
	 * 	then this method returns null.
	 * If the additional BooleanExpr* node produces null,
	 * 	then this method returns a node labeled Or with the 
	 * 	AndExpr as a child.
	 * If there is a non-null BooleanExpr* in the production,
	 * 	then it will return that BooleanExpr* node with the 
	 * 	AndExpr under an Or node on the bottom of the tree.
	 */
	public Node visitBooleanExprRem(BooleanExprRemContext ctx) {
		if (ctx.OR(0) != null && ctx.OR(1) != null) {
			Node andExpr = visit(ctx.andExpr());
			Node booleanExprRem = visit(ctx.booleanExprRem());
			if (booleanExprRem == null) {
				return minimize("Or", booleanExprRem, andExpr);
			} else {
				Node lastOp = booleanExprRem;
				while (lastOp != null && lastOp.kids.size() != 1) {
					lastOp = (Node) lastOp.kids.get(0);
				}
				lastOp.kids.add(0, wrap("Or", andExpr));
				return booleanExprRem;
			}
		}
		return null;
	}

	@Override
	/**
	 * Visits a TStmt node on the parse tree
	 * Because of the productions on this node,
	 * 	either stmt or funcDefStmt will be null
	 *  so minimize will move the non-null statement
	 * 	into the TStmt's place on the tree.
	 */
	public Node visitTStmt(TStmtContext ctx) {
		Node stmt = visit(ctx.stmt());
		Node funcDefStmt = visit(ctx.funcDefStmt());
		return minimize(null, stmt, funcDefStmt);
	}

	@Override
	/**
	 * Visits an AndExpr* node
	 * If the initial && tokens are not present, then
	 * 	the production went to epsilon, so this method returns null
	 * If the produced AndExpr* is null, then this returns an And node
	 * 	with the NotExpr node as a child.
	 * If the produced AndExpr* is not null, then the NotExpr is added as
	 * 	a child to an And node and placed as a child at the bottom of the
	 * 	AndExpr* subtree.
	 */
	public Node visitAndExprRem(AndExprRemContext ctx) {
		Node notExpr = visit(ctx.notExpr());
		Node andExprRem = visit(ctx.andExprRem());
		if (ctx.AND(0) != null && ctx.AND(1) != null) {
			if (andExprRem == null) {
				return minimize("And", andExprRem, notExpr);
			} else {
				Node lastOp = andExprRem;
				while (lastOp != null && lastOp.kids.size() != 1) {
					lastOp = (Node) lastOp.kids.get(0);
				}
				lastOp.kids.add(0, wrap("And", notExpr));
				return andExprRem;
			}
		} else {
			return null;
		}
	}

	@Override
	/**
	 * Visits a SymbolValueExpr node in the parse tree
	 * Under the rules of the grammar either call or name will
	 * 	be null, so only one will be added as a child.
	 * If name is the chosen production, then it is labeled as a symbol
	 * 
	 */
	public Node visitSymbolValueExpr(SymbolValueExprContext ctx) {
		Node call = visit(ctx.call());
		Node name = visit(ctx.NAME());
		if (name != null) {
			name.label = "Symbol";
		}
		return minimize(null, call, name);
	}

	@Override
	/**
	 * Visits a TypeName node
	 * Returns a node with a label of typename and a value of
	 * 	the type
	 */
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
	/**
	 * Visits an AssignStmt node
	 * Returns a AssignStmt node with children of
	 * 	a Name node and the expression
	 */
	public Node visitAssignStmt(AssignStmtContext ctx) {
		Node name = visit(ctx.NAME());
		name.label = "Name";
		Node expr = visit(ctx.expr());
		return minimize("AssignStmt", name, expr);
	}

	@Override
	public Node visitParamDeclsRem(ParamDeclsRemContext ctx) {
		Node name = visit(ctx.NAME());
		if (name != null) {
			name.label = "Name";
		}
		Node typeSpec = visit(ctx.typeSpec());
		Node paramDeclsRem = visit(ctx.paramDeclsRem());
		return minimize("ParamDecl", name, typeSpec, paramDeclsRem);
	}

	@Override
	/**
	 * Visits the start node on a parse tree.
	 * Because the start node does not appear in the
	 * 	AST, this method returns the following stmts
	 * 	node if it exists or null if there is not one.
	 */
	public Node visitStart(StartContext ctx) {
		if (ctx.stmts() != null) {
			Node result = visit(ctx.stmts());
			List<Node> topLevelStatements = pullup("Stmts", result);
			for (Node stmt : topLevelStatements) {
				stmt.label = null;
			}
			return minimize("Stmts", topLevelStatements.toArray(new Node[0]));
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
	/**
	 * Visits a CallStmt node
	 * Because this node is not part of the AST, a null label
	 * 	is passed to minimize and the child passed to minimize
	 * 	is the node produced by visiting the actual call.
	 */
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
	/**
	 * Visits an ArithExpr* node in the parse tree.
	 * First gets the arithmetic symbol of the arithmetic operation
	 * If the symbol is plus, then if checks if the ArithExpr* is null
	 * 	If it is, it returns a node with a label of plus and a muldiv child
	 * 	Otherwise, it finds the bottom of the tree from the ArithExpr* node
	 * 	and adds the addition node to the bottom of that.
	 * If the symbol is -, it does a similar operation, but adds nodes with a
	 *  - label
	 * If there is no plus or minus, returns null becuase the production
	 * 	is to epsilon
	 */
	public Node visitArithExprRem(ArithExprRemContext ctx) {
		Node plus = visit(ctx.PLUS());
		Node minus = visit(ctx.DASH());
		Node mulDiv = visit(ctx.mulDiv());
		Node arithExprRem = visit(ctx.arithExprRem());
		if (plus != null) {
			if (arithExprRem == null) {
				return minimize("+", arithExprRem, mulDiv);
			} else {
				Node lastOp = arithExprRem;
				while (lastOp != null && lastOp.kids.size() != 1) {
					lastOp = (Node) lastOp.kids.get(0);
				}
				lastOp.kids.add(0, wrap("+", mulDiv));
				return arithExprRem;
			}
		} else if (minus != null) {
			if (arithExprRem == null) {
				return minimize("-", arithExprRem, mulDiv);
			} else {
				Node lastOp = arithExprRem;
				while (lastOp != null && lastOp.kids.size() != 1) {
					lastOp = (Node) lastOp.kids.get(0);
				}
				lastOp.kids.add(0, wrap("-", mulDiv));
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
	/**
	 * Visits a TypeSpec node.
	 * Returns a node with a label of type and a child containing
	 * 	the type.
	 */
	public Node visitTypeSpec(TypeSpecContext ctx) {
		return minimize("Type", visit(ctx.typeName()));
	}

	@Override
	/**
	 * Visits an ArithExpr node on the parse tree
	 * If there are no additional children other than the
	 * 	MulDiv node, that node is returned.
	 * If there are is a ArithExpr* child, then the method
	 *  will go down the tree until the last child is found
	 * 	then the mulDiv expression will be added as a child
	 * 	to that node in order to maintain order of operations
	 * 	on the tree.
	 */
	public Node visitArithExpr(ArithExprContext ctx) {
		Node mulDiv = visit(ctx.mulDiv());
		Node arithExprRem = visit(ctx.arithExprRem());
		if (arithExprRem == null) {
			return mulDiv;
		}
		Node lastChild = arithExprRem;
		while (lastChild != null && lastChild.kids.size() != 1) {
			lastChild = (Node) lastChild.kids.get(0);
		}
		lastChild.kids.add(0, mulDiv);
		return arithExprRem;
	}

	@Override
	/**
	 * Visits a terminal node and returns a node with the associated
	 * 	name and label.
	 * @param node the node to visit
	 * @return The visited node
	 */
	public Node visitTerminal(TerminalNode node) {
		Node returnNode = new Node(node.getText(),
				ArrowLangLexer.ruleNames[node.getSymbol().getType() - 1]);
		return (Node) returnNode;
	}

	/**
	 * Wraps a node as the child node of a new node with a specified label. This
	 * is used to add nodes to the AST that are not part of the parse tree.
	 */
	private Node wrap(String nodeLabel, Node childNode) {
		Node newNode = new Node(nodeLabel);
		newNode.addkid(childNode);
		return newNode;
	}

	/**
	 * Creates a new node from a label and a number of child nodes If no
	 * non-null children are provided, then the method returns null Each child
	 * of each non null child with no label is added as a child to the new node.
	 * If a child node does have a label, it is added to the new node as a child
	 * If this results in only one child being added to a null labeled node,
	 * that child is returned. Otherwise, the new node is returned.
	 * 
	 * @param nodeLabel
	 * @param childNodes
	 * @return
	 */
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

	/**
	 * Brings the children of the children of a node up one level This is used
	 * to convert the string of stmts in the parse tree to a singular stmts in
	 * the AST
	 * 
	 * @param label
	 * @param node
	 */
	private List<Node> pullup(String label, Node node) {
		List<Node> allNodesOfType = new ArrayList<Node>();
		if (node.label.equals(label)) {
			allNodesOfType.add(node);
		}
		int index = 0;
		while (index < node.kids.size()) {
			Node kid = (Node) node.kids.get(index);
			if (kid.label.equals(label)) {
				allNodesOfType.addAll(pullup(label, kid));
				node.kids.remove(index);
			} else {
				index++;
			}
		}
		return allNodesOfType;
	}

	@Override
	public Node visit(ParseTree tree) {
		if (tree == null) {
			return null;
		}
		return super.visit(tree);
	}
}
