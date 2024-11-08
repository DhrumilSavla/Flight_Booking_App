package com.example.flight_booking_app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flight_booking_app.Activity.SeatListActivity;
import com.example.flight_booking_app.Model.Flight;
import com.example.flight_booking_app.databinding.ViewholderFlightBinding;
//import com.example.flight_booking_app.databinding.ViewholderFlightsBinding;

import java.util.ArrayList;

public class FlightAdapter extends RecyclerView.Adapter<FlightAdapter.Viewholder> {
    private final ArrayList<Flight> flights;
    private Context context;

    public FlightAdapter(ArrayList<Flight> flights) {
        this.flights = flights;
    }

    @NonNull
    @Override
    public FlightAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewholderFlightBinding binding = ViewholderFlightBinding.inflate(LayoutInflater.from(context),parent,false);
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FlightAdapter.Viewholder holder, int position) {
        Flight flight = flights.get(position);
        Glide.with(context).load(flight.getAirlineLogo()).into(holder.binding.logo);
        holder.binding.fromTxt.setText(flight.getFrom());
        holder.binding.fromshortTxt.setText(flight.getFromShort());
        holder.binding.toTxt.setText(flight.getTo());
        holder.binding.toShortTxt.setText(flight.getToShort());
        holder.binding.arrivalTxt.setText(flight.getArrivalTime());
        holder.binding.classTxt.setText(flight.getClassSeat());
        holder.binding.priceTxt.setText("₹ "+flight.getPrice());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SeatListActivity.class);
                intent.putExtra("flight",flight);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return flights.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private  final ViewholderFlightBinding binding;

        public Viewholder(ViewholderFlightBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
