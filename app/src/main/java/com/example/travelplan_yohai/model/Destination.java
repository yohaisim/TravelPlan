package com.example.travelplan_yohai.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.travelplan_yohai.firestore.DBConstants;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Destination implements Parcelable {
    private String documentId;
    private String name;
    private String imageUrl;
    private List<Map<String, String>> attractions;
    private List<Integer> myFavoriteAttractions;

    public List<Attraction> attractionList;

    // Default constructor required for Firestore deserialization
    public Destination() {
    }

    protected Destination(Parcel in) {
        documentId = in.readString();
        name = in.readString();
        imageUrl = in.readString();

        // Read the list of maps
        int attractionsSize = in.readInt();
        attractions = new ArrayList<>(attractionsSize);
        for (int i = 0; i < attractionsSize; i++) {
            Map<String, String> map = new HashMap<>();
            in.readMap(map, String.class.getClassLoader());
            attractions.add(map);
        }

        // Read the list of integers
        myFavoriteAttractions = new ArrayList<>();
        in.readList(myFavoriteAttractions, Integer.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(documentId);
        dest.writeString(name);
        dest.writeString(imageUrl);

        // Write the list of maps
        if (attractions != null) {
            dest.writeInt(attractions.size());
            for (Map<String, String> map : attractions) {
                dest.writeMap(map);
            }
        } else {
            dest.writeInt(0);
        }

        // Write the list of integers
        dest.writeList(myFavoriteAttractions);
    }

    public static final Parcelable.Creator<Destination> CREATOR = new Parcelable.Creator<Destination>() {
        @Override
        public Destination createFromParcel(Parcel in) {
            return new Destination(in);
        }

        @Override
        public Destination[] newArray(int size) {
            return new Destination[size];
        }
    };

    public List<Integer> getAllAttractionsId() {
        List<Integer> allIds = new ArrayList<>();
        for (Map<String, String> element : attractions) {
            try {
                allIds.add(Integer.parseInt(element.getOrDefault(DBConstants.MY_DESTINATION_TABLE.ATTRACTION_ID, "")));
            } catch (Exception e) {

            }
        }
        return allIds;

    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public Destination(String name, String imageUrl, String imageName, List<Map<String, String>> attractions) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.attractions = attractions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<Map<String, String>> getAttractions() {
        return attractions;
    }

    public void setAttractions(List<Map<String, String>> attractions) {
        this.attractions = attractions;
    }

    public List<Integer> getMyFavoriteAttractions() {
        return myFavoriteAttractions;
    }

    public void setMyFavoriteAttractions(List<Integer> myFavoriteAttractions) {
        this.myFavoriteAttractions = myFavoriteAttractions;
    }

    public void parseAttractions() {
        List<Attraction> list = new ArrayList<>();
        for (Map<String, String> elemnt : attractions) {
            String id = elemnt.getOrDefault(DBConstants.MY_DESTINATION_TABLE.ATTRACTION_ID, "");
            String imageUrl = elemnt.getOrDefault(DBConstants.MY_DESTINATION_TABLE.IMAGE_URL, "");
            String description = elemnt.getOrDefault(DBConstants.MY_DESTINATION_TABLE.DESCRIPTION, "");
            String location = elemnt.getOrDefault(DBConstants.MY_DESTINATION_TABLE.LOCATION, "");
            String name = elemnt.getOrDefault(DBConstants.MY_DESTINATION_TABLE.NAME, "");
            Attraction attraction = new Attraction(this.getDocumentId(), description, Integer.parseInt(id), imageUrl, location, name);
            list.add(attraction);
        }

        attractionList = list;
    }

}