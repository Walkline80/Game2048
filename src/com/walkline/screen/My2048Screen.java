package com.walkline.screen;

import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.TouchEvent;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.util.MathUtilities;

import com.walkline.util.ui.BlockField;
import com.walkline.util.ui.BlockFieldManager;
import com.walkline.util.ui.ForegroundManager;
import com.walkline.util.ui.ScoreboardFieldManager;

public class My2048Screen extends MainScreen
{
	private boolean IS_WIDTH_SCREEN = Display.getWidth() > Display.getHeight() ? true : false;

	private ForegroundManager _foreground = new ForegroundManager(0);
	private int[][] _data = new int[4][4];
	private BlockField[] _block = new BlockField[16];
	private BlockFieldManager _mainFrame;
	private ScoreboardFieldManager _scoreBoard;

    public My2048Screen()
    {
    	super(NO_HORIZONTAL_SCROLL | NO_VERTICAL_SCROLL | NO_SYSTEM_MENU_ITEMS);

    	_mainFrame = new BlockFieldManager(IS_WIDTH_SCREEN ? Field.FIELD_LEFT : Field.FIELD_HCENTER);
    	_scoreBoard = new ScoreboardFieldManager(IS_WIDTH_SCREEN ? Field.FIELD_RIGHT : Field.FIELD_HCENTER);

    	if (IS_WIDTH_SCREEN)
    	{
    		HorizontalFieldManager hfm = new HorizontalFieldManager(USE_ALL_WIDTH);
        	hfm.add(_mainFrame);
        	hfm.add(_scoreBoard);
        	_foreground.add(hfm);
    	} else {
    		VerticalFieldManager vfm = new VerticalFieldManager(USE_ALL_WIDTH);
    		//_scoreBoard.setMargin(10, 10, 0, 0);
    		vfm.add(_scoreBoard);
    		vfm.add(_mainFrame);
    		

    		_foreground.add(vfm);
    		//_foreground.add(_scoreBoard);
    	}

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
        //_foreground.insert(_scoreBoard, 0);
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