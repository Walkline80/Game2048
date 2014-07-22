package com.walkline.util.network;

import java.util.Vector;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;
import com.walkline.util.Function;

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
						JSONObject storyObject = (JSONObject) rankingArray.get(i);

						RankItem item = new GameRankItem(storyObject);
						if (item != null) {_ranking.addElement(item);}
					} catch (JSONException e) {Function.errorDialog(e.toString());}
				}
			}
		}
	}

	public int getTotal() {return _total;}

	public Vector getRanking() {return _ranking;}
}