package com.hkust.android.event;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import com.hkust.android.event.adapters.DateListAdapter;

import java.util.Calendar;

public class DateVotingActivity extends AppCompatActivity {
    int year_x, month_x, day_x;
    static final int DATE_DIALOG_ID = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_voting);
        Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);

        RecyclerView dateList = (RecyclerView)findViewById(R.id.date_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        dateList.setLayoutManager(layoutManager);

        DateListAdapter dateListAdapter = new DateListAdapter();

        dateList.setAdapter(dateListAdapter);

        showDialogOnDateTextFieldClick();
    }

    public void showDialogOnDateTextFieldClick(){
        Button suggestionBtn = (Button) findViewById(R.id.suggestion_btn);
        suggestionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });
    }

    public Dialog onCreateDialog(int id){
        if(id == DATE_DIALOG_ID){
            return new DatePickerDialog(this, dpickerListener, year_x, month_x, day_x);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener dpickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            year_x = year;
            month_x = monthOfYear;
            day_x = dayOfMonth;
            //AutoCompleteTextView dateTextField = (AutoCompleteTextView) findViewById(R.id.new_event_date);
            //dateTextField.setText(year_x+"-"+month_x+"-"+day_x);
        }
    };


}
