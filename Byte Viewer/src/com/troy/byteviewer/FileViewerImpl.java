package com.troy.byteviewer;

import java.awt.Graphics;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import javax.swing.JPanel;

public class FileViewerImpl extends JPanel {
	private FileChannel channel;
	private int rows, cols;
	private boolean mapDirectly;
	private ByteBuffer buffer;
	
	public FileViewerImpl(FileChannel channel, Settings settings, boolean mapDirectly) {
		this.channel = channel;
		this.mapDirectly = mapDirectly;
		updateSettings(settings);
	}

	private void updateSettings(Settings settings) {
		this.rows = settings.rows;
		this.cols = settings.cols;
		buffer = ByteBuffer.allocateDirect(rows * cols);
	}

	public void update(int scroll) {
		try {
			channel.position(scroll * cols);

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		g.drawString("test!", 100, 100);
		super.paintComponent(g);
	}

}
