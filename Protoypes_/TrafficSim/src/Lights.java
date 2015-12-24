import javax.swing.*;
import java.awt.*;

//Graphical representation of traffic light
class Lights extends JPanel  {
	private Color redLight;
	private Color greenLight;
	static TrafficLight tl;
	
	public Lights() {
	}


	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(tl.signal==0){
			g.setColor(redLight);
			g.fillOval(10, 10, 15, 15);
			g.setColor(Color.GREEN);
			g.fillOval(10, 50, 15, 15);
			repaint();
		}
		else{
			g.setColor(Color.RED);
			g.fillOval(10, 10, 15, 15);
			g.setColor(Color.BLACK);
			g.fillOval(10, 50, 15, 15);
			repaint();
		}
		
		g.setColor(Color.BLACK);
		g.drawRect(5, 5, 27, 65);
	}
	public Dimension getPreferredSize() {
		return new Dimension(40, 90);
	}
	

	
	// Main Method To Test
	public static void main(String[] args) {
		Lights lights = new Lights();
		 tl = new TrafficLight(0,3000,1000);

		JFrame frame = new JFrame("Traffic Light Trest");
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setSize(new Dimension(40, 105));

		frame.add(lights);
		
		
		

	}



}