package com.walkline.util.ui;

import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Ui;

public class AppTitleField extends Field
{
	private static Font _font;
	private static final int FOREGROUND_COLOR = 0x776e65;
	private static final String APP_TITLE = "2048";

	public AppTitleField()
	{
		super();

		try {
			int fontSize = 12;
			FontFamily family = FontFamily.forName("Tahoma");

			if (Display.getWidth() < Display.getHeight()) {fontSize = 16;}
			_font = family.getFont(Font.EXTRA_BOLD, fontSize, Ui.UNITS_pt);
		} catch (ClassNotFoundException e) {}
	}

	public int getPreferredWidth() {return _font.getAdvance(APP_TITLE);}

	public int getPreferredHeight() {return _font.getHeight();}

	protected void layout(int width, int height)
	{
		if (getManager().isStyle(Field.FIELD_HCENTER))
		{
			width = _font.getAdvance("2048");
			height = _font.getHeight();
		}

		setExtent(width, height);
	}

	protected void paint(Graphics g)
	{
		g.setColor(FOREGROUND_COLOR);
		g.setFont(_font);
		g.drawText(APP_TITLE, (getWidth() - _font.getAdvance(APP_TITLE)) / 2, (getHeight() - _font.getHeight()) / 2);
	}
}