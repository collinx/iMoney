package com.fintechcodex.imoney.chat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String LOG = "DatabaseHelper";

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "messages";

    // Table Names
    private static final String MESSAGES = "message";

    // Common column names
    private static final String TYPE = "type";
    private static final String MESSAGE = "message";


    private static final String CREATE_MESSAGE = "CREATE TABLE "
            + MESSAGES + "(" + TYPE + " INTEGER ," + MESSAGE
            + " TEXT" + ")";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_MESSAGE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_MESSAGE);


        onCreate(db);
    }

    public void addMessage(int uType, String uMessage) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TYPE, uType);
        values.put(MESSAGE, uMessage);

        db.insert(MESSAGES, null, values);
    }




    public List<ChatMessage> getMessage() {
       List<ChatMessage> msg = new ArrayList<ChatMessage>();
        String selectQuery = "SELECT  * FROM " + MESSAGES;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                msg.add(new ChatMessage(c.getInt(c.getColumnIndex(TYPE)),c.getString(c.getColumnIndex(MESSAGE))));
            } while (c.moveToNext());
        }

        return msg;
    }

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    public void deleteDB(Context context){
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
        context.deleteDatabase(DATABASE_NAME);
    }

}
