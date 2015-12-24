
public class TrafficLight implements Runnable{

   public  int signal;
   public  int pauss, redpauss, greenpauss;
    Thread lighter;

    TrafficLight(){
       signal=1;
       redpauss=6000;
       greenpauss=6000;
       lighter = new Thread(this);
       lighter.start();
   }

public void run() {
              signal=1;
	while (true) {
	    if (signal==1){
                      signal=0;
                      pauss=greenpauss;
                      System.out.println(signal);
                  }
                  else {
                      signal=1;
                      pauss=redpauss;
                      System.out.println(signal);
                  }
	    try {
		Thread.sleep(pauss);
	    } catch (InterruptedException e) {
		break;
	    }
	}
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
