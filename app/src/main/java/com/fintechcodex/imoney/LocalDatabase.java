package com.fintechcodex.imoney;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


public class LocalDatabase extends SQLiteOpenHelper {

    private static final String LOG = "LocalDatabase";

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "iMoney";

    // Table Names
    private static final String CREDIT = "credit";
    private static final String DEBIT = "debit";
    private static final String DUE = "due";

    //  Column names
    private static final String TIMESTAMP = "time";
    private static final String PAYEE = "paymentto";
    private static final String AMOUNT = "amount";
    private static final String REASON = "reason";

    // Table Create Statements

    private static final String CREATE_CREDIT = "CREATE TABLE "
            + CREDIT + "(" + TIMESTAMP + " INTEGER," + PAYEE
            + " TEXT," + AMOUNT + " REAL," + REASON+" TEXT )";

    private static final String CREATE_DEBIT = "CREATE TABLE "
            + DEBIT + "(" + TIMESTAMP + " INTEGER," + PAYEE
            + " TEXT," + AMOUNT + " REAL," + REASON+" TEXT )";

    private static final String CREATE_DUE = "CREATE TABLE "
            + DUE + "(" + TIMESTAMP + " INTEGER," + PAYEE
            + " TEXT," + AMOUNT + " REAL," + REASON+" TEXT )";


    public LocalDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_CREDIT);
        db.execSQL(CREATE_DEBIT);
        db.execSQL(CREATE_DUE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_CREDIT);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_DEBIT);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_DUE);
        onCreate(db);
    }

    public void saveCredit(Message message){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TIMESTAMP, message.id);
        values.put(PAYEE, message.address);
        values.put(AMOUNT, message.amount);
        values.put(REASON,message.reason);

        db.insert(CREDIT, null, values);
    }

    public void saveDebit(Message message){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TIMESTAMP, message.id);
        values.put(PAYEE, message.address);
        values.put(AMOUNT, message.amount);
        values.put(REASON,message.reason);

        db.insert(DEBIT, null, values);
    }

    public void saveDue(Message message){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TIMESTAMP, message.id);
        values.put(PAYEE, message.address);
        values.put(AMOUNT, message.amount);
        values.put(REASON,message.reason);

        db.insert(DUE, null, values);
    }

    public void deleteMessage(Message message){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TIMESTAMP, message.id);

        db.delete(CREDIT, TIMESTAMP + " = ?", new String[] { ""+message.id });
        db.delete(DEBIT, TIMESTAMP + " = ?", new String[] { ""+message.id });
        db.delete(DUE, TIMESTAMP + " = ?", new String[] { ""+message.id });
    }

     public float getCreditTotal() {

        String selectQuery = "SELECT  * FROM " + CREDIT;



        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        float total=0;
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                total+=c.getFloat(c.getColumnIndex(AMOUNT));

            } while (c.moveToNext());
        }

        return total;
    }
    public List<Message> getCreditList() {
        List<Message> messages = new ArrayList<Message>();
        String selectQuery = "SELECT  * FROM " + CREDIT;



        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Message message = new Message(c.getLong(c.getColumnIndex(TIMESTAMP)), null, c.getString(c.getColumnIndex(PAYEE)), c.getString(c.getColumnIndex(REASON)),c.getFloat(c.getColumnIndex(AMOUNT)));

                messages.add(message);
            } while (c.moveToNext());
        }

        return messages;
    }
    public float getDebitTotal() {

        String selectQuery = "SELECT  * FROM " + DEBIT;



        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        float total=0;
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                total+=c.getFloat(c.getColumnIndex(AMOUNT));

            } while (c.moveToNext());
        }

        return total;
    }
    public List<Message> getDebitList() {
        List<Message> messages = new ArrayList<Message>();
        String selectQuery = "SELECT  * FROM " + DEBIT;



        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Message message = new Message(c.getLong(c.getColumnIndex(TIMESTAMP)), null, c.getString(c.getColumnIndex(PAYEE)), c.getString(c.getColumnIndex(REASON)),c.getFloat(c.getColumnIndex(AMOUNT)));

                messages.add(message);
            } while (c.moveToNext());
        }

        return messages;
    }
    public float getDueTotal() {

        String selectQuery = "SELECT  * FROM " + DUE;



        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        float total=0;
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                total+=c.getFloat(c.getColumnIndex(AMOUNT));

            } while (c.moveToNext());
        }

        return total;
    }
    public List<Message> getDueList() {
        List<Message> messages = new ArrayList<Message>();
        String selectQuery = "SELECT  * FROM " + DUE;



        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Message message = new Message(c.getLong(c.getColumnIndex(TIMESTAMP)), null, c.getString(c.getColumnIndex(PAYEE)), c.getString(c.getColumnIndex(REASON)),c.getFloat(c.getColumnIndex(AMOUNT)));

                messages.add(message);
            } while (c.moveToNext());
        }

        return messages;
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