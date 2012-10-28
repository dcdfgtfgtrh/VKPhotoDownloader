package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.security.CryptoPrimitive;
import java.util.HashMap;
import java.util.LinkedList;
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
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import crypt.CryptManager;
import crypt.CryptMode;

import utils.Utils;
import vkapi.ApiUtils;
import vkapi.VKAlbum;
import vkapi.VKException;
import vkapi.VKPhoto;

import manager.Manager;

public class MainFrame extends JFrame
{  
    JPanel backgroundPanel,topPanel,mainPanel, loginPanel, downloadPanel,waitPanel,logoutAndCheckPanel,pathPanel;
    JPanel loginFPanel;
    JScrollPane albumsPane;
    JLabel nameL, passL,pathLabel;
    JTextField name, pass,pathField;
    JButton login,logout,checkAll,uncheckAll,path,download;
    Manager manager;
    File pathFile;
    LinkedList<Album> albumIcons;
    Dimension preferredSize;
    
    public MainFrame() throws IOException
    {
	super();
	SplashScreen splash = new SplashScreen(getToolkit().createImage(CryptManager.decodeResource("img/splash.res")), 400, 500);
	setIconImage(getToolkit().createImage(CryptManager.decodeResource("img/VK.res")));
	manager = new Manager();
	
	final VerticalLayout vertLayout = new VerticalLayout(0, VerticalLayout.CENTER,VerticalLayout.CENTER);
	loginPanel = new JPanel(vertLayout);
	
	nameL = new JLabel("Email или номер телефона:");
	name = new JTextField(15);
	name.setFont(new Font("Font",Font.PLAIN,14));
	
	passL = new JLabel("Пароль:");
	pass = new JPasswordField(15);
	pass.setFont(new Font("Font",Font.PLAIN,14));
	pass.addKeyListener(new KeyListener()
	{
	    
	    @Override
	    public void keyTyped(KeyEvent e)
	    {
		// TODO Auto-generated method stub
		
	    }
	    
	    @Override
	    public void keyReleased(KeyEvent e)
	    {
		// TODO Auto-generated method stub
		
	    }
	    
	    @Override
	    public void keyPressed(KeyEvent e)
	    {
		// TODO Auto-generated method stub
		if (e.getKeyCode()==KeyEvent.VK_ENTER)
		{
		    login.doClick();
		}
	    }
	});
	
	login = ButtonFactory.getBlueButton("Войти");
	login.setFont(new Font("Font",Font.BOLD,18));
	login.setPreferredSize(new Dimension(150,35));
	login.addActionListener(new ActionListener()
	{
	    
	    @Override
	    public void actionPerformed(ActionEvent e)
	    {
		waitPanel = new JPanel(vertLayout);
		JPanel waitFlowPanel =new JPanel(new FlowLayout(FlowLayout.CENTER,15,5));
		JLabel preloaderLabel = null;
		try
		{
		    preloaderLabel = new JLabel(new ImageIcon(CryptManager.decodeResource("img/preload.res")));
		} catch (IOException e1)
		{
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		}
		JLabel waitLabel = new JLabel("Подождите пожалуйста...");
		waitLabel.setForeground(Manager.blue);
		waitLabel.setFont(new Font("Font",Font.BOLD,16));
		waitLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
		waitFlowPanel.add(preloaderLabel);
		waitFlowPanel.add(waitLabel);
		waitPanel.add(waitFlowPanel);
		waitPanel.setOpaque(false);
		waitFlowPanel.setOpaque(false);
		
		backgroundPanel.removeAll();
		backgroundPanel.add(waitPanel);
		backgroundPanel.validate();
		//backgroundPanel.repaint();
		
		Thread makeDnldPanel = new Thread(new Runnable()
		{
		    
		    @Override
		    public void run()
		    {
			try
			{
			    downloadPanel = createDnldPanel(name.getText(), pass.getText());
			    //mainPanel = downloadPanel;
			    backgroundPanel.removeAll();
			    backgroundPanel.add(downloadPanel);
			    backgroundPanel.validate();
			    backgroundPanel.repaint();
			} catch (VKException e)
			{
			    try
			    {
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
				backgroundPanel.removeAll();
				backgroundPanel.add(loginPanel);
				backgroundPanel.validate();
				backgroundPanel.repaint();
			    } catch (Exception ex)
			    {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			    }
			} catch (IOException e)
			{
			    // TODO Auto-generated catch block
			}
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
	passPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
	
	loginPanel.add(namePanel);
	loginPanel.add(passPanel);
	loginPanel.add(login);
	
	/*
	 * forming TopPanel
	 */
	topPanel = new BackgroundPanel(getToolkit().createImage(CryptManager.decodeResource("img/TopTexture.res")), 30, 30);
	//topPanel = new JPanel();
	//topPanel.setBackground(Manager.blue);
	topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
	topPanel.setBorder(BorderFactory.createCompoundBorder(
		BorderFactory.createMatteBorder(2, 2, 0, 2, Manager.blue), 
		BorderFactory.createEmptyBorder(7, 7, 7, 7)));
	JLabel VKName = new JLabel("VK Photo Downloader beta4");
	VKName.setForeground(Color.WHITE);
	VKName.setFont(new Font("Font", Font.BOLD, 20));
	ImageIcon minimIcon=new ImageIcon(CryptManager.decodeResource("img/buttons/minimize.res"));
	JLabel minimize = new JLabel(minimIcon);
	minimize.setToolTipText("Свернуть");
	minimize.addMouseListener(new MouseAdapter() {
		public void mouseReleased(MouseEvent e){
		    setState(JFrame.ICONIFIED);
		}
		public void mouseEntered(MouseEvent e) {
		    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		}
		public void mouseExited(MouseEvent e) {
		    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	});
	ImageIcon exitIcon=new ImageIcon(CryptManager.decodeResource("img/buttons/exit.res"));
	JLabel exit = new JLabel(exitIcon);
	exit.setToolTipText("Закрыть");
	exit.addMouseListener(new MouseAdapter() {
		public void mouseReleased(MouseEvent e){
			System.exit(0);
		}
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			setCursor(new Cursor(Cursor.HAND_CURSOR));
		}
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	});
	topPanel.add(Box.createHorizontalGlue());
	topPanel.add(VKName);
	topPanel.add(Box.createHorizontalGlue());
	topPanel.add(minimize);
	topPanel.add(Box.createHorizontalStrut(5));
	topPanel.add(exit);
	
	setTransparent(new JPanel[]{loginPanel,namePanel,passPanel});
	backgroundPanel = new BackgroundPanel(getToolkit().createImage(CryptManager.decodeResource("img/BackgroundTexture.res")), 60, 60);
	//backgroundPanel = new JPanel();
	//backgroundPanel.setBackground(Color.white);
	backgroundPanel.setLayout(new BoxLayout(backgroundPanel, BoxLayout.Y_AXIS));
	//backgroundPanel.add(Box.createVerticalGlue());
	backgroundPanel.add(loginPanel);
	backgroundPanel.setBorder(BorderFactory.createCompoundBorder(
		BorderFactory.createMatteBorder(1, 10,10, 10, Manager.blue), 
		BorderFactory.createEmptyBorder(30, 50, 20, 50)));
	getContentPane().add(topPanel,BorderLayout.NORTH);
	getContentPane().add(backgroundPanel);
	FrameDragger dragger = new FrameDragger(this);
	topPanel.addMouseListener(dragger);
	topPanel.addMouseMotionListener(dragger);
	FrameResizer resizer = new FrameResizer(this);
	addMouseListener(resizer);
	addMouseMotionListener(resizer);
	setMinimumSize(new Dimension(460,450));
	setSize(new Dimension(705,625));
	setLocation(getToolkit().getScreenSize().width/6, getToolkit().getScreenSize().height/6);
	setUndecorated(true);
	splash.dispose();
	setVisible(true);
    }
    
    /*
     * form the download panel
     */
    private JPanel createDnldPanel(String login, String pass) throws IOException, VKException
    {
	manager.doLogin(login, pass);	
	JPanel downloadPanel = BoxLayoutUtils.createVerticalPanel();
	/*
	 * form the top panel with "logout", "check all", "check none" buttons 
	 * and add it to the download panel
	 */
	logoutAndCheckPanel = BoxLayoutUtils.createHorizontalPanel();
	//logout button
	logout = ButtonFactory.getBlueButton("Выйти");
	logout.setFont(new Font("Font",Font.BOLD,16));
	logout.setMaximumSize(new Dimension(120, 40));
	logout.addActionListener(new ActionListener()
	{
	    
	    @Override
	    public void actionPerformed(ActionEvent e)
	    {
		// TODO Auto-generated method stub
		manager.doLogout();
		backgroundPanel.removeAll();
		backgroundPanel.add(loginPanel);
		backgroundPanel.validate();
		backgroundPanel.repaint();
	    }
	});
	//checkAll button
	checkAll = ButtonFactory.getBlueButton(new ImageIcon(CryptManager.decodeResource("img/Checked2.res")));
	setPreferredWidth(checkAll, 80);
	checkAll.setToolTipText("Выбрать все альбомы");
	checkAll.addActionListener(new ActionListener()
	{
	    
	    @Override
	    public void actionPerformed(ActionEvent e)
	    {
		// TODO Auto-generated method stub
		for (Album album : albumIcons)
		{
		    if (album.isChecked==false)
		    {
			album.setChecked(true);
		    }
		}
	    }
	});
	//uncheckAll button
	uncheckAll = ButtonFactory.getBlueButton(new ImageIcon(CryptManager.decodeResource("img/unChecked2.res")));
	setPreferredWidth(uncheckAll, 80);
	uncheckAll.setToolTipText("Снять выбор");
	uncheckAll.addActionListener(new ActionListener()
	{
	    
	    @Override
	    public void actionPerformed(ActionEvent e)
	    {
		// TODO Auto-generated method stub
		for (Album album : albumIcons)
		{
		    if (album.isChecked==true)
		    {
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
	downloadPanel.add(logoutAndCheckPanel);
	downloadPanel.add(Box.createVerticalStrut(7));
	
	/*
	 * form the panel with album icons
	 * and add it to the download panel
	 */
	JPanel albumsInnerPane = new JPanel(new WrapLayout(FlowLayout.LEFT,12,7));	
	albumsInnerPane.setBackground(Color.WHITE);
	LinkedList<VKAlbum> albums = ApiUtils.getAlbums(manager.api);
	/*
	 * api doesn`t provide the opportunity to get service albums within getAlbums method
	 * so, add them manually
	 */
	try
	{
	    VKAlbum service = new VKAlbum();
	    service.aid="profile";
	    service.thumb_src=ApiUtils.getPhotosByAlbum(manager.api, service.aid).getLast().src;
	    service.title="Фото со стены";
	    albums.add(service);
	}catch (NoSuchElementException e)
	{
	    
	}
	/*
	 * fill the albumIcons list with the icons
	 * create labels with album names
	 * add them to the panels
	 * add panels to the scrollPane
	 */
	albumIcons = new LinkedList<Album>();
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
	    panel.add(label);
	    panel.setOpaque(false);
	    albumsInnerPane.add(panel);
	}
	albumsPane = new JScrollPane(albumsInnerPane,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	albumsPane.setPreferredSize(new Dimension(750,500));
	downloadPanel.add(albumsPane);
	downloadPanel.add(Box.createVerticalStrut(7));
	
	/*
	 * form the panel which provides path choosing
	 * and add it to the download panel
	 */
	//pathPanel = new JPanel();
	pathPanel = BoxLayoutUtils.createHorizontalPanel();
	pathLabel = new JLabel("Путь: ");
	pathField = new JTextField(15);
	GUITools.fixTextFieldSize(pathField);
	pathFile = new File("C:\\Фото\\");
	pathField.setText(pathFile.toString());
	pathField.setEnabled(false);
	pathField.setDisabledTextColor(Color.BLACK);
	path = ButtonFactory.getBlueButton(new ImageIcon(CryptManager.decodeResource("img/View.res")));
	path.setMaximumSize(new Dimension(60,40));
	setPreferredWidth(path, 90);
	path.addActionListener(new ActionListener()
	{

	    @Override
	    public void actionPerformed(ActionEvent arg0)
	    {
		JFileChooser fileChooser = new JFileChooser(
			"Выберите каталог для сохранения:");
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int res = fileChooser.showOpenDialog(MainFrame.this);
		if (res == JFileChooser.APPROVE_OPTION)
		{
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
	downloadPanel.add(pathPanel);
	downloadPanel.add(Box.createVerticalStrut(20));
	
	/*
	 * add the download button
	 */
	download = ButtonFactory.getBlueButton("Скачать!");
	download.setFont(new Font("Font",Font.BOLD,20));
	setPreferredWidth(download, 150);
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
	setTransparent(new JPanel[]{downloadPanel,logoutAndCheckPanel,pathPanel});
	JPanel panel = new JPanel();
	panel.setOpaque(false);
	panel.add(download);
	downloadPanel.add(panel);	
	return downloadPanel;
    }
    
    private void setTransparent(JPanel[] panels)
    {
	for (JPanel panel : panels)
	{
	    panel.setOpaque(false);
	}
    }
    
    private void setPreferredWidth(JComponent comp,int width)
    {
	preferredSize = comp.getPreferredSize();
	preferredSize.width=width;
	comp.setPreferredSize(preferredSize);
    }
    public static void main(String... args)
    {
	try
	{
	    new MainFrame();
	} catch (Exception e)
	{
	    // TODO Auto-generated catch block
	    //System.exit(0);
	    e.printStackTrace();
	}
    }
}
