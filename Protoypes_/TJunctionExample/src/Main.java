import java.applet.Applet;
import java.awt.Color;
import java.awt.Dimension;



public class Main extends Applet{
	int carnumbers = 1;
	MainPanel panel =new MainPanel(this,carnumbers);
	public void init(){
		setLayout(null);
		Dimension d = size();
		resize(1000,800);
		
		panel.setSize(d.width/5*4, d.height);
		panel.setLocation(d.width/5,0);
		panel.setBackground(Color.WHITE);
		
		add(panel);
	
	}
	public void start(){
		setLayout(null);
		Dimension d = size();
		
		panel.setSize(d.width/5*4, d.height);
		panel.setLocation(d.width/5,0);
		panel.setBackground(Color.WHITE);
		
		add(panel);
	}
}
