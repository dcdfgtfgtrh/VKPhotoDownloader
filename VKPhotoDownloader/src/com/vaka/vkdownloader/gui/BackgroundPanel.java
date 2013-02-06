package com.vaka.vkdownloader.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.TexturePaint;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;


public class BackgroundPanel extends JPanel
{

    private static final long serialVersionUID = 4342737083056785514L;
    int		       width, height;
    Image image;

    public BackgroundPanel(Image image, int width, int height)
    {
	super();
	this.image = image;
	this.width = width;
	this.height = height;
    }

    @Override
	public void paintComponent(Graphics g)
    {
	super.paintComponent(g);
	Graphics2D g2D = (Graphics2D) g;

	    /*
	     * BufferedImage bi = new BufferedImage(displayImage.getWidth(this),
	     * displayImage.getHeight(this), BufferedImage.TYPE_INT_RGB);
	     */
	    BufferedImage bi = new BufferedImage(width, height,
		    BufferedImage.TYPE_INT_ARGB);
	    bi.createGraphics().drawImage(image, 0, 0, this);

	    Rectangle2D rectangle = new Rectangle2D.Float(0, 0,
		    image.getWidth(this), image.getHeight(this));

	    TexturePaint tp = new TexturePaint(bi, rectangle);
	    g2D.setPaint(tp);
	    g2D.fill(new Rectangle2D.Float(0, 0, getWidth(), getHeight()));

    }
}
