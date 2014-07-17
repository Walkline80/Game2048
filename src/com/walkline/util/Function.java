package com.walkline.util;

import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;

public class Function
{
    public static void errorDialog(final String message)
    {
        UiApplication.getUiApplication().invokeAndWait(new Runnable()
        {
            public void run() {Dialog.alert(message);} 
        });
    }
}