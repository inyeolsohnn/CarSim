package model;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class CarWorld {
	private String status = "paused";
	private int height;
	private int width;
	private ArrayList<Road> roads = new ArrayList<Road>();
	private HashMap<Integer, Car> cars = new HashMap<Integer, Car>();
	private ArrayList<TrafficLight> lights = new ArrayList<TrafficLight>();
	private ArrayList<CarPark> parks = new ArrayList<CarPark>();
	private QuadTree quad;

	public CarWorld() {
		this.height = 1000;
		this.width = 800;
		quad = new QuadTree(0, new Rectangle(this.width, this.height));
	}

	public CarWorld(int height, int width) {
		this.height = height;
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public void addRoad(Road road) {
		roads.add(road);
	}

	public void addCar(Car car) {
		cars.put(car.getId(), car);
	}

	public void removeRoad(String roadId) {
		roads.remove(roadId);
	}

	public String toString() {
		return "Number of roads: " + roads.size() + "Number of cars"
				+ cars.size();
	}

	public void setStatus(String s) {
		this.status = s;

	}

	public String getStatus() {
		return this.status;
	}

	public HashMap<Integer, Car> getCars() {

		return cars;
	}

	public void removeCar(Car car) {
		cars.remove(car.getId());
	}

	public Road getRoad(int roadId) {
		Road tRoad = null;
		for (int i = 0; i < roads.size(); i++) {
			Road cRoad = roads.get(i);
			if (cRoad.getId() == roadId) {
				tRoad = cRoad;
				break;
			}
		}
		return tRoad;
	}

	public ArrayList<Road> getRoads() {

		return roads;
	}

	public ArrayList<TrafficLight> getLights() {

		return lights;
	}

	// resetting the state of the world for a new road network
	public void flush() {
		TrafficLight.setTotalLights(0);
		Car.setCarsCreated(0);
		CarPark.setParksCreated(0);
		roads = new ArrayList<Road>();
		cars = new HashMap<Integer, Car>();
		parks = new ArrayList<CarPark>();
		this.lights = new ArrayList<TrafficLight>();

	}

	public void addPark(CarPark cp) {
		System.out.println("adding car park");
		parks.add(cp);
	}

	public ArrayList<CarPark> getParks() {
		// TODO Auto-generated method stub
		return this.parks;
	}

	public void addLight(TrafficLight light) {
		// TODO Auto-generated method stub
		this.lights.add(light);

	}

	public ArrayList<Car> checkCollision() {
		quad.clear();
		ArrayList<Car> collided = new ArrayList<Car>();
		Iterator<Entry<Integer, Car>> carIt = cars.entrySet().iterator();
		while (carIt.hasNext()) {
			quad.insert(carIt.next().getValue());
		}
		ArrayList<Car> returnObjects = new ArrayList<Car>();
		carIt = cars.entrySet().iterator();

		while (carIt.hasNext()) {
			returnObjects.clear();
			Car currentCar = carIt.next().getValue();
			quad.retrieve(returnObjects, currentCar);
			for (int i = 0; i < returnObjects.size(); i++) {
				if ((!returnObjects.get(i).equals(currentCar))
						&& Car.distance(returnObjects.get(i).getCoordinate(),
								currentCar.getCoordinate()) < 7) {
					this.status = "paused";
					collided.add(currentCar);
					collided.add(returnObjects.get(i));
					return collided;

				}
			}
		}
		return collided;
	}

	public void reset() {
		// returning to the initial state of the current simulation

		for (int i = 0; i < roads.size(); i++) {

			Road cr = roads.get(i);

			Iterator<Entry<Integer, Lane>> lIt = cr.getLanes().entrySet()
					.iterator();
			while (lIt.hasNext()) {
				Lane currentLane = lIt.next().getValue();
				currentLane.getCarsInLane().clear();
				Iterator<Entry<Point2D.Float, ConnectionPoint>> cpIt = currentLane
						.getConnectionPoints().entrySet().iterator();
				while (cpIt.hasNext()) {
					Iterator<Entry<Lane, Connection>> conIt = cpIt.next()
							.getValue().getConnections().entrySet().iterator();
					while (conIt.hasNext()) {
						conIt.next().getValue().getCarsInLane().clear();
					}
				}
			}
		}
		this.cars.clear();
		for (int i = 0; i < lights.size(); i++) {
			lights.get(i).reset();
		}
		Car.setCarsCreated(0);
		for (int i = 0; i < parks.size(); i++) {
			parks.get(i).reset();
		}

	}
	
	public ArrayList<Lane> getLanes() {
		ArrayList<Lane> laneReturn = new ArrayList<Lane>();
		for (int i = 0; i < this.roads.size(); i++) {
			Road cr = roads.get(i);
			Iterator<Entry<Integer, Lane>> lIt = cr.getLanes().entrySet()
					.iterator();
			while (lIt.hasNext()) {
				laneReturn.add(lIt.next().getValue());
			}
		}
		return laneReturn;
	}

	public void addNewLight(Integer selectedLane, Point point) {

		Lane sl = null;
		for (int i = 0; i < this.getLanes().size(); i++) {
			if (this.getLanes().get(i).getLaneId() == selectedLane) {
				sl = this.getLanes().get(i);
				break;
			}
		}
		Point2D.Float closest = getClosestPointOnSegment(sl.getStart(),
				sl.getEnd(), point);
		TrafficLight tl = new TrafficLight(sl, "green", 5f, 5f, 1f, closest);
		sl.addTrafficLight(tl);

	}

	public static Point2D.Float getClosestPointOnSegment(Point2D.Float ss,
			Point2D.Float se, Point p) {
		return getClosestPointOnSegment(ss.x, ss.y, se.x, se.y, p.x, p.y);
	}

	/**
	 * used to find coordinate for the new traffic light From
	 * http://www.java2s.com
	 * /Code/Java/2D-Graphics-GUI/Returnsclosestpointonsegmenttopoint.htm
	 **/
	public static Point2D.Float getClosestPointOnSegment(float sx1, float sy1,
			float sx2, float sy2, int px, int py) {
		double xDelta = sx2 - sx1;
		double yDelta = sy2 - sy1;

		if ((xDelta == 0) && (yDelta == 0)) {
			throw new IllegalArgumentException(
					"Segment start equals segment end");
		}

		double u = ((px - sx1) * xDelta + (py - sy1) * yDelta)
				/ (xDelta * xDelta + yDelta * yDelta);

		final Point2D.Float closestPoint;
		if (u < 0) {
			closestPoint = new Point2D.Float(sx1, sy1);
		} else if (u > 1) {
			closestPoint = new Point2D.Float(sx2, sy2);
		} else {
			closestPoint = new Point2D.Float((float) Math.round(sx1 + u
					* xDelta), (float) Math.round(sy1 + u * yDelta));
		}

		return closestPoint;
	}

}
