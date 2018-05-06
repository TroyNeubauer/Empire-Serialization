package com.troy.byteviewer;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

public class MainPanel extends JTabbedPane {

	
	
	public void openFile(File file) {
		try {
			FileViewer viewer = new FileViewer(file);
			addTab(file.getName(), viewer);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(getTopLevelAncestor(), "Error reading file:\n" + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
