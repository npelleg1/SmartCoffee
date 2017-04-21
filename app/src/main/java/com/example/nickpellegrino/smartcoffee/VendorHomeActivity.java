package com.example.nickpellegrino.smartcoffee;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class VendorHomeActivity extends AppCompatActivity {

    FirebaseDatabase database;
    ListView myListView;
    DatabaseReference myRef;
    List<CoffeeOrder> orders;
    ArrayAdapter<String> adapter;
    // This is the Adapter being used to display the list's data
    SimpleCursorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_home);
        new RemoteDataTask().execute();
        Log.d("VendorHomeActivity", "Inside activity!");

        database = FirebaseDatabase.getInstance();
        myListView = (ListView) findViewById(R.id.listView);
        myRef = database.getReference("Orders");
    }

    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            /*
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("ClubEvents");
            try {
                ob = query.find();
            } catch (ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            */
            Log.d("VendorHomeActivity", "Before Query");
            Query myQuery = myRef;
            Log.d("VendorHomeActivity", "After Query!");

            myQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                        orders.add(postSnapshot.getValue(CoffeeOrder.class));
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println(databaseError.getMessage());

                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            myListView = (ListView) findViewById(R.id.listView);
            // Pass the results into an ArrayAdapter
            adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.listview_item);
            for (CoffeeOrder order : orders) {
                adapter.add(String.valueOf(order.orderID) + " " + String.valueOf(order.classroom));
            }
            // Binds the Adapter to the ListView
            myListView.setAdapter(adapter);
            // Capture button clicks on ListView items
            myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    /*
                    Intent i = new Intent(EventActivity.this, SingleItemViewEvent.class);
                    startActivity(i);
                    */
                }
            });
        }
    }



}