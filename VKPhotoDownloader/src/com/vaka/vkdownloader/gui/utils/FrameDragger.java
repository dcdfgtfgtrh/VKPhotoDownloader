package com.vaka.vkdownloader.gui.utils;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;

public class FrameDragger implements MouseListener, MouseMotionListener {

	private JFrame frameToDrag;

	private Point lastDragPosition;

	public FrameDragger(JFrame frameToDrag) {
		this.frameToDrag = frameToDrag;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		Point currentDragPosition = e.getLocationOnScreen();
		int deltaX = currentDragPosition.x - lastDragPosition.x;
		int deltaY = currentDragPosition.y - lastDragPosition.y;
		if (deltaX != 0 || deltaY != 0) {
			int x = frameToDrag.getLocation().x + deltaX;
			int y = frameToDrag.getLocation().y + deltaY;
			frameToDrag.setLocation(x, y);
			lastDragPosition = currentDragPosition;
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		e.getComponent().setCursor(new Cursor(Cursor.MOVE_CURSOR));
		lastDragPosition = e.getLocationOnScreen();
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {}

	@Override
	public void mouseClicked(MouseEvent arg0) {}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent e) {
		e.getComponent().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}
}