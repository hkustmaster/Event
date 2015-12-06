package com.hkust.android.event.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.hkust.android.event.R;
import com.hkust.android.event.model.Participant;

import java.util.ArrayList;


/**
 * Created by hozdanny on 15/11/13.
 */
public class ParticipantAdapter extends RecyclerView.Adapter<ParticipantAdapter.ViewHolder> {

    private ArrayList<Participant> participants = new ArrayList<Participant>();

    public ParticipantAdapter(ArrayList<Participant> participants){
        this.participants = participants;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_participant, parent,
                false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.participant.setText(participants.get(position).getId().getName());

    }

    public void setParticipantsList(ArrayList<Participant> participants) {
        this.participants = participants;
    }

    @Override
    public int getItemCount() {
        return participants.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView participant;

        public ViewHolder(View itemView) {
            super(itemView);
            participant = (TextView)itemView.findViewById(R.id.participant_name_textView);
        }
    }
}
