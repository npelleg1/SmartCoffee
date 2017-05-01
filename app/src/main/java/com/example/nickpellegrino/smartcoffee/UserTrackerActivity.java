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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserTrackerActivity extends AppCompatActivity {

    FirebaseDatabase database;
    ListView myListView;
    DatabaseReference myRef;
    List<CoffeeOrder> orders = new ArrayList<CoffeeOrder>();
    ArrayAdapter<String> adapter;

    SimpleAdapter simple_adapter;
    ArrayList<Map<String, String>> list_map = new ArrayList<Map<String, String>>();
    String[] from = { "orderID", "roomNumber", "dateTime", "status", "icon" };
    int[] to = { R.id.orderID, R.id.roomNumber, R.id.dateTime, R.id.statusTextView};

    ProgressBar spinner;    // This is the Adapter being used to display the list's data
    String userID;

    Date currDate = new Date();
    Date orderDate = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_history);

        Log.e("User Tracker ", "STARTING USER TRACKER");

        database = FirebaseDatabase.getInstance();
        myListView = (ListView) findViewById(R.id.listView);
        //myRef = database.getReference().child("Orders");
        Intent i = getIntent();
        userID = i.getStringExtra("userID");
        Log.e("User History ", "userID = " + userID);

        new RemoteDataTask().execute();

    }
/*

`*/

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

            currDate = new Date();
            final SimpleDateFormat df = new SimpleDateFormat("E, dd MM yyyy HH:mm:ss");
            final SimpleDateFormat dfc = new SimpleDateFormat("ddMMyyyy");

            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){

                        if (postSnapshot.getValue(CoffeeOrder.class).orderedDate != null) {
                            String dateStr = postSnapshot.getValue(CoffeeOrder.class).orderedDate;
                            try {
                                orderDate = df.parse(dateStr);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                            Log.e("User History ", "postSnapshot.userID  = " + postSnapshot.getValue(CoffeeOrder.class).userID);
                            // Also Filter Outon ORders that have yet been delivered
                            if (postSnapshot.getValue(CoffeeOrder.class).userID.equals(userID) ) {
                                orders.add(postSnapshot.getValue(CoffeeOrder.class));
                            }
                        }
                    }

                    // Pass the results into an ArrayAdapter
                    adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.listview_item);

                    for (CoffeeOrder order : orders) {
                        HashMap<String, String> item = new HashMap<String, String>();
                        item.put("orderID", String.valueOf(order.orderID));
                        item.put("roomNumber", order.classroom);
                        item.put("dateTime", order.orderedDate);
                        item.put("status", order.status);
                        switch (order.status){
                            case "Pending":
                                item.put("icon", String.valueOf(R.drawable.pending));
                                break;
                            case "Brewing":
                                item.put("icon", String.valueOf(R.drawable.brewing));
                                break;
                            case "In Transit":
                                item.put("icon", String.valueOf(R.drawable.truck));
                                break;
                            case "Delivered":
                                item.put("icon", String.valueOf(R.drawable.delivered));
                                break;
                        }
                        list_map.add(item);
                    }

                    simple_adapter = new SimpleAdapter(getApplicationContext(), list_map, R.layout.simple_listview_item_time, from, to);

                    // Binds the Adapter to the ListView
                    //myListView.setAdapter(adapter);
                    myListView.setAdapter(simple_adapter);

                    // Capture button clicks on ListView items

                    myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            Intent i = new Intent(UserTrackerActivity.this, UserSingleOrderActivity.class);
                            i.putExtra("userID", orders.get(position).userID);
                            i.putExtra("status", orders.get(position).status);
                            i.putExtra("orderID", String.valueOf(orders.get(position).orderID));
                            i.putExtra("classroom", orders.get(position).classroom);
                            i.putExtra("coffeeOrder", orders.get(position).coffeeOrder);
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