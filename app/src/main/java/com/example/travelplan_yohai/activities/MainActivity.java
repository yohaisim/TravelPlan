package com.example.travelplan_yohai.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.bumptech.glide.Glide;
import com.example.travelplan_yohai.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.firebase.ui.auth.AuthUI;

public class MainActivity extends AppCompatActivity {

    private AppCompatImageView main_IMG_image;
    private AppCompatTextView main_LBL_name;
    private AppCompatTextView main_LBL_email;
    private AppCompatTextView main_LBL_phone;
    private Button logoutButton;
    private Button myDestinationsButton; // הוספת כפתור עבור My Destinations

    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        findViews();
        checkUserStatus();

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        myDestinationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMyDestinations(); // הוספת מאזין לחיצה לכפתור
            }
        });
    }

    private void findViews() {
        main_IMG_image = findViewById(R.id.main_IMG_image);
        main_LBL_name = findViewById(R.id.main_LBL_name);
        main_LBL_email = findViewById(R.id.main_LBL_email);
        main_LBL_phone = findViewById(R.id.main_LBL_phone);
        logoutButton = findViewById(R.id.logout);
        myDestinationsButton = findViewById(R.id.button_my_destinations); // איתור הכפתור עבור My Destinations
    }

    private void checkUserStatus() {
        if (user == null) {
            redirectToLogin();
        } else {
            initViews();
        }
    }

    private void initViews() {
        Glide.with(this)
                .load(user.getPhotoUrl())
                .centerCrop()
                .placeholder(R.drawable.logo1)
                .into(main_IMG_image);
        main_LBL_name.setText(user.getDisplayName());
        main_LBL_email.setText(user.getEmail());
        main_LBL_phone.setText(user.getPhoneNumber());
    }

    private void signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        redirectToLogin();
                    }
                });
    }

    private void redirectToLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void openMyDestinations() {
        Intent intent = new Intent(MainActivity.this, MyCitiesActivity.class);
        startActivity(intent);
    }
}
