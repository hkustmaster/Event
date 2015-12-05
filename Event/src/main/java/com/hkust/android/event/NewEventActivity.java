package com.hkust.android.event;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

//import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
//import com.google.android.gms.common.GooglePlayServicesRepairableException;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.location.places.Place;
//import com.google.android.gms.location.places.Places;
//import com.google.android.gms.location.places.ui.PlacePicker;


import com.google.gson.Gson;
import com.hkust.android.event.model.Constants;
import com.hkust.android.event.model.Event;
import com.hkust.android.event.model.Location;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;


public class NewEventActivity extends AppCompatActivity implements View.OnClickListener {

    int year_x, month_x, day_x, hour_x, minute_x;
    static final int START_DATE_DIALOG_ID = 0, END_DATE_DIALOG_ID = 2, TIME_DIALOG_ID = 3;
    private SharedPreferences sp;

    private static final int PLACE_PICKER_REQUEST = 1000;
    //private GoogleApiClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        setTitle("New Event");
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        mClient = new GoogleApiClient
//                .Builder(this)
//                .addApi(Places.GEO_DATA_API)
//                .addApi(Places.PLACE_DETECTION_API)
//                .build();

        CheckBox tbdCheckBox = (CheckBox) findViewById(R.id.start_date_tbd_checkbox);
        tbdCheckBox.setOnClickListener(this);
        TextInputLayout endDateLayout = (TextInputLayout) findViewById(R.id.new_event_end_date_layout);
        endDateLayout.setVisibility(View.INVISIBLE);
        Button createBtn = (Button) findViewById(R.id.new_event_create_btn);
        createBtn.setOnClickListener(this);


        AutoCompleteTextView locationTextView = (AutoCompleteTextView) findViewById(R.id.new_event_location);
        locationTextView.setOnClickListener(this);

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


