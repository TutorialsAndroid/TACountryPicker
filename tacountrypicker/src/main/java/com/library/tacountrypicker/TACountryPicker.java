package com.library.tacountrypicker;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class TACountryPicker {
    private AlertDialog dialog;
    private OnCountrySelectedListener listener;
    private List<CountryModel> countryList;
    private List<CountryModel> filteredList;

    public interface OnCountrySelectedListener {
        void onCountrySelected(CountryModel country);
    }

    public TACountryPicker(Context context, OnCountrySelectedListener listener) {
        this.listener = listener;
        countryList = loadCountryData(context);
        filteredList = new ArrayList<>(countryList);
        showDialog(context);
    }

    private void showDialog(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_country_picker, null);
        EditText searchEditText = view.findViewById(R.id.etSearchCountry);
        RecyclerView recyclerView = view.findViewById(R.id.countryRecyclerView);

        CountryAdapter adapter = new CountryAdapter(filteredList, country -> {
            listener.onCountrySelected(country);
            dialog.dismiss();
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().toLowerCase();
                filteredList.clear();
                for (CountryModel country : countryList) {
                    if (country.getName().toLowerCase().contains(query) || country.getDialCode().contains(query)) {
                        filteredList.add(country);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        dialog = new MaterialAlertDialogBuilder(context)
                .setIcon(R.drawable.baseline_location_pin_24)
                .setTitle("Select Your Country")
                .setView(view)
                .create();
        dialog.show();
    }

    private List<CountryModel> loadCountryData(Context context) {
        List<CountryModel> countries = new ArrayList<>();
        try {
            InputStream is = context.getResources().openRawResource(R.raw.country_codes);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder jsonString = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            reader.close();

            JSONArray jsonArray = new JSONArray(jsonString.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject countryObject = jsonArray.getJSONObject(i);
                String name = countryObject.getString("name");
                String code = countryObject.getString("dial_code");
                String emoji = countryObject.getString("emoji");
                countries.add(new CountryModel(name, code, emoji));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return countries;
    }
}
