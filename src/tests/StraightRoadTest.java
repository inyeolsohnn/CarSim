package tests;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;

import model.Car;
import model.Road;
import model.StraightRoad;

public class StraightRoadTest {
	public static void main(String[] args) throws InterruptedException {
		Road sr = new StraightRoad(new Point2D.Float(100, 100),
				new Point2D.Float(180, 160), 2, 2);
		Car c1 = new Car();
		c1.setCurrentSpeed(10);
		c1.enterLane(sr.getLanes().get(3), sr.getLanes().get(3).getStart());
		while (true) {
			c1.move();
			Thread.sleep(20);
		}
	}
}
