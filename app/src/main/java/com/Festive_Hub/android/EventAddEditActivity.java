package com.Festive_Hub.android;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.Festive_Hub.android.network.Event.InsertEventClient;
import com.Festive_Hub.android.network.Event.UpdateEventClient;

public class EventAddEditActivity extends AppCompatActivity {

    EditText etName, etVendor, etScid, etPrice, etDiscPrice, etDiscount, etImage, etDate, etTime, etLocation;
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
        etScid = findViewById(R.id.et_scid);
        etPrice = findViewById(R.id.et_event_price);
        etDiscPrice = findViewById(R.id.et_event_disc_price);
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
            etScid.setText(String.valueOf(getIntent().getIntExtra("scid", 0)));
            etPrice.setText(getIntent().getStringExtra("price"));
            etDiscPrice.setText(getIntent().getStringExtra("disc_price"));
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
        String scidStr = etScid.getText().toString().trim();
        String price = etPrice.getText().toString().trim();
        String discPrice = etDiscPrice.getText().toString().trim();
        String discount = etDiscount.getText().toString().trim();
        String image = etImage.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        String time = etTime.getText().toString().trim();
        String location = etLocation.getText().toString().trim();

        if (name.isEmpty() || vendor.isEmpty() || scidStr.isEmpty() || price.isEmpty()) {
            Toast.makeText(this, "Please fill in required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int scid = Integer.parseInt(scidStr);

        if (isEdit) {
            UpdateEventClient.execute(this, eventId, scid, vendor, name, price, discPrice, discount, image, date, time, location, new UpdateEventClient.Callback() {
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
            InsertEventClient.execute(this, scid, vendor, name, price, discPrice, discount, image, date, time, location, new InsertEventClient.Callback() {
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
