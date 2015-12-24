package tests;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import model.Car;
import model.Connection;
import model.ConnectionPoint;
import model.Lane;
import model.Road;
import model.StraightRoad;
import model.UnknownConnectionError;

public class ConnectTest {
	public static void main(String[] args) throws InterruptedException {
		Road sr = new StraightRoad(new Point2D.Float(200, 200),
				new Point2D.Float(400, 200), 3, 1);
		System.out.println(sr.getBilateral());
		Road sr2 = new StraightRoad(new Point2D.Float(300, 1000),
				new Point2D.Float(300, 200), 1, 0);
		Car car= new Car();
		System.out.println(sr2.getBilateral());
		try {
			Road.connectLane(sr2, 0, sr, 1);
			
			Lane testLane = sr2.getLanes().get(0);
			System.out.println(testLane);
			HashMap<Point2D.Float, ConnectionPoint> cpMap = testLane
					.getConnectionPoints();
			Iterator<Entry<Point2D.Float, ConnectionPoint>> it = cpMap
					.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry) it.next();
				System.out.println(pair.getKey() + " = " + pair.getValue());
				ConnectionPoint testCp = (ConnectionPoint) pair.getValue();
				HashMap<Lane, Connection> connections = testCp.getConnections();
				Iterator<Entry<Lane, Connection>> it1 = connections.entrySet()
						.iterator();
				while (it1.hasNext()) {
					System.out.println("printing connection");
					Map.Entry pair1 = (Map.Entry) it1.next();
					Connection c1 = (Connection) pair1.getValue();
					System.out.println("Next position test:"+ c1.nextPosition(car, 0.02f, 0.34f));
					System.out.println(c1.getStart());
					System.out.println(c1.getIntersection());
					System.out.println(c1.getEnd());
				}
				
			}
		} catch (UnknownConnectionError e) {
			// TODO Auto-generated catch block
			System.out.println("error");
		}
		Car c1 = new Car();
		c1.setCurrentSpeed(10);
		c1.enterLane(sr2.getLanes().get(0), sr2.getLanes().get(0).getStart());
		while (true) {
			c1.move();
			Thread.sleep(20);
		}

	}
}
