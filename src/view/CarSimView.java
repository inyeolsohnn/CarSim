package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;

import control.ParkController;
import control.TrafficLightController;
import control.WorldController;

public class CarSimView extends JFrame {

	private Container mainContainer;
	private WorldController wControl;
	private TrafficLightController tlc;
	private ParkController pc;
	private JButton stopButton, startButton;
	private MainMenu mainMenu;
	private SimulationPanel simPanel;
	private ConsolePanel consolePanel;
	private TrafficLightPanel tlp;
	private UserHelpPanel usrHelpPanel;
	private boolean addingLight = false;

	public CarSimView(String title, WorldController wControl,
			TrafficLightController tlc, ParkController pc) {
		super(title);
		mainContainer = this.getContentPane();
		BorderLayout borderLayout = new BorderLayout();
		// FlowLayout experimentLayout = new FlowLayout();
		this.setLayout(borderLayout);
		this.wControl = wControl;
		this.tlc = tlc;
		this.pc = pc;
		simPanel = new SimulationPanel(this.wControl, tlc, this);
		mainMenu = new MainMenu(this.wControl, this);
		consolePanel = new ConsolePanel(this.wControl, this, pc);

		usrHelpPanel = new UserHelpPanel(this.wControl, this);

		this.setResizable(false);
		this.setSize(1280, 670);
		mainContainer.setBackground(new Color(0,150,0));
		consolePanel.setPreferredSize(new Dimension(200, 660));
		mainMenu.setPreferredSize(new Dimension(1000, 660));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);

		mainContainer.removeAll();
		mainContainer.add(mainMenu, BorderLayout.CENTER);
		this.repaint();
		this.revalidate();

	}

	public void TrafficPanel(int id) {
		mainContainer.removeAll();
		mainContainer.add(new TrafficLightPanel(this.wControl, tlc, this, id));
		mainContainer.revalidate();
		mainContainer.repaint();
	}

	// THIS IS NEW
	public void HelpPanel() {
		mainContainer.removeAll();
		mainContainer.add(usrHelpPanel);
		mainContainer.revalidate();
		mainContainer.repaint();
	}

	public void mainMenu() {
		// TODO Auto-generated method stub
		// change panel main menu
		mainContainer.removeAll();
		mainContainer.add(mainMenu);
		mainContainer.revalidate();
		mainContainer.repaint();

	}

	public void simulationView() {
		// TODO Auto-generated method stub
		mainContainer.removeAll();
		mainContainer.add(simPanel, BorderLayout.CENTER);
		mainContainer.add(consolePanel, BorderLayout.WEST);
		mainContainer.revalidate();
		mainContainer.repaint();

	}

	public DynamicChart getDynamicChart() {

		return consolePanel.getDynamicChart();
	}

	public void setAddingLight(boolean b) {
		// TODO Auto-generated method stub

		addingLight = b;
		if (b == true) {
			this.consolePanel.disableButtons();
		} else {
			this.consolePanel.enableButtons();
		}

	}

	public boolean getAddingLight() {
		return this.addingLight;
	}

}
