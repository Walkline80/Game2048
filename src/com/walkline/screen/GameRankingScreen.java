package com.walkline.screen;

import java.util.Vector;

import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.container.MainScreen;

import org.json.me.JSONObject;
import org.json.me.JSONTokener;

import com.walkline.app.Game2048AppConfig;
import com.walkline.util.Function;
import com.walkline.util.network.GameRanking;
import com.walkline.util.network.HttpClient;
import com.walkline.util.network.MyConnectionFactory;
import com.walkline.util.network.RankItem;
import com.walkline.util.network.Ranking;
import com.walkline.util.ui.ForegroundManager;
import com.walkline.util.ui.ListStyleButtonField;
import com.walkline.util.ui.ListStyleButtonSet;

public class GameRankingScreen extends MainScreen
{
	protected HttpClient _http;
	private ForegroundManager _foregrond = new ForegroundManager(0);
	private ListStyleButtonSet _rankingSet = new ListStyleButtonSet();

	public GameRankingScreen()
	{
		super(NO_VERTICAL_SCROLL);

		setTitle("排行榜");

		MyConnectionFactory cf = new MyConnectionFactory();

		_http = new HttpClient(cf);

		_foregrond.add(_rankingSet);
		add(_foregrond);

		UiApplication.getUiApplication().invokeLater(new Runnable()
		{
			public void run()
			{
				Ranking ranking = getRanking();
				RankItem rankItem;
				ListStyleButtonField item;

				if (ranking != null)
				{
					Vector rankingVector = ranking.getRanking();

					for (int i=0; i<rankingVector.size(); i++)
					{
						rankItem = (RankItem) rankingVector.elementAt(i);

						if (rankItem != null)
						{
							item = new ListStyleButtonField(rankItem);
							_rankingSet.add(item);
						}
					}
				}
			}
		});
	}

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
			responseBuffer = _http.doGet(api); //.append("{\"total\":1,\"ranking\":[{\"nickname\":\"Jay\",\"score\":\"850\",\"last\":\"12233500832\"}]}");

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
}