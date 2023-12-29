package com.example.chatbotopenai;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ChatAdapter extends ArrayAdapter<ApiResponse.Message> {

    private static final int VIEW_TYPE_USER_MESSAGE = 0;
    private static final int VIEW_TYPE_ASSISTANT_MESSAGE = 1;

     ArrayList<ApiResponse.Message> chatList = new ArrayList<>();

    public ChatAdapter(Context context, ArrayList<ApiResponse.Message> messages) {
        super(context, 0, messages);
        this.chatList = messages;
    }

    @Override
    public int getViewTypeCount() {
        return 2; // Number of view types
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? VIEW_TYPE_USER_MESSAGE : VIEW_TYPE_ASSISTANT_MESSAGE;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ApiResponse.Message message = getItem(position);
        Log.d("TAG", "getView: "+ message.getRole() + "msg: " + message.getContent());
        int position2 = -1;
        if(message.getRole().equals("user"))
            position2 = 0;
        else if (message.getRole().equals("assistant"))
            position2 = 1;
        int viewType = getItemViewType(position2);
        int layoutId = -1;

            LayoutInflater inflater = LayoutInflater.from(getContext());
            layoutId = (viewType == VIEW_TYPE_USER_MESSAGE) ? R.layout.item_sender : R.layout.item_receiver;
            convertView = inflater.inflate(layoutId, parent, false);
        TextView contentTextView = null;
        if(layoutId == R.layout.item_receiver)
            contentTextView = convertView.findViewById(R.id.textViewReceiver);
        else if (layoutId == R.layout.item_sender)
            contentTextView = convertView.findViewById(R.id.textViewSender);
        contentTextView.setText(message.getContent());
        return convertView;
    }
}
