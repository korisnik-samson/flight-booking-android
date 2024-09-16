package com.example.flightmanager.activity;

import android.content.Intent;
import android.os.Bundle;

import com.example.flightmanager.databinding.ActivityIntroBinding;

public class IntroActivity extends BaseActivity {
    private ActivityIntroBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIntroBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        // could override the onClickListener for the button here or use a lambda expression
        binding.getStartedButton.setOnClickListener(view -> {
            startActivity(new Intent(IntroActivity.this, MainActivity.class));
        });
    }
}