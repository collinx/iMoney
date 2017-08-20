package com.fintechcodex.imoney;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.fintechcodex.imoney.chat.chatC;
import com.google.firebase.auth.FirebaseAuth;
import com.hookedonplay.decoviewlib.DecoView;

public class HomeActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DecoView mDecoView;

    private float total,total1,total2,total3;


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


        if(new SharedPref(getApplicationContext()).getFirst()){
            Message.getSMSM(getApplicationContext());
            new SharedPref(getApplicationContext()).setFirst(false);
        }



        total1 = database.getCreditTotal();
        total2 = database.getDebitTotal();
        total3 = database.getDueTotal();
        ((TextView)findViewById(R.id.credit)).setText("₹ "+total1);
        ((TextView)findViewById(R.id.debit)).setText("₹ "+total2);
        ((TextView)findViewById(R.id.due)).setText("₹ "+total3);
        total = total1+total2+total3;

        ((TextView)findViewById(R.id.credit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,MainActivity.class);
                intent.putExtra("Type","Credit");
                startActivity(intent);
            }
        });
        ((TextView)findViewById(R.id.debit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,MainActivity.class);
                intent.putExtra("Type","Debit");
                startActivity(intent);
            }
        });
        ((TextView)findViewById(R.id.due)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,MainActivity.class);
                intent.putExtra("Type","Due");
                startActivity(intent);
            }
        });

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
        }else if(id == R.id.action_sync){
           Message.getSMSM(getApplicationContext());
           total1 = database.getCreditTotal();
           total2 = database.getDebitTotal();
           total3 = database.getDueTotal();
           ((TextView)findViewById(R.id.credit)).setText("₹ "+total1);
           ((TextView)findViewById(R.id.debit)).setText("₹ "+total2);
           ((TextView)findViewById(R.id.due)).setText("₹ "+total3);
           total = total1+total2+total3;
       }

        return super.onOptionsItemSelected(item);
    }



}
