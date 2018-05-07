package com.troy.byteviewer;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import javax.swing.JPanel;

import com.troy.empireserialization.util.StringFormatter;

public class FileViewerImpl extends JPanel {
	protected FileChannel channel;
	protected int rows, cols;
	protected boolean mapDirectly;
	protected ByteBuffer buffer;
	protected int scroll = 0;
	protected Font font;
	protected long length;
	protected int maxDigits;
	protected int fontHeight = -1;
	// How many pixels offset in the top left takes up
	protected int xOffset = -1;
	// How wide is a single character
	private int pixelsSingleChar;
	private long selectionStart = 17, selectionEnd = 25;
	private boolean dragged = false;

	public FileViewerImpl(FileChannel channel, Settings settings, boolean mapDirectly, long length) {
		super(new FlowLayout());
		this.length = length;
		setBackground(Color.WHITE);
		this.channel = channel;
		this.mapDirectly = mapDirectly;
		updateSettings(settings);

		this.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				long x = getTileX(e.getX());
				long y = getTileY(e.getY()) + (long) scroll;
				long finalIndex = x + y * cols;
				if (dragged) {
					selectionEnd = finalIndex;
				} else {
					selectionStart = finalIndex;
				}
				dragged = true;
			}
		});
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				selectionStart = -1;
				selectionEnd = -1;
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				dragged = false;
			}
		});
	}

	private void updateSettings(Settings settings) {
		this.font = settings.font;
		updateSize(settings.rows, settings.cols);
	}

	public void updateSize(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		int newCap = rows * cols;
		if (buffer == null || (buffer != null && buffer.capacity() != newCap)) {
			buffer = ByteBuffer.allocateDirect(newCap);
		}
		if (length == 0) {
			maxDigits = 1;
		} else {
			maxDigits = (int) Math.ceil(Math.log(length) / Math.log(cols));
		}
	}

	public void update(int scroll) {
		this.scroll = scroll;
		repaint();
	}

	protected static final int Y_OFFSET = 50, Y_PADDING = 0, X_PADDING = 4, X_OFFSET = 20;
	private static final String OFFSET = "Offset";
	private static final int OFFSET_X = 5, OFFSET_Y = 23, BOUNDS_OFFSET = 15;

	@Override
	protected void paintComponent(Graphics g) {
		try {
			super.paintComponent(g);
			g.setFont(font);
			g.setColor(Color.BLUE);
			g.drawString(OFFSET, OFFSET_X, OFFSET_Y);
			long index = (long) scroll * (long) cols;

			FontMetrics m = g.getFontMetrics();
			if (fontHeight == -1) {
				fontHeight = m.getHeight();
				xOffset = Math.max(m.stringWidth(StringFormatter.toHexString(index, maxDigits)), m.stringWidth(OFFSET));
				pixelsSingleChar = m.charWidth('A');
			}
			for (int i = 0; i < rows; i++) {
				long value = i * cols + index;
				if (value > length)
					break;
				g.drawString(StringFormatter.toHexString(value, maxDigits), OFFSET_X, getPixelY(i));
			}
			for (int i = 0; i < cols; i++) {
				int finalX = getPixelX(i);
				g.drawString(Integer.toHexString(i), finalX, OFFSET_Y);
			}
			buffer.clear();
			int bytes = channel.read(buffer);
			channel.position(index);
			if (bytes == -1) {
				int finalX = getPixelX(cols / 2);
				int finalY = getPixelY(rows / 2);
				g.drawString("(Empty File)", finalX, finalY);
				return;
			}

			g.setColor(Color.BLACK);
			for (int i = 0; i < bytes; i++) {
				if (i + index > length)
					break;
				int x = i % cols;
				int y = i / cols;
				int finalX = getPixelX(x);
				int finalY = getPixelY(y);
				g.drawString(StringFormatter.toHexString(buffer.get(i)), finalX, finalY);
			}
			g.setColor(Color.GREEN);
			int newXOffset = getPixelX(cols + 1);
			char[] text = Main.byteInterpreter.interpret(buffer, bytes);
			for (int row = 0; row < rows; row++) {
				int offset = row * cols;
				if (offset > bytes)
					break;
				int length = cols;
				if (offset + cols > bytes)
					length = bytes % cols;

				g.drawChars(text, offset, length, newXOffset, getPixelY(row));
			}
			for (int i = 0; i < bytes; i++) {
				if (i + index > length)
					break;
				int x = i % cols;
				int y = i / cols;
			}
			g.setColor(new Color(0, 0, 255, 128));
			if (selectionStart != -1) {
				boolean forward = selectionEnd > selectionStart;
				int increment = forward ? +1 : -1;
				for (long i = selectionStart; forward ? i <= selectionEnd : i >= selectionEnd; i += increment) {
					int x = (int) (i % cols);
					int y = (int) (i / cols - scroll);
					int finalX = x * getSpacingBetweenCols() + xOffset + X_OFFSET - X_PADDING;
					int finalY = y * getSpacingBetweenRows() + Y_OFFSET - fontHeight * 7 / 8;
					g.fillRect(finalX, finalY, getSpacingBetweenCols(), fontHeight);
				}
			}
			
			if (selectionStart != -1) {
				boolean forward = selectionEnd > selectionStart;
				int increment = forward ? +1 : -1;
				for (long i = selectionStart; forward ? i <= selectionEnd : i >= selectionEnd; i += increment) {
					int x = (int) (i % cols);
					int y = (int) (i / cols - scroll);
					int finalX = getPixelX(cols + 1) + x * pixelsSingleChar;
					int finalY = y * getSpacingBetweenRows() + Y_OFFSET - fontHeight * 7 / 8;
					g.fillRect(finalX, finalY, pixelsSingleChar, fontHeight);
				}
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private int getTileX(int pixelX) {
		pixelX -= xOffset;
		pixelX -= X_OFFSET;
		return Math.min(Math.max(pixelX / getSpacingBetweenCols(), 0), cols - 1);
	}

	private int getTileY(int pixelY) {
		pixelY -= Y_OFFSET;
		pixelY += getSpacingBetweenRows();
		return Math.max(0, pixelY / getSpacingBetweenRows());
	}

	private int getPixelX(int x) {
		return x * getSpacingBetweenCols() + xOffset + X_OFFSET;
	}

	private int getPixelY(int y) {
		return y * getSpacingBetweenRows() + Y_OFFSET;
	}

	private int getSpacingBetweenCols() {
		return (pixelsSingleChar + X_PADDING) * 2;
	}

	private int getSpacingBetweenRows() {
		return fontHeight + Y_PADDING * 2;
	}

}
