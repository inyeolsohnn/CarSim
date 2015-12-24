package model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class StraightLane extends Lane {

	private static final int[][] perpenMat = new int[][] { { 0, -1 }, { 1, 0 } };

	public StraightLane(Point2D.Float startingPoint, Point2D.Float endPoint,
			Road cRoad, CarWorld cWorld, int lk) {
		super(startingPoint, endPoint, cRoad, cWorld, lk);
		this.setLaneSpan(calculateLaneSpan());
	}

	@Override
	public float calculateLaneSpan() {
		return (float) Math.sqrt(Math.pow(
				(this.getEnd().x - this.getStart().x), 2.0)
				+ Math.pow(this.getEnd().y - this.getStart().y, 2.0));
	}

	@Override
	public Point2D.Float nextPosition(Car car, float targetDistance,
			float distanceTravelled) {

		// update rate at 20ms
		float desiredDistance = targetDistance;

		Point2D.Float start = this.getStart();
		Point2D.Float end = this.getEnd();
		float laneSpan = calculateLaneSpan();
		Point2D.Float displacement = new Point2D.Float((end.x - start.x)
				/ laneSpan * desiredDistance, (end.y - start.y) / laneSpan
				* desiredDistance);

		Point2D.Float newPoint = new Point2D.Float(car.getCoordinate().x
				+ displacement.x, car.getCoordinate().y + displacement.y);

		car.setTravelled(car.getTravelled() + targetDistance);
		return newPoint;
	}

	@Override
	public void paint(Graphics g) {
		Point2D.Float start = this.getStart();
		Point2D.Float end = this.getEnd();
		Point2D.Float halfVector = gethalf(start, end);
		Point2D.Float p1 = new Point2D.Float(end.x - halfVector.x, end.y
				- halfVector.y);
		Point2D.Float p2 = new Point2D.Float(start.x - halfVector.x, start.y
				- halfVector.y);

		Point2D.Float p3 = new Point2D.Float(start.x + halfVector.x, start.y
				+ halfVector.y);
		Point2D.Float p4 = new Point2D.Float(end.x + halfVector.x, end.y
				+ halfVector.y);
		int xpoints[] = { (int) p1.x, (int) p2.x, (int) p3.x, (int) p4.x };
		int ypoints[] = { (int) p1.y, (int) p2.y, (int) p3.y, (int) p4.y };
		Graphics2D g2D = (Graphics2D) g;
		RenderingHints qualityHints = new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		qualityHints.put(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		g2D.setRenderingHints(qualityHints);
		g.setColor(new Color(160, 160, 160));
		g.fillPolygon(xpoints, ypoints, 4);

	}

	private Float gethalf(Float start, Float end) {
		Point2D.Float vector = new Point2D.Float(end.x - start.x, end.y
				- start.y);

		float vectorLength = (float) Math.sqrt(Math.pow(vector.x, 2.0)
				+ Math.pow(vector.y, 2.0));

		Point2D.Float normalVector = new Point2D.Float(vector.x / vectorLength,
				vector.y / vectorLength);
		Point2D.Float perpenVector = new Point2D.Float(normalVector.x
				* perpenMat[0][0] + normalVector.y * perpenMat[1][0],
				normalVector.x * perpenMat[0][1] + normalVector.y
						* perpenMat[1][1]);
		// System.out.println("Perpen vector: " + perpenVector);
		Point2D.Float halfScaled = new Point2D.Float(perpenVector.x
				* Road.roadWidth / 2, perpenVector.y * Road.roadWidth / 2);
		return halfScaled;

	}

	@Override
	public Road getRoad() {
		// TODO Auto-generated method stub
		return this.contained;
	}

	@Override
	public float findDistance(Car car) {
		// TODO Auto-generated method stub

		return Car.distance(this.getStart(), car.getCoordinate());
	}

	@Override
	public Car getFrontCar(Car car) {
		// also need to see cars infront of it in the nearest connections
		// starting from its lane
		float closest = 100f;
		Car closestCar = null;

		float cClosest = 100f;
		ConnectionPoint closestPoint = null;
		// find closest connection point to the car that is <100f distance

		Iterator<Entry<Point2D.Float, ConnectionPoint>> cpIt = this
				.getConnectionPoints().entrySet().iterator();
		while (cpIt.hasNext()) {
			ConnectionPoint currentPoint = cpIt.next().getValue();
			if (Car.distance(currentPoint.getPointCoordinate(),
					car.getCoordinate()) < cClosest) {
				closestPoint = currentPoint;
				cClosest = Car.distance(currentPoint.getPointCoordinate(),
						car.getCoordinate());
			}
		}
		float dtp = -1f;
		if (closestPoint != null) {
			dtp = Car.distance(closestPoint.getPointCoordinate(),
					this.getStart());

			Iterator<Entry<Lane, Connection>> connectionIt = closestPoint
					.getConnections().entrySet().iterator();
			while (connectionIt.hasNext()) {
				Iterator<Entry<Integer, Car>> connectionCarsIt = connectionIt
						.next().getValue().getCarsInLane().entrySet()
						.iterator();
				while (connectionCarsIt.hasNext()) {
					Car connectionCar = connectionCarsIt.next().getValue();
					if ((connectionCar.getTravelled() + dtp) > car
							.getTravelled()) {
						if (Car.distance(connectionCar.getCoordinate(),
								car.getCoordinate()) < closest
								&& !car.equals(connectionCar)) {
							closest = Car.distance(
									connectionCar.getCoordinate(),
									car.getCoordinate());
							closestCar = connectionCar;
						}
					}
				}
			}
		}

		// retrieve cars in the connections that are starting from the closest
		// connection point

		// get cars in the connections that belongs to the closest connection
		// point
		Iterator<Entry<Integer, Car>> cit = this.getCarsInLane().entrySet()
				.iterator();

		while (cit.hasNext()) {
			Map.Entry<Integer, Car> cPair = cit.next();
			Car cCar = cPair.getValue();

			if (cCar.getTravelled() > car.getTravelled()) {
				// cCar is somewhere ahead of the car in the same lane
				if (((cCar.getTravelled() - car.getTravelled()) < closest)
						&& !car.equals(cCar)) {
					closest = cCar.getTravelled() - car.getTravelled();
					closestCar = cCar;
				}
			}
		}
		// if closest car is null it means the parameter car is the leading
		// car
		return closestCar;
	}

	@Override
	public TrafficLight getNextTrafficLight(Car car) {
		// TODO Auto-generated method stub
		float closest = 100;
		TrafficLight ctl = null;

		for (int i = 0; i < this.trafficLights.size(); i++) {
			TrafficLight currentLight = trafficLights.get(i);
			float td = Car.distance(currentLight.getCoordinate(),
					car.getCoordinate());

			if (td < closest
					&& Car.distance(car.getCurrentLane().getStart(),
							currentLight.getCoordinate()) > car.getTravelled()) {

				closest = td;
				ctl = currentLight;

			}
		}

		return ctl;
	}

	@Override
	public void paintBorders(Graphics g) {
		// TODO Auto-generated method stub
		Point2D.Float start = this.getStart();
		Point2D.Float end = this.getEnd();
		Point2D.Float halfVector = gethalf(start, end);
		Point2D.Float p1 = new Point2D.Float(end.x - halfVector.x, end.y
				- halfVector.y);
		Point2D.Float p2 = new Point2D.Float(start.x - halfVector.x, start.y
				- halfVector.y);

		Point2D.Float p3 = new Point2D.Float(start.x + halfVector.x, start.y
				+ halfVector.y);
		Point2D.Float p4 = new Point2D.Float(end.x + halfVector.x, end.y
				+ halfVector.y);
		int xpoints[] = { (int) p1.x, (int) p2.x, (int) p3.x, (int) p4.x };
		int ypoints[] = { (int) p1.y, (int) p2.y, (int) p3.y, (int) p4.y };
		Graphics2D g2D = (Graphics2D) g;
		RenderingHints qualityHints = new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		qualityHints.put(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		g2D.setRenderingHints(qualityHints);
		g.setColor(Color.white);
		g.drawPolygon(xpoints, ypoints, 4);
	}
}
