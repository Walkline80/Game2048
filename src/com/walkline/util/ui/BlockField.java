package com.walkline.util.ui;

import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.UiApplication;

import com.walkline.util.Function;

public class BlockField extends Field
{
	private static Font _font;
	private static final int CORNER_RADIUS = 12;

	private Object LOCK = new Object();
	private int _value = 0;
	private int _fore_color = 0;
	private int _background_color = 0;
	private String _text = "";
	private int _animation_size = 0;

	public BlockField()
	{
		super(NON_FOCUSABLE);

		try {
			int fontSize = 7;
			FontFamily family = FontFamily.forName("Tahoma");

			if (Display.getWidth() > Display.getHeight())
			{
				if (Display.getWidth() > 480) {fontSize = 9;}	
			} else {
				if (Display.getHeight() >= 480) {fontSize = 9;}
			}

			_font = family.getFont(Font.EXTRA_BOLD, fontSize, Ui.UNITS_pt);
		} catch (ClassNotFoundException e) {}
	}

	public void clear() {setValue(0);}

	public int getValue() {return _value;}

	public void setValue(int value)
	{
		_value = value;
		_text = ((value != 0) ? String.valueOf(value) : ""); 

		_fore_color = ((_value > 4) ? 0xf9f6f2 : 0x776e65);
		setBackgroundColor();
	}

	public void setBackgroundColor()
	{
		switch (_value)
		{
			case 0:
				_background_color = 0xccc0b3;
				break;
			case 2:
				_background_color = 0xeee4da;
				break;
			case 4:
				_background_color = 0xeee0c8;
				break;
			case 8:
				_background_color = 0xf2b179;
				break;
			case 16:
				_background_color = 0xf59563;
				break;
			case 32:
				_background_color = 0xf67c5f;
				break;
			case 64:
				_background_color = 0xf65e3b;
				break;
			case 128:
				_background_color = 0xffedcf72;
				break;
			case 256: 
				_background_color = 0xffedcc61;
				break;
			case 512:
				_background_color = 0xffedc850;
				break;
			case 1024:
				_background_color = 0xffedc53f;
				break;
			case 2048:
				_background_color = 0xffedc22e;
				break;
			case 4096:
				_background_color = 0xff3c39;
				break;
		}
	}

	public synchronized void startRun()
	{
		new Thread()
		{
			public void run()
			{
				while (_animation_size < getWidth());
				{
					synchronized (UiApplication.getEventLock())
					{
						invalidate();	
					}

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {new Exception("thread exception: " + e.toString());}

					_animation_size += 10;
					if (_animation_size > getWidth()) {_animation_size = getWidth();}
				}
			}
		}.start();
	}

	protected void layout(int width, int height)
	{
		_animation_size = width / 2;
		setExtent(width, height);
	}

	protected void paint(Graphics g)
	{
		g.setColor(_fore_color);
		g.setFont(_font);
		g.drawText(_text, (getWidth() - _font.getAdvance(_text)) / 2, (getHeight() - _font.getHeight()) / 2);
	}

	protected void paintBackground(Graphics g)
	{
		g.setColor(_background_color);
		g.fillRoundRect((getWidth() - _animation_size) / 2, (getHeight() - _animation_size) / 2, _animation_size, _animation_size, CORNER_RADIUS, CORNER_RADIUS);

		//g.fillRoundRect(0, 0, getWidth(), getHeight(), CORNER_RADIUS, CORNER_RADIUS);
	}
}