package com.walkline.util.ui;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Ui;

public class BestScoreboardField extends Field
{
	private static final String BEST = "BEST";
	private static final int TITLE_COLOR = 0xe5dacf;
	private static final int SCORE_COLOR = 0xffffff;
	private static final int CORNER_RADIUS = 16;

	private static Font _title_font;
	private static Font _score_font;

	private int _value = 0;

	public BestScoreboardField()
	{
		super();

		try {
			FontFamily family = FontFamily.forName("Tahoma");
			_title_font = family.getFont(Font.EXTRA_BOLD, 6, Ui.UNITS_pt);
			_score_font = family.getFont(Font.EXTRA_BOLD, 8, Ui.UNITS_pt);
		} catch (ClassNotFoundException e) {}
	}

	public void clear() {_value = 0;}

	public void setScore(int value) {_value = value;}

	public int getScore() {return _value;}

	public int getPreferredWidth() {return _score_font.getAdvance("00000") + 20;}

	public int getPreferredHeight() {return _title_font.getHeight() + _score_font.getHeight() + 30;}

	protected void layout(int width, int height) {setExtent(width, height);}

	protected void paint(Graphics g)
	{
		g.setColor(TITLE_COLOR);
		g.setFont(_title_font);
		g.drawText(BEST, (getWidth() - _title_font.getAdvance(BEST)) / 2, (getHeight() - _title_font.getHeight()) / 2 - 15);

		g.setColor(SCORE_COLOR);
		g.setFont(_score_font);
		g.drawText(String.valueOf(_value), (getWidth() - _score_font.getAdvance(String.valueOf(_value))) / 2, (getHeight() - _title_font.getHeight()) / 2 + 10);
	}

	protected void paintBackground(Graphics g)
	{
		g.setColor(0xbbada0);
		g.fillRoundRect(0, 0, getWidth(), getHeight(), CORNER_RADIUS, CORNER_RADIUS);
	}
}