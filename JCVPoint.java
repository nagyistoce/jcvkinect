package com.marist.cs2011;

import com.googlecode.javacv.cpp.opencv_core.CvPoint;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class JCVPoint {

	int _x;
	int _y;
	
	public JCVPoint(int x, int y){
		_x = x;
		_y = y;
	}
	
	public int x(){
		return _x;
	}
	
	public int y(){
		return _y;
	}
	
	public void setX(int x){
		_x = x;
	}
	
	public void setY(int y){
		_y = y;
	}

	public boolean isValidInImage(IplImage image) {
		return (_x < image.width() && _x > 0 && _y < image.height() && _y > 0);
	}
	
	public CvPoint getCvPoint(){
		return new CvPoint(_x,_y);
	}
	
	public String toString(){
		return "Point(" + _x + ", " + _y + ")";
	}
}
