/*
 * This class is a part of JCVKinect a project developed by Nathan Daniel and Matt Barulic
 * under the supervision of Mr. Reinald Yoder in Marist School's 2011
 * Advanced Computer Science seminar.
 * Feel free to use this code and your projects and provide any feedback or improvements.
 * We only ask that you keep this and the above lines intact in your edits. Thank you.
 */
 
package com.marist.cs2011;
import static com.googlecode.javacv.cpp.opencv_core.*;

public interface JCVKinectDelegate {
	/**
	 * Is called every time the kinect gives a new RGB camera frame. image is a 3 channel, 8U IplImage.
	 * @param image
	 * @param device
	 */
	void onRecievedRGBFrame(IplImage image, JCVKinect device);
	/**
	 * Is called every time the kinect gives a new RGB camera frame. image is a 1 channel, 8U IplImage.
	 * @param image
	 * @param device
	 */
	void onRecievedDepthFrame(IplImage image, JCVKinect device);
}
