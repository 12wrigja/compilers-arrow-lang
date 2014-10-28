package cwru.jjs228.pr03;

public class Type {
	public String name;
	public int size;
	
	public static Type UNIT = new Type("unit",0);
	public static Type BOOLEAN = new Type("boolean",1);
	public static Type FLOAT32 = new Type("float32",32);
	
	public Type(String name, int size){
		this.name = name;
		this.size = size;
	}
	

	@Override
	public String toString() {
		return name;
	}
}
