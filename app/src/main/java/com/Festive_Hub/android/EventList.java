package com.Festive_Hub.android;

public class EventList {

    int eventID, SubcategoryId;

    String vendorName, eventName, eventPrice, eventDiscountPrice, Discount, image;

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public int getSubcategoryId() {
        return SubcategoryId;
    }

    public void setSubcategoryId(int subcategoryId) {
        SubcategoryId = subcategoryId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventPrice() {
        return eventPrice;
    }

    public void setEventPrice(String eventPrice) {
        this.eventPrice = eventPrice;
    }

    public String getEventDiscountPrice() {
        return eventDiscountPrice;
    }

    public void setEventDiscountPrice(String eventDiscountPrice) {
        this.eventDiscountPrice = eventDiscountPrice;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
