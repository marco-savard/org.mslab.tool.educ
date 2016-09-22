package org.mslab.tool.educ.shared.util;

public class Random {
	private static long _seed; 
	
	public static int nextInt(int upperBound) {
		int i = (int)(nextDouble() * upperBound);
		return i;
	}
	
	public static double nextDouble() { //accuracy: 0.001
		long ms = System.currentTimeMillis();  //nanoTime() not supported
		ms = ms % 1000;
		ms = ms + (_seed * 13) + (_seed * _seed * 7);
		_seed = ms % 1000;
		double d = (_seed / 1000.0);
		return d;
	}

}
