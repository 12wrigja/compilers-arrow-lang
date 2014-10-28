package cwru.jjs228.pr03;

import java.util.HashMap;
import java.util.LinkedList;

public class SymbolTable {

	public static class SymbolTableException extends Exception {
		public SymbolTableException(String message) {
			super(message);
		}
	}

	LinkedList<HashMap<String, TypedNode>> symbols = new LinkedList<HashMap<String, TypedNode>>();

	public void push() {
		this.symbols.add(0, new HashMap<String, TypedNode>());
	}

	public HashMap<String, TypedNode> pop() throws SymbolTableException {
		if (this.symbols.size() < 1) {
			throw new SymbolTableException("Nothing to pop!");
		}
		return this.symbols.removeFirst();
	}

	public TypedNode get(String symbol) {
		for (HashMap<String, TypedNode> table : this.symbols) {
			if (table.containsKey(symbol)) {
				return table.get(symbol);
			}
		}
		return null;
	}

	public void put(String symbol, TypedNode value) throws SymbolTableException {
		if (this.symbols.size() < 1) {
			throw new SymbolTableException("No tables on the stack");
		}
		this.symbols.getFirst().put(symbol, value);
	}

	public boolean topHas(String symbol) {
		return this.symbols.getFirst().containsKey(symbol);
	}

	public boolean has(String symbol) {
		for (HashMap<String, TypedNode> table : this.symbols) {
			if (table.containsKey(symbol)) {
				return true;
			}
		}
		return false;
	}

}
