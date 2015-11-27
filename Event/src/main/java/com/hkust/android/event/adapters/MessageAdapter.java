package com.hkust.android.event.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.hkust.android.event.R;


/**
 * Created by hozdanny on 15/11/13.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private String[] messageDate = {"I am available on Nov 3.",
            "Debating me breeding be answered an he. Spoil event was words her off cause any. Tears woman which no is world miles woody. Wished be do mutual except in effect answer. Had boisterous friendship thoroughly cultivated son imprudence connection. Windows because concern sex its. Law allow saved views hills day ten. ",
            "Debating me breeding be answered an he. Spoil event was words her off cause any. Tears woman which no is world miles woody. Wished be do mutual except in effect answer. Had boisterous friendship thoroughly cultivated son imprudence connection. Windows because concern sex its. Law allow saved views hills day ten. "};

    private String[] userData = {"Bob", "Alice","Emma"};

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_message, parent,
                false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.user.setText(userData[position]);
        holder.message.setText(messageDate[position]);
        Log.i("this====","show message  +++++++++++++++++++");
    }

    @Override
    public int getItemCount() {
        return messageDate.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView user;
        private TextView message;


        public ViewHolder(View itemView) {
            super(itemView);
            user= (TextView)itemView.findViewById(R.id.message_user_textView);
            message = (TextView)itemView.findViewById(R.id.message_content_textView);
        }
    }
}
