package com.troy.byteviewer;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main {
	
	public static Window window;
	public static Settings settings = new Settings();
	public static ByteInterpreter byteInterpreter = ByteInterpreters.ASCII;
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		window = new Window();
		
		while(true) {
			window.onResize();
			window.repaint();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
		
	}
}
