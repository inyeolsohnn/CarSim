package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

import javax.swing.ButtonModel;
import javax.swing.JButton;
//The creation of a custom JButton that extends the normal java swing JButton
public class CustomJButton extends JButton {

	private GradientPaint gradientPaint; // provides a way to fill a Shape with
											// a linear color gradient pattern
	private Color deafultButtonColour = new Color(255, 255, 255);
	private final Color buttonColour1 = new Color(100, 100, 100);
	private final Color buttonColour2 = new Color(255, 0, 0);

	public CustomJButton(String text) {
		super(text);
		setFont(new Font("Tahoma", Font.BOLD, 12));
		setForeground(Color.WHITE);
		setFocusable(false);
		setContentAreaFilled(false);
		setBorderPainted(false);
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		int height = getHeight();
		int width = getWidth();
		ButtonModel buttonModel = getModel();// The JButton Model

		gradientPaint = new GradientPaint(0, 0, deafultButtonColour, 0,
				height / 2, buttonColour1, true);

		g2d.setPaint(gradientPaint);
		GradientPaint gradientPainter1;

		if (!buttonModel.isPressed()) {
			gradientPainter1 = new GradientPaint(0, 0, new Color(100, 100, 100), 0,
					height - 1, new Color(0, 0, 0));
		} else {
			gradientPaint = new GradientPaint(0, 0, deafultButtonColour, 0,
					height / 2, buttonColour2, true);
			g2d.setPaint(gradientPaint);
			gradientPainter1 = new GradientPaint(0, 0, new Color(0, 0, 0), 0, height - 1,
					new Color(100, 100, 100));
		}
		RoundRectangle2D.Float r2d = new RoundRectangle2D.Float(0, 0,
				width - 1, height - 1, 10, 10);
		Shape clip = g2d.getClip();
		g2d.clip(r2d);
		g2d.fillRect(0, 0, width, height);
		g2d.setClip(clip);
		g2d.setPaint(gradientPainter1);
		g2d.drawRoundRect(0, 0, width - 1, height - 1, 10, 10);
		g2d.dispose();
		super.paintComponent(g);

	}
}
