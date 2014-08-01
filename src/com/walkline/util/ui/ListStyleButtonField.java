package com.walkline.util.ui;

import net.rim.device.api.system.Characters;
import net.rim.device.api.system.Display;
import net.rim.device.api.system.KeypadListener;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.TouchEvent;
import net.rim.device.api.ui.component.LabelField;

import com.walkline.app.Game2048AppConfig;
import com.walkline.util.network.RankItem;

public class ListStyleButtonField extends Field
{
    public static final int DRAWPOSITION_TOP = 0;
    public static final int DRAWPOSITION_BOTTOM = 1;
    public static final int DRAWPOSITION_MIDDLE = 2;
    public static final int DRAWPOSITION_SINGLE = 3;

    private static final int CORNER_RADIUS = 0;

    private static final int HPADDING = Display.getWidth() <= 320 ? 4 : 6;

    private static int COLOR_BACKGROUND = 0xFFFFFF;
    private static int COLOR_BORDER = 0xBBBBBB;
    private static int COLOR_BACKGROUND_FOCUS = 0x186DEF;

    private MyLabelField _labelNickname;
    private MyLabelField _labelScore;

    private int _rightOffset;
    private int _leftOffset;
    private int _labelHeight;
    private int _drawPosition = -1;

	private static Font FONT_LIST_NICKNAME;
	private static Font FONT_LIST_SCORE;

    public ListStyleButtonField(RankItem item)
    {
        super(USE_ALL_WIDTH | Field.FOCUSABLE);

        _labelNickname = new MyLabelField(item.getNickname());
        _labelScore = new MyLabelField(item.getScore());

        setFontSize();
    }

    public void setDrawPosition(int drawPosition) {_drawPosition = drawPosition;}

    public void layout(int width, int height)
    {
    	_leftOffset = HPADDING;
        _rightOffset = HPADDING;

        if (_labelNickname != null)
        {
        	_labelNickname.layout(width - _leftOffset - _rightOffset, height);
            _labelHeight = _labelNickname.getHeight();
        }

        if (_labelScore != null)
        {
        	_labelScore.layout(width - _leftOffset - _rightOffset, height);
        	_labelHeight = _labelScore.getHeight();
        }

        setExtent(width, _labelHeight + 3 * HPADDING);
    }

    private void setFontSize()
    {
    	FONT_LIST_NICKNAME = Game2048AppConfig.FONT_LIST_NICKNAME;
    	FONT_LIST_SCORE = Game2048AppConfig.FONT_LIST_SCORE;

    	if (_labelNickname != null) {_labelNickname.setFont(FONT_LIST_NICKNAME);}
    	if (_labelScore != null) {_labelScore.setFont(FONT_LIST_SCORE);}
    }

    protected void paint(Graphics g)
    {
        // News Title Text
    	if (_labelNickname != null)
    	{
            try
            {
            	g.setFont(FONT_LIST_NICKNAME);
            	g.pushRegion(_leftOffset, (getHeight() - _labelNickname.getHeight()) / 2, _labelNickname.getWidth(), _labelNickname.getHeight(), 0, 0);
            	_labelNickname.paint(g);
            } finally {g.popContext();}
    	}

    	//Section Title Text
    	if (_labelScore != null)
    	{
    		try
    		{
    			g.setFont(FONT_LIST_SCORE);
    			g.pushRegion(getWidth() - _leftOffset - _rightOffset - _labelScore.getWidth(), (getHeight() - _labelScore.getHeight()) / 2, _labelScore.getWidth(), _labelScore.getHeight(), 0, 0);
    			_labelScore.paint(g);
    		} finally {g.popContext();}
    	}
    }

