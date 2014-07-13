package com.walkline.screen;

import javax.microedition.lcdui.game.Layer;
import javax.microedition.lcdui.game.LayerManager;

import net.rim.device.api.io.parser.rss.event.NewRSSChannelEvent;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.TouchEvent;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.util.MathUtilities;

import com.walkline.util.Function;
import com.walkline.util.ui.BlockField;
import com.walkline.util.ui.BlockFieldManager;
import com.walkline.util.ui.ForegroundManager;

public class My2048Screen extends MainScreen
{
	private ForegroundManager _foreground = new ForegroundManager(0);
	private int[][] _data = new int[4][4];
	private BlockField[] _block = new BlockField[16];
	private BlockFieldManager _mainFrame;

    public My2048Screen()
    {
    	super(NO_HORIZONTAL_SCROLL | NO_VERTICAL_SCROLL | NO_SYSTEM_MENU_ITEMS);

    	_mainFrame = new BlockFieldManager(Display.getWidth() > Display.getHeight() ? Field.FIELD_LEFT : Field.FIELD_HCENTER);
    	_foreground.add(_mainFrame);

		int value = 0;
        for (int i=0; i<16; i++)
        {
        	if (i == 0)	{
        		value = 0;
        	} else if (i < 13) {
        		value = (int) MathUtilities.pow(Double.parseDouble(2+""), i);
        	} else {
        		value = 0;
        	}

        	_block[i] = new BlockField();
        	_block[i].setValue(value);
        	_mainFrame.add(_block[i]);
        }

        add(_foreground);
    }

    protected boolean navigationMovement(int dx, int dy, int status, int time)
    {
    	return super.navigationMovement(dx, dy, status, time);
    }

    protected boolean trackwheelRoll(int amount, int status, int time)
    {
    	return super.trackwheelRoll(amount, status, time);
    }

    protected boolean touchEvent(TouchEvent message)
    {
    	return super.touchEvent(message);
    }
}