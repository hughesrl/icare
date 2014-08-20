package com.fourello.icare.widgets;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.fourello.icare.view.CustomTextView;

import java.text.DateFormatSymbols;
import java.util.Calendar;

public class TimeDisplayPicker extends CustomTextView implements TimePickerDialog.OnTimeSetListener{

	private Context _context;

	public TimeDisplayPicker(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		_context = context;
	}

	public TimeDisplayPicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		_context = context;
		setAttributes();
	}

	public TimeDisplayPicker(Context context) {
		super(context);
		_context = context;
		setAttributes();
	}

	private void setAttributes() {
		setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                showTimeDialog();
			}
		});
	}

	private void showTimeDialog() {
        TimePickerDialog tp = new TimePickerDialog(_context, mTimeSetListener, 0, 0, false);
        tp.show();
	}

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        setText(String.format("%s:%s", hourOfDay, minute));
    }
    TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String hour;
            String min;
            if (hourOfDay < 10)
                hour = "0" + hourOfDay ;
            else
                hour = String.valueOf(hourOfDay);

            if (minute < 10)
                min = "0" + minute ;
            else
                min = String.valueOf(minute);

            setText(String.format("%s:%s", hour, min));
        }
    };
}