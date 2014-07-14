package com.walkline.screen;

import java.util.Random;

import net.rim.device.api.system.Characters;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.TouchEvent;
import net.rim.device.api.ui.TouchGesture;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.util.MathUtilities;

import com.walkline.util.Function;
import com.walkline.util.ui.BlockField;
import com.walkline.util.ui.BlockFieldManager;
import com.walkline.util.ui.ForegroundManager;
import com.walkline.util.ui.ScoreboardFieldManager;

public class My2048Screen extends MainScreen
{
	private boolean IS_WIDTH_SCREEN = Display.getWidth() > Display.getHeight() ? true : false;

	private ForegroundManager _foreground = new ForegroundManager(0);
	private int[][] _data = new int[4][4];
	private BlockField[][] _block = new BlockField[4][4];
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
    		vfm.add(_scoreBoard);
    		vfm.add(_mainFrame);
    		

    		_foreground.add(vfm);
    	}

        for (int i=0; i<4; i++)
        {
        	for (int j=0; j<4; j++)
        	{
            	_block[i][j] = new BlockField();
            	_block[i][j].setValue(2);
            	_mainFrame.add(_block[i][j]);
        	}
        }

        add(_foreground);

        UiApplication.getUiApplication().invokeLater(new Runnable()
        {
			public void run() {initGame();}
		});
    }

    private void initGame()
    {
    	for (int x=0; x<4; x++)
    	{
    		for (int y=0; y<4; y++) {_block[x][y].clear();}
    	}

    	for (int i=0; i<2; i++) {appendBlock();}
    }

	private void appendBlock()
	{
		int x = new Random(System.currentTimeMillis() * System.currentTimeMillis()).nextInt(4);
		int y = new Random(System.currentTimeMillis() * System.currentTimeMillis()).nextInt(4);

		if (_block[x][y].getValue() == 0)
		{
			if (new Random(System.currentTimeMillis() * System.currentTimeMillis()).nextFloat() < 0.5)
			{
				_block[x][y].setValue(2);
			} else {
				_block[x][y].setValue(4);
			}

			_mainFrame.invalidate();
		} else {
			if (_mainFrame.DisplayCount() < 16) {appendBlock();}
		}
	}

	private void moveBlockUp()
	{
		boolean merge = false;

		for (int y=0; y<4; y++)
		{
			for (int x=0; x<4; x++)
			{
				for (int x1=x+1; x1<4; x1++)
				{
					if (_block[x1][y].getValue() > 0)
					{
						if (_block[x][y].getValue() <= 0)
						{
							//MainActivity.getMainActivity().getAnimLayer().createMoveAnim(_block[x1][y],_block[x][y], x1, x, y, y);
							_block[x][y].setValue(_block[x1][y].getValue());
							_block[x1][y].setValue(0);

							x--;
							merge = true;
						}else if (_block[x][y].getValue() == _block[x1][y].getValue())
						{
							//MainActivity.getMainActivity().getAnimLayer().createMoveAnim(_block[x1][y], _block[x][y],x1, x, y, y);
							_block[x][y].setValue(_block[x][y].getValue() * 2);
							_block[x1][y].setValue(0);

							//MainActivity.getMainActivity().addScore(_block[x][y].getValue());
							merge = true;
						}

						break;
					}
				}
			}
		}

		if (merge) {
			appendBlock();
			//checkComplete();
		}

		_mainFrame.invalidate();
	}

	private void moveBlockDown()
	{
		boolean merge = false;

		for (int y=0; y<4; y++)
		{
			for (int x=3; x>=0; x--)
			{
				for (int x1=x-1; x1>=0; x1--)
				{
					if (_block[x1][y].getValue() > 0)
					{
						if (_block[x][y].getValue() <= 0)
						{
							//MainActivity.getMainActivity().getAnimLayer().createMoveAnim(_block[x1][y], _block[x][y],x1, x, y, y);
							_block[x][y].setValue(_block[x1][y].getValue());
							_block[x1][y].setValue(0);

							x++;
							merge = true;
						}else if (_block[x][y].getValue() == _block[x1][y].getValue())
						{
							//MainActivity.getMainActivity().getAnimLayer().createMoveAnim(_block[x1][y], _block[x][y],x1, x, y, y);
							_block[x][y].setValue(_block[x][y].getValue() * 2);
							_block[x1][y].setValue(0);
							//MainActivity.getMainActivity().addScore(_block[x][y].getValue());

							merge = true;
						}

						break;
					}
				}
			}
		}

		if (merge) {
			appendBlock();
			//checkComplete();
		}

		_mainFrame.invalidate();
	}

	private void moveBlockLeft()
	{
		boolean merge = false;

		for (int x=0; x<4; x++)
		{
			for (int y=0; y<4; y++)
			{
				for (int y1=y+1; y1<4; y1++)
				{
					if (_block[x][y1].getValue() > 0)
					{
						if (_block[x][y].getValue() <= 0)
						{
							//MainActivity.getMainActivity().getAnimLayer().createMoveAnim(_block[x][y1],_block[x][y], x, x, y1, y);
							_block[x][y].setValue(_block[x][y1].getValue());
							_block[x][y1].setValue(0);

							y--;
							merge = true;
						} else if (_block[x][y].getValue() == _block[x][y1].getValue()) {
							//MainActivity.getMainActivity().getAnimLayer().createMoveAnim(_block[x][y1],_block[x][y], x, x, y1, y);
							_block[x][y].setValue(_block[x][y].getValue() * 2);
							_block[x][y1].setValue(0);
							//MainActivity.getMainActivity().addScore(_block[x][y].getValue());
							merge = true;
						}

						break;
					}
				}
			}
		}

		if (merge) {
			appendBlock();
			//checkComplete();
		}

		_mainFrame.invalidate();
	}

	private void moveBlockRight()
	{
		boolean merge = false;

		for (int x=0; x<4; x++)
		{
			for (int y=3; y>=0; y--)
			{
				for (int y1=y-1; y1>=0; y1--)
				{
					if (_block[x][y1].getValue()>0)
					{
						if (_block[x][y].getValue() <= 0)
						{
							//MainActivity.getMainActivity().getAnimLayer().createMoveAnim(_block[x][y1],_block[x][y], x, x, y1, y);
							_block[x][y].setValue(_block[x][y1].getValue());
							_block[x][y1].setValue(0);

							y++;
							merge = true;
						}else if (_block[x][y].getValue() == _block[x][y1].getValue())
						{
							//MainActivity.getMainActivity().getAnimLayer().createMoveAnim(_block[x][y1],_block[x][y], x, x, y1, y);
							_block[x][y].setValue(_block[x][y].getValue() * 2);
							_block[x][y1].setValue(0);
							//MainActivity.getMainActivity().addScore(_block[x][y].getValue());
							merge = true;
						}

						break;
					}
				}
			}
		}

		if (merge) {
			appendBlock();
			//checkComplete();
		}

		_mainFrame.invalidate();
	}

	protected boolean keyChar(char key, int status, int time)
	{
		switch (key)
		{
			case Characters.LATIN_CAPITAL_LETTER_R:
			case Characters.LATIN_SMALL_LETTER_R:
				initGame();
				return true;
			case Characters.LATIN_CAPITAL_LETTER_A:
			case Characters.LATIN_SMALL_LETTER_A:
				appendBlock();
				return true;
			case Characters.LATIN_CAPITAL_LETTER_I:
			case Characters.LATIN_SMALL_LETTER_I:
				moveBlockUp();
				return true;
			case Characters.LATIN_CAPITAL_LETTER_J:
			case Characters.LATIN_SMALL_LETTER_J:
				moveBlockLeft();
				return true;
			case Characters.LATIN_CAPITAL_LETTER_K:
			case Characters.LATIN_SMALL_LETTER_K:
				moveBlockDown();
				return true;
			case Characters.LATIN_CAPITAL_LETTER_L:
			case Characters.LATIN_SMALL_LETTER_L:
				moveBlockRight();
				return true;
		}

		return super.keyChar(key, status, time);
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
    	int event = message.getEvent();

    	switch (event)
    	{
			case TouchEvent.GESTURE:
				TouchGesture gesture = message.getGesture();
				int gestureEvent = gesture.getEvent();

				switch (gestureEvent)
				{
					case TouchGesture.SWIPE:
						int gestureDirection = gesture.getSwipeDirection();

						switch (gestureDirection)
						{
						case TouchGesture.SWIPE_NORTH:
							moveBlockUp();
							return true;
						case TouchGesture.SWIPE_SOUTH:
							moveBlockDown();
							return true;
						case TouchGesture.SWIPE_WEST:
							moveBlockLeft();
							return true;
						case TouchGesture.SWIPE_EAST:
							moveBlockRight();
							return true;
						}
						break;
				}
			break;
		}

    	return super.touchEvent(message);
    }
}