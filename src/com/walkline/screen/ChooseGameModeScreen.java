package com.walkline.screen;

import localization.Game2048Resource;
import net.rim.device.api.i18n.ResourceBundle;
import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.PopupScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import com.walkline.util.Enumerations.GameModes;
import com.walkline.util.ui.VerticalButtonFieldSet;

public class ChooseGameModeScreen extends PopupScreen implements FieldChangeListener, Game2048Resource
{
	private static ResourceBundle _bundle = ResourceBundle.getBundle(BUNDLE_ID, BUNDLE_NAME);

	ButtonField _buttonModeEasy;
	ButtonField _buttonModeNormal;
	ButtonField _buttonModeHard;
	
	private int _result = -1;

	public ChooseGameModeScreen()
	{
		super(new VerticalFieldManager(VERTICAL_SCROLL | VERTICAL_SCROLL_MASK));
		
		LabelField headLine = new LabelField(getResString(GAME_MODE_TITLE));

		_buttonModeEasy = new ButtonField(getResString(GAME_MODE_EASY), ButtonField.CONSUME_CLICK | ButtonField.NEVER_DIRTY);
		_buttonModeEasy.setChangeListener(this);
		_buttonModeNormal = new ButtonField(getResString(GAME_MODE_NORMAL), ButtonField.CONSUME_CLICK | ButtonField.NEVER_DIRTY);
		_buttonModeNormal.setChangeListener(this);
		_buttonModeHard = new ButtonField(getResString(GAME_MODE_HARD), ButtonField.CONSUME_CLICK | ButtonField.NEVER_DIRTY);
		_buttonModeHard.setChangeListener(this);

		VerticalButtonFieldSet vbf = new VerticalButtonFieldSet(USE_ALL_WIDTH);
		vbf.add(_buttonModeEasy);
		vbf.add(_buttonModeNormal);
		vbf.add(_buttonModeHard);

		add(headLine);
		add(new SeparatorField());
		add(vbf);

		UiApplication.getUiApplication().invokeLater(new Runnable()
		{
			public void run() {_buttonModeNormal.setFocus();}
		});
	}

	public boolean onClose()
	{
		UiApplication.getUiApplication().popScreen(this);					

		return true;
	}

	protected boolean keyChar(char character, int status, int time)
	{
		switch (character)
		{
			case Characters.LATIN_CAPITAL_LETTER_E:
			case Characters.LATIN_SMALL_LETTER_E:
				_result = GameModes.EASY;
				onClose();
				return true;
			case Characters.LATIN_CAPITAL_LETTER_N:
			case Characters.LATIN_SMALL_LETTER_N:
				_result = GameModes.NORMAL;
				onClose();
				return true;
			case Characters.LATIN_CAPITAL_LETTER_H:
			case Characters.LATIN_SMALL_LETTER_H:
				_result = GameModes.HARD;
				onClose();
				return true;
			case Characters.ESCAPE:
				onClose();
				return true;
		}

		return super.keyChar(character, status, time);
	}

	private String getResString(int key) {return _bundle.getString(key);}

	public int getSelection() {return _result;}

	public void fieldChanged(Field field, int context)
	{
		if (field instanceof ButtonField)
		{
			if (field.equals(_buttonModeEasy)) {
				_result = GameModes.EASY;
			} else if (field.equals(_buttonModeNormal)) {
				_result = GameModes.NORMAL;
			} else if (field.equals(_buttonModeHard)) {
				_result = GameModes.HARD;
			}

			onClose();
		}
	}
}