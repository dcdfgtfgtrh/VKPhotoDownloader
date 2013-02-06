package com.vaka.vkdownloader.gui.utils;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;

public class FrameResizer implements MouseListener, MouseMotionListener {
	private JFrame frame;
	private Point lastDragPosition;
	private int dragDirection;
	private static final int WEST = 4;
	private static final int EAST = 3;
	private static final int NORTH = 1;
	private static final int SOUTH = 2;
	private static final int SOUTH_EAST = 10;
	private static final int NORTH_WEST = 20;
	private static final int SOUTH_WEST = 30;
	private static final int NORTH_EAST = 40;

	public static int RESIZE_BORDER_SIZE = 10;
	public static int RESIZE_CORNER_SIZE = 10;

	public FrameResizer(JFrame frame) {
		this.frame = frame;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		Point currentDragPosition = e.getLocationOnScreen();
		int deltaX = currentDragPosition.x - lastDragPosition.x;
		int deltaY = currentDragPosition.y - lastDragPosition.y;
		Rectangle currentBounds = frame.getBounds();

		if (deltaX == 0 && deltaY == 0)
			return;

		int x = currentBounds.x;
		int y = currentBounds.y;
		int width = currentBounds.width;
		int height = currentBounds.height;

		if (dragDirection == WEST | (dragDirection == SOUTH_WEST)) {
			if (frame.getSize().width > frame.getMinimumSize().width) {
				x = currentBounds.x + deltaX;
				width = currentBounds.width - deltaX;
			} else if (deltaX < 0) {
				x = currentBounds.x + deltaX;
				width = currentBounds.width - deltaX;
			}
		}

		if ((dragDirection == EAST) | (dragDirection == SOUTH_EAST)) {
			width = currentBounds.width + deltaX;
		}

		if (dragDirection == NORTH) {
			y = currentBounds.y + deltaY;
			height = currentBounds.height - deltaY;
		}

		if ((dragDirection == SOUTH) | (dragDirection == SOUTH_EAST)
				| (dragDirection == SOUTH_WEST)) {
			height = currentBounds.height + deltaY;
		}

		frame.setBounds(x, y, width, height);
		lastDragPosition = currentDragPosition;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		int border = getBorderSide(e.getX(), e.getY());
		if (border == (SOUTH_EAST) || border == (NORTH_WEST)) {
			frame.setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
			return;
		}

		if (border == (SOUTH_WEST) || border == (NORTH_EAST)) {
			frame.setCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
			return;
		}

		if (border == EAST || border == WEST) {
			frame.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
			return;
		}

		if (border == NORTH || border == SOUTH) {
			frame.setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
			return;
		}
		frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}
	
	@Override
	public void mouseReleased(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent e) {
		lastDragPosition = e.getLocationOnScreen();
		dragDirection = getBorderSide(e.getX(), e.getY());
	}

	private int getBorderSide(int x, int y) {
		int result = 0;

		if (x <= RESIZE_BORDER_SIZE) {
			result = WEST;
		}
		if (x >= frame.getWidth() - RESIZE_BORDER_SIZE - 1) {
			result = EAST;
		}
		if (y <= RESIZE_BORDER_SIZE) {
			result = NORTH;
		}
		if (y >= frame.getHeight() - RESIZE_BORDER_SIZE) {
			result = SOUTH;
		}

		// Now the corners
		if (x >= frame.getWidth() - RESIZE_CORNER_SIZE) {
			if (y >= frame.getHeight() - RESIZE_CORNER_SIZE) {
				result = (SOUTH_EAST);
			}
			if (y <= RESIZE_CORNER_SIZE) {
				result = (NORTH_EAST);
			}
		}

		if (x <= RESIZE_CORNER_SIZE) {
			if (y >= frame.getHeight() - RESIZE_CORNER_SIZE) {
				result = (SOUTH_WEST);
			}
			if (y <= RESIZE_CORNER_SIZE) {
				result = (NORTH_WEST);
			}
		}

		return result;
	}

}
