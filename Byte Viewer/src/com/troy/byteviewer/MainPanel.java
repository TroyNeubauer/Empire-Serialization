package com.troy.byteviewer;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

public class MainPanel extends JTabbedPane {
	JFileChooser chooser = new JFileChooser();

	public MainPanel(Settings settings) {
		setFocusable(true);
		requestFocusInWindow();
	}

	public void openFile(File file) {
		try {
			FileViewer viewer = new FileViewer(file, Main.settings);
			int index = getTabCount();
			insertTab(file.getName(), null, viewer, null, index);
			setSelectedIndex(index);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(getTopLevelAncestor(), "Error reading file:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void showOpenDialog() {
		chooser.showOpenDialog(this);
		chooser.setDialogTitle("Open File");

		openFile(chooser.getSelectedFile());
	}

	public void closeSelectedFile() {
		FileViewer viewer = (FileViewer) getSelectedComponent();
		if (viewer != null) {
			removeTabAt(getSelectedIndex());
			viewer.close();
		}
	}

	public void onResize() {
		for (int i = 0; i < getTabCount(); i++) {
			FileViewer viewer = (FileViewer) getComponentAt(i);
			viewer.onResize();
		}
	}

	public void onKeyPressed(KeyEvent e) {
		if (getSelectedIndex() != -1) {
			FileViewer viewer = (FileViewer) getComponentAt(getSelectedIndex());
			viewer.onKeyPressed(e);
		}
	}

	public boolean needsRepaint() {
		if (getSelectedIndex() != -1) {
			FileViewer viewer = (FileViewer) getComponentAt(getSelectedIndex());
			return viewer.needsRepaint();
		}
		return false;
	}
}
