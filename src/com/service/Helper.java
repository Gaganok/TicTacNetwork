package com.service;



public class Helper {

	/*public static double convertToPos(double x, double y) {
		return x + y * 3;
	}*/
	
	public static int convertToPos(int x, int y) {
		return x + y * 3;
	}
	
	public static Coordinate convertToCord(int pos) {
		int y = pos / 3;
		int x = pos - y * 3;
		return new Coordinate(x, y);
	}
}
