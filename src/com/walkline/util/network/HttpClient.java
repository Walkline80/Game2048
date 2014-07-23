package com.walkline.util.network;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

import javax.microedition.io.HttpConnection;

import net.rim.device.api.io.transport.ConnectionDescriptor;
import net.rim.device.api.io.transport.ConnectionFactory;
import net.rim.device.api.io.transport.TransportInfo;

import com.walkline.util.Function;

public class HttpClient
{
	protected ConnectionFactory cf;

	public HttpClient(ConnectionFactory pcf) {cf = pcf;}

	public StringBuffer doGet(String url, Hashtable args) throws Exception
	{
		StringBuffer urlBuffer = new StringBuffer(url);
		urlBuffer.append('?').append(Function.encodeUrlParameters(args));

		return doGet(urlBuffer.toString());
	}

	public StringBuffer doGet(String url) throws Exception
	{
		HttpConnection conn = null;
		StringBuffer buffer = new StringBuffer();

		try {
			if ((url == null) || url.equalsIgnoreCase("") || (cf == null)) {return null;}

			ConnectionDescriptor connd = cf.getConnection(url);
			String transportTypeName = TransportInfo.getTransportTypeName(connd.getTransportDescriptor().getTransportType());
			conn = (HttpConnection) connd.getConnection();

			int resCode = conn.getResponseCode();
			String resMessage = conn.getResponseMessage();

			switch (resCode)
			{
				case HttpConnection.HTTP_OK: 
				case HttpConnection.HTTP_BAD_REQUEST:
				case HttpConnection.HTTP_NOT_FOUND:
				case HttpConnection.HTTP_UNAUTHORIZED:
				{
					InputStream inputStream = conn.openInputStream();
					int c;

					while ((c = inputStream.read()) != -1) {buffer.append((char) c);}

					inputStream.close();
					break;
				}
			}
		} catch (Exception e) {throw e;}
		  finally {if (conn != null) {try {conn.close(); conn = null;} catch (IOException e) {}}}

		return buffer;
	}
}