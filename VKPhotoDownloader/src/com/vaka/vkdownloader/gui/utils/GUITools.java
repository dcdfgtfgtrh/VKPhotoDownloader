package com.vaka.vkdownloader.gui.utils;

import javax.swing.*;

import java.awt.*;

public class GUITools {
	// ���� ����� ��������� ������ ������ �� ������ JButton � ������� �� ������
	// ������ �� ������ ����� � ������
	public static void createRecommendedMargin(JButton[] buttons) {
		for (int i = 0; i < buttons.length; i++) {
			// � ������� Insets �������� ���������� �� ������ �� ������ ������
			Insets margin = buttons[i].getMargin();
			margin.left = 12;
			margin.right = 12;
			buttons[i].setMargin(margin);
		}
	}

	// ���������� ��� �������� ������ ����������� ���������� ��������
	// (�����������,
	// ���������������� � ������������).
	// ���������� ��������� ������ ������ �������� (�� ������) ���������� �
	// ������
	public static void makeSameSize(JComponent[] components) {
		// ��������� ������ �����������
		int[] sizes = new int[components.length];
		for (int i = 0; i < sizes.length; i++) {
			sizes[i] = components[i].getPreferredSize().width;
		}
		// ����������� ������������� �������
		int maxSizePos = maximumElementPosition(sizes);
		Dimension maxSize = components[maxSizePos].getPreferredSize();
		// �������� ���������� ��������
		for (int i = 0; i < components.length; i++) {
			components[i].setPreferredSize(maxSize);
			components[i].setMinimumSize(maxSize);
			components[i].setMaximumSize(maxSize);
		}
	}

	// ��������� ��������� ���������� � �������� ���������� ���� JTextField
	public static void fixTextFieldSize(JTextField field) {
		Dimension size = field.getPreferredSize();
		// ����� ��������� ���� ��-�������� ����� ����������� ���� ������ �
		// �����
		size.width = field.getMaximumSize().width;
		// ������ ��������� ���� �� ������ ���� ����� ����������� ������
		field.setMaximumSize(size);
	}

	// ��������������� ����� ��� ����������� ������� ������������� ��������
	// �������
	private static int maximumElementPosition(int[] array) {
		int maxPos = 0;
		for (int i = 1; i < array.length; i++) {
			if (array[i] > array[maxPos])
				maxPos = i;
		}
		return maxPos;
	}

	public static void makeTransparent(JPanel[] panels) {
		for (JPanel panel : panels) {
			panel.setOpaque(false);
		}
	}

	public static void setPreferredWidth(JComponent comp, int width) {
		Dimension preferredSize = comp.getPreferredSize();
		preferredSize.width = width;
		comp.setPreferredSize(preferredSize);
	}
}