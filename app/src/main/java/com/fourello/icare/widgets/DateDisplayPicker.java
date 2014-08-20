package com.fourello.icare.widgets;

import java.text.DateFormatSymbols;
import java.util.Calendar;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.fourello.icare.view.CustomTextView;

public class DateDisplayPicker extends CustomTextView implements DatePickerDialog.OnDateSetListener{

	private Context _context;

	public DateDisplayPicker(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		_context = context;
	}

	public DateDisplayPicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		_context = context;
		setAttributes();
	}

	public DateDisplayPicker(Context context) {
		super(context);
		_context = context;
		setAttributes();
	}

	private void setAttributes() {
		setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDateDialog();
			}
		});
	}

	private void showDateDialog() {
		final Calendar c = Calendar.getInstance();
		DatePickerDialog dp = new DatePickerDialog(_context, this,
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH));

		dp.show();
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {

        setText(String.format("%s %s, %s", getMonthForInt(monthOfYear), dayOfMonth, year));
	}
    String getMonthForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getShortMonths();
        if (num >= 0 && num <= 11 ) {
            month = months[num];
        }
        return month;
    }

}