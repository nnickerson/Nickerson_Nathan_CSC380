package importClasses;

public class TestClass {

	public int addTheseNumbers(int ... nums) {
		int result = 0;
		for(int n : nums) {
			result += n;
		}
		return result;
	}

}
