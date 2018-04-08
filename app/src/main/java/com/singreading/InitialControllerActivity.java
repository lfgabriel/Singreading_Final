package com.singreading;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class InitialControllerActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_controller);

        mAuth = FirebaseAuth.getInstance();
        Log.e("Controller", "Init");

        Intent nextActivity;
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            Log.e("Controller", "User: " + currentUser.getEmail() + ". Go to main");
            nextActivity = new Intent(this, MainActivity.class);
        }
        else {
            Log.e("Controller", "Go to login");
            nextActivity = new Intent(this, LoginActivity.class);
        }
        startActivity(nextActivity);
    }
}
