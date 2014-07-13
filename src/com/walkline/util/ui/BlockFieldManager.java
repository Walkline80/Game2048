package com.walkline.util.ui;

import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;

public class BlockFieldManager extends Manager
{
	private static final int _background_color = 0xbbada0;
	private static final int CORNER_RADIUS = 16;
	private static final int PADDING = 10;
	private static final int SPACER = 12;

	public BlockFieldManager(long style)
	{
		super(NON_FOCUSABLE | NO_VERTICAL_SCROLL | NO_HORIZONTAL_SCROLL | style);
		setPadding(PADDING, PADDING, PADDING, PADDING);
	}

	public int getPreferredWidth()
	{
		return Math.min(Display.getWidth(), Display.getHeight());
	}

	public int getPreferredHeight()
	{
		return Math.min(Display.getWidth(), Display.getHeight());
	}

	protected void sublayout(int width, int height)
	{
		if (getFieldCount() != 16) {return;}

		int currentX = 0;
		int currentY = 0;
		int usedWidth = getPreferredWidth() - getPaddingLeft() - getPaddingRight() - 5 * SPACER;
		int usedHeight = getPreferredWidth() - getPaddingTop() - getPaddingBottom() - 5 * SPACER;
		int fieldWidth = usedWidth / 4;
		int fieldHeight = usedHeight / 4;
		int unUsedWidth = usedWidth - fieldWidth * 4;
		int unUsedHeight = usedHeight - fieldHeight * 4;
		Field child;

		for (int row=1; row<=4; row++)
		{
			for (int colum=1; colum<=4; colum++)
			{
				currentX = colum * SPACER + (colum - 1) * fieldWidth;
				currentY = row * SPACER + (row - 1) * fieldHeight;
				child = getField(colum + (row-1) * 4 - 1);

				layoutChild(child, fieldWidth, fieldHeight);
				setPositionChild(child, currentX, currentY);
			}
		}

		setExtent(getPreferredWidth() - unUsedWidth - getPaddingLeft() - getPaddingRight(), getPreferredHeight() - unUsedHeight - getPaddingTop() - getPaddingBottom());
	}

	protected void paint(Graphics g)
	{
		g.setColor(_background_color);
		g.fillRoundRect(0, 0, getWidth() - getPaddingLeft() - getPaddingRight(), getHeight() - getPaddingTop() - getPaddingBottom(), CORNER_RADIUS, CORNER_RADIUS);

		super.paint(g);
	}
}