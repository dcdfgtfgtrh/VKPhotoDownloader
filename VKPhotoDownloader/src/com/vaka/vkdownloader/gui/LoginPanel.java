package com.vaka.vkdownloader.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.vaka.vkapi.VKException;
import com.vaka.vkdownloader.gui.utils.ButtonFactory;
import com.vaka.vkdownloader.gui.utils.GUITools;
import com.vaka.vkdownloader.utils.Utils;

import crypt.CryptManager;

public class LoginPanel extends JPanel {

	private static LoginPanel loginPanel;
	JPanel parentPanel, downloadPanel,
			waitPanel;
	JLabel nameLabel, passLabel;
	JTextField nameField, passField;
	JButton login;
	
	private LoginPanel() {
		setLayout(new VerticalLayout(0,
				VerticalLayout.CENTER, VerticalLayout.CENTER));
		parentPanel=(JPanel) getParent();
		
		nameLabel = new JLabel("Email или номер телефона:");
		nameField = new JTextField(15);
		nameField.setFont(new Font("Font", Font.PLAIN, 14));

		passLabel = new JLabel("Пароль:");
		passField = new JPasswordField(15);
		passField.setFont(new Font("Font", Font.PLAIN, 14));
		passField.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {}
			@Override
			public void keyReleased(KeyEvent e) {}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					login.doClick();
				}
			}
		});

		login = ButtonFactory.getBlueButton("Войти");
		login.setFont(new Font("Font", Font.BOLD, 18));
		login.setPreferredSize(new Dimension(150, 35));
		login.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				waitPanel = new WaitPanel(new VerticalLayout(0,
						VerticalLayout.CENTER, VerticalLayout.CENTER));
				
				parentPanel=(JPanel) getParent();
				parentPanel.removeAll();
				parentPanel.add(waitPanel);
				parentPanel.validate();

				showDownloadPanel();
			}
		});
		GUITools.makeSameSize(new JComponent[] { nameLabel, passLabel });
		GUITools.fixTextFieldSize(nameField);
		GUITools.fixTextFieldSize(passField);

		JPanel namePanel = new JPanel();
		namePanel.add(nameLabel);
		namePanel.add(nameField);

		JPanel passPanel = new JPanel();
		passPanel.add(passLabel);
		passPanel.add(passField);
		passPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

		add(namePanel);
		add(passPanel);
		add(login);
		GUITools.makeTransparent(new JPanel[] {this, namePanel, passPanel });
	}
	
	private void showDownloadPanel(){
		Thread showDnldPanel = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					downloadPanel = new DownloadPanel(
							nameField.getText(), passField.getText());
					parentPanel.removeAll();
					parentPanel.add(downloadPanel);
					parentPanel.validate();
					parentPanel.repaint();
				} catch (VKException e) {
					try {
						Sequencer player = MidiSystem.getSequencer();
						player.open();
						Sequence seq = new Sequence(Sequence.PPQ, 4);
						Track track = seq.createTrack();
						ShortMessage a = new ShortMessage();
						a.setMessage(144, 1, 44, 100);
						MidiEvent noteOn = new MidiEvent(a, 1);
						track.add(noteOn);
						player.setSequence(seq);
						player.start();
						//This might be dangerous
						JOptionPane.showMessageDialog(null,
								e.getMessage());
						parentPanel.removeAll();
						parentPanel.add(LoginPanel.this);
						parentPanel.validate();
						parentPanel.repaint();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				} catch (IOException e) {
				}
			}
		});
		showDnldPanel.start();
	}
	public static LoginPanel getLoginPanel(){
		if (loginPanel==null){
			loginPanel=new LoginPanel();
		}
		return loginPanel;
	}
}
