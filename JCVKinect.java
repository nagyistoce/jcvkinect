package com.marist.cs2011;

import java.nio.ByteBuffer;

import org.openkinect.freenect.*;

import static com.googlecode.javacv.cpp.opencv_core.*;

public class JCVKinect implements DepthHandler, VideoHandler {
	
	private Context _context = null;
	private Device _device = null;
	private JCVKinectDelegate _delegate = null;
	
	public JCVKinect(){
		_context = Freenect.createContext();
	}
	
	public void close(){
		_device.close();
		_context.shutdown();
	}
	
	/**
	 * This method gives access to the OpenKinect device object for the purpose LED/ACCEL/MOTOR control.
	 * This is a design choice based on the face that this wrapper is intended purely for porting the Kinect imaging data into an OpenCV format.
	 * @return an OpenKinect Device object representing this Kinect device.
	 */
	public Device getDevice(){
		return _device;
	}
	
	/**
	 * In order to receive frame callbacks, you must call this method, passing a non-null object.
	 * @param delegate
	 */
	public void setDelegate(JCVKinectDelegate delegate){
		_delegate = delegate;
	}
	
	/**
	 * 
	 * @return number of devices connected to this computer
	 */
	public int getNumberOfDevices(){
		return _context.numDevices();
	}
	
	/**
	 * 
	 * @return the index of this Kinect device
	 */
	public int getDeviceIndex(){
		return _device.getDeviceIndex();
	}
	
	/**
	 * Connects to the specified device. Must be called before start/stop RGB/Depth
	 * @param deviceIndex
	 * @return success bool indicating whether or not the code was able to connect to the Kinect
	 */
	public boolean initDevice(int deviceIndex){
		if(deviceIndex < getNumberOfDevices()){
			_device = _context.openDevice(deviceIndex);
			_device.setDepthFormat(DepthFormat.D10BIT);
			_device.setVideoFormat(VideoFormat.RGB);
			return ( _device != null );
		} else {
			return false;
		}
	}
	
	/**
	 * Starts the Kinect's Depth camera. The delegate will begin receiving Depth callbacks once this method is called.
	 */
	public void startDepth(){
		_device.startDepth(this);
	}
	
	/**
	 * Starts the Kinect's RGB camera. The delegate will begin receiving RGB callbacks once this method is called.
	 */
	public void startRGB(){
		_device.startVideo(this);
	}
	
	/**
	 * Stops the Kinect's Depth camera. The delegate will no longer receive Depth callbacks after this method is called.
	 */
	public void stopDepth(){
		_device.stopDepth();
	}
	
	/**
	 * Stops the Kinect's RGB camera. The delegate will no longer receive RGB callbacks after this method is called.
	 */
	public void stopRGB(){
		_device.stopVideo();
	}

	/**
	 * This method is public only so that it works with the libfreenect java wrapper. Code using JCVKinect should never call this method.
	 */
	@Override
	public void onFrameReceived(DepthFormat format, ByteBuffer buffer, int arg2) {
		IplImage image = IplImage.create(format.getWidth(), format.getHeight(), IPL_DEPTH_8U, 1);
		ByteBuffer imgBuffer = image.getByteBuffer();
		buffer.rewind();
		char min = (char)66000, max = 0;
		for( int y = 0; y < format.getHeight(); y++ ) {
			for( int x = 0; x < format.getWidth(); x++) {
				int firstByte = (0x000000FF & buffer.get());
				int secondByte = (0x000000FF & buffer.get());
				char raw = (char) (firstByte | secondByte << 8);
				if(raw > max) max = raw; if(raw < min)min = raw;
				int val = (int) ( ( (double)raw / 1024.00 ) * 255.00 );
				if(x == format.getWidth()/2 && y == format.getHeight()/2) System.out.println(firstByte + " " + secondByte);
				imgBuffer.put(y*image.widthStep()+image.nChannels()*x, (byte)val );
			}
		}
		System.out.print((int)min); System.out.print("\t"); System.out.println((int)max);
		if(_delegate != null) _delegate.onRecievedDepthFrame(image, this);
	}

	/**
	 * This method is public only so that it works with the libfreenect java wrapper. Code using JCVKinect should never call this method.
	 */
	@Override
	public void onFrameReceived(VideoFormat format, ByteBuffer buffer, int arg2) {
		IplImage image = IplImage.create(format.getWidth(), format.getHeight(), IPL_DEPTH_8U, 3);
		ByteBuffer imgBuffer = image.getByteBuffer();
		buffer.rewind();
		for( int y = 0; y < format.getHeight(); y++ ) {
			for( int x = 0; x < format.getWidth(); x++) {
				imgBuffer.put(y*image.widthStep()+image.nChannels()*x+2, (byte) buffer.get());
				imgBuffer.put(y*image.widthStep()+image.nChannels()*x+1, (byte) buffer.get());
				imgBuffer.put(y*image.widthStep()+image.nChannels()*x+0, (byte) buffer.get());
			}
		}
		if(_delegate != null) _delegate.onRecievedRGBFrame(image, this);
	}
}
