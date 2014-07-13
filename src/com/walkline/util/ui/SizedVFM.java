package com.walkline.util.ui;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class SizedVFM extends VerticalFieldManager {
    int mWidth;
    int mHeight;

    public SizedVFM(int width, int height) {
    	super(VERTICAL_SCROLL | VERTICAL_SCROLLBAR);
    	mWidth = width;
    	mHeight = height;
    }

    public int getPreferredHeight() {
    	return mHeight;
    }

    public int getPreferredWidth() {
    	return mWidth;
    }

    protected void sublayout(int maxWidth, int maxHeight) {
    	super.sublayout(maxWidth, maxHeight);
    	setExtent(getPreferredWidth(), getPreferredHeight());
    }

    protected void paint(Graphics graphics) {
    	super.paint(graphics);
    	if (getVisibleHeight() < getVirtualHeight()) {
    		int y1 = 0, y2 = 0, x1 = 0, x2 = 0;
    		int scrollOff = getVerticalScroll();
    		if (scrollOff > 0) {
    			y1 = scrollOff + 12;
    			y2 = scrollOff + 2;
    			x1 = getVisibleWidth() - 20;
    			x2 = getVisibleWidth() - 2;

    			graphics.setColor(Color.DARKRED);
    			int[] xPts = new int[] { x1, x2, x1 + 9 };
    			int[] yPts = new int[] { y1, y1, y2 };
    			graphics.drawFilledPath(xPts, yPts, null, null);
    		}
    		if (scrollOff < (getVirtualHeight() - getVisibleHeight())) {
    			y1 = scrollOff + getVisibleHeight() - 12;
    			y2 = scrollOff + getVisibleHeight() - 2;
    			x1 = getVisibleWidth() - 20;
    			x2 = getVisibleWidth() - 2;
    			graphics.setColor(Color.DARKRED);
    			int[] xPts = new int[] { x1, x2, x1 + 9 };
    			int[] yPts = new int[] { y1, y1, y2 };
    			graphics.drawFilledPath(xPts, yPts, null, null);
    		}
    	}
    }
}
