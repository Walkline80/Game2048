package com.walkline.app;

import net.rim.device.api.system.Application;
import net.rim.device.api.ui.UiApplication;

import com.walkline.screen.Game2048Screen;

public class Game2048App extends UiApplication
{
	Game2048AppConfig _appConfig;

    public static void main(String[] args)
    {
        Game2048App theApp = new Game2048App();       
        theApp.enterEventDispatcher();
    }

    public Game2048App()
    {
    	synchronized (Application.getEventLock())
		{
			_appConfig = new Game2048AppConfig();
			_appConfig.initialize();
		}

    	pushScreen(new Game2048Screen(_appConfig));
    }    
}