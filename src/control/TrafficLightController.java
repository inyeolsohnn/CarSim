package control;

import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JFrame;

import model.CarWorld;
import model.TrafficLight;

public class TrafficLightController {
	CarWorld world;
	JFrame mainFrame;

	public TrafficLightController(CarWorld cw) {
		this.world = cw;
	}

	public void setView(JFrame frame) {
		// TODO Auto-generated method stub
		mainFrame = frame;
	}

	public ArrayList<TrafficLight> getLights() {
		// TODO Auto-generated method stub
		return this.world.getLights();
	}

	public void setInterval(String color, float interval, int id) {
		ArrayList<TrafficLight> lights = getLights();

		for (int i = 0; i < lights.size(); i++) {
			if (lights.get(i).getId() == id) {
				TrafficLight selected = lights.get(i);
				if (color.equals("red")) {
					selected.setRedInterval(interval);
					System.out.println("traffic light id : " + id
							+ " red interval changed to : " + interval);
				} else if (color.equals("green")) {
					selected.setGreen(interval);
					System.out.println("traffic light id : " + id
							+ " green interval changed to : " + interval);
				} else if (color.equals("initial")) {
					selected.setInit(interval);
					System.out.println("traffic light id : " + id
							+ " initial interval changed to : " + interval);
				}
			}
		}
	}

	public void removeLight(int id) {
		ArrayList<TrafficLight> lights = getLights();
		for (int i = 0; i < lights.size(); i++) {
			if (lights.get(i).getId() == id) {
				lights.get(i).getLane().removeTrafficLight(id);
			}
		}

	}

	public void addNewLight(Integer selectedLane, Point point) {
		// TODO Auto-generated method stub
		this.world.addNewLight(selectedLane, point);
		mainFrame.repaint();
		mainFrame.revalidate();
		
	}
}
