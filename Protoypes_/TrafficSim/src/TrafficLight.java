

class TrafficLight implements Runnable {
	// Updated Traffic Light
	public int signal; // represent the color, while the 0 means green and 1
						// means red
	public int tempIntervel, redIntervel, greenIntervel;
	Thread lighter;

	TrafficLight(int signal, int redIntervel, int greenIntervel) {
		signal = this.signal;

		// greenIntervel = ;
		this.redIntervel = redIntervel;
		this.greenIntervel = greenIntervel;

		lighter = new Thread(this);
		lighter.start();
	}

	@Override
	public void run() {

		while (true) {
			if (signal == 0) {
				signal = 1;

				tempIntervel = greenIntervel;
			} else {
				signal = 0;
				tempIntervel = redIntervel;
			}
			try {
				System.out.println(tempIntervel);
				Thread.sleep(tempIntervel);
			} catch (InterruptedException e) {
				break;
			}
		}
	}

	public void updateRedIntervel(int n) {
		redIntervel = n;
	}

	public void updateGreenIntervel(int n) {
		greenIntervel = n;
	}

	public void start() {
		lighter = new Thread(this);
		lighter.start();
		System.out.println(signal);
	}

	public void stop() {
		lighter.stop();
	}

}
