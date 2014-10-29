package cwru.jjs228.pr03;

public class NativeLibrary {
	
	public static FunctionType[] nativeFunctions(){
		FunctionType printint32 = new FunctionType("print_int32", new Type[]{IntType.Int32}, SizedType.UNIT);
		FunctionType printuint32 = new FunctionType("print_uint32", new Type[]{IntType.UInt32}, SizedType.UNIT);
		FunctionType printint8 = new FunctionType("print_int8", new Type[]{IntType.Int8}, SizedType.UNIT);
		FunctionType printuint8 = new FunctionType("print_uint8", new Type[]{IntType.UInt8}, SizedType.UNIT);
		FunctionType printfloat32 = new FunctionType("print_float32", new Type[]{SizedType.FLOAT32}, SizedType.UNIT);
		FunctionType print = new FunctionType("print", new Type[]{Type.STRING}, SizedType.UNIT);
		return new FunctionType[]{printint32,printuint32,printint8,printuint8,printfloat32,print};
	}
	
	public static FunctionType[] castingFunctions(){
		FunctionType castint32tofloat32 = new FunctionType("cast_int32_to_float32", new Type[]{IntType.Int32}, SizedType.FLOAT32);
		FunctionType castfloat32toint32 = new FunctionType("cast_float32_to_int32", new Type[]{SizedType.FLOAT32}, IntType.Int32);
		FunctionType castint8toint32 = new FunctionType("case_int8_to_int32", new Type[]{IntType.Int8}, IntType.Int32);
		return new FunctionType[]{castint32tofloat32,castfloat32toint32, castint8toint32};
	}
}
