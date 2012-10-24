package gui;

import java.awt.Color;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import manager.Manager;

public class ButtonFactory
{
    
    public static JButton getBlueButton(String text)
    {
	JButton button = new JButton("<html><font color=\"white\">"+text);
	button.setBackground(Manager.blue);
	button.setFocusPainted(false);
	return button;
    }
    public static JButton getBlueButton(ImageIcon icon)
    {
	JButton button = new JButton(icon);
	button.setBackground(Manager.blue);
	button.setFocusPainted(false);
	return button;
    }
}
