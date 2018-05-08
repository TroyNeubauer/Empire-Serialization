package com.troy.byteviewer;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import javafx.scene.media.MediaPlayer;

import javax.swing.JPanel;

import com.troy.empireserialization.util.StringFormatter;

public class FileViewerImpl extends JPanel {
	protected FileChannel channel;
	protected int rows, cols;
	protected boolean mapDirectly;
	protected ByteBuffer buffer;
	protected char[] byteText, textInterp;
	protected int scroll = 0;
	protected Font font;
	protected long length;
	protected int maxDigits;
	protected int fontHeight = -1;
	// How many pixels offset in the top left takes up
	protected int xOffset = -1;
	// How wide is a single character
	protected int pixelsSingleChar;
	protected long selectionStart = 17, selectionEnd = 25;
	protected boolean dragged = false, draggedInText = false;
	private long lastChannelPosition = -1;
	private int bytes;
	private int renderCount = 0;

	public FileViewerImpl(FileChannel channel, Settings settings, boolean mapDirectly, long length) {
		super(new FlowLayout());
		setFocusable(true);
		requestFocusInWindow();
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
				renderCount = 0;
				if (dragged) {
					if (draggedInText) {
						x = getTileTextX(e.getX());
						finalIndex = x + y * cols;
						System.out.println("dragged in text");
					}
					if (finalIndex < length)
						selectionEnd = finalIndex;
				} else {
					if (x >= cols) {
						draggedInText = true;
						x = getTileTextX(e.getX());
						finalIndex = x + y * cols;
						System.out.println("in text " + x + ", " + y);

					} else {
						draggedInText = false;
					}
					if (finalIndex < length) {
						selectionStart = finalIndex;
						selectionEnd = finalIndex;
					} else {
						dragged = false;
					}
				}
				dragged = true;
			}
		});
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				selectionStart = -1;
				selectionEnd = -1;
				renderCount = 0;
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
			byteText = new char[newCap * 3];
		}
		if (length == 0) {
			maxDigits = 1;
		} else {
			maxDigits = (int) Math.ceil(Math.log(length) / Math.log(cols));
		}
		renderCount = 0;
	}

	public void update(int scroll) {
		this.scroll = scroll;
		renderCount = 0;
	}

	protected static final int Y_OFFSET = 50, Y_PADDING = 0, X_OFFSET = 20;
	private static final String OFFSET = "Offset";
	private static final int OFFSET_X = 5, OFFSET_Y = 23, BOUNDS_OFFSET = 15;
	int count = 0;

	@Override
	protected void paintComponent(Graphics g) {
		try {
			super.paintComponent(g);
			renderCount++;
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
			int bytes = 0;

			buffer.clear();
			lastChannelPosition = channel.position();
			bytes = channel.read(buffer);
			channel.position(index);
			for (int i = 0; i < bytes; i++) {
				byte b = buffer.get(i);
				byteText[i * 3 + 0] = StringFormatter.DIGITS[(b >>> 4) & 0x0F];
				byteText[i * 3 + 1] = StringFormatter.DIGITS[(b >>> 0) & 0x0F];
				byteText[i * 3 + 2] = ' ';
			}
			buffer.position(0);
			textInterp = Main.byteInterpreter.interpret(buffer, bytes);
			if (bytes == -1) {
				int finalX = getPixelX(cols / 2);
				int finalY = getPixelY(rows / 2);
				g.drawString("(Empty File)", finalX, finalY);
				return;
			}

			g.setColor(Color.BLACK);
			for (int row = 0; row < rows; row++) {
				int charsPerRow = cols * 3;
				int offset = row * cols;
				if (offset > bytes)
					break;
				int length = cols;
				if (offset + cols > bytes)
					length = bytes % cols;
				g.drawChars(byteText, row * charsPerRow, length * 3, getPixelX(0), getPixelY(row));
			}
			g.setColor(Color.GREEN);
			int newXOffset = getPixelX(cols + 1);
			for (int row = 0; row < rows; row++) {
				int offset = row * cols;
				if (offset > bytes)
					break;
				int length = cols;
				if (offset + cols > bytes)
					length = bytes % cols;

				g.drawChars(textInterp, offset, length, newXOffset, getPixelY(row));
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
					if (y < 0) {
						continue;
					}
					int finalX = x * getSpacingBetweenCols() + xOffset + X_OFFSET - pixelsSingleChar / 2;
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
		return Math.max(pixelX / getSpacingBetweenCols(), 0);
	}

	private int getTileTextX(int pixelX) {
		pixelX -= getPixelX(cols + 1);
		return Math.min(Math.max(pixelX / pixelsSingleChar, -1), cols - 1);
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
		return pixelsSingleChar * 3;
	}

	private int getSpacingBetweenRows() {
		return fontHeight + Y_PADDING * 2;
	}

	public boolean needsRepaint() {
		return renderCount < 5;
	}
}
