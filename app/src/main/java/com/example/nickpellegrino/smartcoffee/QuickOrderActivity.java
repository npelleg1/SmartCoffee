package com.example.nickpellegrino.smartcoffee;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class QuickOrderActivity extends AppCompatActivity {

    String userID;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_order);

        database = FirebaseDatabase.getInstance();

        Intent intent = getIntent();
        userID = intent.getStringExtra("UserID");

        final TextView coffeeFlavorTextView = (TextView) findViewById(R.id.coffeeFlavorTextView);
        final TextView coffeeSizeTextView = (TextView) findViewById(R.id.coffeeSizeTextView);
        final TextView creamKindTextView = (TextView) findViewById(R.id.creamKindTextView);
        final TextView creamCountTextView = (TextView)findViewById(R.id.creamCountTextView);
        final TextView sugarKindTextView = (TextView) findViewById(R.id.sugarKindTextView);
        final TextView sugarCountTextView = (TextView) findViewById(R.id.sugarCountTextView);
        final TextView classroomTextView = (TextView) findViewById(R.id.classroomTextView);

        Button quickOrderButton = (Button) findViewById(R.id.orderButton);
        quickOrderButton.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v){
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.putExtra("UserID", userID);
                DatabaseReference myRef = database.getReference("Orders");
                CoffeeOrder newOrder = new CoffeeOrder(
                        coffeeFlavorTextView.getText().toString(),
                        coffeeSizeTextView.getText().toString(),
                        classroomTextView.getText().toString(),
                        Integer.parseInt(sugarCountTextView.getText().toString()),
                        Integer.parseInt(creamCountTextView.getText().toString()),
                        userID,
                        sugarKindTextView.getText().toString(),
                        creamKindTextView.getText().toString());
                myRef.child(Integer.toString(newOrder.orderID)).setValue(newOrder);
                Toast.makeText(getApplicationContext(), "Your Order has been Placed!", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                CoffeeOrder order = dataSnapshot.getValue(CoffeeOrder.class);
                if (order != null) {
                    sugarCountTextView.setText(String.valueOf(order.sugars));
                    creamCountTextView.setText(String.valueOf(order.creams));
                    classroomTextView.setText(order.classroom);
                    creamKindTextView.setText(order.creamKind);
                    sugarKindTextView.setText(order.sugarKind);
                    coffeeFlavorTextView.setText(order.coffeeOrder);
                    coffeeSizeTextView.setText(order.coffeeSize);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.getMessage());
            }
        };

        database.getReference("Preferences").child(userID).addListenerForSingleValueEvent(postListener);
    }
}
