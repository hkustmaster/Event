package com.hkust.android.event.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.hkust.android.event.R;
import com.hkust.android.event.model.VoteRecord;

import java.util.ArrayList;

/**
 * Created by hozdanny on 15/11/13.
 */
public class DateListAdapter extends RecyclerView.Adapter<DateListAdapter.ViewHolder> {
    private ArrayList<VoteRecord> voteRecords = new ArrayList<VoteRecord>();
    private boolean canVote = false;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_date, parent,
                false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.dateTextView.setText(voteRecords.get(position).getDate());

        StringBuffer buff = new StringBuffer();
        buff.append(voteRecords.get(position).getVoteCount());
        holder.voteCount.setText(buff.toString());

        //if is from host, he/she can not vote. canVote is false
        //if is from participant, he/ she can vote. can Vote is true
        if(canVote){
            if(voteRecords.get(position).isVoted()){
                holder.checkBox.setChecked(true);
            }else{
                holder.checkBox.setChecked(false);
            }
        }else{
            holder.checkBox.setVisibility(View.INVISIBLE);
            holder.checkBox.setClickable(false);
        }

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox)v;
                voteRecords.get(position).setIsVoted(checkBox.isChecked());
            }
        });

    }

    @Override
    public int getItemCount() {
        return voteRecords.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView dateTextView;
        private TextView voteCount;
        private CheckBox checkBox;


        public ViewHolder(View itemView) {
            super(itemView);
            dateTextView = (TextView)itemView.findViewById(R.id.date_item_textView);
            voteCount = (TextView)itemView.findViewById(R.id.num_of_vote_textView);
            checkBox = (CheckBox)itemView.findViewById(R.id.vote_checkbox);

        }


    }

    public void setVoteRecords(ArrayList<VoteRecord> voteRecords){
        this.voteRecords=voteRecords;
    }

    public void setCanVote(boolean canVote){
        this.canVote = canVote;
    }

    public ArrayList<VoteRecord> getVoteRecords(){
        return voteRecords;
    }
}
