

public class TestMid extends TestSuper {
	
	private long x;

	public TestMid(int superInt, String s, long x) {
		super(superInt, s);
		this.x = x;
	}
	
	public long getX() {
		return x;
	}

}
