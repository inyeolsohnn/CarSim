package model;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
//collision checking
//origially from
/*http://gamedevelopment.tutsplus.com/tutorials/
 * quick-tip-use-quadtrees-to-detect-likely-collisions-in-2d-space--gamedev-374*/

public class QuadTree {
	private int MAX_OBJECTS = 8;
	private int MAX_LEVELS = 5;

	private int level;
	private ArrayList<Car> cars;
	private Rectangle bounds;
	private QuadTree[] nodes;

	/*
	 * Constructor
	 */
	public QuadTree(int pLevel, Rectangle pBounds) {
		level = pLevel;
		cars = new ArrayList<Car>();
		bounds = pBounds;
		nodes = new QuadTree[4];
	}

	public void clear() {
		cars.clear();

		for (int i = 0; i < nodes.length; i++) {
			if (nodes[i] != null) {
				nodes[i].clear();
				nodes[i] = null;
			}
		}
	}

	private void split() {
		int subWidth = (int) (bounds.getWidth() / 2);
		int subHeight = (int) (bounds.getHeight() / 2);
		int x = (int) bounds.getX();
		int y = (int) bounds.getY();

		nodes[0] = new QuadTree(level + 1, new Rectangle(x + subWidth, y,
				subWidth, subHeight));
		nodes[1] = new QuadTree(level + 1, new Rectangle(x, y, subWidth,
				subHeight));
		nodes[2] = new QuadTree(level + 1, new Rectangle(x, y + subHeight,
				subWidth, subHeight));
		nodes[3] = new QuadTree(level + 1, new Rectangle(x + subWidth, y
				+ subHeight, subWidth, subHeight));
	}

	private int getIndex(Car car) {
		int index = -1;
		double verticalMidpoint = bounds.getX() + (bounds.getWidth() / 2);
		double horizontalMidpoint = bounds.getY() + (bounds.getHeight() / 2);

		// Object can completely fit within the top quadrants
		boolean topQuadrant = (car.getCoordinate().getY() <= horizontalMidpoint);
		// Object can completely fit within the bottom quadrants
		boolean bottomQuadrant = (car.getCoordinate().getY() > horizontalMidpoint);

		// Object can completely fit within the left quadrants
		if (car.getCoordinate().getX() <= verticalMidpoint) {
			if (topQuadrant) {
				index = 1;
			} else if (bottomQuadrant) {
				index = 2;
			}
		}
		// Object can completely fit within the right quadrants
		else if (car.getCoordinate().getX() > verticalMidpoint) {
			if (topQuadrant) {
				index = 0;
			} else if (bottomQuadrant) {
				index = 3;
			}
		}

		return index;
	}

	public void insert(Car car) {
		if (nodes[0] != null) {
			int index = getIndex(car);

			if (index != -1) {
				nodes[index].insert(car);

				return;
			}
		}

		cars.add(car);

		if (cars.size() > MAX_OBJECTS && level < MAX_LEVELS) {
			if (nodes[0] == null) {
				split();
			}

			int i = 0;
			while (i < cars.size()) {
				int index = getIndex(cars.get(i));
				if (index != -1) {
					nodes[index].insert(cars.remove(i));
				} else {
					i++;
				}
			}
		}
	}

	public ArrayList<Car> retrieve(ArrayList<Car> returnObjects, Car car) {
		int index = getIndex(car);
		if (index != -1 && nodes[0] != null) {
			nodes[index].retrieve(returnObjects, car);
		}

		returnObjects.addAll(cars);

		return returnObjects;
	}
}
