package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import vkapi.ApiUtils;
import vkapi.VKException;

import manager.Manager;

// VK Photo Downloader
public class MainFrame_old extends JFrame
{
    JPanel main, loginPanel, downloadPanel, namePanel, passPanel;
    JLabel nameL, passL, pathL;
    JTextField name, pass;
    JButton    dnld, path;
    File       file;

    public MainFrame_old()
    {
	super("VK Photo Downloader pre-alpha4");
	setDefaultCloseOperation(EXIT_ON_CLOSE);
	pathL = new JLabel(
		"Путь не указан. Сохранение будет выполнено в папку C:/Фото.");

	main = BoxLayoutUtils.createHorizontalPanel();
	loginPanel = BoxLayoutUtils.createVerticalPanel();
	namePanel = BoxLayoutUtils.createHorizontalPanel();
	passPanel = BoxLayoutUtils.createHorizontalPanel();
	downloadPanel = BoxLayoutUtils.createVerticalPanel();

	nameL = new JLabel("Email или номер телефона:");
	name = new JTextField(15);
	namePanel.add(nameL);
	namePanel.add(Box.createHorizontalStrut(12));
	namePanel.add(name);

	passL = new JLabel("Пароль:");
	pass = new JPasswordField(15);
	passPanel.add(passL);
	passPanel.add(Box.createHorizontalStrut(12));
	passPanel.add(pass);

	loginPanel.add(namePanel);
	loginPanel.add(Box.createVerticalStrut(5));
	loginPanel.add(passPanel);
	loginPanel.add(Box.createVerticalStrut(5));

	GUITools.makeSameSize(new JComponent[] { nameL, passL });
	GUITools.fixTextFieldSize(name);
	GUITools.fixTextFieldSize(pass);

	dnld = new JButton("Скачать!");
	dnld.addActionListener(new ActionListener()
	{

	    @Override
	    public void actionPerformed(ActionEvent arg0)
	    {
		if (file == null)
		{
		    file = new File("C:/Фото/");
		}
		Runnable run = new Runnable()
		{

		    @Override
		    public void run()
		    {
			Manager manager = new Manager();
			
			try
			{
			    manager.doLogin(name.getText(), pass.getText());
			    manager.savePhotosToDrive(file,
				    ApiUtils.makeAlbumsPhotosMap(manager.api));
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
				JOptionPane.showMessageDialog(MainFrame_old.this,
					e.getMessage());
			    } catch (Exception ex)
			    {
				ex.printStackTrace();
			    }
			} catch (IOException e)
			{
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			}
		    }
		};
		Thread savePhotos = new Thread(run);
		savePhotos.start();
	    }

	});
	downloadPanel.add(dnld);
	downloadPanel.add(Box.createVerticalStrut(5));
	path = new JButton("Путь");
	path.addActionListener(new ActionListener()
	{

	    @Override
	    public void actionPerformed(ActionEvent arg0)
	    {
		// TODO Auto-generated method stub
		JFileChooser fileChooser = new JFileChooser(
			"Выберите каталог для сохранения:");
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int res = fileChooser.showOpenDialog(MainFrame_old.this);
		// если файл выбран, покажем его
		if (res == JFileChooser.APPROVE_OPTION)
		{
		    file = fileChooser.getSelectedFile();
		    pathL.setText("Путь: \"" + file.toString() + "\"");
		    MainFrame_old.this.pack();
		}
	    }
	});
	downloadPanel.add(path);
	GUITools.makeSameSize(new JComponent[] { dnld, path });

	main.add(loginPanel);
	main.add(Box.createHorizontalStrut(25));
	main.add(downloadPanel);
	main.setBorder(BorderFactory.createEmptyBorder(20, 20, 5, 20));

	JPanel pathPanel = new JPanel();
	pathPanel.add(pathL);
	pathPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

	getContentPane().add(pathPanel, BorderLayout.SOUTH);
	getContentPane().add(main);
	setMinimumSize(new Dimension(200, 100));
	setResizable(false);
	setLocation(getToolkit().getScreenSize().width / 6, getToolkit()
		.getScreenSize().height / 6);
	pack();
	setVisible(true);
    }

    public static void main(String... args)
    {
	new MainFrame_old();
    }
}
