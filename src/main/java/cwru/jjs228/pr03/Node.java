package cwru.jjs228.pr03;

import java.util.ArrayList;

public class Node<T> {

	public T value;
	public String label;
	public ArrayList<Node<?>> kids = new ArrayList<Node<?>>();

	public Node(T value) {
		this.value = value;
		this.label = "";
	}
	
	public Node(String label){
		this.label = label;
	}
	
	public Node(T value, String label){
		this.value = value;
		this.label = label;
	}

	public Node<T> addkid(Node<?> n) {
		this.kids.add(n);
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Node) {
			return this.equals((Node) o);
		}
		return false;
	}

	public boolean equals(Node n) {
		if (!this.value.equals(n.value)) {
			return false;
		}
		if (this.kids.size() != n.kids.size()) {
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
	public int hashCode() {
		int max = Integer.MAX_VALUE;
		int hash = (2 * (value.hashCode() + 1)) % max;
		int i = 3;
		for (Node n : this.kids) {
			hash = (hash + ((3 * (n.hashCode() + 1)) % max)) % max;
		}
		return hash;
	}

	@Override
	public String toString() {
		if (kids.size() == 0) {
			if(value != null){
				return "("+label+","+value.toString()+")";
			}else{
				return "("+label+")";
			}
		}
		StringBuilder sb = new StringBuilder();
		for (Node kid : kids) {
			sb.append(" ");
			sb.append(kid);
		}
		String nodeString = "";
		if(label != null && value != null){
			nodeString = label+":"+value;
		}else if (label != null){
			nodeString = label;
		}else if (value != null){
			nodeString = value.toString();
		}else{
			nodeString = null;
		}
		return String.format("(%s%s)", null != value ? value : label, sb);
	}
}
