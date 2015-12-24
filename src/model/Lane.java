package model;

import java.awt.Graphics;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JOptionPane;

//abstract class that all types of lanes extends
public abstract class Lane {

	private static final float SMALL_NUM = (float) 0.00000001;
	private int laneId; // its id
	private int laneKey;
	protected Road contained;
	protected float laneSpan;
	private HashMap<Point2D.Float, ConnectionPoint> connectionPoints = new HashMap<Point2D.Float, ConnectionPoint>();
	private HashMap<Integer, Car> carsInLane = new HashMap<Integer, Car>();
	private boolean hasPark = false;
	private CarWorld world;
	protected Point2D.Float startPoint;
	protected Point2D.Float endPoint;
	private boolean isEnding = false;
	private static int lanesCreated = 0;

	// store traffic lights that belong to this lane.
	ArrayList<TrafficLight> trafficLights = new ArrayList<TrafficLight>();

	public Lane() {
		// test stub
	}

	public Lane(Point2D.Float start, Point2D.Float end, Road cRoad,
			CarWorld cWorld, int lk) {
		this.startPoint = start;
		this.endPoint = end;
		this.contained = cRoad;
		this.laneId = lanesCreated;
		this.world = cWorld;
		lanesCreated++;
		this.laneKey = lk;

	}

	public abstract float calculateLaneSpan();

	public abstract Point2D.Float nextPosition(Car car, float targetDistance,
			float distanceTravelled);

	public float getLaneSpan() {
		return laneSpan;
	}

	public int getLaneId() {
		return this.laneId;
	}

	public abstract Road getRoad();

	public final void setLaneSpan(float laneSpan) {
		this.laneSpan = laneSpan;
	}

	// in case of roundabout this will return center point
	public Point2D.Float getStart() {
		return this.startPoint;
	}

	// in case of roundabout this will return center point
	public Point2D.Float getEnd() {

		return this.endPoint;
	}

	public void addTrafficLight(TrafficLight light) {

		ArrayList<TrafficLight> lights = light.getLane().trafficLights;
		for (int i = 0; i < lights.size(); i++) {
			TrafficLight currentLight = lights.get(i);
			if (Car.distance(currentLight.getCoordinate(),
					light.getCoordinate()) < 20) {
				JOptionPane.showMessageDialog(null,
						"the light is too close to other lights in lane");
				return;
			}
		}
		this.trafficLights.add(light);
		this.world.addLight(light);

	}

	public void removeTrafficLight(int lid) {

		for (int i = 0; i < trafficLights.size(); i++) {
			if (trafficLights.get(i).getId() == lid) {

				for (int j = 0; j < this.world.getLights().size(); j++) {
					TrafficLight jr = this.world.getLights().get(j);
					if (jr.getId() == lid) {
						this.world.getLights().remove(j);
						break;
					}
				}
				trafficLights.remove(i);
				break;

			}
		}
	}

	public abstract TrafficLight getNextTrafficLight(Car car);

	public float getSpan() {
		// TODO Auto-generated method stub
		return laneSpan;
	}

	public void addCar(Car car) {
		int carId = car.getId();
		carsInLane.put(carId, car);
	}

	public void removeCar(Car car) {
		int carId = car.getId();
		carsInLane.remove(carId);
	}

	public boolean addConnectionPoint(ConnectionPoint cp, Connection cn)
			throws UnknownConnectionError {

		if (connectionPoints.get(new Point2D.Float((int) cp
				.getPointCoordinate().x, (int) cp.getPointCoordinate().y)) == null) {

			System.out
					.println("Connection point not found, adding new connection point");
			cp.addConnection(cn);
			connectionPoints.put(
					new Point2D.Float(((int) cp.getPointCoordinate().x),
							((int) cp.getPointCoordinate().y)), cp);
			TrafficLight tl = new TrafficLight(cp.getLane(), "green", 5f, 5f,
					1f, cp.getPointCoordinate());
			cp.getLane().addTrafficLight(tl);

			return true;
		} else {
			System.out
					.println("connection point found, adding new connection to the existing connection point");
			ConnectionPoint existingCp = connectionPoints
					.get(new Point2D.Float((int) cp.getPointCoordinate().x,
							(int) cp.getPointCoordinate().y));
			existingCp.addConnection(cn);
			return true;
		}
	}

	public HashMap<Point2D.Float, ConnectionPoint> getConnectionPoints() {
		return this.connectionPoints;
	}

	public abstract void paint(Graphics g);

	public void paintTrafficLights(Graphics g) {
		for (int i = 0; i < this.trafficLights.size(); i++) {
			trafficLights.get(i).paint(g);
		}
	}

	public void carEnters(Car car) {
		// TODO Auto-generated method stub
		int carId = car.getId();
		carsInLane.put(carId, car);
	}

	public void carLeaves(Car car) {
		carsInLane.remove(car.getId());
	}

	public abstract Car getFrontCar(Car car);

	public int getLaneKey() {
		return this.laneKey;
	}

	// returns all the connections in the same direction lane
	public ArrayList<Connection> getSameConnections() {

		ArrayList<Connection> cal = new ArrayList<Connection>();
		Iterator<Entry<Integer, Lane>> lit = this.contained.getLanes()
				.entrySet().iterator();
		while (lit.hasNext()) {
			Map.Entry<Integer, Lane> lp = lit.next();
			Lane currentLane = lp.getValue();
			if (currentLane.laneKey % 2 == this.laneKey % 2) {
				Iterator<Entry<Point2D.Float, ConnectionPoint>> cit = currentLane
						.getConnectionPoints().entrySet().iterator();
				while (cit.hasNext()) {
					Map.Entry<Point2D.Float, ConnectionPoint> cp = cit.next();
					ConnectionPoint currentPoint = cp.getValue();
					Iterator<Entry<Lane, Connection>> conIt = currentPoint
							.getConnections().entrySet().iterator();
					while (conIt.hasNext()) {
						Map.Entry<Lane, Connection> conP = conIt.next();
						Connection currentConnection = conP.getValue();
						cal.add(currentConnection);
					}
				}
			}
		}
		return cal;

	}

	public void setHasPark(boolean b) {
		this.hasPark = b;
	}

	public boolean getHasPark() {
		return this.hasPark;
	}

	public abstract float findDistance(Car car);

	public void setEnding(boolean b) {
		this.isEnding = b;
	}

	public boolean isEnding() {
		return this.isEnding;
	}

	// return the list of the lanes of the same direction that are in the same
	// road
	public ArrayList<Lane> getSameLanes() {
		int currentKey = this.getLaneKey();
		ArrayList<Lane> sal = new ArrayList<Lane>();
		Iterator<Entry<Integer, Lane>> lit = this.contained.getLanes()
				.entrySet().iterator();
		while (lit.hasNext()) {
			Map.Entry<Integer, Lane> lp = lit.next();
			Lane currentLane = lp.getValue();
			if (currentKey % 2 == this.laneKey % 2) {
				sal.add(currentLane);
			}
		}
		return sal;
	}

	public abstract void paintBorders(Graphics g);

	public HashMap<Integer, Car> getCarsInLane() {
		// TODO Auto-generated method stub
		return this.carsInLane;
	}
}
