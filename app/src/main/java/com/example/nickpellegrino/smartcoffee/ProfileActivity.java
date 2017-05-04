package com.example.nickpellegrino.smartcoffee;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    String userID;
    String coffeeSize;
    String coffeeType;
    String sugarKind;
    String creamKind;
    String classroom;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        database = FirebaseDatabase.getInstance();

        Intent intent = getIntent();
        userID = intent.getStringExtra("UserID");

        final EditText sugarText = (EditText) findViewById(R.id.sugarEditText);
        final EditText creamText = (EditText) findViewById(R.id.creamEditText);

        //************SET UP NUMBER PICKER FOR ROOM CHOICE***************//
        final NumberPicker roomPicker = (NumberPicker) findViewById(R.id.roomNumberPicker);
        final String[] values= {"101","102", "108",
                "113", "115", "116", "117", "118", "119",
                "120", "125", "126", "128", "129", "131",
                "136", "138", "140", "141", "143", "149",
                "155", "315"};
        roomPicker.setMinValue(0);
        roomPicker.setMaxValue(values.length-1);
        roomPicker.setDisplayedValues(values);
        //****************************************************************//

        Button orderButton = (Button) findViewById(R.id.savePreferencesButton);
        orderButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                classroom = roomPicker.getDisplayedValues()[roomPicker.getValue()];
                DatabaseReference myRef = database.getReference("Preferences");
                CoffeeOrder newOrder = new CoffeeOrder(coffeeType, coffeeSize, classroom, Integer.parseInt(sugarText.getText().toString()), Integer.parseInt(creamText.getText().toString()), userID, sugarKind, creamKind);
                myRef.child(userID).setValue(newOrder);
                Toast.makeText(getApplicationContext(), "Your Preferences Have Been Saved!", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(getApplicationContext(), HomeActivity.class);
                intent2.putExtra("UserID", userID);
                startActivity(intent2);
            }
        });

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                CoffeeOrder order = dataSnapshot.getValue(CoffeeOrder.class);
                if (order != null) {
                    sugarText.setText(String.valueOf(order.sugars));
                    creamText.setText(String.valueOf(order.creams));
                    for (int i = 0; i < values.length; i++) {
                        if (values[i].equals(order.classroom)) {
                            roomPicker.setValue(i);
                            classroom = order.classroom;
                            break;
                        }
                    }
                    creamKind = order.creamKind;
                    switch(order.creamKind){
                        case "A":
                            RadioButton creamARadioButton = (RadioButton) findViewById(R.id.creamARadioButton);
                            creamARadioButton.setChecked(true);
                            break;
                        case "B":
                            RadioButton creamBRadioButton = (RadioButton) findViewById(R.id.creamBRadioButton);
                            creamBRadioButton.setChecked(true);
                            break;
                        case "C":
                            RadioButton creamCRadioButton = (RadioButton) findViewById(R.id.creamCRadioButton);
                            creamCRadioButton.setChecked(true);
                            break;
                    }
                    sugarKind = order.sugarKind;
                    switch(order.sugarKind){
                        case "Regular":
                            RadioButton regularRadioButton = (RadioButton) findViewById(R.id.regularRadioButton);
                            regularRadioButton.setChecked(true);
                            break;
                        case "Splenda":
                            RadioButton splendaRadioButton = (RadioButton) findViewById(R.id.splendaRadioButton);
                            splendaRadioButton.setChecked(true);
                            break;
                        case "Equal":
                            RadioButton equalRadioButton = (RadioButton) findViewById(R.id.equalRadioButton);
                            equalRadioButton.setChecked(true);
                            break;
                    }
                    coffeeType = order.coffeeOrder;
                    switch (order.coffeeOrder){
                        case "Morning Blend":
                            RadioButton morningBlendRadioButton = (RadioButton) findViewById(R.id.morningBlendRadioButton);
                            morningBlendRadioButton.setChecked(true);
                            break;
                        case "Hazelnut":
                            RadioButton hazelnutRadioButton = (RadioButton) findViewById(R.id.hazelnutRadioButton);
                            hazelnutRadioButton.setChecked(true);
                            break;
                    }
                    coffeeSize = order.coffeeSize;
                    switch (order.coffeeSize){
                        case "Medium":
                            RadioButton mediumRadioButton = (RadioButton) findViewById(R.id.mediumSizeRadioButton);
                            mediumRadioButton.setChecked(true);
                            break;
                        case "Large":
                            RadioButton largeRadioButton = (RadioButton) findViewById(R.id.largeSizeRadioButton);
                            largeRadioButton.setChecked(true);
                            break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.getMessage());
            }
        };

        database.getReference("Preferences").child(userID).addListenerForSingleValueEvent(postListener);
    }

    //*****************COFFEE SIZE SELECTION LOGIC****************************//
    public void onSizeChoiceButtonClicked(View view){
        switch(view.getId()){
            case R.id.mediumSizeRadioButton:
                coffeeSize = "Medium";
                break;
            case R.id.largeSizeRadioButton:
                coffeeSize = "Large";
                break;
        }
    }
    //*************************************************************************//

    //******************COFFEE FLAVOR SELECTION LOGIC**************************//
    public void onCoffeeTypeButtonClicked(View view){
        switch(view.getId()){
            case R.id.morningBlendRadioButton:
                coffeeType = "Morning Blend";
                break;
            case R.id.hazelnutRadioButton:
                coffeeType = "Hazelnut";
                break;
        }
    }
    //***************************************************************************//

    //***********************SUGAR SELECTION LOGIC*******************************//
    public void onSugarChoiceButtonClicked(View view){
        switch(view.getId()){
            case R.id.equalRadioButton:
                sugarKind = "Equal";
                break;
            case R.id.regularRadioButton:
                sugarKind = "Regular";
                break;
            case R.id.splendaRadioButton:
                sugarKind = "Splenda";
                break;
        }
    }
    //***************************************************************************//

    //************************CREAM KIND LOGIC***********************************//
    public void onCreamKindButtonClicked(View view){
        switch(view.getId()){
            case R.id.creamARadioButton:
                creamKind = "A";
                break;
            case R.id.creamBRadioButton:
                creamKind = "B";
                break;
            case R.id.creamCRadioButton:
                creamKind = "C";
                break;
        }
    }
    //***************************************************************************//
}
