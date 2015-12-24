package model;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;

//object holding connections
public class ConnectionPoint {
	private HashMap<Lane, Connection> connections = new HashMap<Lane, Connection>();
	private Point2D.Float pointCoordinate;
	private Road road;
	private Lane lane;

	public ConnectionPoint(Road road, Lane lane, Point2D.Float coordinate) {
		this.road = road;
		this.lane = lane;
		this.pointCoordinate = coordinate;

	}

	public Point2D.Float getPointCoordinate() {
		return pointCoordinate;
	}

	public void setPointCoordinate(Point2D.Float pointCoordinate) {
		this.pointCoordinate = pointCoordinate;
	}

	public Road getRoad() {
		return road;
	}

	public Lane getLane() {
		return lane;
	}

	public void addConnection(Connection cn) throws UnknownConnectionError {
		connections.put(cn.getTargetLane(), cn);
	}

	public HashMap<Lane, Connection> getConnections() {
		return connections;
	}
}
