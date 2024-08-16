package com.example.travelplan_yohai.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelplan_yohai.R;
import com.example.travelplan_yohai.firestore.CityFirebaseHelper;
import com.example.travelplan_yohai.firestore.FetchListener;
import com.example.travelplan_yohai.model.Destination;
import com.example.travelplan_yohai.recycler.DestinationAdapter;

import java.util.List;

public class MyCitiesActivity extends AppCompatActivity implements DestinationAdapter.OnDestinationAddClickListener {

    public  static final String DESTINATION_INTENT_KEY = "destination_key";
    ProgressBar progressBar;
    RecyclerView recyclerView;
    private DestinationAdapter destinationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_destinations);
        initViews();
    }


    @Override
    protected void onResume() {
        super.onResume();
        CityFirebaseHelper.fetchUserFavoriteDestination(new FetchListener<List<Destination>>() {
            @Override
            public void onSuccess(List<Destination> destinations) {
                destinationAdapter.setList(destinations);
            }

            @Override
            public void onLoading() {

            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    private void initViews() {
        progressBar = findViewById(R.id.progress_bar);
        recyclerView = findViewById(R.id.recyler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        destinationAdapter = new DestinationAdapter(this, this, false);
        recyclerView.setAdapter(destinationAdapter);
        findViewById(R.id.add_destination_button).setOnClickListener(v -> {
            Intent intent = new Intent(MyCitiesActivity.this, ChooseCityActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onPlusButtonClick(Destination destination) {
        //Not action required
    }

    @Override
    public void onImageClick(Destination destination) {
        Intent intent = new Intent(MyCitiesActivity.this, MyAttractionsActivity.class);
        intent.putExtra(DESTINATION_INTENT_KEY, destination);
        startActivity(intent);
    }
}
