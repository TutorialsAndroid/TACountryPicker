package com.sample.tacountrypicker;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.library.tacountrypicker.TACountryPicker;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText etCountry;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        etCountry = findViewById(R.id.etCountryCode);
        etCountry.setOnClickListener(v -> showCountryPicker());
        etCountry.setFocusable(false); // Prevent keyboard from appearing
        etCountry.setClickable(true);
    }

    private void showCountryPicker() {
        new TACountryPicker(this, country -> {
            // Set selected country to EditText
            etCountry.setText(String.format("%s %s %s", country.getEmoji(), country.getDialCode(), country.getName()));
        });
    }
}
