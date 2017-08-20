package com.fintechcodex.imoney.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fintechcodex.imoney.R;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter   extends ArrayAdapter<ChatMessage> {
    private TextView chatText;
    private List<ChatMessage> chatMessageList = new ArrayList<ChatMessage>();
    private Context context;

    @Override
    public void add(ChatMessage object) {
        chatMessageList.add(object);
        super.add(object);
    }

    public ChatAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.context = context;
    }

    public int getCount() {
        return this.chatMessageList.size();
    }

    public ChatMessage getItem(int index) {
        return this.chatMessageList.get(index);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final ChatMessage chatMessageObj = getItem(position);
        View row = convertView;
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        switch(chatMessageObj.left){
            case 0:
                row = inflater.inflate(R.layout.center, parent, false);
                break;
            case 1:
                row = inflater.inflate(R.layout.left, parent, false);

                break;
            case 2:
                row = inflater.inflate(R.layout.right, parent, false);

                break;
        }
        chatText = (TextView) row.findViewById(R.id.msgr);
        chatText.setText(chatMessageObj.message);
        chatText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(chatMessageObj.left){
                    case 0:
                        ((chatC)context).chatText.setText(chatMessageObj.message);
                        ((chatC)context).sendChatMessage();
                        break;
                    case 1:
                        TTS.speak(chatMessageObj.message);

                        break;
                    case 2:
                        TTS.speak(chatMessageObj.message);
                        break;
                }

            }
        });
        return row;
    }
}
