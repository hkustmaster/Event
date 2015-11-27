package com.hkust.android.event.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hkust.android.event.R;

/**
 * Created by hozdanny on 15/11/13.
 */
public class DateListAdapter extends RecyclerView.Adapter<DateListAdapter.ViewHolder> {
    private String[] dataSet = {"2015-11-1","2015-11-2","2015-11-3"};


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_date, parent,
                false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.dateTextView.setText(dataSet[position]);
    }

    @Override
    public int getItemCount() {
        return dataSet.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView dateTextView;


        public ViewHolder(View itemView) {
            super(itemView);
            dateTextView = (TextView)itemView.findViewById(R.id.date_item_textView);

        }
    }
}