    public void showDialogOnStartDateTextFieldClick() {
        AutoCompleteTextView startDateTextField = (AutoCompleteTextView) findViewById(R.id.new_event_date);
        startDateTextField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(START_DATE_DIALOG_ID);
            }
        });
    }

    public void showDialogOnEndDateTextFieldClick() {
        AutoCompleteTextView endDateTextField = (AutoCompleteTextView) findViewById(R.id.new_event_end_date);
        endDateTextField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(END_DATE_DIALOG_ID);
            }
        });
    }

    public void showDialogOnTimeTextFieldClick() {
        AutoCompleteTextView timeTextField = (AutoCompleteTextView) findViewById(R.id.new_event_time);
        timeTextField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(TIME_DIALOG_ID);
            }
        });
    }

    public Dialog onCreateDialog(int id) {
        if (id == START_DATE_DIALOG_ID) {
            return new DatePickerDialog(this, startDpickerListener, year_x, month_x, day_x);
        } else if (id == TIME_DIALOG_ID) {
            return new TimePickerDialog(this, tpickerListener, hour_x, minute_x, false);
        } else if (id == END_DATE_DIALOG_ID) {
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
            int month = month_x+1;
            AutoCompleteTextView dateTextField = (AutoCompleteTextView) findViewById(R.id.new_event_end_date);
            dateTextField.setText(year_x + "-" + month + "-" + day_x);
        }
    };

    private DatePickerDialog.OnDateSetListener startDpickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            year_x = year;
            month_x = monthOfYear;
            day_x = dayOfMonth;
            int month = month_x+1;
            AutoCompleteTextView dateTextField = (AutoCompleteTextView) findViewById(R.id.new_event_date);
            dateTextField.setText(year_x + "-" + month + "-" + day_x);
        }
    };

    private TimePickerDialog.OnTimeSetListener tpickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            hour_x = hourOfDay;
            minute_x = minute;
            AutoCompleteTextView timeTextField = (AutoCompleteTextView) findViewById(R.id.new_event_time);
            if (minute < 10) {
                timeTextField.setText(hour_x + ": 0" + minute_x);
            } else {
                timeTextField.setText(hour_x + ": " + minute_x);
            }
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


    @Override
    protected void onStart() {
        super.onStart();
//        mClient.connect();
    }

    @Override
    protected void onStop() {
//        mClient.disconnect();
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_date_tbd_checkbox:
                CheckBox tbdCheckBox = (CheckBox) findViewById(R.id.start_date_tbd_checkbox);
                if (tbdCheckBox.isChecked()) {
                    TextInputLayout endDateLayout = (TextInputLayout) findViewById(R.id.new_event_end_date_layout);
                    endDateLayout.setVisibility(View.VISIBLE);
                } else {
                    TextInputLayout endDateLayout = (TextInputLayout) findViewById(R.id.new_event_end_date_layout);
                    endDateLayout.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.new_event_create_btn:
                boolean endTime = false;
                TextView title_Text = (TextView) findViewById(R.id.new_event_title);
                TextView location_Text = (TextView) findViewById(R.id.new_event_location);
                TextView startData_Text = (TextView) findViewById(R.id.new_event_date);
                CheckBox tbd_CheckBox = (CheckBox) findViewById(R.id.start_date_tbd_checkbox);
                TextView endDate_Text = null;
                if (tbd_CheckBox.isChecked()) {
                    endTime = true;
                    endDate_Text = (TextView) findViewById(R.id.new_event_end_date);
                }
                TextView time_Text = (TextView) findViewById(R.id.new_event_time);
                TextView number_Text = (TextView) findViewById(R.id.number_of_parti);
                TextView desc_Text = (TextView) findViewById(R.id.new_event_description);
                TextView locationLatLng = (TextView) findViewById(R.id.address_latlng_textView);

                Location location = new Location();
                location.setType("Point");
                String title = title_Text.getText().toString();
                String l = location_Text.getText().toString();

                location.setCoordinates(getLatitudeLongitude(locationLatLng.getText().toString()));
                String address = location_Text.getText().toString();
                String startDate = startData_Text.getText().toString();
                String endDate = "";
                if (endTime)
                    endDate = endDate_Text.getText().toString();

                String time = time_Text.getText().toString();
                String number = number_Text.getText().toString();
                String desc = desc_Text.getText().toString();

                if ("".equalsIgnoreCase(title) ||
                        "".equalsIgnoreCase(l) ||
                        "".equalsIgnoreCase(startDate) ||
//                        && "".equalsIgnoreCase(endDate)||
                        "".equalsIgnoreCase(time) ||
                        "".equalsIgnoreCase(number) ||
                        "".equalsIgnoreCase(desc)) {
                    Toast.makeText(getApplicationContext(), "All Fields Required.", Toast.LENGTH_LONG).show();

                } else {

                    if (endTime && "".equalsIgnoreCase(endDate)) {
                        Toast.makeText(getApplicationContext(), "End data field required.", Toast.LENGTH_LONG).show();
                    } else {
                        Event event = new Event();
                        event.setTitle(title);
                        //event.setLocation(location);
                        event.setStartAt(startDate);
                        event.setEndAt(endDate);
                        event.setTime(time);
                        event.setSize(Integer.parseInt(number));
                        event.setDescription(desc);
                        event.setStatus(Constants.STATUS_EVENT_PRE);
                        event.setAddress(address);
                        sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                        String token = sp.getString("token", "");
                        event.setToken(token);
                        if (checkEventInfo(event)) {
                            AsyncHttpClient client = new AsyncHttpClient();
                            Gson gson = new Gson();
                            try {
                                JSONObject jsono = new JSONObject(gson.toJson(event));
                                jsono.accumulate("location", getLocationCor(location.getCoordinates()));
                                StringEntity entity = new StringEntity(jsono.toString());
                                Log.i("pppp", "Valid event, Create Event..." + jsono.toString());
                                client.post(this.getApplicationContext(), Constants.SERVER_URL + Constants.ADD_EVENT, entity, "application/json", new AsyncHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                        String response = new String(responseBody);
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            String message = jsonObject.getString("message");
                                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                            if (message.equalsIgnoreCase("succeed")) {
                                                finish();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                        Toast.makeText(getApplicationContext(), "Connection Failed", Toast.LENGTH_LONG).show();
                                    }
                                });
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Error: please check all fields!", Toast.LENGTH_SHORT).show();
                        }
                    }

                }


                break;
            case R.id.new_event_location:
//                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
//                try {
//                    startActivityForResult(builder.build(NewEventActivity.this), PLACE_PICKER_REQUEST);
//                } catch (GooglePlayServicesRepairableException e) {
//                    e.printStackTrace();
//                } catch (GooglePlayServicesNotAvailableException e) {
//                    e.printStackTrace();
//                }


                break;
            default:
        }
    }

    private boolean checkEventInfo(Event event) {

       /* ValidFormTools validFormTools = new ValidFormTools();

        if(!validFormTools.isValidStartDateTime(event.getBeginAt(),event.getTime())){
            Toast.makeText(getApplicationContext(), "Invalid start date or time!", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            if(!"".equalsIgnoreCase(event.getEndAt())&&!validFormTools.isValidEndDate(event.getBeginAt(), event.getEndAt())){
                Toast.makeText(getApplicationContext(), "Invalid start or end date!", Toast.LENGTH_SHORT).show();
                return false;
            }
        }*/
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
//                Place place = PlacePicker.getPlace(data, this);
//                String toastMsg = String.format("Place: %s", place.getAddress());
//                StringBuffer buff = new StringBuffer();
//                buff.append(" getAddress:" + place.getAddress());
//                buff.append(" getId:" + place.getId());
//                buff.append(" getLatlng:" + place.getLatLng());
//                buff.append(" getLocale:" + place.getLocale());
//                buff.append(" getName:" + place.getName());
//                buff.append(" getPhoneName:" + place.getPhoneNumber());
//                buff.append(" getPlaceType:" + place.getPlaceTypes());
//                buff.append(" getPriceLevel:" + place.getPriceLevel());
//                buff.append(" getRating:" + place.getRating());
//                buff.append(" getViewport:" + place.getViewport());
//                buff.append(" getWebsiteUri:" + place.getWebsiteUri());
//                Log.i("pppp location info: ", buff.toString());
//
//                AutoCompleteTextView location = (AutoCompleteTextView) findViewById(R.id.new_event_location);
//                location.setText(place.getName());
//                TextView locationLatLng = (TextView) findViewById(R.id.address_latlng_textView);
//                locationLatLng.setText(place.getLatLng().toString());
//                Log.i("pppp latlng", place.getLatLng().toString());
//                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }

    private double[] getLatitudeLongitude(String location) {
        String subString = location.substring(10, location.length() - 1);
        String[] s = subString.split(",");
        double[] latlng = {0, 0};
        latlng[0] = Double.parseDouble(s[1]);
        latlng[1] = Double.parseDouble(s[0]);
        return latlng;
    }


    private JSONArray getLocationCor(double[] cor) throws JSONException {
        JSONArray ja = new JSONArray();
        for (double d : cor) {
            ja.put(d);
        }
        return ja;
    }
}
