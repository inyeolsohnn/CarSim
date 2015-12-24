import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;


/* Created to support the entire Frame, contains the objects
 * SimFrame (which could hold the simulation created) and UserFrame
 * (which could be used to support the all interaction from the user)
 */
public class MainJFrame{

	public static void main(String[]args){
		JFrame frame = new JFrame("Car Simulation");
		JPanel mainJPanel = new JPanel();
		SimFrame simFrame = new SimFrame(3);
		UserFrame usrFrame = new UserFrame();
		
		//main Frame settings.
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(1270,840));
		frame.setResizable(false);
		
		//mainJPanel settings
		mainJPanel.setBackground(Color.WHITE);
		mainJPanel.setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.BLACK));
		//mainJPanel.setPreferredSize(new Dimension(1280,900));
		frame.add(mainJPanel);
		
		//adding usrFrame and simFrame to mainJPanel
		mainJPanel.add(usrFrame);
		mainJPanel.add(simFrame);
		
		
	}

	
}
