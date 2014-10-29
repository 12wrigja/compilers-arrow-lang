package cwru.jjs228.pr03;

public class SizedType extends Type {
	public int size;
	
	public SizedType(String name, int size){
		super(name);
		this.name = name;
		this.size = size;
	}
}
