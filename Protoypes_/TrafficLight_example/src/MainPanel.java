import java.applet.Applet;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;


public class MainPanel extends Applet implements Runnable {
	  int carnums;	
	  double speed=2;
	  int gap=5;
	  int carheight=15, carlength=20;
	  int xpos[]= new int[5];
	  int ypos=350;
	  int brgright[] = new int[5];
	  int brgleft[] = new int[5]; 
	  int brgtop =ypos+ carlength; 
	  int brgbottom=ypos- carlength;
	  int rdleft[]=new int[5];
	  int rdright[] = new int[5];
	  int rdtop= ypos+ carheight, rdbottom= ypos- carheight;
	  CarNode cars[] ;
	  Thread panelThread;
	  TrafficLight  light[] = new TrafficLight[5];
	  
	public MainPanel(TrafficExample sim, int num){
		carnums = num;
		
		for (int i=0; i<5; i++)
        {
        
			light[i]= new TrafficLight();
         xpos[i]=340*(i+1);
         brgright[i]=xpos[i]- carlength;
         brgleft[i]=xpos[i]+ carlength;
        
          }
         for(int k=1; k<4; k++){
            rdleft[k]= xpos[k-1]- carheight;
            rdright[k]= xpos[k-1]+ carheight; 
         }
        rdleft[0]=0;
        rdright[0]=0;
	}
public void start(){
		
		cars = new CarNode[50];
	
		initCars();
		panelThread = new Thread(this);
		panelThread.start();
		
		
//		
	}
	private void initCars() {
		int temp_startlane;
		int lane0 = 0, lane1 =0,lane2 = 0,lane3=0;
		for(int i=0; i<carnums; i++){
			CarNode n = new CarNode();
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
	public void paint(Graphics g){
		paintRoad(g);
		paintLights(g);
		for(int i=0;i<carnums;i++){
			//System.out.println(cars[i].theta);
			paintCar(g,cars[i]);
			
		}
	}
	
	synchronized void moveCar(CarNode[] car){
		for(int k=0; k<carnums;k++){
			if(car[k].initlane==0){
				
					if(car[0].sx+carlength<rdleft[1]){
						car[k].sx+=speed;
					}
				
				
			}
		}
		repaint();
	}
	public void paintLights(Graphics g){

        Font dispFont, stopFont;
        dispFont=new Font("TimesRoman", 0, 12);
        stopFont=new Font("TimesRoman", Font.BOLD, 14);
 
        g.setFont(dispFont);
        int lightwidth=22;
        int lightheight=30;
        for(int k=1; k<4; k++){
   
        
             g.setColor(Color.black);
             g.fillRect(rdleft[k]-25, rdtop+4, lightwidth-2, lightheight-2); 
             g.setColor(light[k-1].signal==1 ? Color.red : Color.green);
             g.fillOval(rdleft[k]-27, rdtop+14, 10, 17); 
             g.setColor(light[k-1].signal==1 ? Color.green : Color.red);
             g.fillOval(rdleft[k]-24, rdtop+2, 16, 9); 
             g.setColor(Color.black);

           //  g.drawString("Light "+k, rdleft[k]-58, rdtop+17); 
           
        }    

	}
	public void paintCar(Graphics g, CarNode car){
		int lane = car.initlane;
		int sx = (int)car.sx;
	  	int sy = (int)car.sy;
	  	if(lane==0){
	  		g.drawRect(sx, sy, carlength, carheight);
	  	}
	}
	public void paintRoad(Graphics g){

        Dimension d = size();
        g.setColor(Color.gray);
        for(int k=1; k<4; k++){
      	 
             g.drawLine(rdleft[k], 0, rdleft[k], rdbottom);
            g.drawLine(rdleft[k], rdtop, rdleft[k], d.height);
          //  g.drawLine(rdleft[k], rdtop, rdleft[k], 900);
             g.drawLine(rdright[k], 0, rdright[k], rdbottom);
             g.drawLine(rdright[k], rdtop, rdright[k], d.height);
         //    g.drawLine(rdright[k], rdtop, rdright[k], 900);
             g.drawLine(rdright[k-1], rdtop, rdleft[k], rdtop);
             g.drawLine(rdright[k-1], rdbottom, rdleft[k], rdbottom);
            }
        g.drawLine(rdright[3], rdbottom, d.width, rdbottom);
        g.drawLine(rdright[3], rdtop, d.width, rdtop);   
//        g.drawLine(rdright[3], rdbottom, 2000, rdbottom);
//        g.drawLine(rdright[3], rdtop, 2000, rdtop);   
  
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

}
