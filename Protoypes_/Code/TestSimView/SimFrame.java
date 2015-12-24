import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JLabel;

//This class is an attempt at being just the traffic simulation screen, 
// currently just making attempts to understand Jies code for 'TrafficLightExample'.
public class SimFrame extends JPanel implements Runnable {
	int carnums;
	double speed = 2;
	int gap = 5;
	int carheight = 15, carlength = 20;
	int xpos[] = new int[5];
	int ypos = 100;

	int rdtop = ypos + carheight, rdbottom = ypos - carheight;
	Car cars[];
	Thread panelThread;
	JLabel lbl = new JLabel("IM HERE");

	public SimFrame(int n) {
		createSimFrame();
		carnums = n;
		
	}
	public void paint(Graphics g){
		paintRoad(g);
	}

	public void paintRoad(Graphics g){

        Dimension d = new Dimension(500, 500);
        g.setColor(Color.BLACK);
      
        g.drawLine(20, rdbottom, d.width - 100, rdbottom);
        g.drawLine(20, rdtop, d.width -100, rdtop);     
  
	}

	public void createSimFrame() {
		this.setBackground(Color.RED);
		this.setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.RED));
		this.setPreferredSize(new Dimension(890, 800));
		this.setSize(new Dimension(890, 800));
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
public void start(){
		
		cars = new Car[5];
	
		initCars();
		panelThread = new Thread(this);
		panelThread.start();	
	}

private void initCars() {
	int temp_startlane;
	int lane0 = 0, lane1 =0;
	for(int i=0; i<carnums; i++){
		Car n = new Car();
		int x=0;
		int start_lanes[] = {0};
		java.util.Random r=new java.util.Random(); 
		x=r.nextInt(start_lanes.length);
		temp_startlane = start_lanes[x];
		
		if(temp_startlane==0){
			n.sx = 0-lane0*(gap+carlength);
			n.sy = 355-carheight;
			lane0++;
			n.lbl = lane0;
			n.initlane = temp_startlane;
 			cars[i] = n;
		}
}
}


}
