package gui;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import manager.Utils;

public class ButtonFactory
{
    
    public static JButton getBlueButton(String text)
    {
	JButton button = new JButton("<html><font color=\"white\">"+text);
	button.setBackground(Utils.blue);
	button.setFocusPainted(false);
	return button;
    }
    public static JButton getBlueButton(ImageIcon icon)
    {
	JButton button = new JButton(icon);
	button.setBackground(Utils.blue);
	button.setFocusPainted(false);
	return button;
    }
}
