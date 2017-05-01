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

    ImageButton morning_ib;
    ImageButton hazelnut_ib;
    ImageButton splenda_ib;
    ImageButton equal_ib;
    ImageButton regular_ib;
    ImageButton cream_a_ib;
    ImageButton cream_b_ib;
    ImageButton cream_c_ib;
    ImageButton medium_coffee_ib;
    ImageButton large_coffee_ib;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        database = FirebaseDatabase.getInstance();

        Intent intent = getIntent();
        userID = intent.getStringExtra("UserID");

        morning_ib = (ImageButton) findViewById(R.id.morningBlendButton);
        hazelnut_ib = (ImageButton) findViewById(R.id.hazelnutButton);
        splenda_ib = (ImageButton) findViewById(R.id.splendaSugarButton);
        equal_ib = (ImageButton) findViewById(R.id.equalSugarButton);
        regular_ib = (ImageButton) findViewById(R.id.regularSugarButton);
        cream_a_ib = (ImageButton) findViewById(R.id.creamAButton);
        cream_b_ib = (ImageButton) findViewById(R.id.creamBButton);
        cream_c_ib = (ImageButton) findViewById(R.id.creamCButton);
        medium_coffee_ib = (ImageButton) findViewById(R.id.mediumCoffeeButton);
        large_coffee_ib = (ImageButton) findViewById(R.id.largeCoffeeButton);

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
                    CoffeeOrder newOrder = new CoffeeOrder(coffeeOrder, coffeeSize, classroom, sugars, creams, userID, sugarType, creamType);
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
        //Drawable dr = ResourcesCompat.getDrawable(getResources(), R.drawable.button_pressed, null);
        switch (view.getId()) {
            case R.id.morningBlendButton:
                coffeeOrder = "Morning Blend";
                morning_ib.setImageResource(R.drawable.clicked_sunrise);
                hazelnut_ib.setImageResource(R.drawable.hazelnut);
                break;
            case R.id.hazelnutButton:
                coffeeOrder = "Hazelnut";
                morning_ib.setImageResource(R.drawable.sunrise);
                hazelnut_ib.setImageResource(R.drawable.clicked_hazelnut);
                break;
        }
    }
/***************************************************************************************************/

/**************LOGIC FOR COFFEE SIZE BUTTONS**************************************************/
    public void onSizeChoiceButtonClicked(View view){
        switch(view.getId()){
            case R.id.mediumCoffeeButton:
                coffeeSize = "Medium";
                medium_coffee_ib.setImageResource(R.drawable.clicked_coffee);
                large_coffee_ib.setImageResource(R.drawable.coffee);
                break;
            case R.id.largeCoffeeButton:
                coffeeSize = "Large";
                medium_coffee_ib.setImageResource(R.drawable.coffee);
                large_coffee_ib.setImageResource(R.drawable.clicked_coffee);
                break;
        }
    }
/***************************************************************************************************/

/**************LOGIC FOR SUGAR TYPE BUTTONS**************************************************/
    public void onSugarChoiceButtonClicked(View view){
        switch(view.getId()){
            case R.id.splendaSugarButton:
                sugarType = "Splenda";
                splenda_ib.setImageResource(R.drawable.clicked_suggar);
                equal_ib.setImageResource(R.drawable.sugar);
                regular_ib.setImageResource(R.drawable.sugar);
                break;
            case R.id.equalSugarButton:
                sugarType = "Equal";
                splenda_ib.setImageResource(R.drawable.sugar);
                equal_ib.setImageResource(R.drawable.clicked_suggar);
                regular_ib.setImageResource(R.drawable.sugar);
                break;
            case R.id.regularSugarButton:
                sugarType = "Regular";
                splenda_ib.setImageResource(R.drawable.sugar);
                equal_ib.setImageResource(R.drawable.sugar);
                regular_ib.setImageResource(R.drawable.clicked_suggar);
                break;
        }
    }
/***************************************************************************************************/

/**************LOGIC FOR CREAM TYPE BUTTONS**************************************************/
    public void onCreamChoiceButtonClicked(View view){
        switch(view.getId()){
            case R.id.creamAButton:
                creamType = "A";
                cream_a_ib.setImageResource(R.drawable.clicked_cream);
                cream_b_ib.setImageResource(R.drawable.creamcup);
                cream_c_ib.setImageResource(R.drawable.creamcup);
                break;
            case R.id.creamBButton:
                creamType = "B";
                cream_a_ib.setImageResource(R.drawable.creamcup);
                cream_b_ib.setImageResource(R.drawable.clicked_cream);
                cream_c_ib.setImageResource(R.drawable.creamcup);
                break;
            case R.id.creamCButton:
                creamType = "C";
                cream_a_ib.setImageResource(R.drawable.creamcup);
                cream_b_ib.setImageResource(R.drawable.creamcup);
                cream_c_ib.setImageResource(R.drawable.clicked_cream);
                break;
        }
    }
/***************************************************************************************************/

}
