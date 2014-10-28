package cwru.jjs228.pr03;

public class Type {
	public String name;
	
	public static Type STRING = new Type("String");

	public Type(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}
