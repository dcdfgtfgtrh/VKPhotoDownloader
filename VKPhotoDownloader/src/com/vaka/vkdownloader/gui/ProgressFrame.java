package com.vaka.vkdownloader.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.vaka.vkdownloader.gui.utils.BoxLayoutUtils;
import com.vaka.vkdownloader.gui.utils.FrameDragger;
import com.vaka.vkdownloader.utils.Utils;

import crypt.CryptManager;

public class ProgressFrame extends JFrame {
	JPanel background, header,main;
	JLabel progressLabel,headerLabel;
	int count=0,numberOfElements;
	
	public ProgressFrame(int numberOfElements) throws IOException{
		super();
		this.numberOfElements=numberOfElements;
		setIconImage(getToolkit().createImage(
				CryptManager.decodeResource("img/VK.res")));
		
		background = new TexturedPanel(getToolkit().createImage(
				CryptManager.decodeResource("img/BackgroundTexture.res")), 60,
				60);
		background.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
		
		header = new TexturedPanel(getToolkit().createImage(CryptManager
				.decodeResource("img/TopTexture.res")), 30,30);
		header.setPreferredSize(new Dimension(230,30));
		header.setBorder(BorderFactory.createEmptyBorder(5, 0, 3, 0));
		headerLabel=new JLabel("<html><font color=\"white\" size=5>Подождите...");
		header.add(headerLabel);
		
		FrameDragger dragger= new FrameDragger(this);
		header.addMouseListener(dragger);
		header.addMouseMotionListener(dragger);
		
		main = BoxLayoutUtils.createVerticalPanel();
		main.add(header);
		main.add(background);
		main.setBorder(BorderFactory.createMatteBorder(1, 5, 5, 5, Utils.blue));
		add(main);
		
		progressLabel = new JLabel();
		background.add(progressLabel);
		setText(numberOfElements);
		setLocation(getToolkit().getScreenSize().width / 2, getToolkit().getScreenSize().height / 2);
		setSize(new Dimension(240, 110));
		setUndecorated(true);
		setVisible(true);
	}
	public void next(){
		++count;
		setText(numberOfElements);
	}
	private void setText(int numOfElements){
		progressLabel.setText("<html><font size=4>Сохраняю фото " + count
				+ " из " + numOfElements);
	}
}
