package com.walkline.screen;

import java.util.Vector;
import localization.Game2048Resource;
import net.rim.device.api.i18n.ResourceBundle;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.container.MainScreen;
import com.walkline.util.Enumerations.LoadingActions;
import com.walkline.util.network.RankItem;
import com.walkline.util.network.Ranking;
import com.walkline.util.ui.ForegroundManager;
import com.walkline.util.ui.ListStyleButtonField;
import com.walkline.util.ui.ListStyleButtonSet;

public class GameRankingScreen extends MainScreen implements Game2048Resource
{
	private static ResourceBundle _bundle = ResourceBundle.getBundle(BUNDLE_ID, BUNDLE_NAME);
	//protected HttpClient _http;
	private ForegroundManager _foregrond = new ForegroundManager(0);
	private ListStyleButtonSet _rankingSet = new ListStyleButtonSet();

	public GameRankingScreen()
	{
		super(NO_VERTICAL_SCROLL | NO_SYSTEM_MENU_ITEMS);

		setTitle(getResString(SCREEN_TITLE_RANKING));

		//MyConnectionFactory cf = new MyConnectionFactory();

		//_http = new HttpClient(cf);

		_foregrond.add(_rankingSet);
		add(_foregrond);

		UiApplication.getUiApplication().invokeLater(new Runnable()
		{
			public void run()
			{
				Ranking ranking;

				PleaseWaitScreen popupScreen = new PleaseWaitScreen(null, LoadingActions.LOADRANKING);
				UiApplication.getUiApplication().pushModalScreen(popupScreen);

				ranking = popupScreen.getRankingList();

				if (popupScreen != null) {popupScreen = null;}
				if (ranking != null)
				{
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
			}
		});
	}

	private String getResString(int key) {return _bundle.getString(key);}
}