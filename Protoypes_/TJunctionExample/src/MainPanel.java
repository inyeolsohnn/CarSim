import java.applet.Applet;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;


public class MainPanel extends Applet implements Runnable{
	
	int carlength = 25;
	int carheight = 20;
	int speed = 5;
	int gap = 5;
	int carnums;
	int panel_width  = 800;
	int panel_height = 800;
	int panel_locationX = 200;
	int road_height = 80;
	CarNode cars[] ;
	Thread panelThread;
	TrafficLight  lights[] = new TrafficLight[3];
			
	public MainPanel(Main sim, int num){
		carnums = num;
		cars = new CarNode[50];
		
		initCars();
		panelThread = new Thread(this);
		panelThread.start();
		
		for(int i=0;i<3;i++){
			if(i==0){
				lights[i] = new TrafficLight(1,6000,2000);
			}
			if(i==1){
				lights[i] = new TrafficLight(0,5000,5000);
			}
			
		}
		
	}
	public void initCars(){
		int temp_startlane;
		int lane0 =0;
		
		for(int i=0;i<carnums;i++){
			CarNode n = new CarNode();
			int randomIndexS = 0;
			int directionIndex =0;
			boolean tempTurn = true;
			int turnFlag = 0;
			
			int start_lanes[] = {0};
			int direction[] = {1};
			java.util.Random r=new java.util.Random(); 
			randomIndexS=r.nextInt(start_lanes.length);
			directionIndex = r.nextInt(direction.length);
			temp_startlane = start_lanes[randomIndexS];
			turnFlag = direction[directionIndex];
			
			if(turnFlag==0){
				tempTurn =false;
			}
			else{
				tempTurn = true;
			}
	
			
			if(temp_startlane==0){
				n.sx = 42-lane0*(gap+carlength);
				n.sy = 45;
				n.initlane = temp_startlane;
				n.turnDirection = tempTurn;
				n.theta = Math.PI/2;
				n.startTurn = false;
				lane0++;
	 			cars[i] = n;
	 			
			}
		}
	}
	public void paintRoad(Graphics g){
		g.setColor(Color.GRAY);
		Dimension d = size();
		g.fillRect(0, 40, 800, road_height);
		g.fillRect(360,100,road_height,800);
		g.setColor(Color.WHITE);
		drawDottedLine(g,0,80,800,80,10,10);
		drawDottedLine(g,400,120,400,800,10,10);
		
	}
	public void paintLights(Graphics g){
		for(int i=0;i<3;i++){
			if(i==0){
				g.setColor(Color.BLACK);
				g.fillRect(330, 20, 30, 20);
				g.setColor(lights[i].signal==1 ? Color.red : Color.green);
				g.fillOval(335, 20, 15, 15);
			}
			if(i==1){
				g.setColor(Color.BLACK);
				g.fillRect(450, 120, 20, 30);
				g.setColor(lights[i].signal==1 ? Color.red : Color.green);
				g.fillOval(455, 120, 15, 15);
			}
		}
	
	}
	public void paintCars(Graphics g,CarNode car){
		int sx = (int)car.sx;
	  	int sy = (int)car.sy;
		double carRearLeftX = 0;
		double carRearLeftY=0;
		// right rear
		double carRearRightX=0;
		double carRearRightY=0;
		// left head
		double carHeadLeftX=0;
		double carHeadLeftY=0;
		// right head
		double carHeadRightX=0;
		double carHeadRightY=0;
		  Color edgeColor = Color.black;
		  Color nodeColor = new Color(250, 220, 100);
		
		if(car.initlane==0){
			if(car.startTurn){
				double carRearX = sx - carlength/2 * Math.sin(car.theta);
				double carRearY = sy + carlength/2 * Math.cos(car.theta);
			// left rear
				carRearLeftX = carRearX - carheight/2 * Math.cos(car.theta);
				carRearLeftY = carRearY - carheight/2 * Math.sin(car.theta);
			// right rear
				carRearRightX = carRearX + carheight/2 * Math.cos(car.theta);
				carRearRightY = carRearY + carheight/2 * Math.sin(car.theta);
			// car head middle point
				double carHeadX = sx + carlength/2 * Math.sin(car.theta);
				double carHeadY = sy - carlength/2 * Math.cos(car.theta);
			// left head
				carHeadLeftX = carHeadX - carheight/2 * Math.cos(car.theta);
				carHeadLeftY = carHeadY - carheight/2 * Math.sin(car.theta);
			// right head
				carHeadRightX = carHeadX + carheight/2 * Math.cos(car.theta);
				carHeadRightY = carHeadY + carheight/2 * Math.sin(car.theta);
			}
			else{
				// left rear
				carRearLeftX = sx;
				carRearLeftY = sy ;
				// right rear
				carRearRightX = sx;
				carRearRightY = sy + carheight;
				// car head middle point
			
				// left head
				carHeadLeftX = sx + carlength;
				carHeadLeftY = sy ;
				// right head
				carHeadRightX = sx + carlength;
				carHeadRightY = sy + carheight;
				
				
			}
			int xpoints[] = {(int)carRearLeftX, (int)carRearRightX, (int)carHeadRightX, (int)carHeadLeftX};
			int ypoints[] = {(int)carRearLeftY, (int)carRearRightY, (int)carHeadRightY, (int)carHeadLeftY};
			int npoints = 4;
			int xpoints2[] = {(int)carRearLeftX-1, (int)carRearRightX-1, (int)carHeadRightX+1, (int)carHeadLeftX+1};
			int ypoints2[] = {(int)carRearLeftY-1, (int)carRearRightY+1, (int)carHeadRightY+1, (int)carHeadLeftY-1};
			g.setColor(nodeColor); 
			g.fillPolygon(xpoints, ypoints, npoints);
	   	    g.setColor(edgeColor);
	   	    g.drawPolygon(xpoints2, ypoints2, npoints);
			
		}
	}
	public void paint(Graphics g){
		paintRoad(g);
		paintLights(g);
		for(int i=0;i<carnums;i++){
			paintCars(g,cars[i]);
		}
	}
public void start(){
	System.out.println("da");
		cars = new CarNode[50];
		
		initCars();
		panelThread = new Thread(this);
		panelThread.start();
		
		
//		
	}
	@Override
	public void run() {
	
		// TODO Auto-generated method stub
		//initCars();
		while (true) {
		moveCar(cars);
		    try {
			Thread.sleep(50);
		    } catch (InterruptedException e) {
			break;
		    }
		}
	}
	
