package com.example.nickpellegrino.smartcoffee;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserHistoryActivity extends AppCompatActivity {

    FirebaseDatabase database;
    ListView myListView;
    DatabaseReference myRef;
    List<CoffeeOrder> orders = new ArrayList<CoffeeOrder>();
    ArrayAdapter<String> adapter;

    SimpleAdapter simple_adapter;
    ArrayList<Map<String, String>> list_map = new ArrayList<Map<String, String>>();
    String[] from = { "orderID", "roomNumber" };
    int[] to = { R.id.orderID, R.id.roomNumber };

    ProgressBar spinner;    // This is the Adapter being used to display the list's data
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_home);

        Log.e("User History ", "STARTING USER HISTORY");

        database = FirebaseDatabase.getInstance();
        myListView = (ListView) findViewById(R.id.listView);
        //myRef = database.getReference().child("Orders");
        Intent i = getIntent();
        userID = i.getStringExtra("userID");
        Log.e("User History ", "userID = " + userID);

        new RemoteDataTask().execute();

    }

    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            spinner = (ProgressBar)findViewById(R.id.progressBar1);
            spinner.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            myListView = (ListView) findViewById(R.id.listView);
            myRef = database.getReference().child("Orders");

            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                        Log.e("User History ", "postSnapshot.userID  = " + postSnapshot.getValue(CoffeeOrder.class).userID );
                        if (postSnapshot.getValue(CoffeeOrder.class).userID.equals(userID)) {
                            orders.add(postSnapshot.getValue(CoffeeOrder.class));
                        }
                    }

                    // Pass the results into an ArrayAdapter
                    adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.listview_item);

                    for (CoffeeOrder order : orders) {
                        HashMap<String, String> item = new HashMap<String, String>();
                        item.put("orderID", String.valueOf(order.orderID));
                        item.put("roomNumber", order.classroom);
                        list_map.add(item);
                    }

                    simple_adapter = new SimpleAdapter(getApplicationContext(), list_map, R.layout.simple_listview_item, from, to);

                    // Binds the Adapter to the ListView
                    //myListView.setAdapter(adapter);
                    myListView.setAdapter(simple_adapter);

                    // Capture button clicks on ListView items
                    myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            Intent i = new Intent(UserHistoryActivity.this, SingleOrderActivity.class);
                            i.putExtra("userID", orders.get(position).userID);
                            i.putExtra("orderID", String.valueOf(orders.get(position).orderID));
                            i.putExtra("classroom", orders.get(position).classroom);
                            i.putExtra("coffeOrder", orders.get(position).coffeeOrder);
                            i.putExtra("coffeeSize", orders.get(position).coffeeSize);
                            i.putExtra("creamKind", orders.get(position).creamKind);
                            i.putExtra("creams", String.valueOf(orders.get(position).creams));
                            i.putExtra("sugarKind", orders.get(position).sugarKind);
                            i.putExtra("sugars", String.valueOf(orders.get(position).sugars));
                            startActivity(i);

                        }
                    });

                    // Close the progressdialog
                    spinner.setVisibility(View.GONE);

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println(databaseError.getMessage());

                }
            });


        }
    }



}