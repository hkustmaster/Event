package com.hkust.android.event;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import java.util.Calendar;

import java.util.Calendar;

public class NewEventActivity extends AppCompatActivity{

    int year_x, month_x, day_x,hour_x, minute_x;
    static final int DATE_DIALOG_ID = 0, TIME_DIALOG_ID=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);
        hour_x = cal.get(Calendar.HOUR);
        minute_x = cal.get(Calendar.MINUTE);
        showDialogOnDateTextFieldClick();
        showDialogOnTimeTextFieldClick();
    }

    public void showDialogOnDateTextFieldClick(){
        AutoCompleteTextView dateTextField = (AutoCompleteTextView) findViewById(R.id.new_event_date);
        dateTextField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });
    }

    public void showDialogOnTimeTextFieldClick(){
        AutoCompleteTextView timeTextField = (AutoCompleteTextView) findViewById(R.id.new_event_time);
        timeTextField.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showDialog(TIME_DIALOG_ID);
            }
        });
    }

    public Dialog onCreateDialog(int id){
        if(id == DATE_DIALOG_ID){
            return new DatePickerDialog(this, dpickerListener, year_x, month_x, day_x);
        }else if(id == TIME_DIALOG_ID){
            return new TimePickerDialog(this, tpickerListener, hour_x, minute_x, false);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener dpickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            year_x = year;
            month_x = monthOfYear;
            day_x = dayOfMonth;
            AutoCompleteTextView dateTextField = (AutoCompleteTextView) findViewById(R.id.new_event_date);
            dateTextField.setText(year_x+"-"+month_x+"-"+day_x);
        }
    };

    private TimePickerDialog.OnTimeSetListener tpickerListener = new TimePickerDialog.OnTimeSetListener(){
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            hour_x = hourOfDay;
            minute_x = minute;
            AutoCompleteTextView timeTextField = (AutoCompleteTextView) findViewById(R.id.new_event_time);
            if(minute < 10) {
                timeTextField.setText(hour_x + ": 0" + minute_x);
            }else{
                timeTextField.setText(hour_x + ": " + minute_x);
            }
        }
    };
}
