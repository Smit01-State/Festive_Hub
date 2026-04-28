package com.Festive_Hub.android;

public class TicketModel {
    int id, qty;
    String eventName, vendorName, date, time, location, qrData;
    double totalPaid;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getQty() { return qty; }
    public void setQty(int qty) { this.qty = qty; }

    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }

    public String getVendorName() { return vendorName; }
    public void setVendorName(String vendorName) { this.vendorName = vendorName; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getQrData() { return qrData; }
    public void setQrData(String qrData) { this.qrData = qrData; }

    public double getTotalPaid() { return totalPaid; }
    public void setTotalPaid(double totalPaid) { this.totalPaid = totalPaid; }
}
