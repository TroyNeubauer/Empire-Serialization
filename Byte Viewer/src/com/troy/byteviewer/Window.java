package com.troy.byteviewer;

import java.awt.Color;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;

public class Window extends JFrame {
	private MainPanel panel;

	public Window() {
		super("Troy's Viewer");
		setSize(1280, 710);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBackground(Color.WHITE);
		setLocationRelativeTo(null);
		setJMenuBar(new Menu());
		panel = new MainPanel(Main.settings);
		add(panel);
		addComponentListener(new ComponentAdapter() {
		    public void componentResized(ComponentEvent componentEvent) {
		    	panel.onResize();
		    }
		});
		setVisible(true);
		panel.onResize();
	}

	public MainPanel getMainPanel() {
		return panel;
	}

	public void onResize() {
		panel.onResize();
	}
}
