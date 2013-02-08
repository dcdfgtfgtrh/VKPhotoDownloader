package com.vaka.vkdownloader.gui;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.LayoutManager;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.vaka.vkdownloader.utils.Utils;

import crypt.CryptManager;

public class WaitPanel extends JPanel {
	public WaitPanel(){
		super();
		JPanel waitFlowPanel = new JPanel(new FlowLayout(
				FlowLayout.CENTER, 15, 5));
		JLabel preloaderLabel = null;
		try {
			preloaderLabel = new JLabel(new ImageIcon(CryptManager
					.decodeResource("img/preload.res")));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		JLabel waitLabel = new JLabel("Подождите пожалуйста...");
		waitLabel.setForeground(Utils.blue);
		waitLabel.setFont(new Font("Font", Font.BOLD, 16));
		waitLabel.setBorder(BorderFactory
				.createEmptyBorder(10, 0, 0, 0));
		waitFlowPanel.add(preloaderLabel);
		waitFlowPanel.add(waitLabel);
		add(waitFlowPanel);
		setOpaque(false);
		waitFlowPanel.setOpaque(false);
	}
	
	public WaitPanel(LayoutManager mgr){
		this();
		setLayout(mgr);
	}
}
