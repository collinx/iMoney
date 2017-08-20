package com.fintechcodex.imoney;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.fintechcodex.imoney.chat.chatC;
import com.google.firebase.auth.FirebaseAuth;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.DecoDrawEffect;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;

public class HomeActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DecoView mDecoView;

    private float total,total1,total2,total3;

    /**
     * Data series index used for controlling animation of {@link DecoView}. These are set when
     * the data series is created then used in {@link #createEvents} to specify what series to
     * apply a given event to
     */
    private int mBackIndex;
    private int mSeries1Index;
    private int mSeries2Index;
    private int mSeries3Index;
    LocalDatabase database;
    /**
     * Maximum value for each data series in the {@link DecoView}. This can be different for each
     * data series, in this example we are applying the same all data series
     */
    private float mSeriesMax  ;

    @Override
    protected void onResume() {
        total1 = database.getCreditTotal();
        total2 = database.getDebitTotal();
        total3 = database.getDueTotal();
        ((TextView)findViewById(R.id.credit)).setText("₹ "+total1);
        ((TextView)findViewById(R.id.debit)).setText("₹ "+total2);
        ((TextView)findViewById(R.id.due)).setText("₹ "+total3);
        total = total1+total2+total3;
        mSeriesMax = total;
        createBackSeries();
        createDataSeries1();
        createDataSeries2();
        createDataSeries3();

        // Setup events to be fired on a schedule
        createEvents();
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();

        database =new LocalDatabase(getApplicationContext());
        findViewById(R.id.fab_new).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, chatC.class));
            }
        });



        Message.getSMSM(getApplicationContext());
        mDecoView  = (DecoView) findViewById(R.id.dynamicArcView );

        total1 = database.getCreditTotal();
        total2 = database.getDebitTotal();
        total3 = database.getDueTotal();
        ((TextView)findViewById(R.id.credit)).setText("₹ "+total1);
        ((TextView)findViewById(R.id.debit)).setText("₹ "+total2);
        ((TextView)findViewById(R.id.due)).setText("₹ "+total3);
        total = total1+total2+total3;
        mSeriesMax = total;
        createBackSeries();
        createDataSeries1();
        createDataSeries2();
        createDataSeries3();

        // Setup events to be fired on a schedule
        createEvents();

    }
    private void createBackSeries() {
        SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#FFE2E2E2"))
                .setRange(0, mSeriesMax, 0)
                .setInitialVisibility(true)
                .build();

        mBackIndex = mDecoView.addSeries(seriesItem);

    }

    private void createDataSeries1() {
        final SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#FFFF8800"))
                .setRange(0, mSeriesMax, 0)
                .setInitialVisibility(false)
                .build();


        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {

//
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });


//
        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
//

            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });

//
        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {

            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });

        mSeries1Index = mDecoView.addSeries(seriesItem);
    }

    private void createDataSeries2() {
        final SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#FFFF4444"))
                .setRange(0, mSeriesMax, 0)
                .setInitialVisibility(false)
                .build();

//

        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
//
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });

        mSeries2Index = mDecoView.addSeries(seriesItem);
    }

    private void createDataSeries3() {
        final SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#FF6699FF"))
                .setRange(0, mSeriesMax, 0)
                .setInitialVisibility(false)
                .build();

//

        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
//
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });

        mSeries3Index = mDecoView.addSeries(seriesItem);
    }

    private void createEvents() {
        mDecoView.executeReset();

        mDecoView.addEvent(new DecoEvent.Builder(mSeriesMax)
                .setIndex(mBackIndex)
                .setDuration(3000)
                .setDelay(100)
                .build());

        mDecoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_OUT)
                .setIndex(mSeries1Index)
                .setDuration(2000)
                .setDelay(1250)
                .build());

        mDecoView.addEvent(new DecoEvent.Builder(total1)
                .setIndex(mSeries1Index)
                .setDelay(3250)
                .build());

        mDecoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_OUT)
                .setIndex(mSeries2Index)
                .setDuration(1000)
                .setEffectRotations(1)
                .setDelay(7000)
                .build());

        mDecoView.addEvent(new DecoEvent.Builder(total2)
                .setIndex(mSeries2Index)
                .setDelay(8500)
                .build());

        mDecoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_OUT)
                .setIndex(mSeries3Index)
                .setDuration(1000)
                .setEffectRotations(1)
                .setDelay(12500)
                .build());

        mDecoView.addEvent(new DecoEvent.Builder(total3).setIndex(mSeries3Index).setDelay(14000).build());



    }

    private void resetText() {
//        ((TextView) findViewById(R.id.textActivity1)).setText("");
//        ((TextView) findViewById(R.id.textActivity2)).setText("");
//        ((TextView) findViewById(R.id.textActivity3)).setText("");
//        ((TextView) findViewById(R.id.textPercentage)).setText("");
//        ((TextView) findViewById(R.id.textRemaining)).setText("");
//    }
}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       if(id == R.id.action_sign_out){
           mAuth.signOut();
           startActivity(new Intent(HomeActivity.this,LoginActivity.class));
           finish();
        }

        return super.onOptionsItemSelected(item);
    }



}
