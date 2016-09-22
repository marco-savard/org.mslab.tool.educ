package org.mslab.tool.educ.server.services;

public abstract class SecureHashing {
	
	private static SecureHashing _instance; 
	public static SecureHashing getInstance() {
		if (_instance == null) {
			//_instance = new DebugHashing();
			_instance = new BlochHashing();
		}
		
		return _instance;
	}
	
	public abstract long hashCode(String text);
	
	public static void main(String[] args) {
		SecureHashing hashing = SecureHashing.getInstance(); 
		String phrase = "A VeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryLongWord Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."; 
		String[] pws = phrase.split(" "); 
		
		for (String pw : pws) {
			long hash = hashing.hashCode(pw);
			System.out.println("hash(" + pw +") = " + hash); 
		}
		
		System.out.println("Success"); 
	}
	
	//
	// for debugging
	//
	private static class DebugHashing extends SecureHashing {

		@Override
		public long hashCode(String text) {
			return text.length();
		}
	}
	
	//
	// Josh Bloch's method (Effective Java, item 8)
	// implemented only for String
	// http://stackoverflow.com/questions/113511/hash-code-implementation
	//
	private static class BlochHashing extends SecureHashing {
		private static final long INITIAL_RESULT = 37; //An arbitrary prime number
		private static final long HASH_FACTOR = 37; //An arbitrary prime number
		private static final long MAX_VALUE = Long.MAX_VALUE / HASH_FACTOR; 
		
		@Override
		public long hashCode(String text) {
			long result = INITIAL_RESULT; 
			
			for (int i=0; i<text.length(); i++) {
				char f = text.charAt(i);
				int c = (int)f; 
				result *= HASH_FACTOR;
				result = (result > MAX_VALUE) ? result % MAX_VALUE : result;
				result += c; 
			}
			
			return result;
		}
	}
	
	

}
