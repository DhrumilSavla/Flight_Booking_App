package com.example.flight_booking_app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.flight_booking_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.auth.User;

public class RegistrationActivity extends AppCompatActivity {
    private EditText usernameTextView, emailTextView, passwordTextView;
    private Button registerButton;
    private TextView loginLink;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize views
        usernameTextView = findViewById(R.id.username);
        emailTextView = findViewById(R.id.email);
        passwordTextView = findViewById(R.id.passwd);
        registerButton = findViewById(R.id.btnregister);
        loginLink = findViewById(R.id.login_link);

        registerButton.setOnClickListener(v -> registerNewUser());

        // Set OnClickListener for login link
        loginLink.setOnClickListener(v -> {
            Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    // Inside your registerNewUser method in RegistrationActivity.java
    private void registerNewUser() {
        String username = usernameTextView.getText().toString();
        String email = emailTextView.getText().toString();
        String password = passwordTextView.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Get the user ID
                        String userId = mAuth.getCurrentUser().getUid();

                        // Create a new user object
                        User user = new User(username, email); // Assuming User class has username and email fields

                        // Save the user information to Firestore
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("users").document(userId).set(user)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(RegistrationActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish(); // Close registration activity
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(RegistrationActivity.this, "Failed to create user profile.", Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        Toast.makeText(RegistrationActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}