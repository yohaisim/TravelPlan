package com.example.travelplan_yohai.firestore;

import com.example.travelplan_yohai.model.Destination;

import java.util.List;

public interface FetchListener<T> {
    void onSuccess(T destinations);

    void onLoading();

    void onError(Exception e);
}