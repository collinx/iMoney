package com.fintechcodex.imoney;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "iMoneyPref";

    // All Shared Preferences Keys


 //   private static final String Key_SESSION = "session";
//    private static final String KEY_INSTALLED = "install";
    // User name (make variable public to access from outside)
 //   public static final String KEY_NAME = "name";

    // Email address (make variable public to access from outside)
  //  public static final String KEY_ID = "id";
    private static final String USER_UID = "userUid";
    private static final String DATA_SYNC = "sync";
    private static final String FIRST_TIME ="first";
    // Constructor
    public SharedPref(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }



    public String getUserUid(){
        return pref.getString(USER_UID,null);
    }
    public Long getSyncStatuc(){
        return pref.getLong(DATA_SYNC,1);
    }

    public void setUserUid(String userUid){
        editor.putString(USER_UID,userUid);
        editor.commit();
    }
    public void setSyncStatuc(long time){
        editor.putLong(DATA_SYNC,time);
        editor.commit();
    }
    public boolean getFirst(){
        return pref.getBoolean(FIRST_TIME,true);
    }


    public void setFirst(boolean first){
        editor.putBoolean(FIRST_TIME,first);
        editor.commit();
    }
    public void logOut(){
        editor.clear();
        editor.commit();
    }

}
