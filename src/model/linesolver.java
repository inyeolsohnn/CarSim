package model;

import java.awt.geom.Point2D;

//Util clas
public class linesolver {

	/*
	 * we assume that there are two points p1,p2 belonging to line1 two points
	 * p3,p4 belonging to line2 and we need to calculate whether there is an
	 * intersection of these two lines
	 */

	public static Point2D.Float checkIntersection(Point2D.Float p1,
			Point2D.Float p2, Point2D.Float p3, Point2D.Float p4)
			throws UnknownConnectionError {
		Float f = (p1.x - p2.x) * (p3.y - p4.y) - (p1.y - p2.y) * (p3.x - p4.x);
		if (f == 0)
			throw new UnknownConnectionError();
		Float xi = ((p3.x - p4.x) * (p1.x * p2.y - p1.y * p2.x) - (p1.x - p2.x)
				* (p3.x * p4.y - p3.y * p4.x))
				/ f;
		Float yi = ((p3.y - p4.y) * (p1.x * p2.y - p1.y * p2.x) - (p1.y - p2.y)
				* (p3.x * p4.y - p3.y * p4.x))
				/ f;

		Point2D.Float intersection = new Point2D.Float(xi, yi);

		return intersection;
	}

}
