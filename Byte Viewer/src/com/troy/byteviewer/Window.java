package com.troy.byteviewer;

import java.awt.Color;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

public class Window extends JFrame {
	private MainPanel panel;

	public Window() {
		super("Troy's Viewer");
		setFocusable(true);
		requestFocusInWindow();
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
		addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				panel.onKeyPressed(e);
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				panel.onKeyPressed(e);
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

	public boolean needsRepaint() {
		return panel.needsRepaint();
	}
}
