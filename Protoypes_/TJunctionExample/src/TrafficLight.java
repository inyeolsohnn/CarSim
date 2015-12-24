
public class TrafficLight implements Runnable{

   public  int signal;
   public  int pauss, redpauss, greenpauss;
    Thread lighter;

    TrafficLight(int signal,int redpauss, int greenpauss){
    	this.signal=signal;
    	this.redpauss = redpauss;
    	this.greenpauss = greenpauss;
    	System.out.println("signal"+signal);
        lighter = new Thread(this);
        lighter.start();
   }

public void run() {
            
	while (true) {
	    if (signal==1){
                      signal=0;
                      pauss=greenpauss;
                      
                  }
                  else {
                      signal=1;
                      pauss=redpauss;
                     
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
