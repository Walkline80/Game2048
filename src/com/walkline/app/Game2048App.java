package com.walkline.app;

import com.walkline.screen.Game2048Screen;
import net.rim.device.api.ui.UiApplication;

public class Game2048App extends UiApplication
{
    public static void main(String[] args)
    {
        Game2048App theApp = new Game2048App();       
        theApp.enterEventDispatcher();
    }

    public Game2048App() {pushScreen(new Game2048Screen());}    
}