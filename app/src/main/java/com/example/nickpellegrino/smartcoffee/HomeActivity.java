package com.example.nickpellegrino.smartcoffee;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        final String userID = intent.getStringExtra("UserID");

        final ImageButton newOrderButton = (ImageButton) findViewById(R.id.newOrderButton);
        newOrderButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OrderActivity.class);
                intent.putExtra("UserID", userID);
                startActivity(intent);
            }
        });

        final ImageButton statusButton = (ImageButton) findViewById(R.id.statuus);
        statusButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.e("User Home","ABAOUT TO RUN STATUS");
                Intent intent = new Intent(getApplicationContext(), UserTrackerActivity.class);
                intent.putExtra("userID", userID);
                Log.e("User Home", "user ID = " + userID);
                startActivity(intent);
            }
        });

        final ImageButton profileButton = (ImageButton) findViewById(R.id.profileButton);
        profileButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                intent.putExtra("UserID", userID);
                startActivity(intent);
            }
        });

        final Button quickOrderButton = (Button) findViewById(R.id.quickOrderButton);
        quickOrderButton.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v){
                Intent intent = new Intent(getApplicationContext(), QuickOrderActivity.class);
                intent.putExtra("UserID", userID);
                startActivity(intent);
            }
        });

    }
}
