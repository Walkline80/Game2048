package com.walkline.screen;

import java.util.Hashtable;
import net.rim.device.api.system.Application;
import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.PopupScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import com.walkline.app.Game2048AppConfig;
import com.walkline.util.Enumerations.LoadingActions;
import com.walkline.util.json.JSONObject;
import com.walkline.util.json.JSONTokener;
import com.walkline.util.network.GameRanking;
import com.walkline.util.network.HttpClient;
import com.walkline.util.network.MyConnectionFactory;
import com.walkline.util.network.Ranking;

public class PleaseWaitScreen extends PopupScreen
{
	private Thread thread=null;
	private Hashtable _param;
	private HttpClient _http;
	private Ranking _ranking;
	private MyConnectionFactory cf = new MyConnectionFactory();

	public PleaseWaitScreen(Hashtable param, int action)
	{
		super(new VerticalFieldManager());

		_http = new HttpClient(cf);
		_param = param;

		add(new LabelField("Please wait....", Field.FIELD_HCENTER));

		switch (action)
		{
			case LoadingActions.UPLOADSCORE:
				thread = new Thread(new UploadScoreRunnable());
				break;
			case LoadingActions.LOADRANKING:
				thread = new Thread(new LoadRankingRunnable());
				break;
		}

		thread.start();
	}

	class UploadScoreRunnable implements Runnable
	{
		public void run()
		{
			try {
				_http.doGet(Game2048AppConfig.queryUpdateRecorderUrl, _param);

				Application.getApplication().invokeLater(new Runnable()
				{
					public void run() {onClose();}
				});
			} catch (Exception e) {}
		}
	}

	class LoadRankingRunnable implements Runnable
	{
		public void run()
		{
			_ranking = getRanking();

			Application.getApplication().invokeLater(new Runnable()
			{
				public void run() {onClose();}
			});
		}
	}

	public Ranking getRankingList() {return _ranking;}

	private Ranking getRanking()
	{
		Ranking result = null;
		JSONObject jsonObject = new JSONObject();

		try {
			jsonObject = doRequest(Game2048AppConfig.queryRankingRecordersUrl);

			result = (jsonObject != null ? getRanking(jsonObject) : null);
		} catch (Exception e) {}

		return result;
	}

	private Ranking getRanking(JSONObject jsonObject) throws Exception
	{
		return new GameRanking(jsonObject);
	}

	private JSONObject doRequest(String api) throws Exception
	{
		StringBuffer responseBuffer = new StringBuffer();
		JSONObject result = new JSONObject();

		try
		{
			responseBuffer = _http.doGet(api);

			if ((responseBuffer == null) || (responseBuffer.length() <= 0))
			{
				result = null;
			} else {
				result = new JSONObject(new JSONTokener(new String(responseBuffer.toString().getBytes(), "utf-8")));
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} catch (Throwable t) {
			throw new Exception(t.getMessage());
		}

		return result;
	}

	public boolean onClose()
	{
		if (thread != null)
		{
			try {
				thread.interrupt();
				thread = null;
			} catch (Exception e) {}
		}

		try {
			UiApplication.getUiApplication().popScreen(this);	
		} catch (Exception e) {}

		return true;
	}

	protected boolean keyChar(char key, int status, int time)
	{
		if (key == Characters.ESCAPE)
		{
			onClose();

			return true;
		}

		return super.keyChar(key, status, time);
	}
} 