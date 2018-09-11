package com.service;

public class Coordinate {
	public double x, y;
	
	public Coordinate(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	public Coordinate(int x, int y) {
		this((double)x, (double)y);
	}
}