	synchronized void moveCar(CarNode[] car){
		for(int i=0;i<carnums;i++){
			if(car[i].initlane==0){
				if(360-car[i].sx<carlength&&car[i].sx<360){
					if(lights[0].signal==0){
						car[i].sx+=speed;
					}
				
				}
				else{
					
					
					if(car[i].sx<440&&car[i].sx>360){
						
						if(car[i].turnDirection){
							if(lights[1].signal==1){
						//	if(car[i].sy+carheight<90){
								
									
									car[i].startTurn = true;
									//circle coordinate is (360,100) radius is 80
									if(car[i].theta<Math.PI){
									car[i].theta += 0.03;
									car[i].sx = 360 - 60* Math.cos(car[i].theta);
									car[i].sy = 120- 60 * Math.sin(car[i].theta);
									}
										else{
										
											car[i].sy+=speed;
										
										}
									
									
								//}
					
						
							}
							if(car[i].sy>120||car[i].sy==120){
								car[i].sy+=speed;
							}
						}
						else{
							car[i].sx+=speed;
						}
					}
					else{
						car[i].sx+=speed;
					}
					
					
					
				}
				
			}
		}
		repaint();
	}
	
	public void drawDottedLine(Graphics g, int x1, int y1, int x2, int y2, int s, int r) {
    	double d = Math.sqrt((double)(x1 - x2) * (x1 - x2) + (double)(y1 - y2) * (y1 - y2));
    	int x = x1, y = y1;
    	int n = 0;
    	boolean f = true;
    	do {
    	int dx = (int)((n * (s+r) + s - 1) * (x2 - x1) / d + x1 + 0.5);
    	int dy = (int)((n * (s+r) + s - 1) * (y2 - y1) / d + y1 + 0.5);
    	if (Math.abs(dx - x1) > Math.abs(x2 - x1)) {
    	dx = x2;
    	f = false;
    	}
    	if (Math.abs(dy - y1) > Math.abs(y2 - y1)) {
    	dy = y2;
    	f = false;
    	}
    	g.drawLine(x, y, dx, dy);
    	n++;
    	x = (int)(n * (s + r) * (x2 - x1) / d + x1 + 0.5);
    	y = (int)(n * (s + r) * (y2 - y1) / d + y1 + 0.5);	
    	} while(f);	
    	}
	
}
