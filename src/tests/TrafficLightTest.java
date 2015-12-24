package tests;

import model.TrafficLight;

public class TrafficLightTest {
	public static void main(String[] args) throws InterruptedException {
		TrafficLight light = new TrafficLight();
		System.out.println("Traffic light created");
		while (light.getStatus().equals("Green")) {
			light.update();
			System.out.println(light.getStatus());
			Thread.sleep(1000);
		}
	}
}
