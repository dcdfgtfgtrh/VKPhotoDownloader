package com.vaka.vkdownloader.gui;

import javax.swing.*;

import java.awt.*;

public class SplashScreen extends JWindow {
	private Image capture;
	
	public SplashScreen(final Image splash, int width, int height) {
		super();
		// Size and location on the screen
		setSize(width, height);
		setLocation(getToolkit().getScreenSize().width / 2 - getWidth() / 2,
				getToolkit().getScreenSize().height / 2 - getHeight() / 2);
		// Make screenshot
		try {
			Robot robot = new Robot();
			capture = robot.createScreenCapture(new Rectangle(getLocation().x,
					getLocation().y, getLocation().x + getWidth(),
					getLocation().y + getHeight()));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// Add splashscreen
		getContentPane().add(new JComponent() {
			@Override
			public void paintComponent(Graphics g) {
				// draw screen copy
				g.drawImage(capture, 0, 0, this);
				// draw splash above the copy
				g.drawImage(splash, 0, 0, this);
			}
		});
		setVisible(true);
	}
}