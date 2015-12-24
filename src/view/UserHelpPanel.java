package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import control.WorldController;

public class UserHelpPanel extends JPanel implements ActionListener {
	WorldController wController;
	CarSimView mainFrame;
	private JScrollPane jscrollPane;
	private JPanel usrHelpPanel, btnHolderPanel;
	private JButton rtrnButton;
	private JLabel helpTitle, aimTitle, trafficTitle, collsionTitle,
			addTrafficLightLabel, collisionLabel;
	
	private JTextArea introTextArea, trafficTextArea, collsionTextArea;
	private Font titleFont = new Font("Tahoma", Font.BOLD, 20);

	public UserHelpPanel(WorldController wController, CarSimView mainFrame) {
		this.wController = wController;
		this.mainFrame = mainFrame;

		this.setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.BLACK));

		usrHelpPanel = new JPanel();
		btnHolderPanel = new JPanel();

		introTextArea = new JTextArea(2, 50);
		introTextArea.setLineWrap(true);
		introTextArea.setEditable(false);
		trafficTextArea = new JTextArea(2, 50);
		trafficTextArea.setLineWrap(true);
		trafficTextArea.setEditable(false);
		collsionTextArea = new JTextArea(2, 50);
		collsionTextArea.setLineWrap(true);
		collsionTextArea.setEditable(false);

		rtrnButton = new CustomJButton("Return");
		rtrnButton.addActionListener(this);

		helpTitle = new JLabel();
		
		aimTitle = new JLabel();
	

		trafficTitle = new JLabel("How to add a traffic light:");
		collsionTitle = new JLabel("What is a collsion?");
		trafficTitle.setFont(titleFont);
		collsionTitle.setFont(titleFont);
		addTrafficLightLabel = new JLabel();
	

		collisionLabel = new JLabel();
	

		this.add(helpTitle);

		jscrollPane = new JScrollPane(usrHelpPanel,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jscrollPane.setPreferredSize(new Dimension(700, 400));
		jscrollPane.setBackground(Color.WHITE);
		jscrollPane.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2,
				Color.BLACK));

		// introTextArea
		// .setText("The aim of this software is to allow you to find the perfect coordination f");
		trafficTextArea
				.setText("The above image illustrates how you can add new traffic light to the system. (1) Press the 'Add Traffic Light' button and then the screen will pause and you will then see the black circles at each lane entry point (2) Select the lane you would like to add the traffic light to (3) Simply click on the part of the lane you would like to add the traffic light to. If you make a mistake or do not place the traffic light in the position you wanted simply, select the new tarffic light created and once 'Adjust Traffic Light' screen opens up simply delete that traffic light and start again.");
		collsionTextArea
				.setText("The image above illustrates an example of a collision, this is an indication that they setup of the traffic light intervals is not good and has resulted in a collsion between two cars. To solve this issue select 'Adjust Traffic Lights' or select each traffic light and make adjustments and then press the 'submit' button to confirm these changes. When making adjustments to the traffic light intervals the [Initial] Interval is the interval that the traffic light will stay as red before it enters recurring [Green] and [Red] light loop. The [Green] and [Red] interval values are the time (seconds) you would like the interval to last");
		// usrHelpPanel.add(aimTitle);
		// usrHelpPanel.add(introTextArea);
		usrHelpPanel.add(trafficTitle);
		usrHelpPanel.add(addTrafficLightLabel);
		usrHelpPanel.add(trafficTextArea);
		usrHelpPanel.add(collsionTitle);
		usrHelpPanel.add(collisionLabel);
		usrHelpPanel.add(collsionTextArea);
		usrHelpPanel.setBackground(Color.WHITE);
		usrHelpPanel.setPreferredSize(new Dimension(700, 1000));
		btnHolderPanel.setPreferredSize(new Dimension(1000, 150));

		this.add(jscrollPane);
		this.add(btnHolderPanel);
		btnHolderPanel.add(rtrnButton);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == rtrnButton) { // T Junction choosen by user
			this.wController.pause();
			mainFrame.simulationView(); // Need to confirm where user goes back
										// too
		}

	}
}
