/*
 * This class is a part of JCVKinect a project developed by Nathan Daniel and Matt Barulic
 * under the supervision of Mr. Reinald Yoder in Marist School's 2011
 * Advanced Computer Science seminar.
 * Feel free to use this code and your projects and provide any feedback or improvements.
 * We only ask that you keep this and the above lines intact in your edits. Thank you.
 */
 
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
