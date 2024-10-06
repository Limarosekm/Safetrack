package com.example.safetrack;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class login extends AppCompatActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        EditText emailEditText = findViewById(R.id.editTextText);
        EditText passwordEditText = findViewById(R.id.editTextTextPassword);
        Button loginButton = findViewById(R.id.LOGIN);
        Button signup= findViewById(R.id.button3);

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            // Firebase sign in
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                            // Navigate to the main activity or dashboard
                            Intent intent = new Intent(login.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(this, "Login failed: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
        signup.setOnClickListener(v -> {
            Intent intent = new Intent(login.this, MainActivity.class);
            startActivity(intent);

        });

    }
}

