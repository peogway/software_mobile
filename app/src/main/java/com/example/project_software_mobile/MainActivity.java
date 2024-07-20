package com.example.project_software_mobile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private ArrayAdapter<String> suggestionsAdapter, latestSearchesAdapter;
    private String[] municipalityNames;

    private ArrayList<String> latestSearches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Context context =this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View txtLatestSearch = findViewById(R.id.txtLatestSearches);


        if (Storage.latestSearches == null){
            latestSearches = new ArrayList<>();
        }else {
            latestSearches = Storage.latestSearches;
        }

        if (!Storage.checkNameRetrieve){
            ExecutorService service = Executors.newSingleThreadExecutor();
            service.execute(() -> {
                MunicipalityDataRetriever municipalityDataRetriever = new MunicipalityDataRetriever();
                municipalityNames = municipalityDataRetriever.getNameData(context);
                Storage.municipalityNames =municipalityNames;
                Storage.checkNameRetrieve = true;

                runOnUiThread(() -> {});
            });
        }else{
            municipalityNames = Storage.municipalityNames;
        }


        EditText editMunicipalityName = findViewById(R.id.editMunicipalityName);
        findViewById(R.id.searchImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);

                String userInput = editMunicipalityName.getText().toString();
                fetchCitySuggestions(userInput);

            }
        });
        ListView suggestionsListView = findViewById(R.id.suggestionsListView);
        ListView latestSearchesListView = findViewById(R.id.latestSearchesListView);
        if (!latestSearches.isEmpty()){
            findViewById(R.id.txtLatestSearches).setVisibility(View.VISIBLE);
        }

        suggestionsAdapter = new ArrayAdapter<>(this, R.layout.item_list_view, new ArrayList<>());
        suggestionsListView.setAdapter(suggestionsAdapter);
        editMunicipalityName.requestFocus();

        editMunicipalityName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String userInput = s.toString().trim();
                if (!userInput.isEmpty()) {
                    fetchCitySuggestions(userInput);
                } else {
                    updateSuggestionsList(new ArrayList<>()); // Clear suggestions if user input is cleared
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String userInput = s.toString().trim();
                if (!userInput.isEmpty()) {
                    fetchCitySuggestions(userInput);
                } else {
                    updateSuggestionsList(new ArrayList<>()); // Clear suggestions if user input is cleared
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // This method is called to notify you that, somewhere within s, the text has been changed.
                String userInput = s.toString().trim();
                if (!userInput.isEmpty()) {
                    fetchCitySuggestions(userInput);
                } else {
                    updateSuggestionsList(new ArrayList<>()); // Clear suggestions if user input is cleared
                }
            }
        });

        suggestionsListView.setOnItemClickListener((parent, view, position, id) -> {
            txtLatestSearch.setVisibility(View.VISIBLE);
            String selectedCity = suggestionsAdapter.getItem(position);
            Storage.currentMunicipalityName = selectedCity;

            Intent intent = new Intent(MainActivity.this, MunicipalityActivity.class);
            intent.putExtra("CITY_NAME", selectedCity);
            startActivity(intent);

            if (!latestSearches.contains(selectedCity)) {
                latestSearches.add(0, selectedCity);
                if (latestSearches.size() > 5) {
                    latestSearches.remove(latestSearches.size() - 1);
                }
                Storage.latestSearches = latestSearches;



                latestSearchesAdapter.notifyDataSetChanged();
            }


        });



        latestSearchesAdapter = new ArrayAdapter<>(this, R.layout.item_list_view, latestSearches);
        latestSearchesListView.setAdapter(latestSearchesAdapter);
        latestSearchesListView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedCity = latestSearchesAdapter.getItem(position);
            Storage.currentMunicipalityName = selectedCity;

            Intent intent = new Intent(MainActivity.this, MunicipalityActivity.class);
            intent.putExtra("CITY_NAME", selectedCity);
            startActivity(intent);
        });
    }


    private void fetchCitySuggestions(String userInput) {
        if (municipalityNames == null) {
            // Handle the case where municipalityNames hasn't been initialized yet
            return;
        }

        ArrayList<String> suggestions = new ArrayList<>();

        ArrayList<String> startsWithSuggestions = new ArrayList<>();
        ArrayList<String> containsSuggestions = new ArrayList<>();

        String userInputLowerCase = userInput.toLowerCase();

        for (String city : municipalityNames) {
            String cityLowerCase = city.toLowerCase();
            if (cityLowerCase.startsWith(userInputLowerCase)) {
                startsWithSuggestions.add(city);
            } else if (cityLowerCase.contains(userInputLowerCase)) {
                containsSuggestions.add(city);
            }
        }

        suggestions.addAll(startsWithSuggestions);
        suggestions.addAll(containsSuggestions);


        updateSuggestionsList(suggestions);
    }

    private void updateSuggestionsList(ArrayList<String> suggestions) {
        runOnUiThread(() -> {
            suggestionsAdapter.clear();
            suggestionsAdapter.addAll(suggestions);
            suggestionsAdapter.notifyDataSetChanged();
        });
    }
}
