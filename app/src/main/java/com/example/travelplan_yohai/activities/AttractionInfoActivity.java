
package com.example.travelplan_yohai.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.travelplan_yohai.R;
import com.example.travelplan_yohai.firestore.AttractionsFirebaseHelper;
import com.example.travelplan_yohai.firestore.FetchListener;
import com.example.travelplan_yohai.model.Attraction;

public class AttractionInfoActivity extends AppCompatActivity {

    TextView description;
    private Attraction attraction;
    private boolean isFromAllAttractions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attraction_info);

        // Retrieve the Attraction object from the Intent
        attraction = (Attraction) getIntent().getParcelableExtra(AllAttractionsActivity.ATTRACTION_KEY);
        isFromAllAttractions = getIntent().getBooleanExtra("is_from_all_attractions", false);

        initViews();

        if (savedInstanceState == null) {
            String rawLocation = attraction.getLocation();
            Log.d("AttractionInfoActivity", "Raw location: " + rawLocation);
            String processedLocation = processLocation(rawLocation);
            Log.d("AttractionInfoActivity", "Processed location: " + processedLocation);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.map_container, MapFragment.newInstance(processedLocation))
                    .commit();
        }
    }
    private String processLocation(String rawLocation) {
        if (rawLocation == null || rawLocation.trim().isEmpty()) {
            // Return a default location if the rawLocation is null or empty
            return "48.8673893,2.7810181"; // Default to Disneyland Paris coordinates
        }
        // Remove any potential whitespace
        return rawLocation.trim();
    }

    private void initViews() {
        description = findViewById(R.id.description_text);
        description.setText(attraction.getDescription());
        Button addAttraction = findViewById(R.id.btn_add_attraction);
        if (isFromAllAttractions) {
            addAttraction.setVisibility(View.VISIBLE);
            addAttraction.setOnClickListener(v -> addUserAttraction());
        } else {
            addAttraction.setVisibility(View.GONE);
        }
    }

    private void addUserAttraction() {
        AttractionsFirebaseHelper.addUserFavoriteAttraction(
                attraction.getCityId(),
                attraction.getId(),
                new FetchListener<Boolean>() {
                    @Override
                    public void onSuccess(Boolean result) {
                        Toast.makeText(AttractionInfoActivity.this, "Attraction added", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onLoading() {
                        // Optionally handle loading state
                    }

                    @Override
                    public void onError(Exception e) {
                        // Handle error here
                    }
                }
        );
    }
}


