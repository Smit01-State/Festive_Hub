package com.Festive_Hub.android;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EventActivity extends AppCompatActivity {

    RecyclerView EventRecycler;
    SharedPreferences sp;
    ArrayList<EventList> arrayList;
    SQLiteDatabase db;

    // Initial static data to populate DB if empty
    int[] eventIDArray = { 1, 2, 3, 4, 5, 6 };
    int[] cIdArray = { 1, 1, 2, 2, 3, 4 };
    String[] vendorName = { "Central Park, NYC", "City Square, Ahmedabad", "St. Peter's Cathedral",
            "Community Hall, London", "Beach Side, Goa", "Palace Grounds, Bangalore" };
    String[] eventNameArray = { "Grand Diwali Mela 2024", "Deepavali Fireworks Show", "Christmas Eve Grand Mass",
            "Santa's Winter Workshop", "Holi Color Fest 2024", "Navratri Garba Night" };
    String[] eventPriceArray = { "599", "799", "1200", "499", "899", "1500" };
    String[] eventDiscountPriceArray = { "399", "599", "1000", "399", "649", "1199" };
    String[] eventDiscountArray = { "33", "25", "16", "20", "27", "20" };
    String[] imageArray = {
            "https://img.freepik.com/free-vector/happy-diwali-festival-banner-with-realistic-diya-oil-lamp_1017-34085.jpg",
            "https://img.freepik.com/free-vector/diwali-celebration-background-with-fireworks_1017-15632.jpg",
            "https://img.freepik.com/free-vector/christmas-background-with-realistic-decoration_23-2148761273.jpg",
            "https://img.freepik.com/free-vector/christmas-workshop-landing-page_23-2148780366.jpg",
            "https://img.freepik.com/free-vector/colorful-holi-festival-background_23-2148842427.jpg",
            "https://img.freepik.com/free-vector/navratri-festival-background-with-realistic-dandiya_1017-21175.jpg" };

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

        // DB Setup
        db = openOrCreateDatabase("finalApp", MODE_PRIVATE, null);
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS EVENT_LIST(ID INTEGER PRIMARY KEY, CID INTEGER, VENDOR TEXT, NAME TEXT, PRICE TEXT, DISC_PRICE TEXT, DISCOUNT TEXT, IMAGE TEXT)");

        // Populating initial data if empty
        Cursor checkCursor = db.rawQuery("SELECT * FROM EVENT_LIST", null);
        if (checkCursor.getCount() == 0) {
            for (int i = 0; i < eventNameArray.length; i++) {
                ContentValues values = new ContentValues();
                values.put("ID", eventIDArray[i]);
                values.put("CID", cIdArray[i]);
                values.put("VENDOR", vendorName[i]);
                values.put("NAME", eventNameArray[i]);
                values.put("PRICE", eventPriceArray[i]);
                values.put("DISC_PRICE", eventDiscountPriceArray[i]);
                values.put("DISCOUNT", eventDiscountArray[i]);
                values.put("IMAGE", imageArray[i]);
                db.insert("EVENT_LIST", null, values);
            }
        }
        checkCursor.close();

        // Load data from DB into ArrayList
        arrayList = new ArrayList<>();
        int selectedCID = sp.getInt(ConstantSP.CATEGORYID, 0);
        Cursor cursor = db.rawQuery("SELECT * FROM EVENT_LIST WHERE CID = " + selectedCID, null);

        while (cursor.moveToNext()) {
            EventList list = new EventList();
            list.setEventID(cursor.getInt(0));
            list.setCategoryId(cursor.getInt(1));
            list.setVendorName(cursor.getString(2));
            list.setEventName(cursor.getString(3));
            list.setEventPrice(cursor.getString(4));
            list.setEventDiscountPrice(cursor.getString(5));
            list.setDiscount(cursor.getString(6));
            list.setImage(cursor.getString(7));
            arrayList.add(list);
        }
        cursor.close();

        // Setting the new Adapter with new Custom Layout
        EventRecycler.setLayoutManager(new LinearLayoutManager(this));
        EventDetailAdapter adapter = new EventDetailAdapter(EventActivity.this, arrayList);
        EventRecycler.setAdapter(adapter);
    }
}
