package cwru.jjs228.pr03;

public class IntType extends Type {

	public boolean signedInt;
	
	public IntType(String name, int size, boolean signed) {
		super(name, size);
		this.signedInt = signed;
	}

	public static IntType Int32 = new IntType("int32",32,true);
	public static IntType UInt32 = new IntType("uint32",32,false);
	public static IntType Int8 = new IntType("int8",8,true);
	public static IntType UInt8 = new IntType("uint8",8,false);
	
}
