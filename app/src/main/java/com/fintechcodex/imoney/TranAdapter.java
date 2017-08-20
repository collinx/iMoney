package com.fintechcodex.imoney;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

public class TranAdapter extends RecyclerView.Adapter<TranAdapter.MyViewHolder> {
    Message message;
    public ArrayList<Message> messages=new ArrayList<>();

    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView smsName,smsBody,smsDate;
        public Button smsButton;
        public MyViewHolder(View row) {
            super(row);

            smsName = (TextView) row.findViewById(R.id.smsName);
            smsDate = (TextView) row.findViewById(R.id.smsDate);
            smsBody = (TextView) row.findViewById(R.id.smsBody);
            smsButton = (Button) row.findViewById(R.id.smsButton);

        }
    }
    public TranAdapter(Context context,ArrayList<Message> messages) {
        this.context=context;
        this.messages = messages;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        message = messages.get(position);

        holder.smsName.setText(message.address.substring(3,message.address.length()));
        holder.smsButton.setText(message.address.substring(0,2));
        holder.smsBody.setText(message.body);

        holder. smsDate.setText(""+ new Date(message.getId()));

    }

    private class ViewHolder {
          TextView smsName,smsBody,smsDate;
          Button smsButton;
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}
