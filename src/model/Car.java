package model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D; //test//
import java.util.ArrayList;
import java.util.Random;

public class Car {
	private static int carsCreated = 0;

	private Point2D.Float coordinate;
	private final int id;
	private float desiredSpeed;
	private float currentSpeed;
	private Random rng = new Random();
	private Road currentRoad;
	private Lane currentLane;
	/*
	 * targetLane: not used to its full purpose in current version. Was supposed
	 * to be part of inner road lane change
	 */
	private Lane targetLane;
	/*
	 * target connection: connection it aims to make a turn at. Can be null if
	 * the car decides to reach an exit point
	 */
	private Connection targetConnection;
	private CarWorld cWorld;

	/*
	 * distanceTravelled: distance travelled on the current road, required for
	 * perception sequence
	 */
	private float distanceTravelled;

	public Car(Point2D.Float coordinate, float ds, Lane initialLane,
			CarWorld cWorld, Point2D.Float entryPoint) {
		this.id = Car.carsCreated;
		Car.carsCreated++;
		this.coordinate = coordinate;
		this.desiredSpeed = ds;
		this.enterLane(initialLane, entryPoint);
		this.cWorld = cWorld;

	}

	public static int totalCar() {
		return Car.carsCreated;
	}

	public Road getRoad() {
		return this.currentRoad;
	}

	public float getTravelled() {

		return this.distanceTravelled;
	}

	public static void setCarsCreated(int i) {

		carsCreated = i;

	}

	/*
	 * checkCourse: not used to its full purpose in current version. Was
	 * supposed to be part of inner road lane change. Currently being used as a
	 * part of speed decision when making a turn
	 */
	private boolean checkCourse() {

		return (this.currentLane.equals(this.targetLane));
	}

	public int getId() {
		return this.id;
	}

	public void setTravelled(float f) {
		this.distanceTravelled = f;
	}

	public Point2D.Float getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(Point2D.Float coordinate) {
		this.coordinate = coordinate;
	}

	public float getDesiredSpeed() {
		return desiredSpeed;
	}

	public void setDesiredSpeed(float ds) {
		this.desiredSpeed = ds;
	}

	public float getCurrentSpeed() {
		return currentSpeed;
	}

	public void setCurrentSpeed(float currentSpeed) {
		this.currentSpeed = currentSpeed;
	}

	public Lane getCurrentLane() {
		return currentLane;
	}

	public void setCurrentLane(Lane currentLane) {
		this.currentLane = currentLane;
	}

	/*
	 * enterLane: lane entering logic. If the road of the lanes change,
	 * enterRoad will be called as well
	 */
	public void enterLane(Lane lane, Point2D.Float entryPoint) {
		if (this.currentLane != null) {
			this.currentLane.carLeaves(this);
		}
		if (lane instanceof StraightLane) {
			// entering a straightlane
			this.setTravelled(lane.findDistance(this));
		} else if (lane instanceof Connection) {
			// entering a connection
			this.setTravelled(1);
			this.targetConnection = null;

		}
		this.coordinate = entryPoint;

		Road tRoad = lane.getRoad();
		Lane previousLane = this.currentLane;
		Road previousRoad;
		if (previousLane == null) {
			previousRoad = null;
		} else {
			previousRoad = previousLane.getRoad();
		}
		this.currentLane = lane;
		lane.carEnters(this);
		if (!tRoad.equals(previousRoad)) {
			System.out.println("Entering road");
			enterRoad(tRoad);
		}

	}

	private void enterRoad(Road tRoad) {

		/* gets the whole connections available on */
		ArrayList<Connection> connections = currentLane.getSameConnections();
		/* sanitizes the list to have connections that are ahead of the cars */
		ArrayList<Connection> legalConnections = new ArrayList<Connection>();
		for (int i = 0; i < connections.size(); i++) {
			Connection cc = connections.get(i);
			if (Car.distance(this.currentLane.getStart(), cc.getStart()) >= this.distanceTravelled) {
				legalConnections.add(cc);
			}
		}
		boolean ending = currentLane.isEnding();
		/* dummy: Connection representing an exit point */
		Connection dummy = new Connection();
		if (ending) {
			legalConnections.add(dummy);
		}

		int random = rng.nextInt(legalConnections.size());
		Connection chosen = legalConnections.get(random);

		/*
		 * if the dummy connection is randomly chosen it will aim to reach the
		 * exit point of the car. Other wise appropriate state changes occur for
		 * making turns
		 */
		if (chosen.equals(dummy)) {
			ArrayList<Lane> sameLanes = currentLane.getSameLanes();
			int nr = rng.nextInt(sameLanes.size());
			Lane chosenLane = sameLanes.get(nr);
			this.targetLane = chosenLane;
			this.targetConnection = null;
		} else {

			this.targetLane = chosen.getStartLane();
			if (Car.distance(this.currentLane.getStart(), chosen.getStart()) >= this.distanceTravelled) {
				this.targetConnection = chosen;
			} else {
				this.targetConnection = null;
			}
		}
		this.currentRoad = tRoad;
	}

