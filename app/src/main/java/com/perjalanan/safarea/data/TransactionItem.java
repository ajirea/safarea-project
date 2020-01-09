package com.perjalanan.safarea.data;


import android.os.Parcel;
import android.os.Parcelable;

public class TransactionItem implements Parcelable {

    private int id, userid, qty;
    private String name, product, phone, orderedAt, orderDesc, image;
    private double price, totalPrice;

    public TransactionItem(String image, int id, int userid, int qty, String name, String product,
                           String phone, String orderedAt, String orderDesc, double price,
                           double totalPrice)
    {
        this.image = image;
        this.id = id;
        this.userid = userid;
        this.qty = qty;
        this.name = name;
        this.product = product;
        this.phone = phone;
        this.orderedAt = orderedAt;
        this.orderDesc = orderDesc;
        this.price = price;
        this.totalPrice = totalPrice;
    }


    protected TransactionItem(Parcel in) {
        image = in.readString();
        id = in.readInt();
        userid = in.readInt();
        qty = in.readInt();
        name = in.readString();
        product = in.readString();
        phone = in.readString();
        orderedAt = in.readString();
        orderDesc = in.readString();
        price = in.readDouble();
        totalPrice = in.readDouble();
    }

    public static final Creator<TransactionItem> CREATOR = new Creator<TransactionItem>() {
        @Override
        public TransactionItem createFromParcel(Parcel in) {
            return new TransactionItem(in);
        }

        @Override
        public TransactionItem[] newArray(int size) {
            return new TransactionItem[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOrderDesc() {
        return orderDesc;
    }

    public void setOrderDesc(String orderDesc) {
        this.orderDesc = orderDesc;
    }

    public String getOrderedAt() {
        return orderedAt;
    }

    public void setOrderedAt(String orderedAt) {
        this.orderedAt = orderedAt;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(image);
        dest.writeInt(id);
        dest.writeInt(userid);
        dest.writeInt(qty);
        dest.writeString(name);
        dest.writeString(product);
        dest.writeString(phone);
        dest.writeString(orderedAt);
        dest.writeString(orderDesc);
        dest.writeDouble(price);
        dest.writeDouble(totalPrice);
    }
}
