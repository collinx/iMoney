package com.fintechcodex.imoney;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.everything.providers.android.telephony.Conversation;
import me.everything.providers.android.telephony.Sms;
import me.everything.providers.android.telephony.TelephonyProvider;

public class Message implements Comparable<Message>{



     int id;



    String mes;

    public Message(int id, String mes) {
        this.id = id;
        this.mes = mes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }





    @Override
    public int compareTo(@NonNull Message o2) {
        if(this.id==o2.id)
            return 0;
        else if(this.id>o2.id)
            return 1;
        else
            return -1;
    }


    public static List<Message> getAllSms(Context context) {
        TelephonyProvider telephonyProvider = new TelephonyProvider(context);
        List<Sms> smses = telephonyProvider.getSms(TelephonyProvider.Filter.ALL).getList();
        List<Conversation> conversations = telephonyProvider.getConversations().getList();
        List<Message> messages = new ArrayList<Message>();







        for(Sms sms: smses ){
            messages.add(new Message(sms.threadId,sms.body));
        }





        for(Conversation conversation: conversations ){
            messages.add(new Message(conversation.threadId,conversation.snippet));
        }

        Collections.sort(messages);
       return messages;


    }
}

