package cwru.jjs228.pr03;

public class TypedNode<T> extends Node<T> {

	
	public Type type;

	
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
			return this.equals((TypedNode) o);
		}
		return false;
	}
	
	public boolean equals(TypedNode n) {
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
				return "("+label+","+value.toString()+type.toString()+")";
			} else if(value != null){
				return "("+label+","+value.toString()+")";
			} else{
				return "("+label+")";
			}
		}
		StringBuilder sb = new StringBuilder();
		for (Node kid : kids) {
			sb.append(" ");
			sb.append(kid);
		}
		String nodeString = "";
		if(label != null && value != null && type != null){
			nodeString = label+","+value + ":" + type.toString();
		} else if(label != null && value != null){
			nodeString = label +","+ value;
		} else if(label != null && type != null){
			nodeString = label + ":" + type.toString();
		} else if (label != null){
			nodeString = label;
		} else if (value != null){
			nodeString = value.toString();
		} else if(type != null){
			nodeString = type.toString();
		} else{
			nodeString = null;
		}
		return String.format("(%s%s)", null != value ? value : label, sb);
	}
	
	
}
