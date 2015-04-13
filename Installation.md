# Introduction #

JCVKinect is supposed to be an easy to set up and to use class set. There are only two class files necessary to get the Kinect to provide OpenCV image objects.


# Setup #

## Adding OpenKinect and JavaCV to an eclipse project ##
In order to use JCVKinect, you must have the OpenKinect Java [wrapper](http://openkinect.org/wiki/Java_JNA_Wrapper) and [JavaCV](http://code.google.com/p/javacv/) in your project. Install these two libraries per the instructions on their homepages.
The following instructions are for the Eclipse IDE, though other IDEs should be similar.
  1. Open the project you want to add them to and select Project->Properties from the menu bar.
  1. Select **Java Build Path** from the left list. Here, you can add the necessary JAR files. Select **Add external JARs...** and import the two JARs for libfreenect and the eight JARS for JavaCV.
  1. Select **OK** to close the properties window.
  1. Open a Finder window, find the folder in which you downloaded and built libfreenect, and open the /build/lib/ folders.
  1. Right click **libfreenect.dylib** and copy it. Go back to Eclipse, right click on you project's main folder in the Package Explorer, and paste the file into that folder.

You are now done adding the JavaCV and OpenKinect libraries to your Eclipse project.

## Adding JCVKinect to your Project ##
With those libraries installed, here's how to setup JCVKinect
  1. Download [JCVKinect.jar](http://code.google.com/p/jcvkinect/downloads/detail?name=JCVKinect.jar&can=2&q=#makechanges). Save it somewhere where it will be safe to live for a while.
  1. Using the same technique as in the previous section, add JCVKinect.jar to your projects Build Path.
  1. Import JCVKinect and JCVKinectDelegate to your source code using the following code.
```
import com.marist.cs2011.JCVKinect;
import com.marist.cs2011.JCVKinectDelegate;
```


# Use #
  1. Create a new _JCVKinect_ object
```
JCVKinect kinect = new JCVKinect();
```
  1. Set the delegate of the new Kinect object to a class of your own that implements the _JCVKinectDelegate_ interface.
```
kinect.setDelegate( [delegate variable name goes here] );
```
  1. Initialize the Kinect device. The int argument here indicates which device you want to use if more than one are plugged in.
```
kinect.initDevice(0);
```
  1. Call startDepth(); or startRGB(); to begin the depth/video feeds respectively.
  1. When you are done with the Kinect data feed, call stopDepth(); or stopRGB(); You must call the stop command that is appropriate for the start command you called earlier.
  1. Finally, call close(); in order to make sure the Kinect device is properly shut down and available for the next program that wishes to use it.
```
kinect.close();
```

The delegate class **must** implement _JCVKinectDelegate_ and define two methods
  * onRecievedDepthFrame(IplImage image, JCVKinect device);
  * onRecievedRGBFrame(IplImage image, JCVKinect device);

These delegate callbacks are called by a _JCVKinect_ object to provide the depth/RGB data from the Kinect.

# Example #
```

import static com.googlecode.javacv.cpp.opencv_core.*;
import org.openkinect.freenect.LedStatus;
import com.googlecode.javacv.*;

import com.marist.cs2011.JCVKinect;
import com.marist.cs2011.JCVKinectDelegate;

/*
 * Simple example using JCVKinect, OpenKinect, and JavaCV to display depth and RGB feed on screen.
 */

public class TestJCVKinect implements JCVKinectDelegate{

	CanvasFrame RGBcanvas;
	CanvasFrame DepthCanvas;
	
	public TestJCVKinect(){
		RGBcanvas = new CanvasFrame("Tester - RGB");
		DepthCanvas = new CanvasFrame("Tester - Depth");
		DepthCanvas.setBounds(640, 0, DepthCanvas.getWidth(), DepthCanvas.getHeight());
	}
	
	public static void main(String[] args) {
		TestJCVKinect tester = new TestJCVKinect();
		JCVKinect kinect = new JCVKinect();
		if(kinect.getNumberOfDevices() > 0){
			kinect.setDelegate(tester);
			kinect.initDevice(0);
			kinect.getDevice().setLed(LedStatus.GREEN);
			kinect.getDevice().setTiltAngle(10);
			kinect.startRGB();
			kinect.startDepth();
			while(tester.DepthCanvas.isVisible() || tester.RGBcanvas.isVisible()) System.out.print("");
			kinect.stopRGB();
			kinect.stopDepth();
			kinect.getDevice().setLed(LedStatus.BLINK_GREEN);
			kinect.close();
		}
		System.exit(0);
	}

	@Override
	public void onRecievedDepthFrame(IplImage image, JCVKinect device) {
		DepthCanvas.showImage(image);
	}

	@Override
	public void onRecievedRGBFrame(IplImage image, JCVKinect device) {
		RGBcanvas.showImage(image);
	}

}

```