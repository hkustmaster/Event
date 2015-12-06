package com.hkust.android.event;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hkust.android.event.adapters.DateListAdapter;
import com.hkust.android.event.model.Event;
import com.hkust.android.event.model.Participant;
import com.hkust.android.event.model.VoteRecord;

import java.util.ArrayList;
import java.util.Calendar;

public class DateVotingActivity extends AppCompatActivity {
    int year_x, month_x, day_x;
    static final int DATE_DIALOG_ID = 0;
    boolean isHost = false;
    ArrayList<VoteRecord> voteRecords = new ArrayList<VoteRecord>();
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_voting);
        setTitle("Vote For Available Dates");
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //get isHost and the voteResult
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                isHost = false;
                voteRecords = null;
            } else {
                if (extras.getString("isHost").equalsIgnoreCase("true")) {
                    isHost = true;
                } else {
                    isHost = false;
                }
                ;
                voteRecords = gson.fromJson(extras.getString("voteResult"), new TypeToken<ArrayList<VoteRecord>>() {
                }.getType());
            }
        } else {

        }


        //set the vote result to the adapter and display.
        RecyclerView dateList = (RecyclerView) findViewById(R.id.date_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        dateList.setLayoutManager(layoutManager);
        DateListAdapter dateListAdapter = new DateListAdapter(voteRecords, isHost);
        dateList.setAdapter(dateListAdapter);


        Button voteBtn = (Button) findViewById(R.id.vote_btn);
        voteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //post the vote result to server;
                //update the participant;
            }
        });

        Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);
        Button suggestion = (Button) findViewById(R.id.suggestion_btn);
        suggestion.setVisibility(View.INVISIBLE);
        showDialogOnDateTextFieldClick();
    }

    public void showDialogOnDateTextFieldClick() {
        Button suggestionBtn = (Button) findViewById(R.id.suggestion_btn);
        suggestionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });
    }

    public Dialog onCreateDialog(int id) {
        if (id == DATE_DIALOG_ID) {
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
        }

        return super.onOptionsItemSelected(item);
    }

}
