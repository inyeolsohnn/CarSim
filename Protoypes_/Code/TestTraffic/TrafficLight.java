import java.util.Timer;
import java.util.TimerTask;

class TrafficLight{
	/*
	 * A TrafficLight can either be RED or GREEN -- the boolean red determines this, 
	 * therefore determining what the CAR can do! 
	 * trafficLightRed determines whether the light is red or NOT. 
	 */
	private boolean trafficLightRed; //Check whether the traffic light is RED or not
	private Timer timer = new Timer();
	private int seconds = 3;
	
	public TrafficLight() {
		trafficLightRed = true; //initially traffic light is RED!
	   
	}
	
	public Boolean returnTrafficLightStatus(){
		return trafficLightRed;
	}
	
	public  void Red() 	{ //Traffic light is red
		trafficLightRed = true;
		System.out.println("Traffic Light: RED (STOP)");
		timer.schedule(new RemindTask(), seconds * 1000);
		
	}

	
	public void Green() { //Traffic light has turned green
		trafficLightRed = false; //car can move because the traffic light it is at is GREEN!
		System.out.println("Traffic Light: GREEN (GO)");
	}
	
	  class RemindTask extends TimerTask {
		    public void run() {
		      System.out.println("GREEN GO!");
		      timer.cancel();
		      Green();
		    }
		  }
	
}


