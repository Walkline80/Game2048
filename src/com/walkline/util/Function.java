package com.walkline.util;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Hashtable;

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

    public static String encodeUrlParameters(Hashtable parameters)
	{
		if(parameters == null) {return "";}

		StringBuffer sb = new StringBuffer();
		boolean first = true;

		Enumeration keys=parameters.keys();
		while(keys.hasMoreElements())
		{
			if(first)
			{
				first = false;
			} else {
			    sb.append("&");
			}
			
			String _key=(String) keys.nextElement();
			String _value=(String) parameters.get(_key);
			
			if(_value!=null)
			{
			    try {
					sb.append(new String(_key.getBytes(), "UTF-8") + "=" + new String(_value.getBytes(), "UTF-8"));
				} catch (UnsupportedEncodingException e) {Dialog.alert(e.toString());}
			}
		}
		
		return sb.toString();
	}
}