	/* move: decision making as well as updating its coordinate */
	public void move() {

		// perception sequence: START

		Car frontCar = this.currentLane.getFrontCar(this);
		TrafficLight tfl = this.currentLane.getNextTrafficLight(this);
		float cd = 100;
		float td = 100;
		if (frontCar != null) {
			cd = Car.distance(frontCar.getCoordinate(), this.getCoordinate());
		}
		if (tfl != null) {
			td = Car.distance(tfl.getCoordinate(), this.getCoordinate());
		}
		// current---car---trafficlight(red/green)----100m :: react to the car
		// current----trafficlight(green)---car--100m :: react to the car
		// current---trafficlight(red)----car ---100m :: react to the light
		// current -----100m----x-----y :: free run
		if (cd >= 100 && td >= 100) {
			// free run
			accelerate();
		} else if (td < cd && td < 100 && tfl.getStatus().equals("red")) {
			// react to the red light
			if (td < 15)
				this.setCurrentSpeed(0);
			else if (td < 70)
				this.setCurrentSpeed(td);
			else if (td < 100) {
				if (this.currentSpeed > td) {
					decelerate();
				}
			}

		} else if ((td < cd && td < 100 && tfl.getStatus().equals("green"))) {
			// react to the green light-> ignore the light and react to the
			// closest car or slow down if making turn
			if (this.targetConnection != null
					&& checkCourse()
					&& Car.distance(this.coordinate,
							this.targetConnection.getStart()) < 100) {
				if (td < 70)
					if (currentSpeed < 60)
						this.accelerate();
					else if (currentSpeed > 80) {
						this.decelerate();
					}
			} else {
				if (cd < 15) {
					this.setCurrentSpeed(0);
				} else if (cd < 70) {
					this.setCurrentSpeed(cd);
				} else if (cd < 100) {
					if (this.currentSpeed > cd) {
						decelerate();
					}
				} else {
					accelerate();
				}
			}
		} else if ((cd < td && cd < 100)) {
			// react to the car
			if (cd < 15) {
				this.setCurrentSpeed(0);
			} else if (cd < 70) {
				this.setCurrentSpeed(cd);
			} else if (cd < 100) {
				if (this.currentSpeed > cd)
					decelerate();
			}
		}
		// perception sequence: END

		// action sequence: START
		if (this.currentSpeed == 0) {
			if (frontCar != null) {
				System.out.println("front car : " + frontCar.coordinate);
			}
		} else {

			float tempDistance = this.currentSpeed * 0.02f;

			Point2D.Float nextPosition = this.currentLane.nextPosition(this,
					tempDistance, this.distanceTravelled);

			// state change sequence: Additional perception and change in
			// states: START
			Point2D.Float nextDisplacement = new Point2D.Float(
					Math.abs(nextPosition.x - this.coordinate.x),
					Math.abs(nextPosition.y - this.coordinate.y));

			Point2D.Float dToEnd = new Point2D.Float(Math.abs(currentLane
					.getEnd().x - this.getCoordinate().x), Math.abs(currentLane
					.getEnd().y - this.getCoordinate().y));

			if ((this.currentLane instanceof StraightLane)
					&& (dToEnd.x < nextDisplacement.x || dToEnd.y < nextDisplacement.y)) {
				// reached an exit point
				this.coordinate = this.currentLane.getEnd();
				this.remove();
			} else if ((this.currentLane instanceof Connection)) {
				Point2D.Float lastPoint = this.currentLane.getEnd();
				if (Car.distance(this.coordinate, lastPoint) < 5) {
					this.enterLane(
							((Connection) this.currentLane).getTargetLane(),
							((Connection) this.currentLane).getEnd());
				}
			}
			this.coordinate = nextPosition;

			if (this.targetConnection != null) {

				if (Car.distance(this.currentLane.getStart(),
						targetConnection.getStart()) <= this.distanceTravelled
						&& checkCourse()) {

					this.enterLane(targetConnection,
							targetConnection.getStart());
				}

			}

		}
		// State change sequence: END
		// action sequence: END
	}

	private void decelerate() {
		// TODO Auto-generated method stub
		this.currentSpeed -= 500 * 0.02;
	}

	private void accelerate() {
		// TODO Auto-generated method stub
		if (this.currentSpeed < this.desiredSpeed) {
			this.currentSpeed += 30 * 0.02;
		}
	}

	public void remove() {
		// TODO Auto-generated method stub
		this.cWorld.removeCar(this);
		this.currentLane.removeCar(this);
		System.gc();

	}

	public void paint(Graphics g) {
		if (this.currentSpeed >= 160) {
			g.setColor(Color.RED);
		} else if (this.currentSpeed < 160 && this.currentSpeed >= 120) {
			g.setColor(Color.ORANGE);
		} else if (this.currentSpeed < 120 && this.currentSpeed >= 80) {
			g.setColor(Color.YELLOW);
		} else if (this.currentSpeed < 80 && this.currentSpeed >= 40) {
			g.setColor(Color.MAGENTA);
		} else if (this.currentSpeed < 40 && this.currentSpeed > 0) {
			g.setColor(Color.BLUE);
		} else if (this.currentSpeed == 0) {
			g.setColor(Color.BLACK);
		}
		g.fillOval((int) coordinate.x - 4, (int) coordinate.y - 4, 8, 8);

	}

	public static float distance(Point2D.Float p1, Point2D.Float p2) {
		return (float) Math.sqrt(Math.pow((p1.x - p2.x), 2.0)
				+ Math.pow(p1.y - p2.y, 2.0));
	}

}
