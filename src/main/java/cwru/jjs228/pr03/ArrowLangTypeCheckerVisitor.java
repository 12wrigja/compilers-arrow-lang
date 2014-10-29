package cwru.jjs228.pr03;

import java.util.ArrayList;
import java.util.Arrays;
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

	public void visit(TypedNode<?> node) throws TypeCheckingException, SymbolTableException {
		String name = node.label;
		switch(name){
		case "Stmts":{
			visitStmts(node);
			break;
		}
		case "Symbol":{
			visitSymbol(node);
			break;
		}
		case "Int":{
			visitIntConstant(node);
			break;
		}
		case "Float":{
			visitFloatConstant(node);
			break;
		}
		case "Boolean":{
			visitBoolConstant(node);
			break;
		}
		case "String":{
			visitStringConstant(node);
			break;
		}
		case "And":{
			visitAnd(node);
			break;
		}
		case "Or":{
			visitOr(node);
			break;
		}
		case "Not":{
			visitNegateBoolean(node);
			break;
		}
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
		case "DeclExpr":{
			visitDeclExpr(node);
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
		case "Return":{
			visitReturn(node);
			break;
		}
		case "ParamDecls":{
			visitParameters(node);
			break;
		}
		case "ParamDecl":{
			visitParameter(node);
			break;
		}
		case "UpdateExpr":{
			visitUpdateExpr(node);
			break;
		}
		case "Params":{
			visitParameters(node);
			break;
		}
		case "Type":{
			visitType(node);
			break;
		}
		case "ReturnType":{
			visitReturnType(node);
			break;
		}
		case "Cast":{
			visitCast(node);
			break;
		}
		case "Block":{
			visitBlock(node);
			break;
		}
		case "TypeName":{
			visitTypeName(node);
			break;
		}
		case "BooleanExpr":{
			visitBooleanExpr(node);
			break;
		}
		case "+": /* Type-Check as an addition */
		case "-": /* Type-Check as a subtraction */
		case "*": /* Type-Check as a multiplication */
		case "/": /* Type-Check as a division */
		case "%":{ /* Type-Check as a modulo */
			visitArithOp(node);
			break;
		}
		default:{
			throw new TypeCheckingException(
					"Unable to handle statement with label " + name);
		}
 		}
	}

	public void visitIntConstant(TypedNode<?> node) {
		node.type = Type.Int32;
	}

	public void visitFloatConstant(TypedNode<?> node) {
		node.type = Type.FLOAT32;
	}

	public void visitBoolConstant(TypedNode<?> node) {
		node.type = Type.BOOLEAN;
	}

	public void visitStringConstant(TypedNode<?> node) {
		node.type = Type.STRING;
	}

	public void visitSymbol(TypedNode<?> node) throws TypeCheckingException {
		// Get name of the variable used and check symbol table for it.
		String variableName = node.value.toString();

		// If the symbol is not yet defined, throw an error. Else,
		// Get type from symbol table and assign symbol the type retrieved.
		if (context.isDefined(variableName)) {
			node.type = context.get(variableName);
		} else {
			throw new TypeCheckingException("Symbol " + variableName
					+ " is undefined.");
		}
	}

	public void visitStmts(TypedNode<?> node) throws TypeCheckingException,
			SymbolTableException {
		boolean allUnits = true;
		List<TypedNode<?>> kids = node.kids;
		for (TypedNode<?> kid : kids) {
			visit(kid);
			if (kid.type != Type.UNIT) {
				allUnits = false;
			}
		}
		if (allUnits) {
			node.type = Type.UNIT;
		} else {
			throw new TypeCheckingException(
					"Stmts children are not all unit type. Unable to type-check.");
		}
	}

	public void visitStmt(TypedNode<?> node) throws TypeCheckingException,
			SymbolTableException {
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

	public void visitFuncDefStmt(TypedNode<?> node) throws TypeCheckingException,
			SymbolTableException {
		// Name
		// Parameters
		// Return Type
		// Block

		List<TypedNode<?>> children = node.kids;

		// Kid 0 should be the name. Check and see if it is undefined.
		TypedNode<?> nameNode = children.get(0);
		String variableName = nameNode.value.toString();
		if (context.isDefined(variableName)) {
			throw new TypeCheckingException("Variable " + variableName
					+ "is already defined.");
		}

		// Push a new context for the scope of function
		context.push();

		// Child 2 should be the parameters.
		TypedNode<?> parameters = children.get(1);
		visit(parameters);

		// Child 3 should be the return type
		TypedNode<?> returnNode = children.get(2);
		visit(returnNode);
		context.put("Return", returnNode.type);

		// Child 4 should be the function's block.
		TypedNode<?> block = children.get(3);
		visit(block);
		block.type = Type.UNIT;

		// Pop context
		context.pop();

		nameNode.type = new Type("fn" + parameters.type.name + "->"
				+ returnNode.type.name);

		// Set the type of the entire short declaration statement to unit.
		node.type = Type.UNIT;
	}

	public void visitParameters(TypedNode<?> node) throws TypeCheckingException,
			SymbolTableException {
		List<String> paramTypes = new ArrayList<String>();
		List<TypedNode<?>> children = node.kids;
		for (TypedNode<?> kid : children) {
			visit(kid);
			paramTypes.add(kid.type.name);
		}
		node.type = new Type(Arrays.deepToString(paramTypes.toArray())
				.replace("[", "(").replace("]", ")"));
	}

	public void visitParameter(TypedNode<?> node) throws TypeCheckingException,
			SymbolTableException {
		List<TypedNode<?>> children = node.kids;
		String paramName = children.get(0).value.toString();

		if (context.isLocallyDefined(paramName)) {
			throw new TypeCheckingException("The parameter with name "
					+ paramName + " is already defined.");
		}

		TypedNode<?> typeNode = children.get(1);
		visit(typeNode);

		context.put(paramName, typeNode.type);
		node.type = typeNode.type;
	}

	public void visitType(TypedNode<?> node) throws TypeCheckingException, SymbolTableException {
		TypedNode<?> typeName = (TypedNode<?>) node.kids.get(0);
		visit(typeName);
		node.type = typeName.type;
	}

	public void visitTypeName(TypedNode<?> node) {
		node.type = Type.typeForString(node.value.toString());
	}

	public void visitReturnType(TypedNode<?> node) throws TypeCheckingException, SymbolTableException {
		TypedNode<?> typeNode = (TypedNode<?>) node.kids.get(0);
		visit(typeNode);
		node.type = typeNode.type;
	}

	public void visitReturn(TypedNode<?> node) throws TypeCheckingException , SymbolTableException{
		TypedNode<?> typeNode = (TypedNode<?>) node.kids.get(0);
		visit(typeNode);
		node.type = typeNode.type;

		// Get current return type from context
		if (context.isLocallyDefined("Return")) {
			Type returnType = context.get("Return");
			if (returnType != node.type) {
				throw new TypeCheckingException(
						"Mismatching return types: expected " + returnType.name
								+ " but got " + node.type.name);
			}
		} else {
			throw new TypeCheckingException(
					"No defined return type for this context.");
		}
	}

	public void visitDeclStmt(TypedNode<?> node) {
		throw new UnsupportedOperationException("DeclStmt not implemented yet.");
	}

	public void visitShortDeclStmt(TypedNode<?> node)
			throws TypeCheckingException, SymbolTableException {

		List<TypedNode<?>> children = node.kids;

		// Kid 0 should be the name. Check and see if it is undefined.
		TypedNode<?> nameNode = children.get(0);
		String variableName = nameNode.value.toString();
		if (context.isDefined(variableName)) {
			throw new TypeCheckingException("Variable " + variableName
					+ "is already defined.");
		}

		// Variable is undefined at this point.
		// Compute variable type.
		TypedNode<?> expressionNode = children.get(1);
		visit(expressionNode);

		// Add the variable to the symbol table with the defined type.
		context.put(variableName, expressionNode.type);

		// Set the type of the name node to the type of the expression
		nameNode.type = expressionNode.type;

		// Set the type of the entire short declaration statement to unit.
		node.type = Type.UNIT;
	}

	public void visitAssignStmt(TypedNode<?> node) throws TypeCheckingException,
			SymbolTableException {
		List<TypedNode<?>> children = node.kids;

		// Kid 0 should be the name. Check and see if it is undefined.
		TypedNode<?> nameNode = children.get(0);
		String variableName = nameNode.value.toString();
		if (!context.isDefined(variableName)) {
			throw new TypeCheckingException("Variable " + variableName
					+ "is undefined.");
		}

		// Variable is undefined at this point.
		// Compute variable type.
		TypedNode<?> expressionNode = children.get(1);
		visit(expressionNode);

		// Retrieve the variable's type and see if the type is incompatible with
		// the expression's type.
		Type variableType = context.get(variableName);

		if (variableType != expressionNode.type) {
			throw new TypeCheckingException(
					"Type mismatch: cannot convert from " + expressionNode.type
							+ " to " + variableType);
		}

		// Set the type of the name node to the type of the expression
		nameNode.type = expressionNode.type;

		// Set the type of the entire short declaration statement to unit.
		node.type = Type.UNIT;
	}

	public void visitBooleanExpr(TypedNode<?> node) throws TypeCheckingException,
			SymbolTableException {
		// BooleanExpr has only one child.
		TypedNode<?> decendant = (TypedNode<?>) node.kids.get(0);
		visit(decendant);
		node.type = decendant.type;
	}

	public void visitIfElseStmt(TypedNode<?> node) {
		throw new RuntimeException("IfElse not implemented yet.");
	}

	public void visitForStmt(TypedNode<?> node) throws TypeCheckingException,
			SymbolTableException {
		List<TypedNode<?>> children = node.kids;

		// Push a new context so that the variables in the declaration statement
		// are visible to the rest of the block.
		context.push();

		TypedNode<?> declExpr = children.get(0);
		TypedNode<?> booleanExpr = children.get(1);
		TypedNode<?> updateExpr = children.get(2);
		TypedNode<?> block = children.get(3);

		visit(declExpr);
		visit(booleanExpr);
		visit(updateExpr);
		visit(block);
		if (declExpr.type != Type.UNIT) {
			throw new TypeCheckingException("Declaration Expression has a return type.");
		} else if (booleanExpr.type != Type.BOOLEAN) {
			throw new TypeCheckingException("Loop condition does not evaluate to a boolean.");
		} else if (updateExpr.type != Type.UNIT) {
			throw new TypeCheckingException("Update Expression has a return type.");
		} else if (block.type != Type.UNIT) {
			throw new TypeCheckingException("Block has a return type.");
		} else {
			node.type = Type.UNIT;
		}
		context.pop();
	}

	public void visitExpression(TypedNode<?> node) throws TypeCheckingException,
			SymbolTableException {
		switch (node.label) {
		case "+": /* Type-Check as an addition */
		case "-": /* Type-Check as a subtraction */
		case "*": /* Type-Check as a multiplication */
		case "/": /* Type-Check as a division */
		case "%": /* Type-Check as a modulo */
			visitArithOp(node);
			break;
		case "Int": /* Type-Check as a 32-bit int */
			visitIntConstant(node);
			break;
		case "Float": /* Type-Check as a 32-bit floating point number */
			visitFloatConstant(node);
			break;
		case "String": /* Type-Check as a string */
			visitStringConstant(node);
			break;
		case "Symbol": /* Type-Check as a symbol */
			visitSymbol(node);
			break;
		case "Call": /* Type-Check as a function call */
			visitCall(node);
			break;
		default: /* Throw exception and break everything. */
			throw new TypeCheckingException(
					"Trying to typecheck something that should not be there.");
		}
	}

	public void visitBlock(TypedNode<?> node) throws TypeCheckingException,
			SymbolTableException {
		context.push();
		List<TypedNode<?>> kids = node.kids;
		for (TypedNode<?> kid : kids) {
			visit(kid);
		}
		node.type = Type.UNIT;
		context.pop();
	}

	public void visitDeclExpr(TypedNode<?> node) throws TypeCheckingException,
			SymbolTableException {
		// DeclExpr should have a single child with a unit type.
		TypedNode<?> child = (TypedNode<?>) node.kids.get(0);
		visit(child);
		if (child.type != Type.UNIT) {
			throw new TypeCheckingException(
					"Declaration expression in for loop is invalid.");
		} else {
			node.type = Type.UNIT;
		}
	}

	public void visitUpdateExpr(TypedNode<?> node) throws TypeCheckingException,
			SymbolTableException {
		// UpdateExpr should have a single child with a unit type.
		TypedNode<?> child = (TypedNode<?>) node.kids.get(0);
		visit(child);
		if (child.type != Type.UNIT) {
			throw new TypeCheckingException(
					"Update expression in for loop is invalid.");
		} else {
			node.type = Type.UNIT;
		}
	}

	public void visitAnd(TypedNode<?> node) throws TypeCheckingException,
			SymbolTableException {
		// And has two children, have to be boolean
		List<TypedNode<?>> children = node.kids;
		TypedNode<?> operand1 = children.get(0);
		TypedNode<?> operand2 = children.get(1);
		visit(operand1);
		visit(operand2);
		if (operand1.type != Type.BOOLEAN || operand2.type != Type.BOOLEAN) {
			throw new TypeCheckingException(
					"An operand for AND is not a boolean");
		} else {
			node.type = Type.BOOLEAN;
		}
	}

	public void visitOr(TypedNode<?> node) throws TypeCheckingException,
			SymbolTableException {
		// Or has two children, have to be boolean
		List<TypedNode<?>> children = node.kids;
		TypedNode<?> operand1 = children.get(0);
		TypedNode<?> operand2 = children.get(1);
		visit(operand1);
		visit(operand2);
		if (operand1.type != Type.BOOLEAN || operand2.type != Type.BOOLEAN) {
			throw new TypeCheckingException(
					"An operand for OR is not a boolean");
		} else {
			node.type = Type.BOOLEAN;
		}
	}

	public void visitNegateBoolean(TypedNode<?> node)
			throws TypeCheckingException, SymbolTableException {
		// And has two children, have to be boolean
		List<TypedNode<?>> children = node.kids;
		TypedNode<?> operand1 = children.get(0);
		visit(operand1);
		if (operand1.type != Type.BOOLEAN) {
			throw new TypeCheckingException(
					"The operand for NEGATE is not a boolean");
		} else {
			node.type = Type.BOOLEAN;
		}
	}

	public void visitCmpOp(TypedNode<?> node) throws TypeCheckingException,
			SymbolTableException {
		// Comparisons have two children, must have matching types from set
		// int32, uint32, int8, unit8
		List<TypedNode<?>> children = node.kids;
		TypedNode<?> operand1 = children.get(0);
		TypedNode<?> operand2 = children.get(1);
		visit(operand1);
		visit(operand2);
		boolean operand1IsNum = Type.isOneOfType(operand1.type, new Type[] {
				Type.Int32, Type.Int8, Type.UInt32, Type.UInt8 });
		boolean operand2IsNum = Type.isOneOfType(operand2.type, new Type[] {
				Type.Int32, Type.Int8, Type.UInt32, Type.UInt8 });

		if (operand1.type != operand2.type) {
			throw new TypeCheckingException(
					"Comparison Operands do not have matching types: "
							+ operand1.type + " not equal " + operand2.type);
		} else if (!operand1IsNum) {
			throw new TypeCheckingException(
					"Operand 1 is not a valid comparison type. Recieved: "
							+ operand1.type);
		} else if (!operand2IsNum) {
			throw new TypeCheckingException(
					"Operand 2 is not a valid comparison type. Recieved: "
							+ operand2.type);
		} else {
			node.type = Type.BOOLEAN;
		}
	}

	public void visitCall(TypedNode<?> node) throws TypeCheckingException,
			SymbolTableException {
		// Get name of function, see if it exists.
		// Child 1 is the function name.
		List<TypedNode<?>> children = node.kids;
		TypedNode<?> nameNode = children.get(0);
		String callFunctionName = nameNode.value.toString();

		if (Type.isFunctionCast(callFunctionName)) {
			visit(node);
			return;
		}

		// Lookup type value of the function to see if it exists and find out
		// what parameters it requires
		if (context.isDefined(callFunctionName)) {
			Type functionType = context.get(callFunctionName);

			// Compute provided parameter types compare against function types
			// Child 2 is the parameters
			TypedNode<?> paramsNode = children.get(1);
			visit(paramsNode);
			String serializedParameters = paramsNode.type.name;
			// If the function's type name contains the parameters string, then
			// the parameter types match
			if (functionType.name.contains(serializedParameters)) {
				Type returnType = Type.typeForString(callFunctionName
						.split("->")[1]);
				node.type = returnType;
			} else {
				throw new TypeCheckingException(
						"Mismatched parameter types for function "
								+ callFunctionName);
			}
		} else {
			throw new TypeCheckingException("Call to undefined function "
					+ callFunctionName);
		}
	}

	public void visitCast(TypedNode<?> node) {
		throw new RuntimeException("Method not implemented yet!");
	}

	public void visitArithOp(TypedNode<?> node) throws TypeCheckingException,
			SymbolTableException {
		// Arithmetic Operators have two children, must have matching types from
		// set
		// int32, uint32, int8, unit8
		List<TypedNode<?>> children = node.kids;
		TypedNode<?> operand1 = children.get(0);
		TypedNode<?> operand2 = children.get(1);
		visit(operand1);
		visit(operand2);
		boolean operand1IsNum = Type.isOneOfType(operand1.type, new Type[] {
				Type.Int32, Type.Int8, Type.UInt32, Type.UInt8 });
		boolean operand2IsNum = Type.isOneOfType(operand2.type, new Type[] {
				Type.Int32, Type.Int8, Type.UInt32, Type.UInt8 });

		if (operand1.type != operand2.type) {
			throw new TypeCheckingException(
					"Arithmetic Operator Operands do not have matching types: "
							+ operand1.type + " not equal " + operand2.type);
		} else if (!operand1IsNum) {
			throw new TypeCheckingException(
					"Operand 1 is not a valid comparison type. Recieved: "
							+ operand1.type);
		} else if (!operand2IsNum) {
			throw new TypeCheckingException(
					"Operand 2 is not a valid comparison type. Recieved: "
							+ operand2.type);
		} else {
			node.type = operand1.type;
		}
	}

	public void visitNegateArith(TypedNode<?> node) throws TypeCheckingException,
			SymbolTableException {
		// And has two children, have to be boolean
		List<TypedNode<?>> children = node.kids;
		TypedNode<?> operand1 = children.get(0);
		visit(operand1);
		if (Type.isOneOfType(operand1.type, new Type[] { Type.Int32, Type.Int8,
				Type.UInt32, Type.UInt8 })) {
			throw new TypeCheckingException(
					"The operand for arithmetic NEGATE is not a number!");
		} else {
			node.type = operand1.type;
		}
	}
}
