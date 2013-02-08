package com.vaka.vkdownloader.gui;

import java.io.IOException;

import javax.swing.plaf.SliderUI;

public class ProgressFrameTester {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		ProgressFrame frame = new ProgressFrame(50);
		for (int i=0;i<50;i++){
			frame.next();
			Thread.sleep(100);
		}
		System.exit(0);
	}

}
