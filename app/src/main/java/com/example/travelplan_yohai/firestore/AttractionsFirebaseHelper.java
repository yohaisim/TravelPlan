package com.example.travelplan_yohai.firestore;

import com.example.travelplan_yohai.model.Attraction;
import com.example.travelplan_yohai.model.Destination;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class AttractionsFirebaseHelper {

    // Fetch all attractions across all destinations
    public static void fetchAllUserAttraction(String cityId, FetchListener<Destination> listener) {
        listener.onLoading();
        String userUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseFirestore.getInstance().collection(DBConstants.TABLE_NAME.USER_DESTIONATIONS).document(userUid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                Map<String, List<Long>> myFavoriteAttractions = (Map<String, List<Long>>) documentSnapshot.get(DBConstants.MY_DESTINATION_TABLE.MY_ATTRACTIONS);
                                List<Long> myFavoriteIds = myFavoriteAttractions.getOrDefault(cityId, new ArrayList<>());
                                List<Integer> integerList = myFavoriteIds.stream()
                                        .map(Long::intValue) // Convert each Long to Integer
                                        .collect(Collectors.toList());
                                firestore.collection(DBConstants.TABLE_NAME.DESTANATIONS).document(cityId)
                                        .get()
                                        .addOnSuccessListener(document1 -> {
                                            Destination destination = document1.toObject(Destination.class);
                                            destination.setDocumentId(document1.getId());
                                            destination.parseAttractions();
                                            destination.setMyFavoriteAttractions(integerList);
                                            listener.onSuccess(destination);
                                        })
                                        .addOnFailureListener(e -> {
                                            // Handle the error (e.g., show a toast)
                                        });
                            }
                        }
                );


    }

    public static void fetchAllAttractions(String cityId, FetchListener<List<Attraction>> listener) {
        listener.onLoading();
        FirebaseFirestore.getInstance().collection(DBConstants.TABLE_NAME.DESTANATIONS).document(cityId)
                .get()
                .addOnSuccessListener(document1 -> {
                    Destination destination = document1.toObject(Destination.class);
                    destination.setDocumentId(document1.getId());
                    destination.parseAttractions();
                    listener.onSuccess(destination.attractionList);
                })
                .addOnFailureListener(e -> {
                    // Handle the error (e.g., show a toast)
                });


    }

    public static void addUserFavoriteAttraction(String cityId, Integer attractionNumber, FetchListener<Boolean> fetchListener) {
        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        List<Integer> temp = new ArrayList<Integer>();
        temp.add(attractionNumber);
        // Wrap the map in another map to place it under the "my_attractions" field
          Map<String, Object> myAttractionsMap = new HashMap<>();
        myAttractionsMap.put(cityId, temp);

//        // Wrap the map in another map to place it under the "my_attractions" field
//        Map<String, Object> updates = new HashMap<>();
//        updates.put(DBConstants.MY_DESTINATION_TABLE.MY_ATTRACTIONS, myAttractionsMap);
//        FirebaseFirestore.getInstance().collection(DBConstants.TABLE_NAME.USER_DESTIONATIONS).document(userUid)
//                .set(updates, SetOptions.merge())
//                .addOnSuccessListener(aVoid -> {
//                    fetchListener.onSuccess(true);
//                })
//                .addOnFailureListener(fetchListener::onError);

        DocumentReference userDocRef = FirebaseFirestore.getInstance()
                .collection(DBConstants.TABLE_NAME.USER_DESTIONATIONS)
                .document(userUid);

        // Update the map field using the arrayUnion method
        userDocRef.update(
                "my_attractions." + cityId, FieldValue.arrayUnion(attractionNumber)
        ).addOnSuccessListener(aVoid -> {
            // Notify success
            fetchListener.onSuccess(true);
        }).addOnFailureListener(fetchListener::onError);
    }
}


