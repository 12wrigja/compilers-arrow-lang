package cwru.jjs228.pr03;

public class SizedType extends Type {
	public int size;
	
	public static Type UNIT = new SizedType("unit",0);
	public static Type BOOLEAN = new SizedType("boolean",1);
	public static Type FLOAT32 = new SizedType("float32",32);
	
	public SizedType(String name, int size){
		super(name);
		this.name = name;
		this.size = size;
	}
}
