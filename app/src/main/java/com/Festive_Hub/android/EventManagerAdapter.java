package com.Festive_Hub.android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.Festive_Hub.android.network.Event.DeleteEventClient;
import java.util.ArrayList;

public class EventManagerAdapter extends RecyclerView.Adapter<EventManagerAdapter.ViewHolder> {

    Context context;
    ArrayList<EventList> eventList;

    public EventManagerAdapter(Context context, ArrayList<EventList> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_manage_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EventList event = eventList.get(position);

        holder.eventName.setText(event.getEventName());
        holder.vendorName.setText(event.getVendorName());
        holder.price.setText("Price: ₹" + event.getEventPrice());

        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, EventAddEditActivity.class);
            intent.putExtra("isEdit", true);
            intent.putExtra("id", event.getEventID());
            intent.putExtra("scid", event.getSubcategoryId());
            intent.putExtra("vendor", event.getVendorName());
            intent.putExtra("name", event.getEventName());
            intent.putExtra("price", event.getEventPrice());
            intent.putExtra("disc_price", event.getEventDiscountPrice());
            intent.putExtra("discount", event.getDiscount());
            intent.putExtra("image", event.getImage());
            intent.putExtra("event_date", event.getEventDate());
            intent.putExtra("event_time", event.getEventTime());
            intent.putExtra("location", event.getLocation());
            context.startActivity(intent);
        });

        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                .setTitle("Delete Event")
                .setMessage("Are you sure you want to delete this event?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    DeleteEventClient.execute(context, event.getEventID(), new DeleteEventClient.Callback() {
                        @Override
                        public void onSuccess(String message) {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            eventList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, eventList.size());
                        }

                        @Override
                        public void onError(String error) {
                            Toast.makeText(context, "Error: " + error, Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("No", null)
                .show();
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView eventName, vendorName, price;
        ImageView eventImage, btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.item_manage_event_name);
            vendorName = itemView.findViewById(R.id.item_manage_event_vendor);
            price = itemView.findViewById(R.id.item_manage_event_price);
            eventImage = itemView.findViewById(R.id.item_manage_event_image);
            btnEdit = itemView.findViewById(R.id.btn_edit_event);
            btnDelete = itemView.findViewById(R.id.btn_delete_event);
        }
    }
}
