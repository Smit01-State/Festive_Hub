package com.Festive_Hub.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.Festive_Hub.android.network.Event.SelectEventClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class EventManagerActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    EventManagerAdapter adapter;
    ArrayList<EventList> eventList;
    FloatingActionButton fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_manager);

        recyclerView = findViewById(R.id.recycler_manage_events);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        fabAdd = findViewById(R.id.fab_add_event);
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(EventManagerActivity.this, EventAddEditActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchEvents();
    }

    private void fetchEvents() {
        SelectEventClient.execute(this, new SelectEventClient.Callback() {
            @Override
            public void onFetched(JSONArray events) {
                eventList = new ArrayList<>();
                try {
                    for (int i = 0; i < events.length(); i++) {
                        JSONObject obj = events.getJSONObject(i);
                        EventList event = new EventList();
                        event.setEventID(obj.getInt("id"));
                        event.setCategoryId(obj.getInt("cid"));
                        event.setVendorName(obj.getString("vendor"));
                        event.setEventName(obj.getString("name"));
                        event.setEventPrice(obj.getString("price"));
                        event.setEventDiscountPrice(obj.getString("disc_price"));
                        event.setDiscount(obj.getString("discount"));
                        event.setImage(obj.getString("image"));
                        
                        // Use has() or optString to avoid JSONException if fields are missing
                        if(obj.has("event_date")) event.setEventDate(obj.getString("event_date"));
                        if(obj.has("event_time")) event.setEventTime(obj.getString("event_time"));
                        if(obj.has("location")) event.setLocation(obj.getString("location"));

                        eventList.add(event);
                    }
                    adapter = new EventManagerAdapter(EventManagerActivity.this, eventList);
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    Log.e("EventManagerActivity", "Error parsing events", e);
                    Toast.makeText(EventManagerActivity.this, "Error parsing events", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {
                Log.e("EventManagerActivity", "Fetch error: " + error);
                Toast.makeText(EventManagerActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
