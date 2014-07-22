package com.walkline.util.network;

import net.rim.device.api.i18n.SimpleDateFormat;

import com.walkline.util.json.JSONObject;

public class GameRankItem implements RankItem
{
	private String _nickname = "";
	private String _score = "";
	private String _last_time = "";

	private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

	public GameRankItem(JSONObject jsonObject) throws Exception
	{
		JSONObject rankItem = jsonObject;
		if (rankItem != null)
		{
			_nickname = rankItem.optString("nickname");
			_score = rankItem.optString("score");

			_last_time = rankItem.optString("last");
			if (!_last_time.equals(""))
			{
				_last_time = sdf.formatLocal(Long.parseLong(_last_time));	
			}
		}
	}

	public String getNickname() {return _nickname;}

	public String getScore() {return _score;}

	public String getLastTime() {return _last_time;}
}