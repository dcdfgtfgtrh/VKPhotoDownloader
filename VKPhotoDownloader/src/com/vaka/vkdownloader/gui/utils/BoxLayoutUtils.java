package com.vaka.vkdownloader.gui.utils;

import javax.swing.*;

public class BoxLayoutUtils {
	// ������ ������ ������������ �� ��� X ��� ������ �����������
	public static void setGroupAlignmentX(JComponent[] cs, float alignment) {
		for (int i = 0; i < cs.length; i++) {
			cs[i].setAlignmentX(alignment);
		}
	}

	// ������ ������ ������������ �� ��� Y ��� ������ �����������
	public static void setGroupAlignmentY(JComponent[] cs, float alignment) {
		for (int i = 0; i < cs.length; i++) {
			cs[i].setAlignmentY(alignment);
		}
	}

	// ���������� ������ � ������������� ������������ ������� �������������
	public static JPanel createVerticalPanel() {
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		return p;
	}

	// ���������� ������ � ������������� �������������� ������� �������������
	public static JPanel createHorizontalPanel() {
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		return p;
	}
}