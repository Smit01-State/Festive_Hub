package com.Festive_Hub.android;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.Festive_Hub.android.network.Event.SelectEventClient;

import java.util.ArrayList;

public class EventActivity extends AppCompatActivity {

    RecyclerView EventRecycler;
    SharedPreferences sp;
    ArrayList<EventList> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_event);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EventRecycler = findViewById(R.id.Event_Recycler);
        sp = getSharedPreferences(ConstantSP.PREF, MODE_PRIVATE);

        arrayList = new ArrayList<>();
        int selectedCID = sp.getInt(ConstantSP.CATEGORYID, 0);

        EventRecycler.setLayoutManager(new LinearLayoutManager(this));
        EventDetailAdapter adapter = new EventDetailAdapter(EventActivity.this, arrayList);
        EventRecycler.setAdapter(adapter);

        SelectEventClient.execute(this, selectedCID, new SelectEventClient.Callback() {
            @Override
            public void onFetched(JSONArray events) {
                try {
                    for (int i = 0; i < events.length(); i++) {
                        JSONObject obj = events.getJSONObject(i);
                        if (obj.has("cid") && obj.getInt("cid") == selectedCID) {
                            EventList event = new EventList();
                            event.setEventID(obj.getInt("id"));
                            event.setCategoryId(obj.getInt("cid"));
                            event.setVendorName(obj.getString("vendor"));
                            event.setEventName(obj.getString("name"));
                            event.setEventPrice(obj.getString("price"));
                            event.setEventDiscountPrice(obj.getString("disc_price"));
                            event.setDiscount(obj.getString("discount"));
                            event.setImage(obj.getString("image"));

                            if (obj.has("event_date"))
                                event.setEventDate(obj.getString("event_date"));
                            if (obj.has("event_time"))
                                event.setEventTime(obj.getString("event_time"));
                            if (obj.has("location"))
                                event.setLocation(obj.getString("location"));

                            arrayList.add(event);
                        }
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.e("EventActivity", "Error parsing events", e);
                    Toast.makeText(EventActivity.this, "Error parsing events", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {
                Log.e("EventActivity", "Fetch error: " + error);
                Toast.makeText(EventActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
