package com.perjalanan.safarea.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class CatalogItem implements Parcelable {

    private Integer id, stock;
    private String title, thumbnail, description;
    private Double price, profitPrice;
    private ArrayList<String[]> images;

    public CatalogItem(Integer id, String thumbnail, String title, Double price) {
        this.id = id;
        this.thumbnail = thumbnail;
        this.title = title;
        this.price = price;
    }

    public CatalogItem(Integer id, String thumbnail, String title, Double price, Double profitPrice) {
        this.id = id;
        this.thumbnail = thumbnail;
        this.title = title;
        this.price = price;
        this.profitPrice = profitPrice;
    }

    // parcelable implementation constructor
    @SuppressWarnings("unchecked")
    protected CatalogItem(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        if (in.readByte() == 0) {
            thumbnail = null;
        } else {
            thumbnail = in.readString();
        }
        if (in.readByte() == 0) {
            stock = null;
        } else {
            stock = in.readInt();
        }
        title = in.readString();
        description = in.readString();
        if (in.readByte() == 0) {
            price = null;
        } else {
            price = in.readDouble();
        }
        if (in.readByte() == 0) {
            profitPrice = null;
        } else {
            profitPrice = in.readDouble();
        }

        images = in.readArrayList(null);
    }

    // parcelable implementation
    public static final Creator<CatalogItem> CREATOR = new Creator<CatalogItem>() {
        @Override
        public CatalogItem createFromParcel(Parcel in) {
            return new CatalogItem(in);
        }

        @Override
        public CatalogItem[] newArray(int size) {
            return new CatalogItem[size];
        }
    };

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public Double getPrice() {
        return price;
    }

    public Double getProfitedPrice() {
        return price + profitPrice;
    }

    public ArrayList<String[]> getImages() {
        if(images == null)
            images = new ArrayList<>();
        return images;
    }

    public void setImages(ArrayList<String[]> images) {
        this.images = images;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        if (thumbnail == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeString(thumbnail);
        }
        if (stock == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(stock);
        }
        dest.writeString(title);
        dest.writeString(description);
        if (price == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(price);
        }
        if (profitPrice == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(profitPrice);
        }

        dest.writeList(images);
    }
}
