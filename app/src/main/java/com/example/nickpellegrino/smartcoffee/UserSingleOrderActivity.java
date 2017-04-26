package com.example.nickpellegrino.smartcoffee;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

public class UserSingleOrderActivity extends AppCompatActivity {
    TextView orderID_tv, classroom_tv, coffeeOrder_tv, coffeeSize_tv, creamKind_tv, creams_tv, sugarKind_tv, sugars_tv;
    String userID, orderID, classroom, coffeeOrder, coffeeSize, creamKind, creams, sugarKind, sugars;
    String newStatus;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_single_order);
        database = FirebaseDatabase.getInstance();

        Intent i = getIntent();
        userID = i.getStringExtra("userID");
        orderID = i.getStringExtra("orderID");
        classroom = i.getStringExtra("classroom");
        coffeeOrder = i.getStringExtra("coffeeOrder");
        coffeeSize = i.getStringExtra("coffeeSize");
        creamKind = i.getStringExtra("creamKind");
        creams = i.getStringExtra("creams");
        sugarKind = i.getStringExtra("sugarKind");
        sugars = i.getStringExtra("sugars");

        orderID_tv = (TextView) findViewById(R.id.orderID);
        classroom_tv = (TextView) findViewById(R.id.classroom);
        coffeeOrder_tv = (TextView) findViewById(R.id.coffeeType);
        coffeeSize_tv = (TextView)findViewById(R.id.coffeeSize);
        creamKind_tv = (TextView) findViewById(R.id.creamType);
        creams_tv = (TextView) findViewById(R.id.creams);
        sugarKind_tv = (TextView) findViewById(R.id.sugarType);
        sugars_tv = (TextView) findViewById(R.id.sugars);

        orderID_tv.setText(orderID);
        classroom_tv.setText(classroom);
        coffeeOrder_tv.setText(coffeeOrder);
        coffeeSize_tv.setText(coffeeSize);
        creamKind_tv.setText(creamKind);
        creams_tv.setText(creams);
        sugarKind_tv.setText(sugarKind);
        sugars_tv.setText(sugars);

    }

}
