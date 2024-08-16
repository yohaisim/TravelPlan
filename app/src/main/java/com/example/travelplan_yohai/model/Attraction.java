package com.example.travelplan_yohai.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Attraction implements Parcelable {
    private String description;
    private Integer id;
    private String imageUrl;
    private String location;
    private String name;
    private String cityId;

    public Attraction() {
    }

    public Attraction(String cityId, String description, Integer id, String imageUrl, String location, String name) {
        this.description = description;
        this.id = id;
        this.imageUrl = imageUrl;
        this.location = location;
        this.name = name;
        this.cityId = cityId;
    }

    public static final Creator<Attraction> CREATOR = new Creator<Attraction>() {
        @Override
        public Attraction createFromParcel(Parcel in) {
            return new Attraction(in);
        }

        @Override
        public Attraction[] newArray(int size) {
            return new Attraction[size];
        }
    };

    protected Attraction(Parcel in) {
        description = in.readString();
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        imageUrl = in.readString();
        location = in.readString();
        name = in.readString();
        cityId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(description);
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(id);
        }
        parcel.writeString(imageUrl);
        parcel.writeString(location);
        parcel.writeString(name);
        parcel.writeString(cityId);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }
}
