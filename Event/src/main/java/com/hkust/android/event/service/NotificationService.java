package com.hkust.android.event.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hkust.android.event.R;
import com.hkust.android.event.model.Event;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationService extends Service {
    private final static String TAG = "NotificationService";
    private Timer mTimer;
    private Gson gson = new Gson();
    private ArrayList<Event> eventList = new ArrayList<Event>();
    private ArrayList<String> notifiedEvent = new ArrayList<String>();
    DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH: mm");

    public NotificationService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mTimer = new Timer();
        setTimerTask();
        Log.i(TAG, "Notification service started.");
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Service: onStartCommand()");
        handleIntent(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    //handle the intent with action
    private void handleIntent(Intent intent) {
        String eventListString = new String();
        //get event id from the my event activity
        Bundle extras = intent.getExtras();
        if (extras == null) {
            eventListString = null;
        } else {
            eventListString = extras.getString("eventList");
        }

        eventList = gson.fromJson(eventListString, new TypeToken<ArrayList<Event>>() {
        }.getType());


    }


    private void setTimerTask() {
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                //do some action
                checkNotification();
                Log.i(TAG, "NOTIFICATION.......");
            }
        }, 1000, 5000/* 表示1000毫秒之後，每隔1000毫秒執行一次 */);
    }

    public void showNotification(String Title, String contentTitle, String text) {
        Intent intent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Notification notification = new Notification.Builder(this)
                .setTicker(Title)
                .setContentTitle(contentTitle)
                .setStyle(new Notification.BigTextStyle()
                        .bigText(text))
                .setSmallIcon(R.drawable.app_icon)
                .setContentIntent(pendingIntent).getNotification();
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }

    public void checkNotification() {
        Log.i(TAG, "checkNotification");
        if (eventList != null) {
            for (Event event : eventList) {
                //Log.i(TAG,event.getTitle()+" "+event.getStartAt() +" "+ event.getTime());
                if (!checkIfNotified(event.getId())) {
                    //notify the user, when the event before
                    // Log.i(TAG,event.getTitle()+"checked. "+event.getStartAt() +" "+ event.getTime());
                    DateTime eventStartTime = DateTime.parse(event.getStartAt() + " " + event.getTime(), format);
                    Log.i(TAG, eventStartTime.minusHours(3).toString(format) + " Now " + DateTime.now());
                    if (eventStartTime.getDayOfMonth() == DateTime.now().getDayOfMonth()) {
                        if (eventStartTime.minusHours(3).isBefore(DateTime.now())) {
                            Log.i(TAG, "notify: " + event.getTitle() + "start time -3: " + eventStartTime.minusHours(3).toString(format) + "is before Now: " + DateTime.now());
                            showNotification("Together", "Activity Notification", "You have an activity " + event.getTitle() + " will start at " + event.getStartAt() + " " + event.getTime() + ".");
                            notifiedEvent.add(event.getId());
                        }
                    }
                }

            }
        }
    }

    public boolean checkIfNotified(String eventId) {
        if (notifiedEvent.contains(eventId)) {
            return true;
        } else {
            return false;
        }
    }

    public void deleteNotify() {
        if (eventList != null) {
            for (Event event : eventList) {
                if (!checkIfNotified(event.getId())) {
                    //notify the user, when the event before
                    DateTime eventStartTime = DateTime.parse(event.getStartAt() + "" + event.getTime(), format);
                    if (eventStartTime.minusHours(3).isAfter(DateTime.now())) {
                        showNotification("Together", "Activity Notification", event.getTitle() + " will start at " + event.getStartAt() + ".");
                        notifiedEvent.add(event.getId());
                    }
                }

            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "on onDestroy()");
    }
    //    public void compareEventList() {
//        for (Event event : newEventList) {
//            for (Event oldEvent : oldEventList) {
//                if (oldEvent.getId().equalsIgnoreCase(event.getId())) {
//                    if (oldEvent.getStatus().equalsIgnoreCase(event.getStatus())) {
//
//                    } else {
//                        showNotification("Together", "Event Status Changed", event.getTitle() + " start time is finalize at " + event.getStartAt());
//                    }
//                }
//            }
//        }
//        oldEventList = (ArrayList<Event>)newEventList.clone();
//    }
}
