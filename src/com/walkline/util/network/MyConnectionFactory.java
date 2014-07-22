package com.walkline.util.network;

import net.rim.device.api.io.transport.ConnectionFactory;
import net.rim.device.api.io.transport.TransportInfo;
import net.rim.device.api.io.transport.options.BisBOptions;

import com.walkline.app.Game2048AppConfig;

public class MyConnectionFactory extends ConnectionFactory
{
	public MyConnectionFactory()
	{
		setPreferredTransportTypes(Game2048AppConfig.preferredTransportTypes);
		setDisallowedTransportTypes(Game2048AppConfig.disallowedTransportTypes);
		setTransportTypeOptions(TransportInfo.TRANSPORT_BIS_B, new BisBOptions("nds-public"));
		setTimeoutSupported(true);
		setAttemptsLimit(10);
		setRetryFactor(2000);
		setConnectionTimeout(10000);
		setTimeLimit(10000);
	}
}