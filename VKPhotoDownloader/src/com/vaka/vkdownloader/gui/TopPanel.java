package com.vaka.vkdownloader.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.vaka.vkdownloader.utils.Utils;

import crypt.CryptManager;

public class TopPanel extends TexturedPanel {
	public TopPanel(Image image,int width,int height) throws IOException{
		super(image, width, height);
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(2, 2, 0, 2, Utils.blue),
				BorderFactory.createEmptyBorder(7, 7, 7, 7)));
		JLabel VKName = new JLabel("VK Photo Downloader beta4");
		VKName.setForeground(Color.WHITE);
		VKName.setFont(new Font("Font", Font.BOLD, 20));
		ImageIcon minimIcon = new ImageIcon(
				CryptManager.decodeResource("img/buttons/minimize.res"));
		JLabel minimize = new JLabel(minimIcon);
		minimize.setToolTipText("Свернуть");
		minimize.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				((Frame)getTopLevelAncestor()).setState(Frame.ICONIFIED);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		});
		ImageIcon exitIcon = new ImageIcon(
				CryptManager.decodeResource("img/buttons/exit.res"));
		JLabel exit = new JLabel(exitIcon);
		exit.setToolTipText("Закрыть");
		exit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				System.exit(0);
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		});
		add(Box.createHorizontalGlue());
		add(VKName);
		add(Box.createHorizontalGlue());
		add(minimize);
		add(Box.createHorizontalStrut(5));
		add(exit);
	}
}
