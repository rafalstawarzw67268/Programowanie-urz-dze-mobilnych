package com.example.aplikacjadomierzeniawody;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    private TextView welcomeMessage;
    private Spinner spinnerGender;
    private EditText editTextWeight;
    private EditText editTextAge;
    private Button buttonCalculate;
    private TextView textViewWaterIntake;
    private EditText editTextWaterConsumed;
    private RadioGroup radioGroupWaterOptions;
    private RadioButton radioButtonOneGlass;
    private RadioButton radioButtonCustomAmount;
    private Button buttonAddWater;
    private Button buttonLogout;

    private int dailyWaterIntake;
    private int waterConsumed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        welcomeMessage = findViewById(R.id.welcomeMessage);
        spinnerGender = findViewById(R.id.spinnerGender);
        editTextWeight = findViewById(R.id.editTextWeight);
        editTextAge = findViewById(R.id.editTextAge);
        buttonCalculate = findViewById(R.id.buttonCalculate);
        textViewWaterIntake = findViewById(R.id.textViewWaterIntake);
        editTextWaterConsumed = findViewById(R.id.editTextWaterConsumed);
        radioGroupWaterOptions = findViewById(R.id.radioGroupWaterOptions);
        radioButtonOneGlass = findViewById(R.id.radioButtonOneGlass);
        radioButtonCustomAmount = findViewById(R.id.radioButtonCustomAmount);
        buttonAddWater = findViewById(R.id.buttonAddWater);
        buttonLogout = findViewById(R.id.buttonLogout);

        SharedPreferences sharedPref = getSharedPreferences("MyApp", Context.MODE_PRIVATE);
        String username = sharedPref.getString("username", "Użytkownik");

        welcomeMessage.setText("Witaj, " + username + "!");

        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(this, R.array.gender_array, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(genderAdapter);

        buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateWaterIntake();
            }
        });

        radioGroupWaterOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButtonCustomAmount) {
                    editTextWaterConsumed.setVisibility(View.VISIBLE);
                } else {
                    editTextWaterConsumed.setVisibility(View.GONE);
                }
            }
        });

        buttonAddWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addWaterConsumed();
            }
        });

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
    }

    private void calculateWaterIntake() {
        String gender = spinnerGender.getSelectedItem().toString();
        String weightStr = editTextWeight.getText().toString().trim();
        String ageStr = editTextAge.getText().toString().trim();

        if (TextUtils.isEmpty(gender) || TextUtils.isEmpty(weightStr) || TextUtils.isEmpty(ageStr)) {
            Toast.makeText(this, "Wszystkie pola są wymagane", Toast.LENGTH_SHORT).show();
            return;
        }

        int weight = Integer.parseInt(weightStr);
        int age = Integer.parseInt(ageStr);

        dailyWaterIntake = (weight * 35) / 1000;
        waterConsumed = 0;

        textViewWaterIntake.setText("Twoje dzienne zapotrzebowanie na wodę: " + dailyWaterIntake + " L");
    }

    private void addWaterConsumed() {
        int additionalWater = 0;

        if (radioButtonOneGlass.isChecked()) {
            additionalWater = 250;
        } else if (radioButtonCustomAmount.isChecked()) {
            String waterConsumedStr = editTextWaterConsumed.getText().toString().trim();
            if (!TextUtils.isEmpty(waterConsumedStr)) {
                additionalWater = Integer.parseInt(waterConsumedStr);
            }
        }

        if (additionalWater == 0) {
            Toast.makeText(this, "Podaj ilość wypitej wody", Toast.LENGTH_SHORT).show();
            return;
        }

        waterConsumed += additionalWater;
        int remainingWater = dailyWaterIntake * 1000 - waterConsumed; // w ml
        int remainingGlasses = remainingWater / 250;

        if (remainingWater > 0) {
            Toast.makeText(this, "Musisz jeszcze wypić: " + remainingWater + " ml (" + remainingGlasses + " szklanek)", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Osiągnąłeś dzienne zapotrzebowanie na wodę!", Toast.LENGTH_LONG).show();
        }
    }

    private void logoutUser() {
        SharedPreferences sharedPref = getSharedPreferences("MyApp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
