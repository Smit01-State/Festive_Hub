package com.Festive_Hub.android;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.Festive_Hub.android.network.Event.InsertEventClient;
import com.Festive_Hub.android.network.Event.UpdateEventClient;

public class EventAddEditActivity extends AppCompatActivity {

    EditText etName, etVendor, etPrice, etDiscount, etImage, etDate, etTime, etLocation;
    Spinner spinnerCategory;
    int[] categoryIds = { 1, 2, 3, 4, 5, 6 };
    String[] categoryNames = { "Religious Festivals", "Cultural Festivals", "Music & Arts", "Food Festivals",
            "Tech Expos", "Sports Events" };
    Button btnSave;
    TextView title;

    boolean isEdit = false;
    int eventId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_add_edit);

        title = findViewById(R.id.title_add_edit_event);
        etName = findViewById(R.id.et_event_name);
        etVendor = findViewById(R.id.et_vendor_name);
        spinnerCategory = findViewById(R.id.spinner_category);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                categoryNames);
        spinnerCategory.setAdapter(spinnerAdapter);
        etPrice = findViewById(R.id.et_event_price);
        etDiscount = findViewById(R.id.et_event_discount);
        etImage = findViewById(R.id.et_event_image_url);
        etDate = findViewById(R.id.et_event_date);
        etTime = findViewById(R.id.et_event_time);
        etLocation = findViewById(R.id.et_event_location);
        btnSave = findViewById(R.id.btn_save_event);

        isEdit = getIntent().getBooleanExtra("isEdit", false);
        if (isEdit) {
            title.setText("Edit Event");
            eventId = getIntent().getIntExtra("id", -1);
            etName.setText(getIntent().getStringExtra("name"));
            etVendor.setText(getIntent().getStringExtra("vendor"));
            int existingCid = getIntent().getIntExtra("cid", 1);
            for (int i = 0; i < categoryIds.length; i++) {
                if (categoryIds[i] == existingCid) {
                    spinnerCategory.setSelection(i);
                    break;
                }
            }
            etPrice.setText(getIntent().getStringExtra("price"));
            etDiscount.setText(getIntent().getStringExtra("discount"));
            etImage.setText(getIntent().getStringExtra("image"));
            etDate.setText(getIntent().getStringExtra("event_date"));
            etTime.setText(getIntent().getStringExtra("event_time"));
            etLocation.setText(getIntent().getStringExtra("location"));
        }

        btnSave.setOnClickListener(v -> saveEvent());
    }

    private void saveEvent() {
        String name = etName.getText().toString().trim();
        String vendor = etVendor.getText().toString().trim();
        int selectedPosition = spinnerCategory.getSelectedItemPosition();
        int cid = categoryIds[selectedPosition];
        String price = etPrice.getText().toString().trim();
        String discount = etDiscount.getText().toString().trim();
        String discPrice = price;
        try {
            if (!price.isEmpty() && !discount.isEmpty()) {
                double priceVal = Double.parseDouble(price);
                String numericDiscount = discount.replaceAll("[^0-9.]", "");
                if (!numericDiscount.isEmpty()) {
                    double discountVal = Double.parseDouble(numericDiscount);
                    double calculatedDiscPrice = priceVal - (priceVal * discountVal / 100);
                    discPrice = String.valueOf(calculatedDiscPrice);
                    if (discPrice.endsWith(".0")) {
                        discPrice = discPrice.substring(0, discPrice.length() - 2);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String image = etImage.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        String time = etTime.getText().toString().trim();
        String location = etLocation.getText().toString().trim();

        if (name.isEmpty() || vendor.isEmpty() || price.isEmpty()) {
            Toast.makeText(this, "Please fill in required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isEdit) {
            UpdateEventClient.execute(this, eventId, cid, vendor, name, price, discPrice, discount, image, date, time,
                    location, new UpdateEventClient.Callback() {
                        @Override
                        public void onSuccess(String message) {
                            Toast.makeText(EventAddEditActivity.this, "Event Updated!", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void onError(String error) {
                            Toast.makeText(EventAddEditActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            InsertEventClient.execute(this, cid, vendor, name, price, discPrice, discount, image, date, time, location,
                    new InsertEventClient.Callback() {
                        @Override
                        public void onSuccess(String message) {
                            Toast.makeText(EventAddEditActivity.this, "Event Added!", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void onError(String error) {
                            Toast.makeText(EventAddEditActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
