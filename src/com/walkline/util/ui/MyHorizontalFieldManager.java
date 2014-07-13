package com.walkline.util.ui;

import net.rim.device.api.ui.container.HorizontalFieldManager;

public class MyHorizontalFieldManager extends HorizontalFieldManager
{
	public MyHorizontalFieldManager()
	{
		super(USE_ALL_WIDTH);
	}

	protected void sublayout(int maxWidth, int maxHeight)
	{
		super.sublayout(maxWidth, maxHeight);

		layoutChild(getField(0), 480, 480);
	}
}