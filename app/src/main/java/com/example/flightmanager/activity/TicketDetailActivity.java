package com.example.flightmanager.activity;

import static com.example.flightmanager.lib.Utils.concatenate;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.flightmanager.databinding.ActivityTicketDetailBinding;
import com.example.flightmanager.model.Flight;

public class TicketDetailActivity extends BaseActivity {
    private ActivityTicketDetailBinding binding;
    private Flight flight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTicketDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getIntentExtras();
        setVariables();
    }

    private void setVariables() {
        binding.backButtonTicket.setOnClickListener(v -> finish());

        // fromText or fromBigText
        binding.fromBigText.setText(flight.getFromShort());
        binding.fromTextDescription.setText(flight.getFrom());

        // toText or toBigText
        binding.toBigText.setText(flight.getToShort());
        //binding.toText.setText(flight.getTo());
        binding.toTextDescription.setText(flight.getTo());

        // dateText or dateBigText
        binding.dateTextDescription.setText(flight.getDate());

        // timeText or timeBigText
        binding.timeTextDescription.setText(flight.getTime());

        // arrival
        binding.arrivalText.setText(flight.getArriveTime());

        // seat class
        binding.flightClassTextDescription.setText(flight.getClassSeat());

        // price
        binding.priceTextDescription.setText(concatenate("EUR ", String.valueOf(flight.getPrice())));

        // airline detail
        binding.airlineTextDesciption.setText(flight.getAirlineName());

        // seat numbers
        binding.seatsTextDescription.setText(flight.getPassenger());

        Glide.with(TicketDetailActivity.this)
                .load(flight.getAirlineLogo())
                .into(binding.logoImage);
    }

    private void getIntentExtras() {
        flight = (Flight) getIntent().getSerializableExtra("flight");
    }
}