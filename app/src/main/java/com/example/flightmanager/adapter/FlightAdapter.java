package com.example.flightmanager.adapter;

import static android.text.TextUtils.concat;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flightmanager.activity.SeatListActivity;
import com.example.flightmanager.databinding.ViewholderFlightsBinding;
import com.example.flightmanager.model.Flight;

import java.util.ArrayList;

public class FlightAdapter extends RecyclerView.Adapter<FlightAdapter.Viewholder> {

    private final ArrayList<Flight> flightList;
    private Context context;

    public FlightAdapter(ArrayList<Flight> flightList) {
        this.flightList = flightList;
    }

    @NonNull
    @Override
    public FlightAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        ViewholderFlightsBinding binding = ViewholderFlightsBinding.inflate(
            LayoutInflater.from(context), parent, false
        );

        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FlightAdapter.Viewholder holder, int position) {
        Flight flight = flightList.get(position);
        Glide.with(context).load(flight.getAirlineLogo()).into(holder.binding.logoImage);

        holder.binding.fromText.setText(flight.getFrom());
        holder.binding.fromBigText.setText(flight.getFromShort());

        holder.binding.toText.setText(flight.getTo());
        holder.binding.toBigText.setText(flight.getToShort());

        holder.binding.timeText.setText(flight.getArriveTime());
        holder.binding.seatClassText.setText(flight.getClassSeat());

        holder.binding.priceText.setText(concat("EUR ", String.valueOf(flight.getPrice())));

        // Add an OnClickListener to the itemView
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SeatListActivity.class);
            intent.putExtra("flight", flight);

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return flightList.size();
    }

    public static class Viewholder extends RecyclerView.ViewHolder {
        private final ViewholderFlightsBinding binding;

        public Viewholder(@NonNull ViewholderFlightsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
