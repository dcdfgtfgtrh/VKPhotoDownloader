package com.vaka.vkdownloader.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import com.vaka.vkapi.ApiUtils;
import com.vaka.vkapi.VKAlbum;
import com.vaka.vkapi.VKException;
import com.vaka.vkapi.VKPhoto;
import com.vaka.vkdownloader.gui.utils.BoxLayoutUtils;
import com.vaka.vkdownloader.gui.utils.ButtonFactory;
import com.vaka.vkdownloader.gui.utils.FrameDragger;
import com.vaka.vkdownloader.gui.utils.FrameResizer;
import com.vaka.vkdownloader.gui.utils.GUITools;
import com.vaka.vkdownloader.utils.Utils;

import crypt.CryptManager;


public class MainFrame extends JFrame {
	JPanel backgroundPanel, headerPanel, loginPanel;
	public MainFrame() throws IOException {
		super();
		SplashScreen splash = new SplashScreen(getToolkit().createImage(
				CryptManager.decodeResource("img/splash.res")), 400, 500);
		setIconImage(getToolkit().createImage(
				CryptManager.decodeResource("img/VK.res")));

		loginPanel = LoginPanel.getLoginPanel();
		headerPanel = new TopPanel(getToolkit().createImage(
				CryptManager.decodeResource("img/TopTexture.res")), 30, 30);
		
		//Background
		backgroundPanel = new TexturedPanel(getToolkit().createImage(
				CryptManager.decodeResource("img/BackgroundTexture.res")), 60,
				60);
		backgroundPanel.setLayout(new BoxLayout(backgroundPanel,
				BoxLayout.Y_AXIS));
		backgroundPanel.add(loginPanel);
		backgroundPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(1, 10, 10, 10, Utils.blue),
				BorderFactory.createEmptyBorder(30, 50, 20, 50)));
		getContentPane().add(headerPanel, BorderLayout.NORTH);
		getContentPane().add(backgroundPanel);
		
		FrameDragger dragger = new FrameDragger(this);
		headerPanel.addMouseListener(dragger);
		headerPanel.addMouseMotionListener(dragger);
		FrameResizer resizer = new FrameResizer(this);
		addMouseListener(resizer);
		addMouseMotionListener(resizer);
		setMinimumSize(new Dimension(460, 450));
		setSize(new Dimension(705, 625));
		setLocation(getToolkit().getScreenSize().width / 6, getToolkit()
				.getScreenSize().height / 6);
		setUndecorated(true);
		splash.dispose();
		setVisible(true);
	}


	public static void main(String... args) {
		try {
			new MainFrame();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
