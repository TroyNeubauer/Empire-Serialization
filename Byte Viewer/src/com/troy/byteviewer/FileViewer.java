package com.troy.byteviewer;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import javax.swing.JPanel;
import javax.swing.JScrollBar;

public class FileViewer extends JPanel {
	private File file;
	private FileChannel channel;
	private JScrollBar bar;
	private FileViewerImpl impl;

	public FileViewer(File file, Settings settings) throws IOException {
		super(new GridBagLayout(), true);
		this.file = file;
		channel = FileChannel.open(Paths.get(file.toURI()), StandardOpenOption.READ);
		updateSettings(settings);
		update();
		bar.addAdjustmentListener(new AdjustmentListener() {

			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				update();
			}
		});
		this.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				bar.setValue(bar.getValue() + e.getWheelRotation());
				update();
			}
		});
	}
	

	public void updateSettings(Settings settings) {
		this.removeAll();
		impl = new FileViewerImpl(channel, new Settings(), false, file.length());
		bar = new JScrollBar(JScrollBar.VERTICAL, 0, 0, 0, 0);
		GridBagConstraints g = new GridBagConstraints();
		onResize();
		
		g.weightx = 1000.0;
		g.weighty = 10.0;
		g.fill = GridBagConstraints.BOTH;

		add(impl, g);

		g.gridx = 1;
		g.weightx = 1.0;
		g.fill = GridBagConstraints.VERTICAL;

		add(bar, g);
	}

	public int calculateMaxRows() {
		int height = impl.getHeight();
		height -= FileViewerImpl.Y_OFFSET;
		int rows = (int) Math.ceil((double) height / ((double) impl.fontHeight + (double) FileViewerImpl.Y_PADDING) + 0.5);
		rows = Math.max(rows, 0);
		return rows;
	}

	private void update() {
		impl.update(bar.getValue());
	}

	public void close() {
		try {
			channel.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void onResize() {
		int rows = calculateMaxRows();
		bar.getModel().setExtent(rows);
		long totalRows = impl.length / impl.cols + 10;
		boolean mapDirectly = totalRows < Integer.MAX_VALUE;
		impl.mapDirectly = mapDirectly;
		int max = mapDirectly ? (int) totalRows : Integer.MAX_VALUE;
		bar.getModel().setMaximum(max);
		impl.updateSize(rows, impl.cols);
	}

}
