package com.example.travelplan_yohai.firestore;

import com.example.travelplan_yohai.model.Destination;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CityFirebaseHelper {

    public static void fetchDestinations(FetchListener<List<Destination>> listener) {
        listener.onLoading();
        FirebaseFirestore.getInstance().collection(DBConstants.TABLE_NAME.DESTANATIONS).addSnapshotListener((value, error) -> {
            if (error != null) {
                listener.onError(error);
                return;
            }
            if (value != null) {
                List<Destination> destinations = new ArrayList<>();
                for (DocumentSnapshot document : value.getDocuments()) {
                    Destination destination = document.toObject(Destination.class);
                    destination.setDocumentId(document.getId());
                    destinations.add(destination);
                }
                listener.onSuccess(destinations);
            }
        });
    }

    public static void fetchUserFavoriteDestination(FetchListener<List<Destination>> listener) {
        listener.onLoading();
        String userUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        FirebaseFirestore firestore =  FirebaseFirestore.getInstance();
        FirebaseFirestore.getInstance().collection(DBConstants.TABLE_NAME.USER_DESTIONATIONS).document(userUid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Map<String, List<Integer>> myAttractions = (Map<String, List<Integer>>) documentSnapshot.get(DBConstants.MY_DESTINATION_TABLE.MY_ATTRACTIONS);
                        if (myAttractions != null) {
                            List<String> destinationIds = new ArrayList<>(myAttractions.keySet());
                            firestore.collection(DBConstants.TABLE_NAME.DESTANATIONS)
                                    .whereIn(FieldPath.documentId(), destinationIds)
                                    .get()
                                    .addOnSuccessListener(queryDocumentSnapshots -> {
                                        List<Destination> likedDestinations = new ArrayList<>();
                                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                                            Destination destination = document.toObject(Destination.class);
                                            destination.setDocumentId(document.getId());
                                            List<Integer> attracitionFavorideIds = myAttractions.getOrDefault(destination.getDocumentId(),new ArrayList<>());
                                            destination.setMyFavoriteAttractions(attracitionFavorideIds);
                                            if (destination != null) {
                                                likedDestinations.add(destination);
                                            }
                                        }
                                        listener.onSuccess(likedDestinations);
                                    })
                                    .addOnFailureListener(e -> {
                                        // Handle the error (e.g., show a toast)
                                    });
                        } else {
                            // Handle the case where my_attractions is empty or null
                        }
                    } else {
                        // Handle the case where the document does not exist
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle the error (e.g., show a toast)
                });
    }
    public static void addUserFavoriteDestination(Destination destination, FetchListener<Boolean> fetchListener) {
        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Map<String, Object> myAttractionsMap = new HashMap<>();
        myAttractionsMap.put(destination.getDocumentId(), new ArrayList<Integer>());

        // Wrap the map in another map to place it under the "my_attractions" field
        Map<String, Object> updates = new HashMap<>();
        updates.put(DBConstants.MY_DESTINATION_TABLE.MY_ATTRACTIONS, myAttractionsMap);
        FirebaseFirestore.getInstance().collection(DBConstants.TABLE_NAME.USER_DESTIONATIONS).document(userUid)
                .set(updates, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    fetchListener.onSuccess(true);
                })
                .addOnFailureListener(fetchListener::onError);
    }
}
