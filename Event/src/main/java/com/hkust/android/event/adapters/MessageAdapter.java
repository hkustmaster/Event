package com.hkust.android.event.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.hkust.android.event.R;
import com.hkust.android.event.model.Message;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;


/**
 * Created by hozdanny on 15/11/13.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    ArrayList<Message> messages = new ArrayList<Message>();
    DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:MM:SS");

    public MessageAdapter(ArrayList<Message> messages){
        this.messages = messages;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_message, parent,
                false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.user.setText(messages.get(position).getFrom().getId().getName());
        holder.message.setText(messages.get(position).getContent());
        DateTime dateTime = DateTime.parse(messages.get(position).getCreateAt());
        holder.date.setText(dateTime.toString(format));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView user;
        private TextView message;
        private TextView date;

        public ViewHolder(View itemView) {
            super(itemView);
            user= (TextView)itemView.findViewById(R.id.message_user_textView);
            message = (TextView)itemView.findViewById(R.id.message_content_textView);
            date= (TextView)itemView.findViewById(R.id.message_date_textView);
        }
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }
}
