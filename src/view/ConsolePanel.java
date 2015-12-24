package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import control.ParkController;
import control.WorldController;

public class ConsolePanel extends JPanel implements ActionListener {

	private WorldController wController;
	private CarSimView mainFrame;
	private Font font = new Font("Tahoma", Font.BOLD, 20);
	private JButton startButton, stopButton, returnButton, trafficlightButton,
			helpButton, resetButton, addLightBtn;
	private JLabel cntrlPanel_gfx, carSpawnLabel, teamLabel, carSpeedLabel;
	private JPanel bottomButtonPanel, topButtonPanel;
	private JSlider carSpawnSlider = new JSlider(0, 10, 3);
	private BorderLayout borderLayout = new BorderLayout();
	DynamicChart demo;
	

	public ConsolePanel(WorldController wController, CarSimView mainFrame,
			final ParkController pc) {
		this.mainFrame = mainFrame;
		this.wController = wController;
		this.setPreferredSize(new Dimension(200, 800));

		this.setLayout(borderLayout);

		topButtonPanel = new JPanel();
		bottomButtonPanel = new JPanel();
		demo = new DynamicChart("", this.wController);

		Font font = new Font("Tahoma", Font.BOLD, 14);
		carSpawnLabel = new JLabel("Car Spawn Rate");
		carSpawnLabel.setFont(font);
		cntrlPanel_gfx = new JLabel();
		
		
		teamLabel = new JLabel();
		
		carSpeedLabel = new JLabel();
		

		this.add(topButtonPanel, BorderLayout.NORTH);
		// topButtonPanel
		this.add(bottomButtonPanel, BorderLayout.SOUTH);

		// JButton setup!
		stopButton = new CustomJButton("Stop");
		stopButton.addActionListener(this);
		startButton = new CustomJButton("Start");
		startButton.addActionListener(this);
		addLightBtn = new CustomJButton("Add Traffic Light");
		addLightBtn.addActionListener(this);
		returnButton = new CustomJButton("Return To Main Menu");
		returnButton.addActionListener(this);
		trafficlightButton = new CustomJButton("Adjust Traffic Lights");
		trafficlightButton.addActionListener(this);
		helpButton = new CustomJButton("Help?");
		helpButton.addActionListener(this);
		resetButton = new CustomJButton("RESET");
		resetButton.addActionListener(this);

		JLabel controlTitleLabel = new JLabel("Control Panel");
		controlTitleLabel.setFont(font);
		JLabel trafficTitleLabel = new JLabel("Adjust Traffic Ligts");

		carSpawnSlider.setPaintTicks(true);
		carSpawnSlider.setPaintLabels(true);
		carSpawnSlider.setMinorTickSpacing(1);
		carSpawnSlider.setMajorTickSpacing(5);
		carSpawnSlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {

				JSlider source = (JSlider) e.getSource();
				// red slider

				int value = source.getValue();
				pc.setSpawnChance(value);

			}

		});

		topButtonPanel.add(cntrlPanel_gfx);
		topButtonPanel.add(stopButton);
		topButtonPanel.add(startButton);
		topButtonPanel.add(resetButton);

		topButtonPanel.add(trafficlightButton);

		topButtonPanel.add(addLightBtn);
		topButtonPanel.add(carSpawnLabel);
		topButtonPanel.add(carSpawnSlider);
		topButtonPanel.add(demo.content);
		topButtonPanel.add(carSpeedLabel);
		bottomButtonPanel.add(returnButton);
		bottomButtonPanel.add(helpButton);
		bottomButtonPanel.add(teamLabel);
		topButtonPanel.setPreferredSize(new Dimension(200, 520));
		bottomButtonPanel.setPreferredSize(new Dimension(200, 100));

		mainFrame.add(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == stopButton) {
			this.wController.pause();
		} else if (e.getSource() == startButton) {
			this.wController.start();
		} else if (e.getSource() == returnButton) {
			this.wController.pause();
			mainFrame.setAddingLight(false);
			System.out.println("Main Menu");
			mainFrame.mainMenu();
		} else if (e.getSource() == trafficlightButton) {
			System.out.println("Traffic Light Menu");
			this.wController.pause();
			mainFrame.TrafficPanel(0);
		} else if (e.getSource() == helpButton) {
			this.wController.pause();
			mainFrame.HelpPanel();
		} else if (e.getSource() == resetButton) {
			// RESET
			this.wController.reset();
		} else if (e.getSource() == addLightBtn) {
			if (mainFrame.getAddingLight()) {
				mainFrame.setAddingLight(false);

			} else if (!mainFrame.getAddingLight()) {
				wController.pause();
				mainFrame.setAddingLight(true);

			}
			mainFrame.repaint();
		}
	}

	public DynamicChart getDynamicChart() {
		// TODO Auto-generated method stub
		return this.demo;
	}

	public void enableButtons() {
		// TODO Auto-generated method stub
		startButton.setEnabled(true);
		stopButton.setEnabled(true);
		helpButton.setEnabled(true);
		trafficlightButton.setEnabled(true);
		helpButton.setEnabled(true);
		resetButton.setEnabled(true);
	}

	public void disableButtons() {
		startButton.setEnabled(false);
		stopButton.setEnabled(false);
		helpButton.setEnabled(false);
		trafficlightButton.setEnabled(false);
		helpButton.setEnabled(false);
		resetButton.setEnabled(false);
	}
}
