package com.summithillsoftware.ultimate.ui;

import android.graphics.Point;
import android.graphics.Rect;

public class ShapeMath {
	
	/* Given a rectangle, compute the point at which a line (angle specified by degrees)
	 * drawn from the center of the rectangle intersects the rectangle.  0 degrees is north
	 */
	public final static Point pointOnRectAtAngle(int rectWidth, int rectHeight, int degrees) {
		Rect rect = new Rect(0, 0, rectWidth, rectHeight);
		// 1.) find a distant point that will intersect the rect
		int distantPointLineLength = Math.max(rect.height(), rect.width());  // length is not important as long as it is beyond border of rect
		Point rectMid = new Point((int)rect.exactCenterX(), (int)rect.exactCenterY());
		Point distantPoint = calcPointOnCirle(rectMid, distantPointLineLength, degrees - 90);
		
		// 2.) check each side of the rect until we find the intersection
		int x1 = rectMid.x;
		int y1 = rectMid.y;
		int x2 = distantPoint.x;
		int y2 = distantPoint.y;
		int x3, x4, y3, y4;
		Point intersection = null;
		
		// top and bottom
		if (distantPoint.y < rectMid.y) {
			// top
			x3 = rect.left;
			y3 = rect.top;
			x4 = rect.right;
			y4 = rect.top;
			intersection = intersection(x1,y1,x2,y2, x3, y3, x4,y4);
			if (intersection != null) {
				return intersection;
			}
		} else {
			// bottom
			x3 = rect.left;
			y3 = rect.bottom;
			x4 = rect.right;
			y4 = rect.bottom;
			intersection = intersection(x1,y1,x2,y2, x3, y3, x4,y4);
			if (intersection != null) {
				return intersection;
			}
		}
		
		// right and left
		if (distantPoint.y > rectMid.y) {
			// right
			x3 = rect.right;
			y3 = rect.top;
			x4 = rect.right;
			y4 = rect.bottom;
			intersection = intersection(x1,y1,x2,y2, x3, y3, x4,y4);
			if (intersection != null && intersection.x > rectMid.x) {
				return intersection;
			}
		} else {
			// left
			x3 = rect.left;
			y3 = rect.top;
			x4 = rect.left;
			y4 = rect.bottom;
			intersection = intersection(x1,y1,x2,y2, x3, y3, x4,y4);
			if (intersection != null && intersection.x < rectMid.x) {
				return intersection;
			}
		}
		
		return null;
	}
	
	private static final Point calcPointOnCirle(Point centerPoint, int radius, int degrees) {
	    double radians = Math.toRadians(degrees);
	    return new Point(centerPoint.x + (int)Math.round(radius * Math.cos(radians)), centerPoint.y + (int)Math.round(radius * Math.sin(radians)));
	}
	
	/**
	   * Computes the intersection between two lines. The calculated point is approximate, 
		 * since integers are used. If you need a more precise result, use doubles
		 * everywhere. 
		 * (c) 2007 Alexander Hristov. Use Freely (LGPL license). http://www.ahristov.com
		 *
	   * @param x1 Point 1 of Line 1
	   * @param y1 Point 1 of Line 1
	   * @param x2 Point 2 of Line 1
	   * @param y2 Point 2 of Line 1
	   * @param x3 Point 1 of Line 2
	   * @param y3 Point 1 of Line 2
	   * @param x4 Point 2 of Line 2
	   * @param y4 Point 2 of Line 2
	   * @return Point where the segments intersect, or null if they don't
	   */
	public static final Point intersection(
		int x1,int y1,int x2,int y2, 
		int x3, int y3, int x4,int y4
		) {
	  int d = (x1-x2)*(y3-y4) - (y1-y2)*(x3-x4);
	  if (d == 0) { 
		  return null;
	  }
	    
	  int xi = ((x3-x4)*(x1*y2-y1*x2)-(x1-x2)*(x3*y4-y3*x4))/d;
	  int yi = ((y3-y4)*(x1*y2-y1*x2)-(y1-y2)*(x3*y4-y3*x4))/d;
	    
	  return new Point(xi,yi);
	}

}
