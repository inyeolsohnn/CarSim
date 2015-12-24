import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

//Simple idea for how the user will interact with system, will be alot easier to implement when the MVC 
//is set up so we can use the Controller for communication between View and Model. 
public class UserFrame extends JPanel {
	
	BufferedImage title_Img, title_HowToUse, title_UserControls;
	JLabel howToUseImgLabel, titleImgLabel, userControlsImgLabel;
	
	public UserFrame(){
		createUserFrame(); //creates the simple view
		createJSliders(); //Adds JSliders to page. 
		createCarNumberInput();
	}
	

	public void createUserFrame(){
		this.setBackground(Color.WHITE);
		this.setBorder(BorderFactory.createMatteBorder(
                3, 3, 3, 3, Color.BLACK));
		this.setPreferredSize(new Dimension(350,800));
		
		try {
			title_Img = ImageIO.read(new File("Title.png"));
			title_HowToUse = ImageIO.read(new File("HowToUseTitle.png"));
			title_UserControls = ImageIO.read(new File("UserControlTitle.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		titleImgLabel = new JLabel(new ImageIcon(title_Img));
		howToUseImgLabel = new JLabel(new ImageIcon(title_HowToUse));
		add(titleImgLabel);
	}
	
	//Potenial creation of JSliders for user to increase and decrease. 
	public void createJSliders() {
		userControlsImgLabel = new JLabel(new ImageIcon(title_UserControls));
		JSlider trafficSlider = new JSlider(JSlider.HORIZONTAL);
		trafficSlider.setVisible(true);
		trafficSlider.setBackground(Color.WHITE);
		trafficSlider.setPaintTicks(true);
		trafficSlider.setMajorTickSpacing(10);
		add(userControlsImgLabel);
		add(new JLabel("Increase/Decrease Traffic Light Speed"));
		this.add(trafficSlider);
		
	}
	
	public void createCarNumberInput(){
		add(new JLabel("Input Number Of Cars (MAX 100)"));
		JTextField carInputField = new JTextField(20);
		this.add(carInputField);
	}
	
}
