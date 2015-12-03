package com.hkust.android.event;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import java.util.Calendar;

import java.util.Calendar;

public class NewEventActivity extends AppCompatActivity implements  View.OnClickListener{

    int year_x, month_x, day_x,hour_x, minute_x;
    static final int START_DATE_DIALOG_ID = 0, END_DATE_DIALOG_ID = 2, TIME_DIALOG_ID=3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        setTitle("New Event");
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CheckBox tbdCheckBox = (CheckBox)findViewById(R.id.start_date_tbd_checkbox);
        tbdCheckBox.setOnClickListener(this);
        TextInputLayout endDateLayout = (TextInputLayout)findViewById(R.id.new_event_end_date_layout);
        endDateLayout.setVisibility(View.INVISIBLE);


        Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);
        hour_x = cal.get(Calendar.HOUR);
        minute_x = cal.get(Calendar.MINUTE);
        showDialogOnStartDateTextFieldClick();
        showDialogOnEndDateTextFieldClick();
        showDialogOnTimeTextFieldClick();
    }

    public void showDialogOnStartDateTextFieldClick(){
        AutoCompleteTextView startDateTextField = (AutoCompleteTextView) findViewById(R.id.new_event_date);
        startDateTextField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(START_DATE_DIALOG_ID);
            }
        });
    }
    public void showDialogOnEndDateTextFieldClick(){
        AutoCompleteTextView endDateTextField = (AutoCompleteTextView) findViewById(R.id.new_event_end_date);
        endDateTextField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(END_DATE_DIALOG_ID);
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
        if(id == START_DATE_DIALOG_ID){
            return new DatePickerDialog(this, startDpickerListener, year_x, month_x, day_x);
        }else if(id == TIME_DIALOG_ID){
            return new TimePickerDialog(this, tpickerListener, hour_x, minute_x, false);
        }else if(id == END_DATE_DIALOG_ID){
            return new DatePickerDialog(this, endDpickerListener, year_x, month_x, day_x);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener endDpickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            year_x = year;
            month_x = monthOfYear;
            day_x = dayOfMonth;
            AutoCompleteTextView dateTextField = (AutoCompleteTextView) findViewById(R.id.new_event_end_date);
            dateTextField.setText(year_x+"-"+month_x+"-"+day_x);
        }
    };

    private DatePickerDialog.OnDateSetListener startDpickerListener = new DatePickerDialog.OnDateSetListener() {
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.start_date_tbd_checkbox:
                CheckBox tbdCheckBox = (CheckBox)findViewById(R.id.start_date_tbd_checkbox);
                if(tbdCheckBox.isChecked()){
                    TextInputLayout endDateLayout = (TextInputLayout)findViewById(R.id.new_event_end_date_layout);
                    endDateLayout.setVisibility(View.VISIBLE);
                }else{
                    TextInputLayout endDateLayout = (TextInputLayout)findViewById(R.id.new_event_end_date_layout);
                    endDateLayout.setVisibility(View.INVISIBLE);
                }
                break;
            default:
        }
    }

}
