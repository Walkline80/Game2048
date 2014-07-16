package com.walkline.util.ui;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Manager;

public class ScoreboardFieldManager extends Manager
{
	private static final int PADDING = 10;
	private ScoreboardField _score = new ScoreboardField();
	private BestScoreboardField _best = new BestScoreboardField();

	public ScoreboardFieldManager(long sytle)
	{
		super(NON_FOCUSABLE | NO_HORIZONTAL_SCROLL | NO_VERTICAL_SCROLL | USE_ALL_WIDTH | sytle);

		add(new AppTitleField());
		add(_score);
		add(_best);

		if (sytle == Field.FIELD_RIGHT)
		{
			setPadding(10, 10, 10, 0);
		} else if (sytle == Field.FIELD_HCENTER) {
			setPadding(10, 10, 0, 10);
		}
	}

	public void clear()
	{
		_score.clear();

		invalidate();
	}

	public void update(int value)
	{
		_score.update(value);

		if (_score.getScore() > _best.getScore()) {_best.setScore(_score.getScore());}

		invalidate();
	}

	protected void sublayout(int width, int height)
	{
		int currentX = 0;
		int currentY = 0;
		int fieldWidth = 0;

		Field titleChild = getField(0);
		Field scoreField = getField(1);
		Field bestField = getField(2);

		if (isStyle(Field.FIELD_HCENTER)) {
			currentY = (bestField.getPreferredHeight() - titleChild.getPreferredHeight() + PADDING) / 2;
		}

		layoutChild(titleChild, width, titleChild.getPreferredHeight());
		setPositionChild(titleChild, currentX, currentY);

		if (isStyle(Field.FIELD_HCENTER)) {
			fieldWidth = scoreField.getPreferredWidth();
			currentX = width - scoreField.getPreferredWidth() - bestField.getPreferredWidth() - PADDING;
			currentY = 0;
		} else if (isStyle(Field.FIELD_RIGHT)) {
			fieldWidth = width;
			currentX = 0;
			currentY += titleChild.getPreferredHeight() + PADDING;
		}

		layoutChild(scoreField, fieldWidth, scoreField.getPreferredHeight());
		setPositionChild(scoreField, currentX, currentY);

		if (isStyle(Field.FIELD_HCENTER)) {
			fieldWidth = bestField.getPreferredWidth();
			currentX = width - bestField.getPreferredWidth();
			currentY = 0;
		} else if (isStyle(Field.FIELD_RIGHT)) {
			fieldWidth = width;
			currentX = 0;
			currentY += scoreField.getPreferredHeight() + PADDING;
		}

		layoutChild(bestField, fieldWidth, bestField.getPreferredHeight());
		setPositionChild(bestField, currentX, currentY);

		int managerHeight = 0;

		if (isStyle(Field.FIELD_HCENTER)) {
			managerHeight = bestField.getPreferredHeight();
		} else if (isStyle(Field.FIELD_RIGHT)) {
			managerHeight = height;
		}

		setExtent(width, managerHeight);
	}
}