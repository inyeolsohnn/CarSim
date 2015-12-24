package control;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import view.CarSimView;
import model.Car;
import model.CarPark;
import model.CarWorld;
import model.Lane;
import model.Road;
import model.StraightRoad;
import model.TrafficLight;

public class WorldController {
	private CarWorld cWorld; // core model
	private JFrame carView; // JFrame

	public WorldController() {

		this.cWorld = this.createWorld();

	}

	public CarWorld getcWorld() {
		return cWorld;
	}

	public void setcWorld(CarWorld cWorld) {
		this.cWorld = cWorld;
	}

	public String toString() {
		return cWorld.toString();

	}

	public void setView(JFrame frame) {
		this.carView = frame;
	}

	public void pause() {
		this.cWorld.setStatus("paused");
	}

	public void start() {
		this.cWorld.setStatus("running");
	}

	public void exit() {
		this.cWorld.setStatus("exit");
	}

	// program loop
	public void simulate() throws InterruptedException {

		while (true) {

			if (cWorld.getStatus().equals("running")) {
				update();

				Thread.sleep(20); // the timing mechanism
									// needs improvement
			} else if (cWorld.getStatus().equals("paused")) {
				Thread.sleep(20);
			} else if (cWorld.getStatus().equals("exit")) {
				return;
			}
		}
	}

	public HashMap<Integer, Car> getCars() {
		// TODO Auto-generated method stub
		return this.cWorld.getCars();
	}

	public ArrayList<Road> getRoads() {
		// TODO Auto-generated method stub
		return this.cWorld.getRoads();
	}

	private void update() {

		for (int i = 0; i < cWorld.getParks().size(); i++) {
			cWorld.getParks().get(i).update();
		}

		ArrayList<Car> cars = new ArrayList<Car>(cWorld.getCars().values());
		for (int i = 0; i < cars.size(); i++) {
			cars.get(i).move();

		}
		ArrayList<TrafficLight> lights = cWorld.getLights();
		for (int i = 0; i < lights.size(); i++) {
			lights.get(i).update();
		}
		((CarSimView) carView).getDynamicChart().updateData();
		ArrayList<Car> collided = this.cWorld.checkCollision();
		if (collided.size() == 2) {

			cWorld.reset();
			for (int i = 0; i < collided.size(); i++) {
				this.cWorld.addCar(collided.get(i));
			}
			this.carView.repaint();
			JOptionPane.showMessageDialog(null, "Collision detected!");
			cWorld.reset();
		}

		this.carView.repaint();

	}

	public void setFullSimulation() {
		this.cWorld.setStatus("paused");
		this.cWorld.flush();
		Road road1 = new StraightRoad(new Point2D.Float(100, 70),
				new Point2D.Float(1000, 70), 1, 1, this.getcWorld());
		Road road2 = new StraightRoad(new Point2D.Float(800, 50),
				new Point2D.Float(800, 590), 1, 1, this.getcWorld());
		Road road3 = new StraightRoad(new Point2D.Float(100, 570),
				new Point2D.Float(1000, 570), 1, 1, this.getcWorld());
		Road road4 = new StraightRoad(new Point2D.Float(100, 200),
				new Point2D.Float(1000, 400), 2, 2, this.getcWorld());
		Road road5 = new StraightRoad(new Point2D.Float(550, 70),
				new Point2D.Float(180, 570), 1, 1, this.getcWorld());
		try {
			Road.connectLane(road1, 0, road2, 0);
			Road.connectLane(road1, 0, road5, 0);
			Road.connectLane(road1, 1, road2, 0);
			Road.connectLane(road1, 1, road5, 0);

			Road.connectLane(road2, 1, road1, 0);
			Road.connectLane(road2, 1, road1, 1);
			Road.connectLane(road2, 0, road3, 0);
			Road.connectLane(road2, 0, road3, 1);
			Road.connectLane(road2, 0, road4, 2);
			Road.connectLane(road2, 0, road4, 0);
			Road.connectLane(road2, 0, road4, 1);
			Road.connectLane(road2, 0, road4, 3);
			Road.connectLane(road2, 1, road4, 2);
			Road.connectLane(road2, 1, road4, 0);
			Road.connectLane(road2, 1, road4, 1);
			Road.connectLane(road2, 1, road4, 3);

			Road.connectLane(road3, 0, road2, 1);
			Road.connectLane(road3, 1, road2, 1);
			Road.connectLane(road3, 0, road5, 1);
			Road.connectLane(road3, 1, road5, 1);

			Road.connectLane(road5, 1, road1, 0);
			Road.connectLane(road5, 1, road1, 1);
			Road.connectLane(road5, 0, road3, 0);
			Road.connectLane(road5, 0, road3, 1);
			Road.connectLane(road5, 0, road4, 2);
			Road.connectLane(road5, 0, road4, 0);
			Road.connectLane(road5, 0, road4, 1);
			Road.connectLane(road5, 0, road4, 3);
			Road.connectLane(road5, 1, road4, 2);
			Road.connectLane(road5, 1, road4, 0);
			Road.connectLane(road5, 1, road4, 1);
			Road.connectLane(road5, 1, road4, 3);

			Road.connectLane(road4, 2, road5, 1);
			Road.connectLane(road4, 0, road5, 0);
			Road.connectLane(road4, 2, road2, 1);
			Road.connectLane(road4, 0, road2, 0);
			Road.connectLane(road4, 1, road5, 1);
			Road.connectLane(road4, 3, road5, 0);
			Road.connectLane(road4, 1, road2, 1);
			Road.connectLane(road4, 3, road2, 0);

		} catch (Exception e) {

		}
		road1.setCarParks(0);
		road1.setCarParks(1);
		road1.setEnding(0, true);
		road1.setEnding(1, true);

		road1.setCarParks(0);
		road1.setCarParks(1);
		road1.setEnding(0, true);
		road1.setEnding(1, true);

		road4.setCarParks(0);
		road4.setCarParks(1);
		road4.setCarParks(2);
		road4.setCarParks(3);
		road4.setEnding(0, true);
		road4.setEnding(1, true);
		road4.setEnding(2, true);
		road4.setEnding(3, true);

		road3.setCarParks(0);
		road3.setCarParks(1);
		road3.setEnding(0, true);
		road3.setEnding(1, true);

		this.cWorld.addRoad(road1);
		this.cWorld.addRoad(road2);
		this.cWorld.addRoad(road3);
		this.cWorld.addRoad(road4);
		this.cWorld.addRoad(road5);
	}

