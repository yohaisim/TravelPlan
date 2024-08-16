package com.example.travelplan_yohai.activities;

import static com.example.travelplan_yohai.activities.AllAttractionsActivity.ATTRACTION_KEY;
import static com.example.travelplan_yohai.activities.MyCitiesActivity.DESTINATION_INTENT_KEY;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelplan_yohai.R;
import com.example.travelplan_yohai.firestore.AttractionsFirebaseHelper;
import com.example.travelplan_yohai.firestore.FetchListener;
import com.example.travelplan_yohai.model.Attraction;
import com.example.travelplan_yohai.model.Destination;
import com.example.travelplan_yohai.recycler.AttractionAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyAttractionsActivity extends AppCompatActivity implements AttractionAdapter.OnAttractionClickListener {

    private AttractionAdapter attractionAdapter;

    private Destination destination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_attractions);
        initViews();
        destination = getIntent().getParcelableExtra(DESTINATION_INTENT_KEY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fecthUserLikeAttractions();
    }


    private void initViews() {
        RecyclerView rvMyAttractions = findViewById(R.id.rv_my_attractions);
        attractionAdapter = new AttractionAdapter(this,this);
        rvMyAttractions.setLayoutManager(new LinearLayoutManager(this));
        rvMyAttractions.setAdapter(attractionAdapter);
        findViewById(R.id.btn_add_attraction).setOnClickListener(v -> {
            Intent intent = new Intent(MyAttractionsActivity.this, AllAttractionsActivity.class);
            intent.putExtra(DESTINATION_INTENT_KEY, destination);
            startActivity(intent);
        });
    }


    private void fecthUserLikeAttractions() {
        AttractionsFirebaseHelper.fetchAllUserAttraction(destination.getDocumentId(), new FetchListener<Destination>() {
            @Override
            public void onSuccess(Destination destinations) {
                List<Attraction> favoriteAttractions = new ArrayList<>();

                for (Attraction attraction : destinations.attractionList) {
                    if (destinations.getMyFavoriteAttractions().contains(attraction.getId())) {
                        favoriteAttractions.add(attraction);
                    }
                }
                attractionAdapter.setAttractions(favoriteAttractions);
            }

            @Override
            public void onLoading() {

            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    @Override
    public void onAttractionClicked(Attraction attraction) {
        Intent intent = new Intent(MyAttractionsActivity.this, AttractionInfoActivity.class);
        intent.putExtra(ATTRACTION_KEY, attraction);
        startActivity(intent);
    }
}
