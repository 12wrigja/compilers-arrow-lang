package cwru.jjs228.pr03;

public class FunctionType extends Type {
	
	public Type[] parameterTypes;
	public Type returnType;
	
	public FunctionType(String name, int size, Type[] parameterTypes, Type returnType) {
		super(name, size);
		this.parameterTypes = parameterTypes;
		this.returnType = returnType;
	}
	
}
