package cwru.jjs228.pr03;

public class Type {
	public String name;
	
	public static Type STRING = new Type("String");
	public static IntType Int32 = new IntType("int32",32,true);
	public static IntType UInt32 = new IntType("uint32",32,false);
	public static IntType Int8 = new IntType("int8",8,true);
	public static IntType UInt8 = new IntType("uint8",8,false);
	public static Type UNIT = new SizedType("unit",0);
	public static Type BOOLEAN = new SizedType("boolean",1);
	public static Type FLOAT32 = new SizedType("float32",32);
	
	
	public Type(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(null != obj && obj instanceof Type){
			return ((Type)obj).name.equals(this.name);
		}else{
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
	public static Type typeForString(String str){
		switch(str){
		case "boolean": return SizedType.BOOLEAN;
		case "unit": return SizedType.UNIT;
		case "int32": return IntType.Int32;
		case "unit32": return IntType.UInt32;
		case "int8": return IntType.Int8;
		case "uint8": return IntType.UInt8;
		case "float32": return SizedType.FLOAT32;
		case "string": return STRING;
		default: return null;
		}
	}
	
	public static boolean isOneOfType(Type source, Type[] potentialTypes){
		for(Type t : potentialTypes){
			if(source.equals(t)){
				return true;
			}
		}
		return false;
	}
	
	public static boolean isFunctionCast(String functionName){
		return typeForString(functionName)!=null;
	}
}
