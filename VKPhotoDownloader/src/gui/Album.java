package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JComponent;

import vkapi.VKAlbum;

public class Album extends JComponent
{
    private VKAlbum album;
    private static Logger log = Logger.getLogger(Album.class.getName());
    public boolean isChecked = false;
    
    public Album(VKAlbum album)
    {
	this.album = album;
	setPreferredSize(new Dimension(130,130));
	addMouseListener(new MouseAdapter()
	{
	    public void mouseEntered(MouseEvent e) 
	    {
		if(isChecked==false)
		{
		    setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLUE));
		}
	    }
	    public void mouseExited(MouseEvent e) 
	    {
		if(isChecked==false)
		{
		    setBorder(null);
		}
	    }
	    public void mousePressed(MouseEvent e) 
	    {
		if (isChecked == false)
		{
		    setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.BLUE));
		    isChecked=true;
		}else{
		    setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLUE));
		    isChecked=false;
		}
	    }
	});
    }
    
    public void paintComponent(Graphics g)
    {
	try
	{
	    Image image = getToolkit().getImage(new URL(album.thumb_src));
	    int x,y;
	    y=getHeight()/2-image.getHeight(this)/2;
	    x=getWidth()/2-image.getWidth(this)/2;
	    g.drawImage(image, x, y, this);
	} catch (MalformedURLException e)
	{
	    log.log(Level.SEVERE, "Can`t get image- bad url source");
	    e.printStackTrace();
	}
    }
    
    public  String getAlbumID()
    {
	return album.aid;
    }
    
    public String getThumbSrc()
    {
	return album.thumb_src;
    }
    public VKAlbum getAlbum()
    {
	return album;
    }
}
