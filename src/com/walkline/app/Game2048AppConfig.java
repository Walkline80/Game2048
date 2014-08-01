package com.walkline.app;

import java.util.Vector;

import net.rim.device.api.io.transport.TransportInfo;
import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.decor.Background;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.ui.decor.Border;
import net.rim.device.api.ui.decor.BorderFactory;

public class Game2048AppConfig
{
	public static final int LINES = 4;

	public static final Border border_popup_Transparent=BorderFactory.createRoundedBorder(new XYEdges(16,16,16,16), Color.BLACK, Border.STYLE_TRANSPARENT);
	public static final Background bg_popup_Transparent=BackgroundFactory.createSolidTransparentBackground(Color.BLACK, 200);

	public static int[] preferredTransportTypes = {
		TransportInfo.TRANSPORT_TCP_WIFI,
		TransportInfo.TRANSPORT_BIS_B,
		TransportInfo.TRANSPORT_TCP_CELLULAR,
		TransportInfo.TRANSPORT_WAP2
	};

	public static int[] disallowedTransportTypes = {
		TransportInfo.TRANSPORT_MDS,
		TransportInfo.TRANSPORT_WAP
	};

	public static final String queryUpdateRecorderUrl = "http://walkline2048.jd-app.com/index.php";
	public static final String queryRankingRecordersUrl = "http://walkline2048.jd-app.com/index.php?action=query";

	public static final Font FONT_LIST_NICKNAME = Font.getDefault().derive(Font.PLAIN, Font.getDefault().getHeight(Ui.UNITS_pt) + 3, Ui.UNITS_pt);
	public static final Font FONT_LIST_SCORE = Font.getDefault().derive(Font.PLAIN, Font.getDefault().getHeight(Ui.UNITS_pt) + 2, Ui.UNITS_pt);

	//Elements and Keys
	private Vector _elements;
	private static final int BEST_SCORE = 0;
	private static final int NICKNAME = 1;

	//Persistent objects
	private static Vector _data;
	private static PersistentObject _store;
	private int _best_score = 0;
	private String _nickname = "";

	public Game2048AppConfig()
	{
		_elements = new Vector(2);

		for (int i=0; i<_elements.capacity(); i++)
		{
			_elements.addElement(new Object());
		}
	}

	private int getElementInt(int id) {return Integer.parseInt(_elements.elementAt(id).toString());}

	private String getElementString(int id) {return (String) _elements.elementAt(id);}

	private void setElement(int id, Object value) {_elements.setElementAt(value, id);}

	public void setBestScore(int value)
	{
		setElement(BEST_SCORE, new Integer(value));
		_best_score = value;
	}

	public int getBestScore() {return _best_score;}

	public void setNickname(String value)
	{
		setElement(NICKNAME, value);
		_nickname = value;
	}

	public String getNickname() {return _nickname;}

	public void initialize()
	{
		synchronized(_store)
		{
			try {
				_data = (Vector) _store.getContents();

				if (!_data.isEmpty())
				{
					_elements = (Vector) _data.lastElement();

					_best_score = getElementInt(BEST_SCORE);
					_nickname = getElementString(NICKNAME);
				}
			} catch (Exception e) {
				_store.setContents(new Vector());
				_store.forceCommit();
				_data = new Vector();
			}
		}
	}

	public void save()
	{
		setBestScore(_best_score);
		setNickname(_nickname);

		_data.addElement(_elements);

		synchronized(_store)
		{
			_store.setContents(_data);
			_store.commit();
		}
	}

	static
	{
		_store = PersistentStore.getPersistentObject(0x4a8a7b69f292a404L); //blackberry_2048_written_by_Walkline_Wang
	}

	//SKU: 0xcdf7f4fe131aef6L (blackberry_2048_game_written_by_walkline_wang)
}