	// example case setup
	public void setTJunction() {
		this.cWorld.setStatus("paused");
		this.cWorld.flush();
		// add new roads and such
		Road sr = new StraightRoad(new Point2D.Float(100, 100),
				new Point2D.Float(1000, 100), 1, 1, this.getcWorld());
		Road ar = new StraightRoad(new Point2D.Float(550, 80),
				new Point2D.Float(550, 600), 1, 1, this.getcWorld());
		try {
			Road.connectLane(sr, 0, ar, 0);
			Road.connectLane(sr, 1, ar, 0);
			Road.connectLane(ar, 1, sr, 0);
			Road.connectLane(ar, 1, sr, 1);
		} catch (Exception e) {

		}
		sr.setCarParks(0);
		sr.setCarParks(1);
		ar.setCarParks(1);

		sr.setEnding(0, true);
		sr.setEnding(1, true);
		ar.setEnding(0, true);
		this.cWorld.addRoad(sr);
		this.cWorld.addRoad(ar);
	}

	public void setIntersection() {
		// construct the world with road network
		this.cWorld.setStatus("paused");
		this.cWorld.flush();
		// add new roads and such
		Road sr = new StraightRoad(new Point2D.Float(100, 200),
				new Point2D.Float(1000, 400), 2, 2, this.getcWorld());

		Road cr = new StraightRoad(new Point2D.Float(700, 100),
				new Point2D.Float(700, 600), 2, 2, this.getcWorld());

		try {

			Road.connectLane(sr, 0, cr, 0);
			Road.connectLane(sr, 2, cr, 3);
			Road.connectLane(sr, 3, cr, 2);
			Road.connectLane(sr, 1, cr, 1);

			Road.connectLane(cr, 0, sr, 1);
			Road.connectLane(cr, 2, sr, 2);
			Road.connectLane(cr, 3, sr, 3);
			Road.connectLane(cr, 1, sr, 0);

		} catch (Exception e) {
			System.out.println("error while connecting");
			e.printStackTrace();

		}
		sr.setCarParks(0);

		sr.setCarParks(1);

		sr.setCarParks(2);
		sr.setCarParks(3);

		cr.setCarParks(1);
		cr.setCarParks(0);
		cr.setCarParks(2);
		cr.setCarParks(3);

		sr.setEnding(0, true);
		sr.setEnding(1, true);
		sr.setEnding(2, true);
		sr.setEnding(3, true);

		cr.setEnding(0, true);
		cr.setEnding(1, true);
		cr.setEnding(2, true);
		cr.setEnding(3, true);

		this.cWorld.addRoad(sr);

		this.cWorld.addRoad(cr);

	}

	public CarWorld createWorld() {
		if (this.cWorld == null) {
			CarWorld world = new CarWorld();
			this.cWorld = world;
			return cWorld;
		} else {
			return this.cWorld;
		}
	}

	public double getAverageSpeed() {
		// TODO Auto-generated method stub
		Iterator<Entry<Integer, Car>> cit = this.cWorld.getCars().entrySet()
				.iterator();
		float sum = 0;
		float count = 0;
		while (cit.hasNext()) {
			Car currentCar = cit.next().getValue();
			sum += currentCar.getCurrentSpeed();
			count++;

		}
		if (count == 0)
			return 0;
		else
			return sum / count;
	}

	public int findLight(Point p) {
		ArrayList<TrafficLight> lights = this.cWorld.getLights();
		TrafficLight selected = null;
		for (int i = 0; i < lights.size(); i++) {
			TrafficLight cl = lights.get(i);
			if ((p.x > cl.getCoordinate().x - 7.5 && p.x < cl.getCoordinate().x + 7.5)
					&& (p.y > cl.getCoordinate().y - 7.5 && p.y < cl
							.getCoordinate().y + 7.5)) {
				selected = cl;
				break;
			}
		}
		if (selected == null)
			return 0;

		return selected.getId();

	}

	public ArrayList<TrafficLight> getLights() {
		// TODO Auto-generated method stub
		return this.cWorld.getLights();
	}

	public ArrayList<CarPark> getParks() {
		// TODO Auto-generated method stub
		return this.cWorld.getParks();
	}

	public void reset() {
		this.cWorld.reset();
	}

	public Integer findLane(Point p) {
		// TODO Auto-generated method stub
		ArrayList<Lane> lanes = this.cWorld.getLanes();

		for (int i = 0; i < lanes.size(); i++) {
			Lane cl = lanes.get(i);
			if ((p.x > cl.getStart().x - 7.5 && p.x < cl.getStart().x + 7.5)
					&& (p.y > cl.getStart().y - 7.5 && p.y < cl.getStart().y + 7.5)) {
				return cl.getLaneId();
			}
		}
		return null;
	}

	public ArrayList<Lane> getLanes() {
		return this.cWorld.getLanes();
	}
}
