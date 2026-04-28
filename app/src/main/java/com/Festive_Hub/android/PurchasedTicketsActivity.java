package com.Festive_Hub.android;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PurchasedTicketsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SQLiteDatabase db;
    ArrayList<TicketModel> ticketList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchased_tickets);

        recyclerView = findViewById(R.id.purchased_tickets_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ticketList = new ArrayList<>();
        
        db = openOrCreateDatabase("finalApp", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS PURCHASED_TICKETS(ID INTEGER PRIMARY KEY AUTOINCREMENT, EVENT_NAME VARCHAR, VENDOR_NAME VARCHAR, DATE VARCHAR, TIME VARCHAR, LOCATION VARCHAR, QTY INTEGER, TOTAL_PAID DOUBLE, QR_DATA TEXT)");

        loadTickets();

        PurchasedTicketAdapter adapter = new PurchasedTicketAdapter(this, ticketList);
        recyclerView.setAdapter(adapter);
    }

    private void loadTickets() {
        Cursor cursor = db.rawQuery("SELECT * FROM PURCHASED_TICKETS ORDER BY ID DESC", null);
        if (cursor.moveToFirst()) {
            do {
                TicketModel ticket = new TicketModel();
                ticket.setId(cursor.getInt(0));
                ticket.setEventName(cursor.getString(1));
                ticket.setVendorName(cursor.getString(2));
                ticket.setDate(cursor.getString(3));
                ticket.setTime(cursor.getString(4));
                ticket.setLocation(cursor.getString(5));
                ticket.setQty(cursor.getInt(6));
                ticket.setTotalPaid(cursor.getDouble(7));
                ticket.setQrData(cursor.getString(8));
                ticketList.add(ticket);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
