package com.example.flightmanager.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.flightmanager.adapter.FlightAdapter;
import com.example.flightmanager.databinding.ActivitySearchBinding;
import com.example.flightmanager.model.Flight;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends BaseActivity {
    private ActivitySearchBinding binding;
    private String from;
    private String to;
    private String date;
    private int passengerCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getIntentExtras();
        initList();
        setVariable();
    }

    private void setVariable() {
        binding.backButton.setOnClickListener(view -> finish());
    }

    private void initList() {
        DatabaseReference reference = database.getReference("Flights");
        ArrayList<Flight> flightList = new ArrayList<>();

        Query query = reference.orderByChild("from").equalTo(from);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        Flight flight = data.getValue(Flight.class);

                        assert flight != null;
                        if (flight.getTo().equals(to)) flightList.add(flight);

                        // in cases of filtering date, uncomment this and vice versa for the above line
                        //if (flight.getTo().equals(to) && flight.getDate().equals(date)) flightList.add(flight);

                        if (!flightList.isEmpty()) {
                            binding.searchRecyclerView.setLayoutManager(
                                new LinearLayoutManager(
                                        SearchActivity.this,
                                        LinearLayoutManager.VERTICAL, false
                                )
                            );

                            binding.searchRecyclerView.setAdapter(new FlightAdapter(flightList));
                        }

                        binding.searchProgressBar.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("SearchActivity", "onCancelled: " + error.getMessage());
            }
        });
    }

    private void getIntentExtras() {
        from = getIntent().getStringExtra("from");
        to = getIntent().getStringExtra("to");
        date = getIntent().getStringExtra("date");
    }
}