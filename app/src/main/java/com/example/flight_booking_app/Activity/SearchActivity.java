package com.example.flight_booking_app.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.flight_booking_app.Adapter.FlightAdapter;
import com.example.flight_booking_app.Model.Flight;
import com.example.flight_booking_app.R;
import com.example.flight_booking_app.databinding.ActivitySearchBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends BaseActivity {
private ActivitySearchBinding binding;
private String  from,to,date,class_seat;
private int numPassenger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getIntentExtra();
        initList();
        setVariable();
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initList() {

        DatabaseReference myRef = database.getReference().child("Flights");
        ArrayList<Flight> list = new ArrayList<>();
        Query query = myRef.orderByChild("from").equalTo(from);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot issue:snapshot.getChildren()){

                        Flight flight = issue.getValue(Flight.class);
                        if (flight.getTo().equals(to) && flight.getFrom().equals(from)){
                            list.add(flight);
                        }
//                        To filter with date; uncomment below lines and comment above lines;
//                        if (flight.getTo().equals(to)&& flight.getDate().equals(date)){
//
//                            list.add(flight);
//
//                        }
                        if (!list.isEmpty()){
                            binding.searchView.setLayoutManager(new LinearLayoutManager(SearchActivity.this,LinearLayoutManager.VERTICAL,false));
                            binding.searchView.setAdapter(new FlightAdapter(list));

                        }
                        binding.progressBarSearch.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void getIntentExtra() {
        from = getIntent().getStringExtra("from");
        to =    getIntent().getStringExtra("to");
        date = getIntent().getStringExtra("date");


    }
}