package com.example.nickpellegrino.smartcoffee;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderActivity extends AppCompatActivity {

    String coffeeOrder = null;
    String coffeeSize = null;
    String sugarType = null;
    String creamType = null;
    String classroom;
    String userID;
    int sugars = 0;
    int creams = 0;
    FirebaseDatabase database;

    ImageButton mediumCoffeeSizeButton;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        database = FirebaseDatabase.getInstance();

        Intent intent = getIntent();
        userID = intent.getStringExtra("UserID");

        mediumCoffeeSizeButton = (ImageButton)findViewById(R.id.mediumCoffeeButton);


/*******************SUGAR COUNT LOGIC***************************************************************/
        final TextView sugarCountTextView = (TextView) findViewById(R.id.sugarCountTextView);
        ImageButton minusSugarButton = (ImageButton) findViewById(R.id.minusSugarButton);
        minusSugarButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (sugars > 0) {
                    sugars--;
                    sugarCountTextView.setText(Integer.toString(sugars));
                }
            }
        });
        ImageButton addSugarButton = (ImageButton) findViewById(R.id.plusSugarButton);
        addSugarButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                sugars++;
                sugarCountTextView.setText(Integer.toString(sugars));
            }
        });
/***************************************************************************************************/

/*******************CREAM COUNT LOGIC***************************************************************/
        final TextView creamCountTextView = (TextView) findViewById(R.id.creamCountTextView);
        ImageButton minusCreamButton = (ImageButton) findViewById(R.id.minusCreamButton);
        minusCreamButton.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v){
                if (creams > 0){
                    creams--;
                    creamCountTextView.setText(Integer.toString(creams));
                }
            }
        });
        ImageButton addCreamButton = (ImageButton) findViewById(R.id.plusCreamButton);
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

/***********************LOGIC FOR COFFEE SELECTION BUTTONS************************************/
    public void onCoffeeChoiceButtonClicked(View view) {
        //boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.morningBlendButton:
                coffeeOrder = "Morning Blend";
                break;
            case R.id.hazelnutButton:
                coffeeOrder = "Hazelnut";
                break;
        }
    }
/***************************************************************************************************/

/**************LOGIC FOR COFFEE SIZE BUTTONS**************************************************/
    public void onSizeChoiceButtonClicked(View view){
        //boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()){
            case R.id.mediumCoffeeButton:
                coffeeSize = "Medium";
                break;
            case R.id.largeCoffeeButton:
                coffeeSize = "Large";
                break;
        }
    }
/***************************************************************************************************/

/**************LOGIC FOR SUGAR TYPE BUTTONS**************************************************/
    public void onSugarChoiceButtonClicked(View view){
        //boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()){
            case R.id.splendaSugarButton:
                sugarType = "Splenda";
                break;
            case R.id.equalSugarButton:
                sugarType = "Equal";
                break;
            case R.id.regularSugarButton:
                sugarType = "Regular";
                break;
        }
    }
/***************************************************************************************************/

/**************LOGIC FOR CREAM TYPE BUTTONS**************************************************/
    public void onCreamChoiceButtonClicked(View view){
        //boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()){
            case R.id.creamAButton:
                creamType = "A";
                break;
            case R.id.creamBButton:
                creamType = "B";
                break;
            case R.id.creamCButton:
                creamType = "C";
                break;
        }
    }
/***************************************************************************************************/

}
