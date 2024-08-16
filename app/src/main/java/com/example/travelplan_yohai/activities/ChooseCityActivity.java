package com.example.travelplan_yohai.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelplan_yohai.R;
import com.example.travelplan_yohai.firestore.CityFirebaseHelper;
import com.example.travelplan_yohai.firestore.FetchListener;
import com.example.travelplan_yohai.model.Destination;
import com.example.travelplan_yohai.recycler.DestinationAdapter;

import java.util.List;

public class ChooseCityActivity extends AppCompatActivity implements DestinationAdapter.OnDestinationAddClickListener {
    ProgressBar progressBar;
    RecyclerView recyclerView;
    private DestinationAdapter destinationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_destinations);
        initViews();
        fetchData();
    }

    private void initViews() {
        progressBar = findViewById(R.id.progress_bar);
        recyclerView = findViewById(R.id.recyler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        destinationAdapter = new DestinationAdapter(this, this,true);
        recyclerView.setAdapter(destinationAdapter);
    }

    private void fetchData() {
        CityFirebaseHelper.fetchDestinations(new FetchListener<List<Destination>>() {
            @Override
            public void onSuccess(List<Destination> destinations) {
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                destinationAdapter.setList(destinations);
            }

            @Override
            public void onLoading() {
                progressBar.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                onErrorMsg(e.getLocalizedMessage());
            }
        });
    }

    private void onErrorMsg(String msg) {
        Toast.makeText(ChooseCityActivity.this, "Error :" + msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onPlusButtonClick(Destination destination) {
        CityFirebaseHelper.addUserFavoriteDestination(destination, new FetchListener<Boolean>() {
            @Override
            public void onSuccess(Boolean destinations) {
                Toast.makeText(ChooseCityActivity.this, "Item Added", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onLoading() {

            }

            @Override
            public void onError(Exception e) {
                onErrorMsg(e.getLocalizedMessage());
            }
        });
    }

    @Override
    public void onImageClick(Destination destination) {
        //No Action is required
    }
}
