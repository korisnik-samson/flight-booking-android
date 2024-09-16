package com.example.flightmanager.activity;

import static com.example.flightmanager.lib.Utils.concatenate;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.flightmanager.R;
import com.example.flightmanager.databinding.ActivityMainBinding;
import com.example.flightmanager.model.Location;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends BaseActivity {
    private ActivityMainBinding binding;
    private int adultPassengerCount = 1;
    private int childPassengerCount = 0;

    // Locale.getDefault() or Locale.ENGLISH
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM, yyyy", Locale.getDefault());
    private final Calendar finalcalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FirebaseApp.initializeApp(MainActivity.this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLocations();
        initPassengers();
        initPassengerClass();
        initDatePickup();
        setVariable();
    }

    private void setVariable() {
        binding.searchButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);

            intent.putExtra("from", ((Location) binding.fromSpinner.getSelectedItem()).getName());
            intent.putExtra("to", ((Location) binding.toSpinner.getSelectedItem()).getName());
            intent.putExtra("date", binding.departureDateText.getText().toString());
            intent.putExtra("numPassenger", adultPassengerCount + childPassengerCount);

            startActivity(intent);
        });
    }

    private void initDatePickup() {
        Calendar calendarToday = Calendar.getInstance();
        String currentDate = dateFormat.format(calendarToday.getTime());
        binding.departureDateText.setText(currentDate);

        Calendar calendarTomorrow = Calendar.getInstance(); // get current instance of calendar
        calendarTomorrow.add(Calendar.DAY_OF_YEAR, 1); // add one day to the current date

        String tomorrowDate = dateFormat.format(calendarTomorrow.getTime());
        binding.arrivalText.setText(tomorrowDate);

        binding.departureDateText.setOnClickListener(view -> showDatePickerDialog(binding.departureDateText));
        binding.arrivalText.setOnClickListener(view -> showDatePickerDialog(binding.arrivalText));
    }

    private void initPassengerClass() {
        binding.progressBarClass.setVisibility(View.VISIBLE);
        ArrayList<String> passengerClassList = new ArrayList<>();

        passengerClassList.add("Economy Class");
        passengerClassList.add("Business Class");
        passengerClassList.add("First Class");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                MainActivity.this,
                R.layout.sp_item,
                passengerClassList
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.classSpinner.setAdapter(adapter);
        binding.progressBarClass.setVisibility(View.GONE);
    }

    private void initPassengers() {
        // Increase adult and child passengers
        binding.increaseAdultPassenger.setOnClickListener(view -> {
            adultPassengerCount++;
            binding.adultText.setText(concatenate(String.valueOf(adultPassengerCount), " Adult"));
        });

        // Decrease adult passengers
        binding.decreaseAdultPassenger.setOnClickListener(view -> {
            // da budem siguran da ne moze da se smanji na 0
            if (adultPassengerCount > 1) {
                adultPassengerCount--;
                binding.adultText.setText(concatenate(String.valueOf(adultPassengerCount), " Adult"));
            }
        });

        // Increase child passengers
        binding.increaseChildPassenger.setOnClickListener(view -> {
            childPassengerCount++;
            binding.childText.setText(concatenate(String.valueOf(childPassengerCount), " Child"));
        });

        // Decrease child passengers
        binding.decreaseChildPassenger.setOnClickListener(view -> {
            // ponovo da budem siguran
            if (childPassengerCount > 1) {
                childPassengerCount--;
                binding.childText.setText(concatenate(String.valueOf(childPassengerCount), " Child"));
            }
        });
    }

    private void initLocations() {
        // Show progress bar while loading data
        binding.progressBarFrom.setVisibility(View.VISIBLE);
        binding.progressBarTo.setVisibility(View.VISIBLE);

        DatabaseReference dbReference = database.getReference("Locations");
        ArrayList<Location> locationList = new ArrayList<>();

        dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    for (DataSnapshot data : snapshot.getChildren()) {
                        Log.d("MainActivity", "Child data: " + data.getValue());
                        locationList.add(data.getValue(Location.class));
                    }

                    ArrayAdapter<Location> adapter = new ArrayAdapter<>(
                            MainActivity.this,
                            R.layout.sp_item,
                            locationList
                    );

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    binding.fromSpinner.setAdapter(adapter);
                    binding.toSpinner.setAdapter(adapter);
                    binding.fromSpinner.setSelection(1);

                    // debugging purposes
                    Log.d("MainActivity", "Location list: " + locationList);

                    // Hide progress bar after loading data
                    binding.progressBarFrom.setVisibility(View.GONE);
                    binding.progressBarTo.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Getting Post failed, log a message
                // no logic for now
                Log.w("MainActivity", "Failed to read value.", error.toException());
            }
        });
    }

    private void showDatePickerDialog(TextView textView) {
        int year = finalcalendar.get(Calendar.YEAR);
        int month = finalcalendar.get(Calendar.MONTH);
        int day = finalcalendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                MainActivity.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    finalcalendar.set(selectedYear, selectedMonth, selectedDay);
                    textView.setText(dateFormat.format(finalcalendar.getTime()));
                },
                year, month, day
        );

        datePickerDialog.show();
    }
}