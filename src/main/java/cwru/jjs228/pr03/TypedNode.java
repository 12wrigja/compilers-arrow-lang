package cwru.jjs228.pr03;

import java.util.ArrayList;
import java.util.List;

public class TypedNode<T> extends Node<T> {

	
	public Type type;
	public List<TypedNode<?>> kids = new ArrayList<TypedNode<?>>();
	
	public TypedNode(String label, Type type){
		super(label);
		this.type = type;
	}
	
	public TypedNode(String label, T value, Type type){
		super(value, label);
		this.type = type;
	}
	
	public TypedNode(Node<T> node){
		super(node.value, node.label);
		for(Node<?> kid: node.kids){
			TypedNode<?> child = new TypedNode<>(kid);
			addkid(child);
		}
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Node) {
			return this.equals((TypedNode<?>) o);
		}
		return false;
	}
	
	public TypedNode<T> addkid(TypedNode<?> n) {
		this.kids.add(n);
		return this;
	}
	
	public boolean equals(TypedNode<?> n) {
		if (!this.value.equals(n.value)) {
			return false;
		}
		if (this.kids.size() != n.kids.size()) {
			return false;
		}
		if (!this.type.equals(n.type)){
			return false;
		}
		for (int i = 0; i < this.kids.size(); i++) {
			if (!this.kids.get(i).equals(n.kids.get(i))) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public String toString() {
		if (kids.size() == 0) {
			if(value != null && type != null){
				return "("+label+","+value.toString()+")";
			} else if(value != null){
				return "("+label+","+value.toString()+")";
			} else{
				return "("+label+")";
			}
		}
		StringBuilder sb = new StringBuilder();
		for (Node<?> kid : kids) {
			sb.append(" ");
			sb.append(kid);
		}
		return String.format("(%s%s)", null != value ? value : label, sb);
	}
	
	
}
