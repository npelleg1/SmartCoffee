package com.example.nickpellegrino.smartcoffee;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SingleOrderActivity extends AppCompatActivity {

    TextView orderID_tv, classroom_tv, coffeeOrder_tv, coffeeSize_tv, creamKind_tv, creams_tv, sugarKind_tv, sugars_tv;
    String userID, orderID, classroom, coffeeOrder, coffeeSize, creamKind, creams, sugarKind, sugars, status;
    String newStatus;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_order);
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
        status = i.getStringExtra("status");

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
        switch (status){
            case "Pending":
                RadioButton pendingRB = (RadioButton)findViewById(R.id.pendingRadioButton);
                pendingRB.setChecked(true);
                break;
            case "Brewing":
                RadioButton brewingRB = (RadioButton)findViewById(R.id.brewingRadioButton);
                brewingRB.setChecked(true);
                break;
            case "In Transit":
                RadioButton intransitRB = (RadioButton)findViewById(R.id.intransitRadioButton);
                intransitRB.setChecked(true);
                break;
            case "Delivered":
                RadioButton deliveredRB = (RadioButton)findViewById(R.id.deliveredRadioButton);
                deliveredRB.setChecked(true);
                break;
        }


        /**********************ORDER BUTTON LOGIC***********************************************************/
        Button changeStatusButton = (Button) findViewById(R.id.changeStatusButton);
        changeStatusButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DatabaseReference myRef = database.getReference("Orders").child(orderID).child("status");
                myRef.setValue(newStatus);
                if (newStatus.equals("In Transit")){
                    InTransitOrder ito = new InTransitOrder(orderID, newStatus, classroom);
                    database.getReference("InTransit").setValue(ito);
                }
                Toast.makeText(getApplicationContext(), "Order status has been updated!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), VendorHomeActivity.class);
                startActivity(intent);
            }
        });
        /***************************************************************************************************/

    }

    /**********************ORDER BUTTON LOGIC***********************************************************/
    public void onStatusClicked(View view) {
        switch (view.getId()) {
            case R.id.pendingRadioButton:
                newStatus = "Pending";
                break;
            case R.id.brewingRadioButton:
                newStatus = "Brewing";
                break;
            case R.id.intransitRadioButton:
                newStatus = "In Transit";
                break;
            case R.id.deliveredRadioButton:
                newStatus = "Delivered";
                break;
        }

    }

    /***************************************************************************************************/

}
