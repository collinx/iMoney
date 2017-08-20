package com.fintechcodex.imoney;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TranAdapter adapter;
    ArrayList<Message> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalDatabase database = new LocalDatabase(getApplicationContext());
        String type = getIntent().getStringExtra("Type");
        switch (type){
            case "Credit": messages = (ArrayList<Message>) database.getCreditList();
                break;
            case "Debit":messages = (ArrayList<Message>) database.getDebitList();
                break;
            case "Due":messages = (ArrayList<Message>) database.getDueList();
                break;
        }

        setContentView(R.layout.activity_main);
        ((TextView)findViewById(R.id.type)).setText(type);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new TranAdapter(getApplicationContext(),messages);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }
}
