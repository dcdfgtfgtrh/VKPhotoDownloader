package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import vkapi.ApiUtils;
import vkapi.VKAlbum;
import vkapi.VKException;
import vkapi.VKPhoto;

import manager.Manager;

public class MainFrame extends JFrame
{
    JPanel mainPanel, loginPanel, downloadPanel,waitPanel,logoutAndCheckPanel,pathPanel;
    JScrollPane albumsPane;
    JLabel nameL, passL,pathLabel;
    JTextField name, pass,pathField;
    JButton login,logout,checkAll,uncheckAll,path,download;
    Manager manager;
    File pathFile;
    LinkedList<Album> albumIcons;
    
    public MainFrame()
    {
	super ("VK Photo Downloader alpha1");
	setDefaultCloseOperation(EXIT_ON_CLOSE);
	
	mainPanel = new JPanel();
	loginPanel = BoxLayoutUtils.createVerticalPanel();
	
	nameL = new JLabel("Email или номер телефона:");
	name = new JTextField(15);
	
	passL = new JLabel("Пароль:");
	pass = new JPasswordField(15);
	
	login = new JButton("Войти");
	login.addActionListener(new ActionListener()
	{
	    
	    @Override
	    public void actionPerformed(ActionEvent e)
	    {
		System.out.println("in actionPerformed");
		waitPanel = BoxLayoutUtils.createVerticalPanel();
		JLabel waitLabel = new JLabel("Подождите пожалуйста");
		waitPanel.add(waitLabel);
		//mainPanel=waitPanel;
		setContentPane(waitPanel);
		validate();
		
		Thread makeDnldPanel = new Thread(new Runnable()
		{
		    
		    @Override
		    public void run()
		    {
			// TODO Auto-generated method stub
			downloadPanel = createDnldPanel(name.getText(), pass.getText());
			//mainPanel = downloadPanel;
			setContentPane(downloadPanel);
			validate();
			pack();
			System.out.println("in new thread");
		    }
		});
		makeDnldPanel.start();		
	    }
	});
	
	GUITools.makeSameSize(new JComponent[] { nameL, passL });
	GUITools.fixTextFieldSize(name);
	GUITools.fixTextFieldSize(pass);
	
	JPanel namePanel = new JPanel();
	namePanel.add(nameL);
	namePanel.add(name);
	
	JPanel passPanel = new JPanel();
	passPanel.add(passL);
	passPanel.add(pass);
	
	loginPanel.add(Box.createVerticalGlue());
	loginPanel.add(namePanel);
	loginPanel.add(Box.createVerticalStrut(5));
	loginPanel.add(passPanel);
	loginPanel.add(Box.createVerticalStrut(12));
	loginPanel.add(login);
	loginPanel.add(Box.createVerticalGlue());
	JPanel loginFPanel = new JPanel();
	loginFPanel.add(loginPanel);
	JPanel loginBPanel = BoxLayoutUtils.createVerticalPanel();
	loginBPanel.add(Box.createVerticalGlue());
	loginBPanel.add(loginFPanel);
	loginBPanel.add(Box.createVerticalGlue());
	
	mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
	mainPanel= loginBPanel;
	setContentPane(loginBPanel);
	//getContentPane().add(mainPanel);
	setSize(new Dimension(580,580));
	setVisible(true);
    }
    