    protected void paintBackground(Graphics g)
    {
        if(_drawPosition < 0)
        {
            super.paintBackground(g);
            return;
        }

        int oldColour = g.getColor();
        int background = g.isDrawingStyleSet(Graphics.DRAWSTYLE_FOCUS) ? COLOR_BACKGROUND_FOCUS : COLOR_BACKGROUND;

        try {
            if(_drawPosition == 0)
            {
                // Top
                g.setColor(background);
                g.fillRoundRect(0, 0, getWidth(), getHeight() + CORNER_RADIUS, CORNER_RADIUS, CORNER_RADIUS);
                g.setColor(COLOR_BORDER);
                g.drawRoundRect(0, 0, getWidth(), getHeight() + CORNER_RADIUS, CORNER_RADIUS, CORNER_RADIUS);
                g.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
            } else if(_drawPosition == 1)
            {
                // Bottom 
                g.setColor(background);
                g.fillRoundRect(0, -CORNER_RADIUS, getWidth(), getHeight() + CORNER_RADIUS, CORNER_RADIUS, CORNER_RADIUS);
                g.setColor(COLOR_BORDER);
                g.drawRoundRect(0, -CORNER_RADIUS, getWidth(), getHeight() + CORNER_RADIUS, CORNER_RADIUS, CORNER_RADIUS);
            } else if(_drawPosition == 2)
            {
                // Middle
                g.setColor(background);
                g.fillRoundRect(0, -CORNER_RADIUS, getWidth(), getHeight() + 2 * CORNER_RADIUS, CORNER_RADIUS, CORNER_RADIUS);
                g.setColor(COLOR_BORDER);
                g.drawRoundRect(0, -CORNER_RADIUS, getWidth(), getHeight() + 2 * CORNER_RADIUS, CORNER_RADIUS, CORNER_RADIUS);
                g.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
            } else {
                // Single
                g.setColor(background);
                g.fillRoundRect(0, 0, getWidth(), getHeight(), CORNER_RADIUS, CORNER_RADIUS);
                g.setColor(COLOR_BORDER);
                g.drawRoundRect(0, 0, getWidth(), getHeight(), CORNER_RADIUS, CORNER_RADIUS);
            }
        } finally {g.setColor(oldColour);}
    }

    protected void drawFocus(Graphics g, boolean on)
    {
        if(_drawPosition >= 0)
        {
            boolean oldDrawStyleFocus = g.isDrawingStyleSet(Graphics.DRAWSTYLE_FOCUS);
            try {
                if(on)
                {
                	g.setColor(Color.WHITE);
                    g.setDrawingStyle(Graphics.DRAWSTYLE_FOCUS, true);
                }
                paintBackground(g);
                paint(g);
            } finally {
                g.setDrawingStyle(Graphics.DRAWSTYLE_FOCUS, oldDrawStyleFocus);
            }
        }
    }

    protected boolean keyChar(char character, int status, int time) 
    {
    	switch (character)
    	{
			case Characters.ENTER:
	            clickButton();
	            return true;
        }

        return super.keyChar(character, status, time);
    }

    protected boolean navigationUnclick(int status, int time) 
    {
    	if ((status & KeypadListener.STATUS_FOUR_WAY) == KeypadListener.STATUS_FOUR_WAY)
    	{
        	clickButton();
        	return true;
    	}

    	return super.navigationClick(status, time);
    }

    protected boolean trackwheelClick(int status, int time)
    {
    	if ((status & KeypadListener.STATUS_TRACKWHEEL) == KeypadListener.STATUS_TRACKWHEEL)
    	{
       		clickButton();
       		return true;
    	}

    	return super.trackwheelClick(status, time);
    }

    protected boolean invokeAction(int action) 
    {
    	switch(action)
    	{
    		case ACTION_INVOKE:
           		clickButton();
           		return true;
    	}

    	return super.invokeAction(action);
    }

    protected boolean touchEvent(TouchEvent message)
    {
        int x = message.getX(1);
        int y = message.getY(1);

        if (x < 0 || y < 0 || x > getExtent().width || y > getExtent().height) {return false;}

        switch (message.getEvent())
        {
            case TouchEvent.UNCLICK:
           		clickButton();
           		return true;
        }

        return super.touchEvent(message);
    }

    public void clickButton() {fieldChangeNotify(0);}

    public void setDirty(boolean dirty) {}
    public void setMuddy(boolean muddy) {}

    class MyLabelField extends LabelField
    {
        public MyLabelField(String text) {super(text, LabelField.ELLIPSIS);}

    	public void layout(int width, int height) {super.layout(width, height);}   

    	public void paint(Graphics g) {super.paint(g);}
    }
}