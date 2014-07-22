package com.walkline.util.network;

import java.util.Vector;

import com.walkline.util.Function;
import com.walkline.util.json.JSONArray;
import com.walkline.util.json.JSONException;
import com.walkline.util.json.JSONObject;

public class GameRanking implements Ranking
{
	private int _total = 0;
	private Vector _ranking = new Vector();

	public GameRanking(JSONObject jsonObject) throws Exception
	{
		JSONObject ranking = jsonObject;
		if (ranking != null)
		{
			_total = ranking.optInt("total");

			JSONArray rankingArray = ranking.optJSONArray("ranking");
			if (rankingArray != null)
			{
				for (int i=0; i<rankingArray.length(); i++)
				{
					try {
						JSONObject rankingObject = (JSONObject) rankingArray.get(i);

						RankItem item = new GameRankItem(rankingObject);
						if (item != null) {_ranking.addElement(item);}
					} catch (JSONException e) {Function.errorDialog(e.toString());}
				}
			}
		}
	}

	public int getTotal() {return _total;}

	public Vector getRanking() {return _ranking;}
}