import java.awt.geom.Point2D;


public class Example {
	
	/* the straight line equation is 
	 * y=k*x+b   
	 * the basic circle equation is (x-cx)^2 + (y-cy)^2 = r^2 
	 * in the circle equation, cx,cy,r are the center coordinate and radius separately
	 * there are two points belonging to the straight line, where the coordinates are
	 * p1:(x1,y1), p2:(x2,y2)
	 * Hence the k would be (y2-y1)/(x2-x1)*/
	public static Point2D checkIntersection(Point2D.Double p1, Point2D.Double p2, Point2D.
			Double center, double radius){
		double k = (p2.y-p1.y)/(p2.x-p1.x);     // the slope 
		double b = p1.y-k*p1.x; 
		/*now we use kx+b to replace y and combine with circle equation and get a new equation
		 * then we simplify the new equation to:
		 * (1+k^2)*x^2 +2*(k*b-k*cy-cx)*x+ cx^2+(b-cy)^2 -r^2 = 0
		*/
		Double A = 1+k*k;
		Double B = 2*(k*b-k*center.y-center.x);
		Double C = center.x*center.x+(b-center.y)*(b-center.y)-radius*radius;
		/* now the equation has been transfered into Ax^2+Bx+C = 0
		 * Hence two answers of this equation would be (-B+tempValue/2)/2a and
		 * (-B-tempValue/2)/2a
		*/
		Double checkValue = B*B-4*A*C;
		Double tempValue = Math.sqrt(B*B-4*A*C);
		Double x1 = (-B+tempValue)/2*A;
		Double y1 = k*x1+b;
		Double x2 = (-B-tempValue)/2*A;
		Double y2 = k*x2+b;
		Point2D.Double interPoint1 = new Point2D.Double(x1, y1);
		Point2D.Double interPoint2 = new Point2D.Double(x2, y2);
		if(checkValue<0) return null;				//there is no intersection
		else if(checkValue ==0){					//there are two even intersections
			
			return interPoint1;
		}
		else if(checkValue>0){						//there are two different intersections
			
			if(Math.abs(p1.x-center.x)<Math.abs(p2.x-center.x)){			//point1 is closer
				if(Math.abs(interPoint1.x-p1.x)<Math.abs(interPoint2.x-p1.x)){
					return interPoint1;
				}
				else{
					return interPoint2;
				}
			}
			else 	//point2 is closer
			{
				if(Math.abs(interPoint1.x-p2.x)<Math.abs(interPoint2.x-p2.x)){
					return interPoint1;
				}
				else{
					return interPoint2;
				}
			}
			
			
		}
		return null;
	}
	public static void main(String[] args){
		Point2D.Double  p1 = new Point2D.Double(1, 3);
		Point2D.Double  p2 = new Point2D.Double(2, 3);
		Point2D.Double  center = new Point2D.Double(5, 5);
		Double radius = 2.0;
		System.out.println(checkIntersection(p1,p2,center,radius));
		
	}
}
