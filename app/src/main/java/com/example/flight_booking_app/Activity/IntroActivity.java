package com.example.flight_booking_app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;

import com.example.flight_booking_app.databinding.ActivityIntroBinding;

public class IntroActivity extends BaseActivity {

private ActivityIntroBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding= ActivityIntroBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        binding.registerDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IntroActivity.this, RegistrationActivity.class));

            }
        });

        binding.loginDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IntroActivity.this, LoginActivity.class));
            }
        });


    }
}