    private JPanel createDnldPanel(String login, String pass)
    {
	albumIcons = new LinkedList<Album>();
	LinkedList<VKAlbum> albums=null;
	manager = new Manager(login, pass);
	JPanel downloadPanel = BoxLayoutUtils.createVerticalPanel();
	logoutAndCheckPanel = BoxLayoutUtils.createHorizontalPanel();
	logout = new JButton("Выйти");
	checkAll = new JButton("Все");
	uncheckAll = new JButton("Ни одной");
	logoutAndCheckPanel.add(logout);
	logoutAndCheckPanel.add(Box.createHorizontalGlue());
	logoutAndCheckPanel.add(checkAll);
	logoutAndCheckPanel.add(Box.createHorizontalStrut(5));
	logoutAndCheckPanel.add(uncheckAll);
	downloadPanel.add(logoutAndCheckPanel);
	downloadPanel.add(Box.createVerticalStrut(12));
	
	JPanel albumsInnerPane = new JPanel(new GridLayout(0, 3, 12, 12));
	albumsInnerPane.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
	albumsPane = new JScrollPane(albumsInnerPane);	
	try
	{
	    albums = ApiUtils.getAlbums(manager.api);
	    VKAlbum service = new VKAlbum();
	    service.aid="profile";
	    service.thumb_src=ApiUtils.getPhotosByAlbum(manager.api, service.aid).getLast().src;
	    service.title="Фото со стены";
	    albums.add(service);
	} catch (VKException e)
	{
	    try
	    {
		// TODO Auto-generated catch block

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
		JOptionPane.showMessageDialog(MainFrame.this,
			e.getMessage());
	    } catch (Exception ex)
	    {
		ex.printStackTrace();
	    }
	}
	if (albums!=null){
	    for (VKAlbum album : albums)
	    {
		Album alb = new Album (album);
		albumIcons.add(alb);
		JPanel panel = BoxLayoutUtils.createVerticalPanel();
		panel.add(alb);
		panel.add(Box.createVerticalStrut(5));
		JTextArea label = new JTextArea(album.title);
		label.setPreferredSize(new Dimension(130,30));
		label.setEditable(false);
		label.setLineWrap(true);
		label.setWrapStyleWord(true);
		label.setOpaque(false);
		//BoxLayoutUtils.setGroupAlignmentX(new JComponent[] {alb,label}, Box.LEFT_ALIGNMENT);
		panel.add(label);
		albumsInnerPane.add(panel);
	    }
	}
	
	//TODO fill this with albums
	
	downloadPanel.add(albumsPane);
	
	pathPanel = BoxLayoutUtils.createHorizontalPanel();
	pathLabel = new JLabel("Путь");
	pathField = new JTextField(15);
	GUITools.fixTextFieldSize(pathField);
	pathFile = new File("C:\\Фото\\");
	pathField.setText(pathFile.toString());
	pathField.setEnabled(false);
	path = new JButton("Обзор");
	path.addActionListener(new ActionListener()
	{

	    @Override
	    public void actionPerformed(ActionEvent arg0)
	    {
		// TODO Auto-generated method stub
		JFileChooser fileChooser = new JFileChooser(
			"Выберите каталог для сохранения:");
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int res = fileChooser.showOpenDialog(MainFrame.this);
		// если файл выбран, покажем его
		if (res == JFileChooser.APPROVE_OPTION)
		{
		    pathFile = fileChooser.getSelectedFile();
		    pathField.setText(pathFile.toString() + "\\");
		    MainFrame.this.pack();
		}
	    }
	});
	pathPanel.add(pathLabel);
	pathPanel.add(Box.createHorizontalStrut(5));
	pathPanel.add(pathField);
	pathPanel.add(Box.createHorizontalStrut(5));
	pathPanel.add(path);
	downloadPanel.add(pathPanel);
	downloadPanel.add(Box.createVerticalStrut(12));
	
	download = new JButton("Скачать!");
	download.addActionListener(new ActionListener()
	{

	    @Override
	    public void actionPerformed(ActionEvent arg0)
	    {
		if (pathFile == null)
		{
		    pathFile = new File("C:/Фото/");
		}
		Runnable run = new Runnable()
		{

		    @Override
		    public void run()
		    {
			HashMap<VKAlbum, LinkedList<VKPhoto>> map = new HashMap<VKAlbum, LinkedList<VKPhoto>>();
			for (Album alb : albumIcons)
			{
			    if (alb.isChecked)
			    {
				try
				{
				    map.put(alb.getAlbum(), ApiUtils.getPhotosByAlbum(manager.api, alb.getAlbumID()));
				} catch (VKException e)
				{
				    // TODO Auto-generated catch block
				    e.printStackTrace();
				}
			    }
			}
			manager.savePhotosToDrive(pathFile,map);
		    }
		};
		Thread savePhotos = new Thread(run);
		savePhotos.start();
	    }

	});
	downloadPanel.add(download);
	downloadPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
	
	return downloadPanel;
    }
    public static void main(String... args)
    {
	new MainFrame();
    }
}
