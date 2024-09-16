package com.example.flightmanager.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flightmanager.R;
import com.example.flightmanager.databinding.SeatItemBinding;
import com.example.flightmanager.model.Seat;

import java.util.ArrayList;
import java.util.List;

public class SeatAdapter extends RecyclerView.Adapter<SeatAdapter.SeatViewholder> {
    private final List<Seat> seatList;
    private final Context context;
    private ArrayList<String> selectedSeatName = new ArrayList<>();
    private SelectedSeat selectedSeat;

    public SeatAdapter(List<Seat> seatList, Context context, SelectedSeat selectedSeat) {
        this.seatList = seatList;
        this.context = context;
        this.selectedSeat = selectedSeat;
    }

    @NonNull
    @Override
    public SeatAdapter.SeatViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SeatItemBinding binding = SeatItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SeatViewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SeatAdapter.SeatViewholder holder, int position) {
        Seat seat = seatList.get(position);
        holder.binding.seatImageView.setText(seat.getName());

        switch (seat.getStatus()) {
            case AVAILABLE:
                holder.binding.seatImageView.setBackgroundResource(R.drawable.ic_seat_available);
                holder.binding.seatImageView.setTextColor(context.getResources().getColor(R.color.white));
                break;

            case SELECTED:
                holder.binding.seatImageView.setBackgroundResource(R.drawable.ic_seat_selected);
                holder.binding.seatImageView.setTextColor(context.getResources().getColor(R.color.black));
                break;

            case UNAVAILABLE:
                holder.binding.seatImageView.setBackgroundResource(R.drawable.ic_seat_unavailable);
                // could be a darker grey
                holder.binding.seatImageView.setTextColor(context.getResources().getColor(R.color.black));
                break;

            case EMPTY:
                holder.binding.seatImageView.setBackgroundResource(R.drawable.ic_seat_empty);
                holder.binding.seatImageView.setTextColor(Color.parseColor("#00000000"));
                break;
        }

        holder.binding.seatImageView.setOnClickListener(v -> {
            if (seat.getStatus() == Seat.SeatStatus.AVAILABLE) {
                seat.setStatus(Seat.SeatStatus.SELECTED);
                selectedSeatName.add(seat.getName());
                notifyItemChanged(position);

            } else if (seat.getStatus() == Seat.SeatStatus.SELECTED) {
                seat.setStatus(Seat.SeatStatus.AVAILABLE);
                selectedSeatName.remove(seat.getName());
                notifyItemChanged(position);
            }

            String selected = selectedSeatName.toString()
                    .replace("[", "")
                    .replace("]", "")
                    .replace(" ", "");

            selectedSeat.Return(selected, selectedSeatName.size());
        });
    }

    @Override
    public int getItemCount() {
        return seatList.size();
    }

    public static class SeatViewholder extends RecyclerView.ViewHolder {
        SeatItemBinding binding;

        public SeatViewholder(@NonNull SeatItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface SelectedSeat {
        void Return(String selectedName, int num);
    }
}
