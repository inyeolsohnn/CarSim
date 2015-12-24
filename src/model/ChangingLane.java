package model;

import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;

public class ChangingLane extends Lane {
	// special lane that will be created dynamically when ever a car changes
	//NOT IN USE IN CURRENT VERSION OF THE SYSTEM
	private Lane startLane;
	private Lane targetLane;
	private Point2D.Float startPoint, endPoint;

	public ChangingLane(Lane sl, Lane tl, Road road, Point2D.Float sp) {
		super();
		this.contained=road;
		this.startLane = sl;
		this.targetLane = tl;
		this.startPoint = sp;
		this.endPoint = setUpChangingLane(sl, tl, sp);
	}

	private Float setUpChangingLane(Lane sl, Lane tl, Float sp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float calculateLaneSpan() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Float nextPosition(Car car, float targetDistance,
			float distanceTravelled) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Point2D.Float getStart() {
		return this.startPoint;
	}

	@Override
	public Point2D.Float getEnd() {
		return this.endPoint;

	}

	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub

	}

	@Override
	public Road getRoad() {
		// TODO Auto-generated method stub
		return this.contained;
	}

	@Override
	public float findDistance(Car car) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Car getFrontCar(Car car) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TrafficLight getNextTrafficLight(Car car) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void paintBorders(Graphics g) {
		// TODO Auto-generated method stub
		
	}

}
