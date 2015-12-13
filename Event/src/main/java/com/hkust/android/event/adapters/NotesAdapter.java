package com.hkust.android.event.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.hkust.android.event.R;
import com.hkust.android.event.model.Event;
import java.util.ArrayList;

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
        String endDate = eventModel.getEndAt();
        String quota = String.valueOf(eventModel.getQuota());
        String size = String.valueOf(eventModel.getSize());
        //String location = "location";
        // Set text
        holder.titleTextView.setText(title);
        holder.eventLocationTextView.setText(location);
        if(endDate.equalsIgnoreCase("")) {
            holder.eventStartDateTextView.setText(startDate);
        }else{
            holder.eventStartDateTextView.setText(startDate +" - "+endDate);
        }
        holder.hostTextView.setText("Host: " + holderName);
        holder.quotaTextView.setText("Quota: "+quota+"/"+size);
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
        private TextView quotaTextView;
        private TextView eventStartDateTextView;
        private TextView eventLocationTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.event_title);
            hostTextView = (TextView) itemView.findViewById(R.id.event_host);
            quotaTextView = (TextView) itemView.findViewById(R.id.quota);
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

}
