package gui;

import java.awt.Cursor;
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

import manager.Utils;

import vkapi.VKAlbum;

public class Album extends JComponent
{
    private VKAlbum album;
    private static Logger log = Logger.getLogger(Album.class.getName());
    private boolean isMouseOver = false;
    public boolean isChecked = false;
    
    public Album(VKAlbum album)
    {
	this.album = album;
	setPreferredSize(new Dimension(130,130));
	addMouseListener(new MouseAdapter()
	{
	    @Override
		public void mouseEntered(MouseEvent e) 
	    {
		isMouseOver = true;
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		repaint();
		    
	    }
	    @Override
		public void mouseExited(MouseEvent e) 
	    {
		isMouseOver = false;
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		repaint();
	    }
	    @Override
		public void mousePressed(MouseEvent e) 
	    {
		if (isChecked == false)
		{
		    isChecked=true;
		    repaint();	
		}else{
		    isChecked=false;
		    repaint();
		}
	    }
	});
    }
    
    @Override
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
	if(isChecked==true)
	{
	    setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Utils.blue));
	}else{
	    setBorder(null);
	    if(isMouseOver==true)
		{
		    setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Utils.blue));
		}
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
    public void setChecked(boolean checked)
    {
	isChecked = checked;
	repaint();
    }
}
