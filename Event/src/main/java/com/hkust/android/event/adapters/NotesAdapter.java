package com.hkust.android.event.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hkust.android.event.R;
import com.hkust.android.event.model.Constants;
import com.hkust.android.event.model.Event;
import com.hkust.android.event.model.Note;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpRequest;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.entity.mime.Header;


public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {
    private ArrayList<Event> events = new ArrayList<Event>();
    private ClickListener clickListener;
    private Context context;


    public NotesAdapter(Context context, ArrayList<Event> events) {
        this.context = context;
        this.events = events;
    }

    @Override
    public NotesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_note, parent,
                false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Event eventModel = events.get(position);
        String title = eventModel.getTitle();
        String holderName = eventModel.getHost().getName();
        String startDate = eventModel.getStartAt();
        String location = eventModel.getAddress();
        //String location = "location";
        // Set text
        holder.titleTextView.setText(title);
        holder.eventLocationTextView.setText(location);
        holder.eventStartDateTextView.setText(startDate);
        holder.hostTextView.setText(holderName);

        int paddingTop = (holder.titleTextView.getVisibility() != View.VISIBLE) ? 0
                : holder.itemView.getContext().getResources()
                .getDimensionPixelSize(R.dimen.note_content_spacing);
        holder.titleTextView.setPadding(holder.titleTextView.getPaddingLeft(), paddingTop,
                holder.titleTextView.getPaddingRight(), holder.titleTextView.getPaddingBottom());

        // Set background color
        int[] colorsNeutral = context.getResources().getIntArray(R.array.note_neutral_colors);

        ((CardView) holder.itemView).setCardBackgroundColor(colorsNeutral[2]);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView titleTextView;
        private TextView hostTextView;
        private TextView eventStartDateTextView;
        private TextView eventLocationTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.event_title);
            hostTextView = (TextView) itemView.findViewById(R.id.event_host);
            eventStartDateTextView = (TextView) itemView.findViewById(R.id.event_start_date);
            eventLocationTextView = (TextView) itemView.findViewById(R.id.event_location);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // context.startActivity(new Intent(context, PendingEventDetailActivity.class));
            if (clickListener != null) {
                clickListener.itemClicked(v, getAdapterPosition());
            }
        }
    }

    public interface ClickListener {
        public void itemClicked(View view, int position);

    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setEventsList(ArrayList<Event> events) {
        this.events = events;
    }
//    private void getExploreEvents(final Context context) {
//
//        // send request to server
//        AsyncHttpClient client = new AsyncHttpClient();
//        sp = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
//        String token = sp.getString("token", "");
//        Log.i("PPPP", token);
//        RequestParams params = new RequestParams();
//        params.put("token", token);
//
//        client.post(Constants.SERVER_URL + Constants.GET_ALL_EVENT, params, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
//                String response = new String(responseBody);
//
//                JSONObject jsonObject = null;
//                try {
//                    jsonObject = new JSONObject(response);
//                    String message = jsonObject.getString("message");
//
//                    if (message.equalsIgnoreCase("succeed")) {
//                        String eventString = jsonObject.getString("act");
//                        Gson gson = new Gson();
//                        Log.i("ppppp", eventString);
//                        ArrayList<Event> arrayEventList = gson.fromJson(eventString, new TypeToken<ArrayList<Event>>() {
//                        }.getType());
//                        Log.i("ppppp", arrayEventList.get(0).getTitle());
//                        events = arrayEventList;
//                    } else {
//                        Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_LONG).show();
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
//
//            }
//        });
//    }

//    private Note[] getPendingEvents(Context context, int numNotes) {
//        Note[] notes = new Note[numNotes];
//        for (int i = 0; i < notes.length; i++) {
//            notes[i] = Note.randomOwnNote(context);
//        }
//        return notes;
//    }
//
//    private Note[] getMyEventEvents(Context context, int numNotes) {
//        Note[] notes = new Note[numNotes];
//        for (int i = 0; i < notes.length; i++) {
//            notes[i] = Note.randomOwnNote(context);
//        }
//        return notes;
//    }
}
