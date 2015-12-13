package com.hkust.android.event.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.hkust.android.event.R;


/**
 * Created by hozdanny on 15/12/13.
 */
public class ParticipantInfoCustomDialog extends Dialog {
    private String name;
    private String email;
    private String phone;

    public ParticipantInfoCustomDialog(Context context, String name, String email, String phone){
        super(context);
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_user_info_dialog);
        TextView nameTextView = (TextView)findViewById(R.id.dialog_name_textView);
        TextView emailTextView = (TextView)findViewById(R.id.dialog_email_textView);
        TextView phoneTextView = (TextView)findViewById(R.id.dialog_phone_textView);

        nameTextView.setText(name);
        emailTextView.setText(email);
        phoneTextView.setText(phone);
    }
}
