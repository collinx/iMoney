package com.fintechcodex.imoney;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends Activity   {
    final Context context = this;
    private ToggleButton Button1;

    private SpeechRecog speech;
    private String LOG_TAG = "VoiceRecognActivity";
    public String text="";




    /*****For Dots Animation*****/
    private TextView hangoutTvOne;
    private TextView hangoutTvTwo;
    private TextView hangoutTvThree;
    private TextView hangoutTvFour;
    AudioManager audioManager;
    TextView part_results;

    ScrollView scroll;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mItemsDatabaseReference;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        part_results = (TextView) findViewById(R.id.partResults);
        scroll = (ScrollView) findViewById(R.id.scrollViewBill);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mItemsDatabaseReference = mFirebaseDatabase.getReference().child("data");

        hangoutTvOne = (TextView) findViewById(R.id.hangoutTvOne);
        hangoutTvTwo = (TextView) findViewById(R.id.hangoutTvTwo);
        hangoutTvThree = (TextView) findViewById(R.id.hangoutTvThree);
        hangoutTvFour = (TextView) findViewById(R.id.hangoutTvFour);
        Button1 = (ToggleButton) findViewById(R.id.mic_button);

        Button1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    Log.v("TAGe",""+isChecked);
                    startSpeech();
                    // The toggle is enabled
                }  else {
                    Log.v("TAGc",""+isChecked);
                    stopSpeech();
                }
            }
        });

    }

    public void startSpeech(){
        speech = new SpeechRecog(getApplicationContext(), part_results, Button1, audioManager,hangoutTvOne, hangoutTvTwo, hangoutTvThree, hangoutTvFour, scroll );
        speech.getSpeechRe().startListening(speech.getSpeechIn());
     /*   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            Button1.setBackground(getResources().getDrawable(R.drawable.circle_red));*/
    }

    public void stopSpeech(){
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_UNMUTE,0);
       /* audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,speech.initial_volume , 0);
        audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, speech.initial_volume_notification , 0);*/
        speech.getSpeechRe().stopListening();
        hangoutTvOne.setScaleY(0.1f);
        hangoutTvTwo.setScaleY(0.1f);
        hangoutTvThree.setScaleY(0.1f);
        hangoutTvFour.setScaleY(0.1f);
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            Button1.setBackground(getResources().getDrawable(R.drawable.circle));*/
    }

    @Override
    public void onBackPressed() {
        Button1.setChecked(false);
        super.onBackPressed();
        // android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    protected void onStop() {
      Button1.setChecked(false);
        super.onStop();
    }
}