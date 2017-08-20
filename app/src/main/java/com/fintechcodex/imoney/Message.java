package com.fintechcodex.imoney;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class Message implements Comparable<Message>{



     long id;
    String address;
    String body;
    String reason;

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    float amount;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }



    public Message(long id, String body, String address, String reason) {
        this.id = id;
        this.body = body;
        this.address = address;
        this.reason = reason;
    }
    public Message(long id, String body, String address, String reason, float amount) {
        this.id = id;
        this.body = body;
        this.address = address;
        this.reason = reason;
        this.amount = amount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String Reason) {
        this.reason = reason;
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


   /* public static List<Message> getAllSms(Context context) {
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


    }*/

    public static void getSMSM(Context context){
        Uri uri = Uri.parse("content://sms/inbox");
        ContentResolver cr = context.getContentResolver();
        Cursor c = null;
        SharedPref pref = new SharedPref(context);


        try {
            c = cr.query(uri, new String[]{"_id", "body", "address", "date_sent", "date", "case when date_sent IS NOT 0 then date_sent else date END as dateSent"}, "dateSent >= 0", null, "dateSent ASC");
        } catch (SQLiteException e) {


        }
        List<Message> messagesSent = new ArrayList<Message>();
        List<Message> messagesReceived = new ArrayList<Message>();
        List<Message> messagesDue = new ArrayList<Message>();

        if (c != null) {
            int totalCount = c.getCount();

            if (c.getCount() > 0) {
                Log.v("TAG", "Parsing " + totalCount + " SMSs");

            }
            c.moveToFirst();
            while (!c.isAfterLast()) {
                String body = c.getString(c.getColumnIndexOrThrow("body")).toLowerCase();
                String number = c.getString(c.getColumnIndexOrThrow("address"));

                int smsId =   c.getInt(c.getColumnIndexOrThrow("_id"));

                if(!Pattern.matches("[\\D]*\\d{5,}",number)  && !body.contains("otp") && !body.contains("get") && !body.contains("enjoy")   && !body.contains("sale") && !body.contains("expire") && !body.contains("free") && !body.contains("one time password") && !body.contains("offer") && !body.contains("off")  && !body.contains("flat")  ){


                    if( body.contains("due") && Pattern.matches("\\d{1,}",body)  ){
                        Log.v("TAG", "due " + number+ body + " SMSs");
                        messagesDue.add(new Message(smsId,body,number,null));
                    }
                        else if(body.contains("code")  )  {

                    }
                    else {
                        if((body.contains("rs") || body.contains("inr"))&& Pattern.matches("\\d{1,}",body)){

                            Log.v("TAG", "Rec " + number+ body + " SMSs");
                            messagesReceived.add(new Message(smsId,body,number,null));

                        }else{
                            Log.v("TAG", "sen " + number+ body + " SMSs");
                            messagesSent.add(new Message(smsId,body,number,null)) ;
                        }
                    }


//                    if(body.contains("Received")   ||  body.contains("Receive")   ||body.contains("Credit")  || body.contains("Credited")  ){
//
//
//                    }
//
//                    else{
//
//                    }

                }
                c.moveToNext();
            }

            Collections.sort(messagesSent);
            Collections.sort(messagesReceived);
            Log.v("TAG", "Parsing " + messagesSent.size() +" "+ messagesReceived.size() + " SMSs");
            pref.setSyncStatuc(new Date().getTime());
            c.close();
        }
    }
}

