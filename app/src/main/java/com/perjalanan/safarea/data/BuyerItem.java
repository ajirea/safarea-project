package com.perjalanan.safarea.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Kelas data untuk pembeli item
 */
public class BuyerItem implements Parcelable {
    private Integer id, userId;
    private String name, phone;
    private String createdAt, deletedAt;

    public BuyerItem(Integer id, Integer userId, String name, String phone) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.phone = phone;
    }

    public static final Creator<BuyerItem> CREATOR = new Creator<BuyerItem>() {
        @Override
        public BuyerItem createFromParcel(Parcel in) {
            return new BuyerItem(in);
        }

        @Override
        public BuyerItem[] newArray(int size) {
            return new BuyerItem[size];
        }
    };

    protected BuyerItem(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        if (in.readByte() == 0) {
            userId = null;
        } else {
            userId = in.readInt();
        }
        name = in.readString();
        phone = in.readString();
        createdAt = in.readString();
        deletedAt = in.readString();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
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
        if (userId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(userId);
        }
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(createdAt);
        dest.writeString(deletedAt);
    }
}
