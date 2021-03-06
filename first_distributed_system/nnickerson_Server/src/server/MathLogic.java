package server;

public class MathLogic {

	public MathLogic() {
		
	}
	
	public int add(int a, int b) {
		int sum = 0;
		
		sum = a + b;
		
		return sum;
	}
	
	public int subtract(int a, int b) {
		int sub = 0;
		
		sub = a - b;
		
		return sub;
	}
	
	public double divide(double ... divisors) {
		double result = 0;
		
		double[] params = divisors;
		
		for(int i = 0; i < params.length; i++) {
			if(i == 0) {
				result += params[i];
			}
			else {
				result = result/params[i];
			}
		}
		
		return result;
	}
	
	public float multiply(float flo, float ... multiplies) {
		float result = 0;
		float[] mults = multiplies;
		
		for(int i = 0; i < mults.length; i++) {
			if(i == 0) {
				result = result + mults[i];
			}
			else {
				result = result*mults[i];
			}
		}
		
		return result;
	}
	
	public int square(int sq) {
		int r = 0;
		r = sq*sq;
		return r;
	}
}
