import java.awt.Toolkit;
import java.util.Timer;
import java.util.TimerTask;

public class TrafficLightTimer {

  private Timer timer;
  private int seconds = 2;

  public TrafficLightTimer() {
    timer = new Timer();
    timer.schedule(new RemindTask(), seconds * 1000);
  }

  class RemindTask extends TimerTask {
    public void run() {
      System.out.println("GREEN GO!");
      System.exit(0); //Stops the AWT thread (and everything else)
    }
  }

  public static void main(String args[]) {
	  
	TrafficLight tl = new TrafficLight();
  	Car c1 = new Car("Honda");
  	
  	System.out.println("Traffic Light System");
  	System.out.println(c1.getCarName()+" is "+c1.getStatus());
  	
    tl.Red();
    System.out.println("CARS STOP");
    new TrafficLightTimer();
    
  }
}