package com.gprs.haryana;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

class CustomChatBotAdapter extends ArrayAdapter {
    private Activity context;
    private ArrayList<Message> message;

    public CustomChatBotAdapter(Activity context, ArrayList<Message> message, ArrayList<Integer> toggle, ArrayList<ArrayList<String>> option) {
        super(context, R.layout.activity_chatbot, message);
        this.context = context;
        this.message = message;
    }

    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = null;

        if (message.get(position).getId().equals("2")) {
            rowView = inflater.inflate(R.layout.chat_item_watson, null, true);
            TextView textView = rowView.findViewById(R.id.message);
            textView.setText(message.get(position).getMessage());
        } else {
            rowView = inflater.inflate(R.layout.chat_item_self, null, true);

            TextView textView = rowView.findViewById(R.id.message);
            textView.setText(message.get(position).getMessage());
        }

        return rowView;

    }


}


