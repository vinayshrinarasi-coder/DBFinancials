package com.financials.db.dbfinancials;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.core.view.WindowCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LogIn extends AppCompatActivity {
    TextInputLayout unameInputLayout, passInputLayout;
    TextInputEditText unameEditText, passEditText;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();
    }

    private void init() {
        unameInputLayout = findViewById(R.id.unameInputLayout);
        passInputLayout = findViewById(R.id.passInputLayout);
        unameEditText = findViewById(R.id.unameEditText);
        passEditText = findViewById(R.id.passEditText);
        login = findViewById(R.id.btnLogin);
        login.setOnClickListener(view -> {
            passInputLayout.setError(null);
            unameInputLayout.setError(null);
            if (unameEditText.getText() == null || !unameEditText.getText().toString().trim().equalsIgnoreCase("bhat@1950")) {
                unameInputLayout.setError("Invalid Username..!!");
            } else if (passEditText.getText() == null || !passEditText.getText().toString().trim().equalsIgnoreCase("9092")) {
                passInputLayout.setError("Invalid Password..!!");
            } else {
                startActivity(new Intent(getApplicationContext(), DashBoard.class));
            }
        });
    }
}