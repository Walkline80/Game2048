package com.walkline.screen;

import java.util.Random;

import net.rim.device.api.system.Characters;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.TouchEvent;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.walkline.app.Game2048AppConfig;
import com.walkline.util.Function;
import com.walkline.util.ui.BlockField;
import com.walkline.util.ui.BlockFieldManager;
import com.walkline.util.ui.ForegroundManager;
import com.walkline.util.ui.ScoreboardFieldManager;

public class Game2048Screen extends MainScreen
{
	private boolean IS_WIDTH_SCREEN = Display.getWidth() > Display.getHeight() ? true : false;

	private int LINES = Game2048AppConfig.LINES;
	private ForegroundManager _foreground = new ForegroundManager(0);
	private BlockField[][] _block = new BlockField[LINES][LINES];
	private BlockFieldManager _mainFrame;
	private ScoreboardFieldManager _scoreBoard;
	private static int _lastMovementTime = 0;
	private static float _startX;
	private static float _startY;
	private static float _offsetX;
	private static float _offsetY;

    public Game2048Screen()
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

        for (int x=0; x<LINES; x++)
        {
        	for (int y=0; y<LINES; y++)
        	{
            	_block[x][y] = new BlockField();
            	_block[x][y].setAnimationMode(false);
            	//_block[i][j].setValue(0);
            	_block[x][y].Clear();
            	_mainFrame.add(_block[x][y]);
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
    	_scoreBoard.clear();

    	for (int x=0; x<LINES; x++)
    	{
    		for (int y=0; y<LINES; y++)
    		{
    			_block[x][y].setAnimationMode(false);
    			_block[x][y].Clear();
    		}
    	}

    	_mainFrame.invalidate();
    	appendBlock();
    	appendBlock();
    }

	private void appendBlock()
	{
		int x = 0;
		int y = 0;
		boolean found = false;

		if (_mainFrame.DisplayCount() >= 16) {return;}

		while (!found)
		{
			x = new Random(System.currentTimeMillis() * System.currentTimeMillis()).nextInt(LINES);
			y = new Random(System.currentTimeMillis() * System.currentTimeMillis()).nextInt(LINES);

			if (_block[x][y].getValue() == 0)
			{
				if (new Random(System.currentTimeMillis() * System.currentTimeMillis()).nextFloat() > 0.3)
				{
					_block[x][y].setValue(2);
				} else {
					_block[x][y].setValue(4);
				}

				found = true;
				_block[x][y].setAnimationMode(true);
				_block[x][y].startAnimation();
			}
		}
	}

	private void updateScore(int value) {_scoreBoard.update(value);}

	private void moveBlockUp()
	{
		boolean merge = false;

		for (int y=0; y<LINES; y++)
		{
			for (int x=0; x<LINES; x++)
			{
				for (int x1=x+1; x1<LINES; x1++)
				{
					if (_block[x1][y].getValue() > 0)
					{
						if (_block[x][y].getValue() <= 0)
						{
							//final int newX1 = x1;
							//final int newX = x;
							//final int newY = y;
							//MainActivity.getMainActivity().getAnimLayer().createMoveAnim(_block[x1][y],_block[x][y], x1, x, y, y);
							//UiApplication.getUiApplication().invokeAndWait(new Runnable()
							//{
							//	public void run() {
							//		_mainFrame.startMoveAnimation(_block[newX1][newY], _block[newX][newY], _block[newX1][newY].getLeft(), _block[newX][newY].getLeft(), _block[newX1][newY].getTop(), _block[newX][newY].getTop());									
							//	}
							//});

							_block[x][y].setValue(_block[x1][y].getValue());
							_block[x1][y].setValue(0);

							x--;
							merge = true;
						} else if (_block[x][y].equals(_block[x1][y])) {
							//MainActivity.getMainActivity().getAnimLayer().createMoveAnim(_block[x1][y], _block[x][y],x1, x, y, y);
							_block[x][y].setValue(_block[x][y].getValue() * 2);
							_block[x][y].setAnimationMode(true);
							_block[x][y].startAnimation();
							_block[x1][y].setValue(0);

							updateScore(_block[x][y].getValue());
							merge = true;
						}

						break;
					}
				}
			}
		}

		if (merge) {
			appendBlock();
			checkComplete();
		}

		_mainFrame.invalidate();
	}

	private void moveBlockDown()
	{
		boolean merge = false;

		for (int y=0; y<LINES; y++)
		{
			for (int x=LINES-1; x>=0; x--)
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
						} else if (_block[x][y].equals(_block[x1][y])) {
							//MainActivity.getMainActivity().getAnimLayer().createMoveAnim(_block[x1][y], _block[x][y],x1, x, y, y);
							_block[x][y].setValue(_block[x][y].getValue() * 2);
							_block[x][y].setAnimationMode(true);
							_block[x][y].startAnimation();
							_block[x1][y].setValue(0);

							updateScore(_block[x][y].getValue());
							merge = true;
						}

						break;
					}
				}
			}
		}

		if (merge) {
			appendBlock();
			checkComplete();
		}

		_mainFrame.invalidate();
	}

	private void moveBlockLeft()
	{
		boolean merge = false;

		for (int x=0; x<LINES; x++)
		{
			for (int y=0; y<LINES; y++)
			{
				for (int y1=y+1; y1<LINES; y1++)
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
						} else if (_block[x][y].equals(_block[x][y1])) {
							//MainActivity.getMainActivity().getAnimLayer().createMoveAnim(_block[x][y1],_block[x][y], x, x, y1, y);
							_block[x][y].setValue(_block[x][y].getValue() * 2);
							_block[x][y].setAnimationMode(true);
							_block[x][y].startAnimation();
							_block[x][y1].setValue(0);

							updateScore(_block[x][y].getValue());
							merge = true;
						}

						break;
					}
				}
			}
		}

		if (merge) {
			appendBlock();
			checkComplete();
		}

		_mainFrame.invalidate();
	}

	private void moveBlockRight()
	{
		boolean merge = false;

		for (int x=0; x<LINES; x++)
		{
			for (int y=LINES-1; y>=0; y--)
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
						} else if (_block[x][y].equals(_block[x][y1])) {
							//MainActivity.getMainActivity().getAnimLayer().createMoveAnim(_block[x][y1],_block[x][y], x, x, y1, y);
							_block[x][y].setValue(_block[x][y].getValue() * 2);
							_block[x][y].setAnimationMode(true);
							_block[x][y].startAnimation();
							_block[x][y1].setValue(0);

							updateScore(_block[x][y].getValue());
							merge = true;
						}

						break;
					}
				}
			}
		}

		if (merge) {
			appendBlock();
			checkComplete();
		}

		_mainFrame.invalidate();
	}

	private void checkComplete()
	{
		boolean complete = true;

ALL:
		for (int y=0; y<LINES; y++)
		{
			for (int x=0; x<LINES; x++)
			{
				if (_block[x][y].getValue() == 0 ||
				   (x > 0 && _block[x][y].equals(_block[x-1][y])) ||
				   (x < 3 && _block[x][y].equals(_block[x+1][y])) ||
				   (y > 0 && _block[x][y].equals(_block[x][y-1])) ||
				   (y < 3 && _block[x][y].equals(_block[x][y+1]))) {
						complete = false;
						break ALL;
				}
			}
		}

		if (complete)
		{
			UiApplication.getUiApplication().invokeLater(new Runnable()
			{
				public void run() {Function.errorDialog("游戏结束");}
			});
		}
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
			case Characters.LATIN_CAPITAL_LETTER_C:
			case Characters.LATIN_SMALL_LETTER_C:
				Function.errorDialog(_mainFrame.DisplayCount() + "");
				return true;
		}

		return super.keyChar(key, status, time);
	}

    protected boolean navigationMovement(int dx, int dy, int status, int time)
    {
    	if (Math.abs(dx) > Math.abs(dy))
    	{
    		if (time - _lastMovementTime > 300)
    		{
    			if (dx < 0)
    			{
    				moveBlockLeft();
    			} else if (dx > 0) {
    				moveBlockRight();
    			}

    			_lastMovementTime = time;
    			return true;
    		}
    	} else {
    		if (time - _lastMovementTime > 300)
    		{
    			if (dy < 0)
    			{
    				moveBlockUp();
    			} else if (dy > 0) {
    				moveBlockDown();
    			}

    			_lastMovementTime = time;
    			return true;
    		}
    	}

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
    		case TouchEvent.DOWN:
    			_startX = message.getX(1);
    			_startY = message.getY(1);
    			break;
    		case TouchEvent.UP:
    			_offsetX = message.getX(1) - _startX;
    			_offsetY = message.getY(1) - _startY;

    			if (Math.abs(_offsetX) > Math.abs(_offsetY))
    			{
    				if (_offsetX < -5)
    				{
    					moveBlockLeft();
    				} else if (_offsetX > 5) {
    					moveBlockRight();
    				}
    			} else {
    				if (_offsetY < -5)
    				{
    					moveBlockUp();
    				} else if (_offsetY > 5) {
    					moveBlockDown();
    				}
    			}
    			break;
    		/*
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
			*/
		}

    	return true; //super.touchEvent(message);
    }
}