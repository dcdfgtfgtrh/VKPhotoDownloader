package com.vaka.vkdownloader.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
import com.vaka.vkdownloader.gui.utils.GUITools;
import com.vaka.vkdownloader.utils.Utils;

import crypt.CryptManager;

public class DownloadPanel extends JPanel {
	JPanel backgroundPanel, loginPanel, logoutAndCheckPanel, pathPanel;
	JScrollPane albumsPane;
	JLabel pathLabel;
	JTextField pathField;
	JButton logout, checkAll, uncheckAll, path, download;
	File pathFile;
	List<Album> albumIcons;
	public DownloadPanel(String login, String pass) throws IOException,
			VKException {
		super();
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		Utils.doLogin(login, pass);
		
		/*
		 * form the top panel with "logout", "check all", "check none" buttons
		 * and add it to the download panel
		 */
		logoutAndCheckPanel = BoxLayoutUtils.createHorizontalPanel();
		
		// logout button
		logout = ButtonFactory.getBlueButton("Выйти");
		logout.setFont(new Font("Font", Font.BOLD, 16));
		logout.setMaximumSize(new Dimension(120, 40));
		logout.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Utils.doLogout();
				
				backgroundPanel= (JPanel) getParent();
				backgroundPanel.removeAll();
				backgroundPanel.add(LoginPanel.getLoginPanel());
				backgroundPanel.validate();
			}
		});
		
		// checkAll button
		checkAll = ButtonFactory.getBlueButton(new ImageIcon(CryptManager
				.decodeResource("img/Checked2.res")));
		GUITools.setPreferredWidth(checkAll, 80);
		checkAll.setToolTipText("Выбрать все альбомы");
		checkAll.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (albumIcons==null){return;}
				for (Album album : albumIcons) {
					if (album.isChecked == false) {
						album.setChecked(true);
					}
				}
			}
		});
		
		// uncheckAll button
		uncheckAll = ButtonFactory.getBlueButton(new ImageIcon(CryptManager
				.decodeResource("img/unChecked2.res")));
		GUITools.setPreferredWidth(uncheckAll, 80);
		uncheckAll.setToolTipText("Снять выбор");
		uncheckAll.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (albumIcons==null){return;}
				for (Album album : albumIcons) {
					if (album.isChecked == true) {
						album.setChecked(false);
					}
				}
			}
		});
		
		logoutAndCheckPanel.add(logout);
		logoutAndCheckPanel.add(Box.createHorizontalGlue());
		logoutAndCheckPanel.add(checkAll);
		logoutAndCheckPanel.add(Box.createHorizontalStrut(5));
		logoutAndCheckPanel.add(uncheckAll);
		add(logoutAndCheckPanel);
		add(Box.createVerticalStrut(7));

		/*
		 * form the panel with album icons and add it to the download panel
		 */
		JPanel albumsInnerPane = new JPanel(new WrapLayout(FlowLayout.LEFT, 12,	7));
		albumsInnerPane.setBackground(Color.WHITE);
		List<VKAlbum> albums = ApiUtils.getAlbums(Utils.api);
		/*
		 * api doesn`t provide the opportunity to get service albums within
		 * getAlbums method so, add them manually
		 */
		VKAlbum service = new VKAlbum();
		service.aid = "profile";
		List<VKPhoto> photos= ApiUtils.getPhotosByAlbum(Utils.api, service.aid);
		if (photos.isEmpty() == false) {
			service.thumb_src = photos.get(photos.size() - 1).src;
			service.title = "Фото со стены";
			albums.add(service);
		}

		/*
		 * fill the albumIcons list with the icons. Create labels with album
		 * names add them to the panels add panels to the scrollPane
		 */
		albumIcons = new LinkedList<Album>();
		for (VKAlbum album : albums) {
			Album alb = new Album(album);
			albumIcons.add(alb);
			JPanel panel = BoxLayoutUtils.createVerticalPanel();
			panel.add(alb);
			panel.add(Box.createVerticalStrut(5));
			JTextArea label = new JTextArea(album.title);
			// TODO fix align with darwinsys StringAlign.java
			label.setPreferredSize(new Dimension(130, 30));
			label.setEditable(false);
			label.setLineWrap(true);
			label.setWrapStyleWord(true);
			label.setOpaque(false);
			panel.add(label);
			panel.setOpaque(false);
			albumsInnerPane.add(panel);
		}
		
		albumsPane = new JScrollPane(albumsInnerPane,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		albumsPane.setPreferredSize(new Dimension(750, 500));
		add(albumsPane);
		add(Box.createVerticalStrut(7));

		/*
		 * form the panel which provides path choosing and add it to the
		 * download panel
		 */
		pathPanel = BoxLayoutUtils.createHorizontalPanel();
		pathLabel = new JLabel("Путь: ");
		pathField = new JTextField(15);
		GUITools.fixTextFieldSize(pathField);
		pathFile = new File("C:\\Фото\\");
		pathField.setText(pathFile.toString());
		pathField.setEnabled(false);
		pathField.setDisabledTextColor(Color.BLACK);
		path = ButtonFactory.getBlueButton(new ImageIcon(CryptManager
				.decodeResource("img/View.res")));
		path.setMaximumSize(new Dimension(60, 40));
		GUITools.setPreferredWidth(path, 90);
		path.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser(
						"Выберите каталог для сохранения:");
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int res = fileChooser.showOpenDialog(getTopLevelAncestor());
				if (res == JFileChooser.APPROVE_OPTION) {
					pathFile = fileChooser.getSelectedFile();
					pathField.setText(pathFile.toString() + "\\");
				}
			}
		});
		pathPanel.add(pathLabel);
		pathPanel.add(Box.createHorizontalStrut(5));
		pathPanel.add(pathField);
		pathPanel.add(Box.createHorizontalStrut(5));
		pathPanel.add(path);
		add(pathPanel);
		add(Box.createVerticalStrut(20));

		/*
		 * add the download button
		 */
		download = ButtonFactory.getBlueButton("Скачать!");
		download.setFont(new Font("Font", Font.BOLD, 20));
		GUITools.setPreferredWidth(download, 150);
		download.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (pathFile == null) {
					pathFile = new File("C:/Фото/");
				}
				Thread savePhotos = new SavePhotosThread(albumIcons, pathFile);
				savePhotos.start();
			}

		});
		GUITools.makeTransparent(new JPanel[] {this, logoutAndCheckPanel, pathPanel });
		//wrap it in flow layout
		JPanel flowLayoutPanel = new JPanel();
		flowLayoutPanel.setOpaque(false);
		flowLayoutPanel.add(download);
		add(flowLayoutPanel);
	}
}
