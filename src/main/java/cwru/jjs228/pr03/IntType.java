package cwru.jjs228.pr03;

public class IntType extends SizedType {

	public boolean signedInt;
	
	public IntType(String name, int size, boolean signed) {
		super(name, size);
		this.signedInt = signed;
	}
}
