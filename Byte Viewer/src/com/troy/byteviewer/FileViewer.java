package com.troy.byteviewer;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class FileViewer extends JPanel {
	private File file;
	private FileChannel channel;
	private JScrollPane scroll;
	private FileViewerImpl impl;

	public FileViewer(File file) throws IOException {
		this.file = file;
		channel = FileChannel.open(Paths.get(file.toURI()), StandardOpenOption.READ);
		updateSettings(new Settings());
		add(scroll);
		update();
	}

	public void updateSettings(Settings settings) {
		long length = file.length();
		long rows = length / settings.cols;
		boolean mapDirectly = rows < Integer.MAX_VALUE;
		impl = new FileViewerImpl(channel, new Settings(), mapDirectly);
		scroll = new JScrollPane(impl);
		scroll.getVerticalScrollBar().getModel().setMaximum(0);
		scroll.getVerticalScrollBar().getModel().setExtent(settings.rows);
		scroll.getVerticalScrollBar().getModel().setMaximum(mapDirectly ? (int) rows : Integer.MAX_VALUE);
	}

	private void update() {
		impl.update(scroll.getVerticalScrollBar().getValue());
	}

}
