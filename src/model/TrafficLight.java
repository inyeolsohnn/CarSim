package model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.Date;

public class TrafficLight {
	private Lane lane; // lane object it belongs to
	private String status;
	private boolean initial;
	private float greenInterval;
	private float redInterval;
	private float initInterval;
	private Date lastChanged;
	private Point2D.Float coordination;
	private int id;
	private static int totalLights = 0;
	private double tempInterval = 0.0;

	// testing stubs
	public TrafficLight() {
		this.greenInterval = 5;

		this.redInterval = 5;
		this.lastChanged = new Date();
		System.out.println(this.lastChanged);
		this.status = "Green";
		this.id = totalLights;
		totalLights++;
	}

	public TrafficLight(Lane lane) {
		this.lane = lane;

		this.lastChanged = new Date();
		this.status = "Green";
		this.id = totalLights;
		totalLights++;
	}

	// testing stubs end

	public TrafficLight(Lane lane, String status, float greenInterval,
			float redInterval, float initInterval, Point2D.Float coordination) {
		this.lane = lane;
		System.out.println("traffic coord" + coordination);
		initial = true;
		if (initInterval == 0) {
			initial = false;
		}
		this.initInterval = initInterval;
		this.redInterval = redInterval;
		this.greenInterval = greenInterval;
		this.lastChanged = new Date();
		this.status = status;
		this.coordination = coordination;
		this.id = totalLights + 1;
		totalLights++;
	}

	public void setCoordinate(Point2D.Float coordination) {
		this.coordination = coordination;
	}

	public Point2D.Float getCoordinate() {
		return this.coordination;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setRedInterval(float interval) {
		this.redInterval = interval;
	}

	public Lane getLane() {
		return this.lane;
	}

	public void paint(Graphics g) {

		if (status.equalsIgnoreCase("green")) {

			g.setColor(Color.GREEN);

		} else if (status.equalsIgnoreCase("red")) {
			g.setColor(Color.RED);
		}
		g.fillOval(
				(int) (coordination.x - Math.sqrt(2 * (Math.pow(7.5 / 2, 2)))),
				(int) (coordination.y - Math.sqrt(2 * (Math.pow(7.5 / 2, 2)))),
				15, 15);
		g.setColor(Color.BLACK);

	}

	public void update() {
		/*
		 * Date currentDate = new Date(); if (currentDate.getTime() -
		 * lastChanged.getTime() > interval) { System.out.println("changing");
		 * System.out.println(currentDate.getTime() - lastChanged.getTime());
		 * lastChanged = currentDate; if (status.equals("Green")) { this.status
		 * = "Red"; } else if (status.equals("Red")) { this.status = "Green"; }
		 * System.out.println(this.status); }
		 */

		if (initial) {
			// stay red till initial interval
			// past initial interval make initial false
			this.status = "red";
			tempInterval += 0.02;
			if (tempInterval >= initInterval) {
				this.initial = false;
				this.status = "green";
			}
		} else {
			if (this.status.equalsIgnoreCase("green")) {

				if (tempInterval >= greenInterval + initInterval) {
					if (redInterval != 0)
						this.status = "red";
					tempInterval = initInterval;
				} else {
					tempInterval += 0.02;
				}
			} else {
				if (tempInterval >= redInterval + initInterval) {
					if (greenInterval != 0)
						this.status = "green";
					tempInterval = initInterval;
				} else {
					tempInterval += 0.02;
				}
			}

		}

		// System.out.println(getStatus());
	}

	public int getId() {
		return this.id;
	}

	public float getGreen() {
		return this.greenInterval;
	}

	public float getRed() {
		return this.redInterval;
	}

	public float getInit() {
		return this.initInterval;
	}

	public void setGreen(float f) {
		this.greenInterval = f;
	}

	public void setInit(float f) {
		this.initInterval = f;
	}

	public void reset() {
		this.tempInterval = 0d;
		this.status = "green";
		if (initInterval != 0)
			this.initial = true;
		else if (initInterval == 0)
			this.initial = false;
	}

	public static void setTotalLights(int i) {
		// TODO Auto-generated method stub
		totalLights = i;
	}

}
