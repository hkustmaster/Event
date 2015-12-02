package com.hkust.android.event;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MyEventDetailActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_event_detail);
        setTitle("My Event Detail");
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayout viewParticipantBtn = (LinearLayout) findViewById(R.id.view_participant_layout_btn);
        viewParticipantBtn.setOnClickListener(this);

        LinearLayout viewMessageBtn = (LinearLayout)findViewById(R.id.view_message_layout_btn);
        viewMessageBtn.setOnClickListener(this);

        LinearLayout dateLayout = (LinearLayout)findViewById(R.id.date_layout);
        dateLayout.setOnClickListener(this);

        Button closeDiscussionBtn = (Button)findViewById(R.id.close_discussion_btn);
        closeDiscussionBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.view_participant_layout_btn:
                Intent intent = new Intent(getApplicationContext(), DetailParticipantListActivity.class);
                startActivityForResult(intent, 100);
                break;
            case  R.id.view_message_layout_btn:
                Intent intent2 = new Intent(getApplicationContext(), DetailMessageListActivity.class);
                startActivityForResult(intent2, 100);
                break;
            case R.id.date_layout:
                Intent intent3 = new Intent(getApplicationContext(), DateVotingActivity.class);
                startActivityForResult(intent3, 100);
                break;
            case R.id.close_discussion_btn:
                Intent intent4 = new Intent(getApplicationContext(), CloseDiscussionActivity.class);
                startActivityForResult(intent4, 100);
                break;
            default:
        }
    }
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
}
