package com.Festive_Hub.android;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;

public class PurchasedTicketAdapter extends RecyclerView.Adapter<PurchasedTicketAdapter.ViewHolder> {

    Context context;
    ArrayList<TicketModel> list;

    public PurchasedTicketAdapter(Context context, ArrayList<TicketModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_purchased_ticket, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TicketModel ticket = list.get(position);

        holder.eventName.setText(ticket.getEventName());

        holder.ticketId.setText(android.text.Html.fromHtml("Ticket ID: <b>#TKT" + ticket.getId() + "</b>",
                android.text.Html.FROM_HTML_MODE_LEGACY));
        holder.location.setText(android.text.Html.fromHtml("Location: <b>" + ticket.getLocation() + "</b>",
                android.text.Html.FROM_HTML_MODE_LEGACY));

        holder.dateTime.setText(ticket.getDate() + " " + ticket.getTime());

        holder.bookingId.setText(android.text.Html.fromHtml("Booking ID: <b>#BKG" + ticket.getId() + "</b>",
                android.text.Html.FROM_HTML_MODE_LEGACY));
        holder.ticketType.setText(android.text.Html.fromHtml(
                "Ticket Type: <b>" + (ticket.getQty() == 1 ? "Single" : "Group") + " Ticket</b>",
                android.text.Html.FROM_HTML_MODE_LEGACY));

        // Generate QR Code Thumbnail
        Bitmap qrBitmap = generateQRCode(ticket.getQrData(), 400);
        if (qrBitmap != null) {
            holder.qrThumb.setImageBitmap(qrBitmap);
        }

        // Click on item to show full QR
        holder.itemView.setOnClickListener(v -> {
            Bitmap fullQr = generateQRCode(ticket.getQrData(), 800);
            if (fullQr != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                ImageView qrImageView = new ImageView(context);
                qrImageView.setImageBitmap(fullQr);
                qrImageView.setPadding(50, 50, 50, 50);

                builder.setView(qrImageView);
                builder.setTitle("Ticket - " + ticket.getEventName());
                builder.setMessage("Scan this QR code at the venue.");
                builder.setPositiveButton("Close", (dialog, which) -> dialog.dismiss());
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView qrThumb;
        TextView eventName, ticketId, location, dateTime, bookingId, ticketType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            qrThumb = itemView.findViewById(R.id.ticket_qr_thumb);
            eventName = itemView.findViewById(R.id.ticket_event_name);
            ticketId = itemView.findViewById(R.id.ticket_id);
            location = itemView.findViewById(R.id.ticket_name);
            dateTime = itemView.findViewById(R.id.ticket_date_time);
            bookingId = itemView.findViewById(R.id.ticket_booking_id);
            ticketType = itemView.findViewById(R.id.ticket_type);
        }
    }

    private Bitmap generateQRCode(String data, int size) {
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            return barcodeEncoder.encodeBitmap(data, BarcodeFormat.QR_CODE, size, size);
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }
}
