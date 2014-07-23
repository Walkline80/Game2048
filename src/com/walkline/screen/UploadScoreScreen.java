package com.walkline.screen;

import java.util.Hashtable;

import net.rim.device.api.system.Characters;
import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.PopupScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import com.walkline.app.Game2048AppConfig;
import com.walkline.util.Function;
import com.walkline.util.network.HttpClient;
import com.walkline.util.network.MyConnectionFactory;

public class UploadScoreScreen extends PopupScreen
{
	private HttpClient _http;
	private Game2048AppConfig _appConfig;
	private LabelField _labelTitle;
	private EditField _blockField;
	private ButtonField _buttonOk;

	public UploadScoreScreen(Game2048AppConfig appConfig)
	{
		super(new VerticalFieldManager());

		MyConnectionFactory cf = new MyConnectionFactory();

		_http = new HttpClient(cf);
		_appConfig = appConfig;
		_labelTitle = new LabelField("请输入你的名字：", Field.FIELD_HCENTER);
		_labelTitle.setMargin(0,  0, 5, 0);
		_blockField=new EditField(null, _appConfig.getNickname(), 10, EditField.NO_NEWLINE);

		_buttonOk = new ButtonField("确定", ButtonField.CONSUME_CLICK | ButtonField.NEVER_DIRTY);
		_buttonOk.setChangeListener(new FieldChangeListener()
		{
			public void fieldChanged(Field field, int context)
			{
				UiApplication.getUiApplication().invokeLater(new Runnable()
				{
					public void run() 
					{
						Hashtable param = new Hashtable();

						param.put("pin", Integer.toHexString(DeviceInfo.getDeviceId()).toUpperCase());
						param.put("nickname", Function.encode(_blockField.getText()));
						param.put("device", "");
						param.put("score", String.valueOf(_appConfig.getBestScore()));
						param.put("action", "update");
						param.put("last", String.valueOf(System.currentTimeMillis()));

						_appConfig.setNickname(_blockField.getText());
						_appConfig.save();
						try {
							_http.doGet(Game2048AppConfig.queryUpdateRecorderUrl, param);
						} catch (Exception e) {}

						close();
					}
				});
			}
		});
		_buttonOk.setMinimalWidth(100);

		ButtonField buttonCancel = new ButtonField("取消", ButtonField.CONSUME_CLICK | ButtonField.NEVER_DIRTY);
		buttonCancel.setChangeListener(new FieldChangeListener()
		{
			public void fieldChanged(Field field, int context) 
			{
				onClose();
			}
		});
		buttonCancel.setMinimalWidth(100);

		add(_labelTitle);
		add(_blockField);
		HorizontalFieldManager hfm = new HorizontalFieldManager(Manager.FIELD_HCENTER);
		hfm.add(_buttonOk);
		hfm.add(buttonCancel);
		add(hfm);
	}

	public boolean onClose()
	{
		UiApplication.getUiApplication().popScreen(this);
		return true;
	}

	protected boolean onSavePrompt() {return true;}
	
	protected boolean keyChar(char character, int status, int time)
	{
		switch (character)
		{
			case Characters.ESCAPE:
				_blockField.setText("");
				onClose();
				return true;
			case Characters.ENTER:
				if (_blockField.isFocus())
				{
					_buttonOk.setFocus();
					return true;
				} 				
		}

		return super.keyChar(character, status, time);
	}
}