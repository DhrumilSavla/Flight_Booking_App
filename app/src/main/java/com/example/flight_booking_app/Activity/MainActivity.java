package com.example.flight_booking_app.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.flight_booking_app.Model.Location;
import com.example.flight_booking_app.R;
import com.example.flight_booking_app.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding;
    private int adultPassenger=1,childPassenger=1;
    private SimpleDateFormat dateFormat= new SimpleDateFormat("d MMM,yyyy ", Locale.ENGLISH);
    private Calendar calendar = Calendar.getInstance();
    private TextView user;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        setContentView(binding.getRoot());

        initLocations();
        initPassengers();
        initClassSeat();
        initDatePickup();
        setVariable();

        user = binding.username;
        String username = getIntent().getStringExtra("username");
        user.setText("Hello "+username);
//        loadUserProfile();
    }
    private void loadUserProfile() {
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("users").document(userId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String username = document.getString("username"); // Assuming you saved username as 'username'
                            user.setText(username);
                        } else {
                            user.setText("User profile not found.");
                        }
                    } else {
                        user.setText("Failed to load profile.");
                    }
                });
    }
    private void setVariable() {
        binding.searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                intent.putExtra("from",((Location)binding.fromSp.getSelectedItem()).getName());
                intent.putExtra("to",((Location)binding.toSp.getSelectedItem()).getName());
                intent.putExtra("date",binding.departureDateTxt.getText().toString());
                intent.putExtra("numPassenger",adultPassenger+childPassenger);
                startActivity(intent);
            }
        });
    }

    private void initDatePickup() {
        Calendar calendarToday = Calendar.getInstance();
        String currentDate = dateFormat.format(calendarToday.getTime());
        binding.departureDateTxt.setText(currentDate);


        Calendar calendarTommorrow = Calendar.getInstance();
        calendarTommorrow.add(Calendar.DAY_OF_YEAR,1);
        String tommorrowDate = dateFormat.format(calendarTommorrow.getTime());
        binding.returnDateTxt.setText(tommorrowDate);

        binding.departureDateTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerdialog(binding.departureDateTxt);
            }
        });
        binding.returnDateTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerdialog(binding.returnDateTxt);
            }
        });
    }

    private void initClassSeat() {
        binding.progressBarClass.setVisibility(View.VISIBLE);
        ArrayList<String> list = new ArrayList<>();
        list.add("Economy Class");
        list.add("Business Class");
        list.add("First Class");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, R.layout.sp_item,list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.classSp.setAdapter(adapter);
        binding.progressBarClass.setVisibility(View.GONE);
    }

    private void initPassengers() {
        binding.plusAdultBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adultPassenger++;
                binding.AdultTxt.setText(adultPassenger+" Adult");
            }
        });
        binding.minAdultBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adultPassenger>1){
                    adultPassenger--;
                    binding.AdultTxt.setText(adultPassenger+" Adult");
                }
            }
        });
        binding.plusChildBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                childPassenger++;
                binding.childTxt.setText(childPassenger+" Child");
            }
        });
        binding.minusChildBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (childPassenger>0){
                    childPassenger--;
                    binding.childTxt.setText(childPassenger+" Child");

                }
            }
        });
    }

    private void initLocations() {
        binding.progressBarFrom.setVisibility(View.VISIBLE);
        binding.progressBarTo.setVisibility(View.VISIBLE);
        DatabaseReference myRef = database.getReference().child("Locations");
        ArrayList<Location> list = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot issue:snapshot.getChildren()){
                        list.add(issue.getValue(Location.class));
                    }
                    ArrayAdapter<Location> adapter = new ArrayAdapter<>(MainActivity.this,R.layout.sp_item,list);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    binding.fromSp.setAdapter(adapter);
                    binding.toSp.setAdapter(adapter);
                    binding.fromSp.setSelection(1);
                    binding.progressBarFrom.setVisibility(View.GONE);
                    binding.progressBarTo.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showDatePickerdialog(TextView textView){
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,(view,selectedYear,selecteMonth,selectedDay)->{
            calendar.set(selectedYear,selecteMonth,selectedDay);
            String formattedDate = dateFormat.format(calendar.getTime());
            textView.setText(formattedDate);

        },year,month,day);
        datePickerDialog.show();
    }
}