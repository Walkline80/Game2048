package com.walkline.app;

import com.walkline.screen.My2048Screen;
import net.rim.device.api.ui.UiApplication;

public class My2048App extends UiApplication
{
    public static void main(String[] args)
    {
        My2048App theApp = new My2048App();       
        theApp.enterEventDispatcher();
    }

    public My2048App() {pushScreen(new My2048Screen());}    
}