import java.applet.Applet;
import java.awt.BorderLayout;


public class TrafficSIM extends Applet{
	int carnumbers = 2;
	MainPanel panel = new MainPanel(this,carnumbers);
	
	
	public void init(){
		setLayout(new BorderLayout());
	
		add("Center", panel);
	}
	public void start(){
		panel.start();
	}
}
