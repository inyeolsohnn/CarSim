
import javax.swing.JFrame;
import javax.swing.JPanel;

public class TrafficLightSimulation {

    public static void main(String[] args) {
    	JFrame frame = new JFrame("Traffic Light");
    	JPanel panel = new JPanel();
    	frame.setVisible(true);
    	frame.setResizable(false);
    	frame.setSize(new java.awt.Dimension(100,200));
    	frame.setLocationRelativeTo(null);
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.add(panel);
    	
    	TrafficLight tl = new TrafficLight();
    	Car c1 = new Car("Honda");
    	
    	System.out.println("Traffic Light System");
    	System.out.println(c1.getCarName()+" is "+c1.getStatus());
    	tl.Red();
    	c1.setStatus(tl.returnTrafficLightStatus());
    	c1.run();
    	System.out.println(c1.getCarName()+" is "+c1.getStatus());
    
     
    }

    }

