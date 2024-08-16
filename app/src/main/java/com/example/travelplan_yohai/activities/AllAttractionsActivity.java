package com.example.travelplan_yohai.activities;

import static com.example.travelplan_yohai.activities.MyCitiesActivity.DESTINATION_INTENT_KEY;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelplan_yohai.R;
import com.example.travelplan_yohai.firestore.AttractionsFirebaseHelper;
import com.example.travelplan_yohai.firestore.FetchListener;
import com.example.travelplan_yohai.model.Attraction;
import com.example.travelplan_yohai.model.Destination;
import com.example.travelplan_yohai.recycler.AttractionAdapter;

import java.util.List;

public class AllAttractionsActivity extends AppCompatActivity implements AttractionAdapter.OnAttractionClickListener {
    public static final String ATTRACTION_KEY = "attraction_key";
    private AttractionAdapter attractionAdapter;
    private Destination destination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_attraction);
        destination = getIntent().getParcelableExtra(DESTINATION_INTENT_KEY);

        initViews();
        fetchAllUserAttractions();
    }


    private void initViews() {
        RecyclerView rvMyAttractions = findViewById(R.id.rv_my_attractions);
        attractionAdapter = new AttractionAdapter(this, this);
        rvMyAttractions.setLayoutManager(new LinearLayoutManager(this));
        rvMyAttractions.setAdapter(attractionAdapter);
    }


    private void fetchAllUserAttractions() {
        AttractionsFirebaseHelper.fetchAllAttractions(destination.getDocumentId(), new FetchListener<List<Attraction>>() {
            @Override
            public void onSuccess(List<Attraction> attractions) {
                attractionAdapter.setAttractions(attractions);
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
        Intent intent = new Intent(AllAttractionsActivity.this, AttractionInfoActivity.class);
        intent.putExtra(ATTRACTION_KEY, attraction);
        intent.putExtra("is_from_all_attractions",true);
        startActivity(intent);
    }
}
