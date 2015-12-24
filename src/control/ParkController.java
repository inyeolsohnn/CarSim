package control;

import java.util.ArrayList;

import model.CarPark;
import model.CarWorld;

public class ParkController {
	private CarWorld world;

	public ParkController(CarWorld world) {
		this.world = world;
	}

	public void setSpawnChance(int spawn) {
		ArrayList<CarPark> parks = world.getParks();
		for (int i = 0; i < parks.size(); i++) {
			parks.get(i).setSpawn(spawn);
		}
	}
}
