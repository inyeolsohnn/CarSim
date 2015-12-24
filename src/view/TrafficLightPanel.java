package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import model.TrafficLight;
import control.TrafficLightController;
import control.WorldController;

public class TrafficLightPanel extends JPanel implements ActionListener {
	private JPanel panel, btnHolderPanel;
	private JScrollPane jsp;
	private BorderLayout bl = new BorderLayout();
	private JButton rtrnButton;
	private static final int INTERVAL_MIN = 0;
	private static final int INTERVAL_MAX = 20;
	private ArrayList<TrafficLight> lights;

	private WorldController wController;
	private CarSimView mainFrame;

	private String TrifficLightID;
	private ImageIcon traffic_img;
	private HashMap<Integer, JTextField> greenList, redList, initList;

	public TrafficLightPanel(WorldController wc,
			final TrafficLightController tlc, final CarSimView mainFrame, int id) {
		this.wController = wc;
		this.greenList = new HashMap<Integer, JTextField>();
		this.redList = new HashMap<Integer, JTextField>();
		this.initList = new HashMap<Integer, JTextField>();
		this.mainFrame = mainFrame;

		panel = new JPanel();
		btnHolderPanel = new JPanel();
		rtrnButton = new CustomJButton("Return");
		rtrnButton.addActionListener(this);
		lights = tlc.getLights();
		System.out.println(lights.size());

		JLabel trafficLightTitle = new JLabel();
		traffic_img = new ImageIcon(getClass().getResource(File.separator + "gfx"
				+ File.separator + "adjustTrafficLight_gfx.png"));
		trafficLightTitle.setIcon(traffic_img);
		//JLabel space = new JLabel("  ");
		JLabel iInterval = new JLabel("Initial Interval ");
		JLabel gInterval = new JLabel("  Green Interval  ");
		JLabel rInterval = new JLabel("  Red Interval");

		Font font = new Font("Tahoma", Font.BOLD, 15);
		iInterval.setFont(font);
		gInterval.setFont(font);
		rInterval.setFont(font);
		//space.setFont(font);

		JPanel a = new JPanel();
		a.setPreferredSize(new Dimension(920, 30));
	//	a.add(space);
		a.add(iInterval);
		a.add(gInterval);
		a.add(rInterval);
		a.add(new JLabel("                         "));

		panel.add(a);
		int trafficLightSize = lights.size();
		panel.setPreferredSize(new Dimension(920, 64 * trafficLightSize));

		System.out.println("size" + trafficLightSize);
		if (id == 0) {
			for (int i = 0; i < trafficLightSize; i++) {

				JPanel container = new JPanel();
				container.setBackground(Color.WHITE);
				container.setPreferredSize(new Dimension(920, 58));
				TrifficLightID = "" + lights.get(i).getId(); // THIS IS NEW

				JLabel lbl = new JLabel("traffic light "+TrifficLightID+"   ");
				Font font2 = new Font("Tahoma", Font.BOLD, 16);
				lbl.setFont(font2);
				lbl.setForeground(Color.black);
				if (lights.get(i).getId() == id) {
					lbl.setOpaque(true);
					container.setBackground(Color.red);
					lbl.setForeground(Color.blue);
				}
				container.add(lbl);
				JTextField initialTField = new JTextField(""
						+ lights.get(i).getInit(), 7);
				initList.put(lights.get(i).getId(), initialTField);
				JTextField greenTField = new JTextField(""
						+ lights.get(i).getGreen(), 7);
				greenList.put(lights.get(i).getId(), greenTField);
				JTextField redTField = new JTextField(""
						+ lights.get(i).getRed(), 7);
				redList.put(lights.get(i).getId(), redTField);

				JButton submitBtn = new LightButton("submit",
						Integer.parseInt(TrifficLightID));
				submitBtn.setPreferredSize(new Dimension(100, 25));
				submitBtn.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						int id = ((LightButton) e.getSource()).getId();

						JTextField init = initList.get(id);

						JTextField red = redList.get(id);

						JTextField green = greenList.get(id);
						try {
							float redInt = Float.parseFloat(red.getText());
							float initInt = Float.parseFloat(init.getText());
							float greenInt = Float.parseFloat(green.getText());
							if (redInt < 0 || initInt < 0 || greenInt < 0)
								throw new Exception();

							tlc.setInterval("red", redInt, id);
							tlc.setInterval("green", greenInt, id);
							tlc.setInterval("initial", initInt, id);
						} catch (Exception exc) {
							JOptionPane
									.showMessageDialog(null,
											"check if the input is valid. Input needs to be a positive float number");
						}

					}

				});
				JButton removeBtn = new LightButton("remove",
						Integer.parseInt(TrifficLightID));
				removeBtn.setPreferredSize(new Dimension(100, 25));
				removeBtn.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						tlc.removeLight(((LightButton) e.getSource()).getId());
						mainFrame.TrafficPanel(0);

					}

				});
				container.add(new JLabel("      "));
				container.add(initialTField);
				container.add(new JLabel("            "));
				container.add(greenTField);
				container.add(new JLabel("             "));
				container.add(redTField);
				container.add(new JLabel("      "));
				container.add(submitBtn);
				container.add(removeBtn);
				panel.add(container);

			}
		} else {
			JPanel container = new JPanel();
			container.setBackground(Color.WHITE);
			container.setPreferredSize(new Dimension(920, 58));
			TrifficLightID = ""; // THIS IS NEW
			TrafficLight cl = null;
			for (int i = 0; i < lights.size(); i++) {

				if (lights.get(i).getId() == id) {
					cl = lights.get(i);
					TrifficLightID += cl.getId();
					break;
				}

			}
			JLabel lbl = new JLabel("traffic light "+TrifficLightID+"   ");
			Font font2 = new Font("Tahoma", Font.BOLD, 16);
			lbl.setFont(font2);
			lbl.setForeground(Color.black);

			container.add(lbl);
			JTextField initialTField = new JTextField("" + cl.getInit(), 7);
			initList.put(cl.getId(), initialTField);
			JTextField greenTField = new JTextField("" + cl.getGreen(), 7);
			greenList.put(cl.getId(), greenTField);
			JTextField redTField = new JTextField("" + cl.getRed(), 7);
			redList.put(cl.getId(), redTField);

			JButton submitBtn = new LightButton("submit",
					Integer.parseInt(TrifficLightID));
			submitBtn.setPreferredSize(new Dimension(100, 25));
			submitBtn.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					int id = ((LightButton) e.getSource()).getId();

					JTextField init = initList.get(id);

					JTextField red = redList.get(id);

					JTextField green = greenList.get(id);
					try {
						float redInt = Float.parseFloat(red.getText());
						float initInt = Float.parseFloat(init.getText());
						float greenInt = Float.parseFloat(green.getText());
						if (redInt < 0 || initInt < 0 || greenInt < 0)
							throw new Exception();
						tlc.setInterval("red", redInt, id);
						tlc.setInterval("green", greenInt, id);
						tlc.setInterval("initial", initInt, id);
					} catch (Exception exc) {
						JOptionPane
								.showMessageDialog(null,
										"check if the input is valid. Input needs to be a positive float number");
					}

				}

			});
			JButton removeBtn = new LightButton("remove",
					Integer.parseInt(TrifficLightID));
			removeBtn.setPreferredSize(new Dimension(100, 25));
			removeBtn.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					tlc.removeLight(((LightButton) e.getSource()).getId());
					panel.removeAll();
					repaint();

				}

			});
			container.add(new JLabel("      "));
			container.add(initialTField);
			container.add(new JLabel("            "));
			container.add(greenTField);
			container.add(new JLabel("             "));
			container.add(redTField);
			container.add(new JLabel("      "));
			container.add(submitBtn);
			container.add(removeBtn);
			panel.add(container);
		}
		/*
		 * JSlider slider1 = new JSlider(); JSlider slider2 = new JSlider();
		 * JSlider slider3 = new JSlider(); JSlider slider4 = new JSlider();
		 * slider1.setPaintTicks(true); slider1.setPaintLabels(true);
		 * slider1.setMinorTickSpacing(2);
		 */

		jsp = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		panel.setBackground(Color.WHITE);
		btnHolderPanel.setPreferredSize(new Dimension(1000, 200));
		jsp.setPreferredSize(new Dimension(920, 500));
		jsp.setBackground(Color.WHITE);
		this.add(trafficLightTitle);
		this.add(jsp);
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