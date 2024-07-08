package com.example.aplikacjadomierzeniawody;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextUsername;
    private Button buttonResetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextUsername = findViewById(R.id.editTextUsername);
        buttonResetPassword = findViewById(R.id.buttonResetPassword);

        buttonResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }

    private void resetPassword() {
        String email = editTextEmail.getText().toString().trim();
        String username = editTextUsername.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Wszystkie pola są wymagane", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences sharedPref = getSharedPreferences("MyApp", MODE_PRIVATE);
        String savedEmail = sharedPref.getString("email", null);
        String savedUsername = sharedPref.getString("username", null);

        if (email.equals(savedEmail) && username.equals(savedUsername)) {
            Toast.makeText(this, "Twoje hasło to: " + sharedPref.getString("password", ""), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Błędny email lub login", Toast.LENGTH_SHORT).show();
        }
    }
}
