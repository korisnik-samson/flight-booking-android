package com.example.flightmanager.activity;

import static com.example.flightmanager.lib.Utils.concatenate;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;

import com.example.flightmanager.adapter.SeatAdapter;
import com.example.flightmanager.databinding.ActivitySeatListBinding;
import com.example.flightmanager.model.Flight;
import com.example.flightmanager.model.Seat;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SeatListActivity extends BaseActivity {
    private ActivitySeatListBinding binding;
    private Flight flight;
    private Double price = 0.00;
    private int seatCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySeatListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getIntentExtras();
        initSeatList();
        setVariables();
    }

    private void setVariables() {
        binding.backButton.setOnClickListener(v -> finish());

        binding.confirmSeatsButton.setOnClickListener(v -> {
            if (seatCount > 0) {
                flight.setPassenger(binding.nameSeatSelectedText.getText().toString());
                flight.setPrice(price);

                Intent newIntent = new Intent(SeatListActivity.this, TicketDetailActivity.class);
                newIntent.putExtra("flight", flight);
                startActivity(newIntent);

            } else Toast.makeText(SeatListActivity.this,
                    "Please select your seat", Toast.LENGTH_SHORT).show();
        });
    }

    private void initSeatList() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 7);

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                // 3rd item will take 1 span size
                // or just 1 maybe, since we return 1 anyways
                return (position % 7 == 3) ? 1 : 1;
            }
        });

        binding.seatRecyclerView.setLayoutManager(gridLayoutManager);
        List<Seat> seatList = new ArrayList<>();

        int row = 0;
        int numberSeat = flight.getNumberSeat() + (flight.getNumberSeat() / 7) + 1;

        Map<Integer, String> seatAlphabetMap = new HashMap<>();

        // also be aware of the missing "3" in the map because it's considered as an in-between the isles
        seatAlphabetMap.put(0, "A");
        seatAlphabetMap.put(1, "B");
        seatAlphabetMap.put(2, "C");
        seatAlphabetMap.put(4, "D");
        seatAlphabetMap.put(5, "E");
        seatAlphabetMap.put(6, "F");

        for (int i = 0; i < numberSeat; i++) {
            if (i % 7 == 0) row++;

            if (i % 7 == 3) seatList.add(new Seat(Seat.SeatStatus.EMPTY, String.valueOf(row)));
            else {
                String seatName = seatAlphabetMap.get(i % 7) + row;
                Seat.SeatStatus seatStatus = flight.getReservedSeats()
                        .contains(seatName) ? Seat.SeatStatus.UNAVAILABLE : Seat.SeatStatus.AVAILABLE;
                seatList.add(new Seat(seatStatus, seatName));
            }
        }

        SeatAdapter seatAdapter = new SeatAdapter(seatList, this, (selectedName, num) -> {
            // or use your own concatenate() method
            binding.selectedSeatCount.setText(concatenate(String.valueOf(num), " selected seats"));
            binding.nameSeatSelectedText.setText(selectedName);

            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            price = (Double.valueOf(decimalFormat.format(num * flight.getPrice())));

            this.seatCount = num;
            binding.seatPriceText.setText(concatenate("Total: EUR ", String.valueOf(price)));
        });

        binding.seatRecyclerView.setAdapter(seatAdapter);
        binding.seatRecyclerView.setNestedScrollingEnabled(false);
    }

    private void getIntentExtras() {
        flight = (Flight) getIntent().getSerializableExtra("flight");
    }
}