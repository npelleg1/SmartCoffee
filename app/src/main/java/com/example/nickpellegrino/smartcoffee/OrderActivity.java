package com.example.nickpellegrino.smartcoffee;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class OrderActivity extends AppCompatActivity {

    String coffeeOrder = null;
    String coffeeSize = null;
    String classroom;
    String userID;
    int sugars = 0;
    int creams = 0;
    FirebaseDatabase database;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        database = FirebaseDatabase.getInstance();

        Intent intent = getIntent();
        userID = intent.getStringExtra("UserID");

/*******************SUGAR COUNT LOGIC***************************************************************/
        final TextView sugarCountTextView = (TextView) findViewById(R.id.sugarCountTextView);
        Button minusSugarButton = (Button) findViewById(R.id.minusSugarButton);
        minusSugarButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (sugars > 0) {
                    sugars--;
                    sugarCountTextView.setText(Integer.toString(sugars));
                }
            }
        });
        Button addSugarButton = (Button) findViewById(R.id.plusSugarButton);
        addSugarButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                sugars++;
                sugarCountTextView.setText(Integer.toString(sugars));
            }
        });
/***************************************************************************************************/

/*******************CREAM COUNT LOGIC***************************************************************/
        final TextView creamCountTextView = (TextView) findViewById(R.id.creamCountTextView);
        Button minusCreamButton = (Button) findViewById(R.id.minusCreamButton);
        minusCreamButton.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v){
                if (creams > 0){
                    creams--;
                    creamCountTextView.setText(Integer.toString(creams));
                }
            }
        });
        Button addCreamButton = (Button) findViewById(R.id.plusCreamButton);
        addCreamButton.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v){
                creams++;
                creamCountTextView.setText(Integer.toString(creams));
            }
        });
/***************************************************************************************************/

/**********************SET UP NUMBER PICKER FOR ROOM CHOICE*****************************************/
        final NumberPicker roomPicker = (NumberPicker) findViewById(R.id.roomNumberPicker);
        final String[] values= {"101","102", "108",
                "113", "115", "116", "117", "118", "119",
                "120", "125", "126", "128", "129", "131",
                "136", "138", "140", "141", "143", "149",
                "155"};
        roomPicker.setMinValue(0);
        roomPicker.setMaxValue(values.length-1);
        roomPicker.setDisplayedValues(values);
/***************************************************************************************************/

/**********************ORDER BUTTON LOGIC***********************************************************/
        Button orderButton = (Button) findViewById(R.id.submitOrderButton);
        orderButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                if (coffeeOrder == null){
                    Toast.makeText(getApplicationContext(), "Please Select a Coffee Flavor", Toast.LENGTH_SHORT).show();
                }
                else if (coffeeSize == null){
                    Toast.makeText(getApplicationContext(), "Please Select a Drink Size", Toast.LENGTH_SHORT).show();
                }
                else{
                    classroom = roomPicker.getDisplayedValues()[roomPicker.getValue()];
                    DatabaseReference myRef = database.getReference("Orders");
                    CoffeeOrder newOrder = new CoffeeOrder(coffeeOrder, coffeeSize, classroom, sugars, creams, userID);
                    myRef.child(Integer.toString(newOrder.orderID)).setValue(newOrder);
                    Toast.makeText(getApplicationContext(), "Your Order has been Placed!", Toast.LENGTH_SHORT).show();
                    Intent intent2 = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent2);
                }
            }
        });
/***************************************************************************************************/
    }

/***********************LOGIC FOR COFFEE SELECTION RADIO BUTTONS************************************/
    public void onCoffeeChoiceRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.morningBlendRadioButton:
                if (checked) {
                    coffeeOrder = "Morning Blend";
                }
                break;
            case R.id.hazelnutBlendRadioButton:
                if (checked) {
                    coffeeOrder = "Hazelnut";
                }
                break;
        }
    }
/***************************************************************************************************/

/**************LOGIC FOR COFFEE SIZE RADIO BUTTONS**************************************************/
    public void onSizeChoiceRadioButtonClicked(View view){
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()){
            case R.id.smallRadioButton:
                if (checked) {
                    coffeeSize = "Small";
                }
                break;
            case R.id.mediumRadioButton:
                if (checked) {
                    coffeeSize = "Medium";
                }
                break;
            case R.id.largeRadioButton:
                if (checked) {
                    coffeeSize = "Large";
                }
                break;
        }
    }
/***************************************************************************************************/
}
