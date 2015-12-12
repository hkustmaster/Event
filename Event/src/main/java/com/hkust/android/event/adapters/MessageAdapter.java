package com.hkust.android.event.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.hkust.android.event.R;
import com.hkust.android.event.model.Message;

import java.util.ArrayList;


/**
 * Created by hozdanny on 15/11/13.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    ArrayList<Message> messages = new ArrayList<Message>();

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
        holder.user.setText(messages.get(position).getFrom().getName());
        holder.message.setText(messages.get(position).getContent());
        holder.date.setText(messages.get(position).getCreateAt());
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
