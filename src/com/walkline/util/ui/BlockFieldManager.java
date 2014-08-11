package com.walkline.util.ui;

import com.walkline.app.Game2048AppConfig;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;

public class BlockFieldManager extends Manager
{
	private static final int BACKGROUND_COLOR = 0xbbada0;
	private static final int CORNER_RADIUS = 16;
	private static int PADDING = 14;
	private static int SPACER = 6;
	private static int LINES = Game2048AppConfig.LINES;

	private boolean _need_animation = false;
	private Bitmap _bitmap;
	private int _fromX = 0;
	private int _fromY = 0;
	private int _toX = 0;
	private int _toY = 0;
	private int _currentX = 0;
	private int _currentY = 0;

	public BlockFieldManager(long style)
	{
		super(NON_FOCUSABLE | USE_ALL_WIDTH | NO_VERTICAL_SCROLL | NO_HORIZONTAL_SCROLL | style);
		setPadding(PADDING, PADDING, PADDING, PADDING);
	}

	public void setGameMode(int value)
	{
		int[] values = Game2048AppConfig.SETTINGS[value];

		LINES = values[0];
		PADDING = values[1];
		SPACER = values[2];
	}

	public int DisplayCount()
	{
		int count = 0;

		for (int i=0; i<LINES*LINES; i++)
		{
			BlockField block = (BlockField) getField(i);

			if (block.getValue() > 0) {count += 1;}
		}

		return count;
	}

	public synchronized void startMoveAnimation(BlockField fromBlock, BlockField toBlock, int fromX, int toX, int fromY, int toY)
	{
		_need_animation = true;
		_bitmap = fromBlock.getBitmap();
		_fromX = fromX;
		_fromY = fromY;
		_toX = toX;
		_toY = toY;

		new Thread(new TransitionRunnable()).start();
	}

	class TransitionRunnable implements Runnable
	{
		public void run()
		{
			if (_fromX == _toX)
			{
				_currentX = _fromX;
				_currentY = _fromY;

				if (_fromY > _toY)
				{
					while (_currentY > _toY)
					{
						try {
		                	Thread.sleep(100);

							_currentY -= 15;
							if (_currentY < _toY) {_currentY = _toY;}

							synchronized (UiApplication.getEventLock()) {doPaint();}
						} catch (InterruptedException e) {}
					}
				} else if (_fromY < _toY) {
					while (_currentY < _toY)
					{
						try {
		                	Thread.sleep(100);

							_currentY += 15;
							if (_currentY > _toY) {_currentY = _toY;}

							synchronized (UiApplication.getEventLock()) {doPaint();}
						} catch (InterruptedException e) {}
					}
				}
			} else if (_fromY == _toY) {
			}

			_need_animation = false;
		}
	}

	private void doPaint() {this.invalidate();}

	public int getPreferredWidth() {return Math.min(Display.getWidth(), Display.getHeight());}

	public int getPreferredHeight() {return Math.min(Display.getWidth(), Display.getHeight());}

	protected void sublayout(int width, int height)
	{
		if (getFieldCount() != (LINES * LINES)) {return;}

		int currentX = 0;
		int currentY = 0;
		int usedWidth = getPreferredWidth() - getPaddingLeft() - getPaddingRight() - (LINES + 1) * SPACER;
		int usedHeight = getPreferredWidth() - getPaddingTop() - getPaddingBottom() - (LINES + 1) * SPACER;
		int fieldWidth = usedWidth / LINES;
		int fieldHeight = usedHeight / LINES;
		int unUsedWidth = usedWidth - fieldWidth * LINES;
		int unUsedHeight = usedHeight - fieldHeight * LINES;
		Field child;

		for (int row=1; row<=LINES; row++)
		{
			for (int colum=1; colum<=LINES; colum++)
			{
				currentX = colum * SPACER + (colum - 1) * fieldWidth;
				currentY = row * SPACER + (row - 1) * fieldHeight;
				child = getField(colum + (row-1) * LINES - 1);

				layoutChild(child, fieldWidth, fieldHeight);
				setPositionChild(child, currentX, currentY);
			}
		}

		setExtent(getPreferredWidth() - unUsedWidth - getPaddingLeft() - getPaddingRight(), getPreferredHeight() - unUsedHeight - getPaddingTop() - getPaddingBottom());
	}

	protected void paint(Graphics g)
	{
		g.setColor(BACKGROUND_COLOR);
		g.fillRoundRect(0, 0, getWidth() - getPaddingLeft() - getPaddingRight(), getHeight() - getPaddingTop() - getPaddingBottom(), CORNER_RADIUS, CORNER_RADIUS);

		if (!_need_animation)
		{
			super.paint(g);
		} else {
			super.paint(g);
			g.drawBitmap(_currentX, _currentY, _bitmap.getWidth(), _bitmap.getHeight(), _bitmap, 0, 0);
			
		}
	}
}