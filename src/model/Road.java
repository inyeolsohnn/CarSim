package model;

import java.awt.Graphics;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public abstract class Road {
	// knows which lane is connecting to which road, pair of (lane number, and
	// the road it connects to).

	// some logical constrictions
	// base lane decides the type of the lanes that can be added

	private int roadId;
	private final int roadType;
	public final static int roadWidth = 20; // each lanes are 20 pixels wide
	private static int roadsCreated = 0;
	public static final int STRAIGHT_LANE = 0;
	public static final int ROUNDABOUT = 1;
	public static final int CURVE = 2;
	private HashMap<Integer, Lane> lanes = new HashMap<Integer, Lane>();
	private CarWorld world;

	// bilateral vs unilateral state
	// definition of connection: contiguous legal movements possible
	private boolean bilateral;

	// used for 90 degrees rotation
	private static final int[][] perpenMat = new int[][] { { 0, -1 }, { 1, 0 } };

	public Road(int roadType, CarWorld cWorld) {
		this.roadId = roadsCreated;
		this.world = cWorld;
		roadsCreated++;
		this.roadType = roadType;
	}

	public void paint(Graphics g) {
		Iterator<Entry<Integer, Lane>> it = lanes.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			Lane currentLane = (Lane) pair.getValue();
			currentLane.paint(g);

		}

	}

	/**** road type dependent ****/

	// contaaining all lanes

	/***********************/

	/***** road type independent *******/
	public int getId() {
		return this.roadId;

	}

	public HashMap<Integer, Lane> getLanes() {
		return lanes;// should return an arraylist
	}

	public void update() {
		// update all lanes belonging to this road
	}

	public int getType() {
		return this.roadType;
	}

	public boolean getBilateral() {
		return this.bilateral;
	}

	protected void setBilateral(boolean bl) {
		this.bilateral = bl;
	}

	// currently only supports connections between straight lanes
	public static void connectLane(Road currentRoad, int currentLane,
			Road targetRoad, int targetLane) throws UnknownConnectionError {

		if ((currentRoad instanceof StraightRoad && targetRoad instanceof StraightRoad)) {
			lineLineConnect(currentRoad, currentLane, targetRoad, targetLane);

		} else if ((currentRoad instanceof StraightRoad && targetRoad instanceof RoundRoad)) {

		} else if (currentRoad instanceof RoundRoad
				&& targetRoad instanceof StraightRoad) {

		} else {
			throw new UnknownConnectionError();
		}
		// sets connection state between the lane corresponding to the
		// currentLane in currentRoad and the corresponding targetLane, in
		// targetRoad

	}

	// lane connection logic between straight lines
	private static boolean lineLineConnect(Road currentRoad, int currentLane,
			Road targetRoad, int targetLane) throws UnknownConnectionError {
		System.out.println("Start connecting");
		HashMap<Integer, Lane> sLanes = currentRoad.getLanes();
		HashMap<Integer, Lane> tLanes = targetRoad.getLanes();
		Point2D.Float intersectingStartPoint;
		Point2D.Float intersectingControlPoint;
		Point2D.Float intersectingEndPoint;
		boolean success = false;
		boolean overlapFound = false;
		// checks if the roads actually has the lanes that are needed to be
		// connected
		if (sLanes.containsKey(currentLane) && tLanes.containsKey(targetLane)) {
			System.out.println("Lanes in the roads found");
			// get two most outer roads-> get most outer boundaries of the two
			// roads
			// check if the closest outer boundary intersects with the
			// boundaries of the other road.
			// if it doesn't check if it works other way.
			// case where they are all even or all odd->
			// for one of the roads a1-a2, a3-a4
			Point2D.Float a1;
			Point2D.Float a2;
			Point2D.Float a3;
			Point2D.Float a4;

			// for another road b1-b2, b3-b4
			Point2D.Float b1;
			Point2D.Float b2;
			Point2D.Float b3;
			Point2D.Float b4;

			// for sLanes
			ArrayList<Integer> sortedS = new ArrayList<Integer>(sLanes.keySet());
			Collections.sort(sortedS);
			int highest = sortedS.get(sortedS.size() - 1);
			int modulus = highest % 2;
			int nextHighest = -1;

			// ////////
			// ////////
			// start if
			if (currentRoad.getBilateral()) {
				// bilateral get one lane with highest even key and one with
				// highest odd key

				// start checking for nextHighest
				System.out.println("starting road is bilateral");
				if (modulus == 0) {
					System.out.println("Starting highest==even");
					// highest is even-> next highest should be odd since
					// bilateral
					for (int i = sortedS.size() - 2; i >= 0; i--) {
						if (sortedS.get(i) % 2 == 1) {
							nextHighest = sortedS.get(i);
							break;
						}
					}
					// highest is even the outer line is another addition of the
					// half offset vector
					// nextHighest is odd the outer line is another subtraction
					// of the half offset vector

					Point2D.Float base1 = sLanes.get(highest).getStart();
					Point2D.Float base2 = sLanes.get(highest).getEnd();
					Point2D.Float halfVector = getHalf(base1, base2);

					Lane h = sLanes.get(highest);
					Lane nh = sLanes.get(nextHighest);
					a1 = new Point2D.Float(h.getStart().x + halfVector.x,
							h.getStart().y + halfVector.y);
					a2 = new Point2D.Float(h.getEnd().x + halfVector.x,
							h.getEnd().y + halfVector.y);
					a3 = new Point2D.Float(nh.getStart().x - halfVector.x,
							nh.getStart().y - halfVector.y);
					a4 = new Point2D.Float(nh.getEnd().x - halfVector.x,
							nh.getEnd().y - halfVector.y);

				} else {
					// highest is odd-> next highest should be even since
					// bilateral
					System.out.println("Starting highest== odd");
					for (int i = sortedS.size() - 2; i >= 0; i--) {
						if (sortedS.get(i) % 2 == 0) {
							nextHighest = sortedS.get(i);
							break;
						}
					}
					// highest is odd the outer line is another subtraction of
					// the half offset vector
					// nextHighest is even the outer line is another addition
					// of the half offset vector
					Point2D.Float base1 = sLanes.get(nextHighest).getStart();
					Point2D.Float base2 = sLanes.get(nextHighest).getEnd();
					Point2D.Float halfVector = getHalf(base1, base2);

					Lane h = sLanes.get(highest);
					Lane nh = sLanes.get(nextHighest);
					a1 = new Point2D.Float(h.getStart().x - halfVector.x,
							h.getStart().y - halfVector.y);
					a2 = new Point2D.Float(h.getEnd().x - halfVector.x,
							h.getEnd().y - halfVector.y);
					a3 = new Point2D.Float(nh.getStart().x + halfVector.x,
							nh.getStart().y + halfVector.y);
					a4 = new Point2D.Float(nh.getEnd().x + halfVector.x,
							nh.getEnd().y + halfVector.y);
				}
				// end of bilateral sLane.

			} else {
				// unilateral sLAne->get a lane with highest key and one with
				// the lowest key

				nextHighest = sortedS.get(0);
				if (nextHighest % 2 == 0) {
					System.out.println("Starting lane is even unilateral");
					// even lanes
					// one outer line is highest + offset
					// one outer line is nextHighest - offset
					Point2D.Float base1 = sLanes.get(nextHighest).getStart();
					Point2D.Float base2 = sLanes.get(nextHighest).getEnd();
					Point2D.Float halfVector = getHalf(base1, base2);
					Lane h = sLanes.get(highest);
					Lane nh = sLanes.get(nextHighest);
					a1 = new Point2D.Float(h.getStart().x + halfVector.x,
							h.getStart().y + halfVector.y);
					a2 = new Point2D.Float(h.getEnd().x + halfVector.x,
							h.getEnd().y + halfVector.y);
					a3 = new Point2D.Float(nh.getStart().x - halfVector.x,
							nh.getStart().y - halfVector.y);
					a4 = new Point2D.Float(nh.getEnd().x - halfVector.x,
							nh.getEnd().y - halfVector.y);
				} else {
					System.out.println("Starting lane is odd unilateral");
					// odd lanes
					// one outer line is highest - offset
					// one outer line is nextHighest + offset
					Point2D.Float base1 = sLanes.get(nextHighest).getEnd();
					Point2D.Float base2 = sLanes.get(nextHighest).getStart();
					Point2D.Float halfVector = getHalf(base1, base2);
					Lane h = sLanes.get(highest);
					Lane nh = sLanes.get(nextHighest);
					a1 = new Point2D.Float(h.getStart().x - halfVector.x,
							h.getStart().y - halfVector.y);
					a2 = new Point2D.Float(h.getEnd().x - halfVector.x,
							h.getEnd().y - halfVector.y);
					a3 = new Point2D.Float(nh.getStart().x + halfVector.x,
							nh.getStart().y + halfVector.y);
					a4 = new Point2D.Float(nh.getEnd().x + halfVector.x,
							nh.getEnd().y + halfVector.y);
				}

			}
			if (nextHighest == -1)
				throw new UnknownConnectionError();
			// END for sLane
			// //////////////
			// //////////////

			// for targetRoad
			ArrayList<Integer> sortedT = new ArrayList<Integer>(tLanes.keySet());
			Collections.sort(sortedT);
			int tHighest = sortedT.get(sortedT.size() - 1);

			int tModulus = tHighest % 2;
			int tNextHighest = -1;
			if (targetRoad.getBilateral()) {
				// bilateral get one lane with highest even key and one with
				// highest odd key

				if (tModulus == 0) {
					// highest is even-> next highest should be odd since
					// bilateral

					for (int i = sortedT.size() - 2; i >= 0; i--) {

						if (sortedT.get(i) % 2 == 1) {
							tNextHighest = sortedT.get(i);
							break;
						}
					}

					// highest is even the outer line is another addition of the
					// half offset vector
					// nextHighest is odd the outer line is another subtraction
					// of the half offset vector
					Point2D.Float base1 = tLanes.get(tHighest).getStart();
					Point2D.Float base2 = tLanes.get(tHighest).getEnd();
					Point2D.Float halfVector = getHalf(base1, base2);

					Lane h = tLanes.get(tHighest);
					Lane nh = tLanes.get(tNextHighest);
					b1 = new Point2D.Float(h.getStart().x + halfVector.x,
							h.getStart().y + halfVector.y);
					b2 = new Point2D.Float(h.getEnd().x + halfVector.x,
							h.getEnd().y + halfVector.y);
					b3 = new Point2D.Float(nh.getStart().x - halfVector.x,
							nh.getStart().y - halfVector.y);
					b4 = new Point2D.Float(nh.getEnd().x - halfVector.x,
							nh.getEnd().y - halfVector.y);
				} else {
					// highest is odd-> next highest should be even since
					// bilateral
					for (int i = sortedT.size() - 2; i >= 0; i--) {
						if (sortedT.get(i) % 2 == 0) {
							tNextHighest = sortedT.get(i);
							break;
						}
					}
					// highest is odd the outer line is another subtraction of
					// the half offset vector
					// nextHighest is even the outer line is another addition
					// of the half offset vector
					Point2D.Float base1 = tLanes.get(tNextHighest).getStart();
					Point2D.Float base2 = tLanes.get(tNextHighest).getEnd();
					Point2D.Float halfVector = getHalf(base1, base2);

					Lane h = tLanes.get(tHighest);
					Lane nh = tLanes.get(tNextHighest);
					b1 = new Point2D.Float(h.getStart().x - halfVector.x,
							h.getStart().y - halfVector.y);
					b2 = new Point2D.Float(h.getEnd().x - halfVector.x,
							h.getEnd().y - halfVector.y);
					b3 = new Point2D.Float(nh.getStart().x + halfVector.x,
							nh.getStart().y + halfVector.y);
					b4 = new Point2D.Float(nh.getEnd().x + halfVector.x,
							nh.getEnd().y + halfVector.y);

				}
			} else {
				// unilateral get a lane with highest key and one with the
				// lowest key
				tNextHighest = sortedT.get(0);
				if (tNextHighest % 2 == 0) {
					// even lanes
					// one outer line is highest + offset
					// one outer line is nextHighest - offset
					Point2D.Float base1 = tLanes.get(tNextHighest).getStart();
					Point2D.Float base2 = tLanes.get(tNextHighest).getEnd();
					Point2D.Float halfVector = getHalf(base1, base2);
					Lane h = tLanes.get(tHighest);
					Lane nh = tLanes.get(tNextHighest);
					b1 = new Point2D.Float(h.getStart().x + halfVector.x,
							h.getStart().y + halfVector.y);
					b2 = new Point2D.Float(h.getEnd().x + halfVector.x,
							h.getEnd().y + halfVector.y);
					b3 = new Point2D.Float(nh.getStart().x - halfVector.x,
							nh.getStart().y - halfVector.y);
					b4 = new Point2D.Float(nh.getEnd().x - halfVector.x,
							nh.getEnd().y - halfVector.y);
				} else {
					// odd lanes
					// one outer line is highest - offset
					// one outer line is nextHighest + offset
					Lane h = tLanes.get(tHighest);
					Lane nh = tLanes.get(tNextHighest);
					Point2D.Float base1 = nh.getEnd();
					Point2D.Float base2 = nh.getStart();
					Point2D.Float halfVector = getHalf(base1, base2);

					b1 = new Point2D.Float(h.getStart().x - halfVector.x,
							h.getStart().y - halfVector.y);
					b2 = new Point2D.Float(h.getEnd().x - halfVector.x,
							h.getEnd().y - halfVector.y);
					b3 = new Point2D.Float(nh.getStart().x + halfVector.x,
							nh.getStart().y + halfVector.y);
					b4 = new Point2D.Float(nh.getEnd().x + halfVector.x,
							nh.getEnd().y + halfVector.y);
				}

			}
			// now see if a1-a2, a3-a4 intersects with b1-b2 or b3-b4
			// or if b1-b2 and b3-b4 overlaps with a1-a2 or a3-a4

			Line2D.Float line1 = new Line2D.Float(a1, a2);
			Line2D.Float line2 = new Line2D.Float(a3, a4);
			Line2D.Float line3 = new Line2D.Float(b1, b2);
			Line2D.Float line4 = new Line2D.Float(b3, b4);

			// if the line overlaps check if the overlapped segment is closest
			// to the starting lane's starting point

			if ((line1.intersectsLine(line3) && line2.intersectsLine(line3))) {

				overlapFound = true;

				// check distance between the starting point of the starting
				// lane and line3, as well as the distance between the same
				// point and line4. If line 3 is the closest intersection
				// between this line is the starting point

			} else if ((line1.intersectsLine(line4) && line2
					.intersectsLine(line4))) {

				overlapFound = true;

			} else if ((line3.intersectsLine(line1) && line4
					.intersectsLine(line1))) {

				overlapFound = true;

			} else if ((line3.intersectsLine(line2) && line4
					.intersectsLine(line2))) {

				overlapFound = true;

			} else {
				System.out.println("overlapping not found");
				return false;
			}

			if (overlapFound) {
				System.out
						.println("Finding closest outerline of the target road");
				// get starting lane's outer lines
				// find two intersection point with the closest road outerline
				// of the target road
				// get the vector that is perpendicular to the outer line that
				// previously found intersection point lines on
				// get intersection of the previously found vector and the
				// starting lane's vector-> starting point of the connection

				// get intersection between the starting lane vector and target
				// lane vector-> control point of the quadratic bezier

				// get a point that lies on target lane's vector with the
				// distance of the displacement between the two previously found
				// points-> end point of the quadratic bezier
				Lane startingL = sLanes.get(currentLane);
				Lane endingL = tLanes.get(targetLane);
				Point2D.Float slStarting = startingL.getStart();
				Point2D.Float slEnding = startingL.getEnd();
				Point2D.Float halfVector = getHalf(slStarting, slEnding);
				Point2D.Float sla1;
				Point2D.Float sla2;
				Point2D.Float sla3;
				Point2D.Float sla4;
				sla1 = new Point2D.Float(slStarting.x + halfVector.x,
						slStarting.y + halfVector.y);
				sla2 = new Point2D.Float(slEnding.x + halfVector.x, slEnding.y
						+ halfVector.y);
				sla3 = new Point2D.Float(slStarting.x - halfVector.x,
						slStarting.y - halfVector.y);
				sla4 = new Point2D.Float(slEnding.x - halfVector.x, slEnding.y
						- halfVector.y);

				Line2D.Float sLine1 = new Line2D.Float(sla1, sla2);
				Line2D.Float sLine2 = new Line2D.Float(sla3, sla4);

				Point2D.Float i1 = linesolver.checkIntersection(slStarting,
						slEnding, (Point2D.Float) line3.getP1(),
						(Point2D.Float) line3.getP2());
				Point2D.Float i2 = linesolver.checkIntersection(slStarting,
						slEnding, (Point2D.Float) line4.getP1(),
						(Point2D.Float) line4.getP2());

				float d1 = (float) Math.sqrt(Math.pow((slStarting.x - i1.x),
						2.0) + Math.pow(slStarting.y - i1.y, 2.0)); // length
																	// between
																	// line 3
				float d2 = (float) Math.sqrt(Math.pow((slStarting.x - i2.x),
						2.0) + Math.pow(slStarting.y - i2.y, 2.0)); // length
																	// between

				Point2D.Float closestPoint;
				Line2D.Float closestLine;
				if (d1 > d2) {
					// line 4 is the closest outer line
					// get intersection between line 4 and line 1,line 2

					Point2D.Float ii1 = linesolver.checkIntersection(
							(Point2D.Float) line4.getP1(),
							(Point2D.Float) line4.getP2(),
							(Point2D.Float) sLine1.getP1(),
							(Point2D.Float) sLine1.getP2());
					Point2D.Float ii2 = linesolver.checkIntersection(
							(Point2D.Float) line4.getP1(),
							(Point2D.Float) line4.getP2(),
							(Point2D.Float) sLine2.getP1(),
							(Point2D.Float) sLine2.getP2());

					float dd1 = (float) Math.sqrt(Math.pow(
							(slStarting.x - ii1.x), 2.0)
							+ Math.pow(slStarting.y - ii1.y, 2.0));
					float dd2 = (float) Math.sqrt(Math.pow(
							(slStarting.x - ii2.x), 2.0)
							+ Math.pow(slStarting.y - ii2.y, 2.0));

					if (dd1 > dd2) {
						// ii2 is the closest intersection point
						// lies on line2

						closestPoint = ii2;
						closestLine = sLine2;

					} else if (dd2 > dd1) {
						// ii1 is the closest intersection point
						// lies on line1

						closestPoint = ii1;
						closestLine = sLine1;
					} else {
						// they are the same so chose which ever point
						// lies on line1

						closestPoint = ii1;
						closestLine = sLine1;
					}
				} else if (d2 > d1) {
					// line 3 is the shorted outer line
					// get intersection between line 3 and line 1, line 2

					Point2D.Float ii1 = linesolver.checkIntersection(
							(Point2D.Float) line3.getP1(),
							(Point2D.Float) line3.getP2(),
							(Point2D.Float) sLine1.getP1(),
							(Point2D.Float) sLine1.getP2());
					Point2D.Float ii2 = linesolver.checkIntersection(
							(Point2D.Float) line3.getP1(),
							(Point2D.Float) line3.getP2(),
							(Point2D.Float) sLine2.getP1(),
							(Point2D.Float) sLine2.getP2());

					float dd1 = (float) Math.sqrt(Math.pow(
							(slStarting.x - ii1.x), 2.0)
							+ Math.pow(slStarting.y - ii1.y, 2.0));
					float dd2 = (float) Math.sqrt(Math.pow(
							(slStarting.x - ii2.x), 2.0)
							+ Math.pow(slStarting.y - ii2.y, 2.0));

					if (dd1 > dd2) {
						// ii2 is the closest intersection point
						// lies on line2

						closestPoint = ii2;
						closestLine = sLine2;

					} else if (dd2 > dd1) {
						// ii1 is the closest intersection point
						// lies on line1

						closestPoint = ii1;
						closestLine = sLine1;
					} else {
						// they are the same so chose which ever point
						// lies on line1

						closestPoint = ii1;
						closestLine = sLine1;
					}
				} else {
					throw new UnknownConnectionError();
				}

				Point2D.Float closestVec = new Point2D.Float(
						((Point2D.Float) closestLine.getP1()).x
								- closestPoint.x,
						((Point2D.Float) closestLine.getP1()).y
								- closestPoint.y);

				intersectingStartPoint = new Point2D.Float(
						((Point2D.Float) startingL.getStart()).x - closestVec.x,
						((Point2D.Float) startingL.getStart()).y - closestVec.y);

				intersectingControlPoint = linesolver.checkIntersection(
						slStarting, slEnding, endingL.getStart(),
						endingL.getEnd());

				Point2D.Float iVec = new Point2D.Float(intersectingStartPoint.x
						- intersectingControlPoint.x, intersectingStartPoint.y
						- intersectingControlPoint.y);
				float scd = (float) Math.sqrt(Math.pow((iVec.x), 2.0)
						+ Math.pow(iVec.y, 2.0));
				Point2D.Float tVec = new Point2D.Float(endingL.getStart().x
						- endingL.getEnd().x, endingL.getStart().y
						- endingL.getEnd().y);
				float tVecD = (float) Math.sqrt(Math.pow((tVec.x), 2.0)
						+ Math.pow(tVec.y, 2.0));
				intersectingEndPoint = new Point2D.Float(
						intersectingControlPoint.x
								+ ((endingL.getEnd().x - endingL.getStart().x)
										/ tVecD * scd),
						intersectingControlPoint.y
								+ ((endingL.getEnd().y - endingL.getStart().y)
										/ tVecD * scd));

				ConnectionPoint cp = new ConnectionPoint(currentRoad,
						startingL, intersectingStartPoint);
				Connection cn = new Connection(currentRoad, startingL,
						targetRoad, endingL, cp, intersectingStartPoint,
						intersectingEndPoint, intersectingControlPoint);

				success = startingL.addConnectionPoint(cp, cn);
			}

			return success;
		} else {

			throw new UnknownConnectionError();
		}
		// end if
		// //////////
		// ////////////

	}

	private static Point2D.Float getHalf(Point2D.Float base1,
			Point2D.Float base2) {
		Point2D.Float vector = new Point2D.Float(base2.x - base1.x, base2.y
				- base1.y);
		// System.out.println("Vector: " + vector);
		float vectorLength = (float) Math.sqrt(Math.pow(vector.x, 2.0)
				+ Math.pow(vector.y, 2.0));
		// System.out.println("Length: " + vectorLength);
		Point2D.Float normalVector = new Point2D.Float(vector.x / vectorLength,
				vector.y / vectorLength);
		// System.out.println("x calculation: " + normalVector.x +
		// "*"
		// + perpenMat[0][0] + " + " + normalVector.y + "*"
		// + perpenMat[1][0]);
		Point2D.Float perpenVector = new Point2D.Float(normalVector.x
				* perpenMat[0][0] + normalVector.y * perpenMat[1][0],
				normalVector.x * perpenMat[0][1] + normalVector.y
						* perpenMat[1][1]);
		// System.out.println("Perpen vector: " + perpenVector);
		Point2D.Float halfScaled = new Point2D.Float(perpenVector.x
				* Road.roadWidth / 2, perpenVector.y * Road.roadWidth / 2);
		return halfScaled;

	}

	/****************************/
	public static ArrayList<Road> bfsParks(Road currentRoad,
			CarPark destinationPark) {
		// TODO Auto-generated method stub
		return null;
	}

	public CarWorld getWorld() {
		return this.world;
	}

	public ArrayList<Connection> getConnections() {
		// TODO Auto-generated method stub
		ArrayList<Connection> cal = new ArrayList<Connection>();
		Iterator<Entry<Integer, Lane>> lit = this.lanes.entrySet().iterator();
		while (lit.hasNext()) {
			Map.Entry<Integer, Lane> lp = lit.next();
			Lane currentLane = lp.getValue();
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
		return cal;
	}

	// car parks are only allowed to exist on straight roads
	public void setCarParks(int s) {
		if (this instanceof StraightRoad) {
			Iterator<Entry<Integer, Lane>> lit = this.lanes.entrySet()
					.iterator();
			while (lit.hasNext()) {
				Map.Entry<Integer, Lane> lp = lit.next();
				Lane cl = lp.getValue();
				int laneKey = cl.getLaneKey();
				if (laneKey == s && !cl.getHasPark()) {

					CarPark newPark = new CarPark(cl, CarPark.START, this.world);
					this.world.getParks().add(newPark);
					System.out.println("Car park spawned");
					cl.setHasPark(true);

				}
			}

		} else {
			System.out.println("road type doesn't support car park");
		}

	}

	//currently exit point can only exist on straight road
	public void setEnding(int s, boolean b) {
		if (this instanceof StraightRoad) {
			System.out.println("Straight road detected");
			int remainder = s % 2;
			Iterator<Entry<Integer, Lane>> lit = this.lanes.entrySet()
					.iterator();
			while (lit.hasNext()) {
				Map.Entry<Integer, Lane> lp = lit.next();
				Lane cl = lp.getValue();
				int laneKey = cl.getLaneKey();
				if (laneKey % 2 == remainder) {

					cl.setEnding(b);

				}
			}

		} else {
			System.out.println("road type doesn't support car park");
		}
	}

	public void paintBorders(Graphics g) {
		// TODO Auto-generated method stub
		Iterator<Entry<Integer, Lane>> lit = lanes.entrySet().iterator();

		while (lit.hasNext()) {
			Lane currentLane = lit.next().getValue();
			currentLane.paintBorders(g);
		}
	}

}
