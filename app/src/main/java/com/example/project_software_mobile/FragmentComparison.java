package com.example.project_software_mobile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FragmentComparison extends Fragment{
    private EditText editMunicipalityName;
    private ArrayAdapter<String> suggestionsAdapter, recentCompareAdapter;
    private ArrayList<MunicipalityData> currentMuni = Storage.currentMunicipalityData;
    private static final String ARG_CITY_NAME = "cityName";
    private TextView descriptionText, name1, name2, popu1, popu2, popuInc1, popuInc2, wSS1, wSS2, eR1, eR2, nameWeather1, nameWeather2, signWeather1, signWeather2, signWeather3, signWeather4, des1, des2, humidity1, humidity2, windSpeed1, windSpeed2, tem1, tem2;
    private ImageView image1, image2, resetImage,homeImage, searchImage;
    private  Spinner spinner1, spinner2;
    private ArrayList<String> years = Storage.years, recentCompare = Storage.recentCompare;
    private WeatherData weather1 = Storage.currentWeather;
    private LinearLayout layout, layout2, recentLayout;
    private ListView suggestionsListView;
    private ListView recentCompareListView;
    private Button compareButton, dataCompareButton, weatherCompareButton;
    private String currentCityName;
    private MunicipalityDataRetriever municipalityDataRetriever = new MunicipalityDataRetriever();

    private WeatherDataRetriever weatherDataRetriever = new WeatherDataRetriever();


    @SuppressLint("String[]")
    private String[] municipalityNames = Storage.municipalityNames;

    public static FragmentComparison newInstance(String cityName) {
        FragmentComparison fragment = new FragmentComparison();
        Bundle args = new Bundle();
        args.putString(ARG_CITY_NAME, cityName);
        fragment.setArguments(args);
        return fragment;
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_comparison, container, false);
        ArrayList <String> years = Storage.years;
        currentCityName = getArguments().getString(ARG_CITY_NAME);
        descriptionText = view.findViewById(R.id.descriptionText);
        resetImage = view.findViewById(R.id.resetImage);
        homeImage = view.findViewById(R.id.homeImage);
        searchImage = view.findViewById(R.id.searchImage);
        searchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);

                String userInput = editMunicipalityName.getText().toString();
                fetchCitySuggestions(userInput);
            }
        });

        name1 = view.findViewById(R.id.name1);
        name2 = view.findViewById(R.id.name2);

        popu1 = view.findViewById(R.id.population1);
        popu2 = view.findViewById(R.id.population2);

        popuInc1 = view.findViewById(R.id.populationIncrease1);
        popuInc2 = view.findViewById(R.id.populationIncrease2);

        wSS1 = view.findViewById(R.id.wSS1);
        wSS2 = view.findViewById(R.id.wSS2);

        eR1 = view.findViewById(R.id.eR1);
        eR2 = view.findViewById(R.id.eR2);





        spinner1 = view.findViewById(R.id.spinner1);
        spinner2 = view.findViewById(R.id.spinner2);

        nameWeather1 = view.findViewById(R.id.nameWeather1);
        nameWeather2 = view.findViewById(R.id.nameWeather2);

        signWeather2 = view.findViewById(R.id.signWeather2);
        signWeather3 = view.findViewById(R.id.signWeather3);
        signWeather4 = view.findViewById(R.id.signWeather4);

        tem1 = view.findViewById(R.id.tem1);
        tem2 = view.findViewById(R.id.tem2);

        humidity1 = view.findViewById(R.id.humidity1);
        humidity2 = view.findViewById(R.id.humidity2);

        windSpeed1 = view.findViewById(R.id.windSpeed1);
        windSpeed2 = view.findViewById(R.id.windSpeed2);

        des1 = view.findViewById(R.id.des1);
        des2 = view.findViewById(R.id.des2);

        image1 = view.findViewById(R.id.image1);
        image2 = view.findViewById(R.id.image2);


        recentLayout = view.findViewById(R.id.recentLayout);

        layout = view.findViewById(R.id.layout);
        layout2 = view.findViewById(R.id.layout2);
        compareButton = view.findViewById(R.id.compareButton);
        dataCompareButton = view.findViewById(R.id.dataCompareButton);
        weatherCompareButton = view.findViewById(R.id.weatherCompareButton);

        homeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                // Optionally, if you want to clear all previous activities on the stack
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                // Start MainActivity
                startActivity(intent);
                // Optionally, if you want to finish the current activity (e.g. if it's also an activity)
                if (getActivity() != null) {
                    getActivity().finish();
                }
                // Do something in response to the image view click
            }
        });

        editMunicipalityName = view.findViewById(R.id.editMunicipalityName);
        editMunicipalityName.requestFocus();



        suggestionsListView = view.findViewById(R.id.suggestionsListView);
        recentCompareListView = view.findViewById(R.id.recentSearchListView);

        suggestionsAdapter = new ArrayAdapter<>(getContext(), R.layout.item_list_view, new ArrayList<>());
        suggestionsListView.setAdapter(suggestionsAdapter);
        editMunicipalityName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                fetchCitySuggestions(s.toString().trim());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fetchCitySuggestions(s.toString().trim());
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

        suggestionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                doClickItem(parent,view, position,id, "suggest");
            }
        });

        resetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editMunicipalityName.setVisibility(View.VISIBLE);
                if (!Storage.recentCompare.isEmpty()) {
                    recentLayout.setVisibility(View.VISIBLE);
                }
                editMunicipalityName.setText("");
                editMunicipalityName.clearFocus();


                searchImage.setVisibility(View.VISIBLE);
                updateSuggestionsList(new ArrayList<>());

                suggestionsListView.setVisibility(View.VISIBLE);
                descriptionText.setText("Municipality For Comparing");
                descriptionText.setTextSize(30);
                if (getActivity() != null && getView() != null) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                }
                layout.setVisibility(View.GONE);
                layout2.setVisibility(View.GONE);
                compareButton.setVisibility(View.GONE);

                searchImage.setVisibility(View.VISIBLE);
                weatherCompareButton.setVisibility(View.GONE);
                dataCompareButton.setVisibility(View.GONE);


                Animation blinkAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.blink);

                // Apply the blink animation to the entire view or specific elements
                view.startAnimation(blinkAnimation);


            }
        });

        if (!Storage.recentCompare.isEmpty()) {
            recentLayout.setVisibility(View.VISIBLE);
        }


        recentCompareAdapter = new ArrayAdapter<>(getContext(), R.layout.item_list_view, recentCompare);
        recentCompareListView.setAdapter(recentCompareAdapter);
        recentCompareListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                doClickItem(parent,view, position,id, "recent");
            }
        });

        return view;
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
        suggestionsAdapter.clear();
        suggestionsAdapter.addAll(suggestions);
        suggestionsAdapter.notifyDataSetChanged();
    }

    private void doClickItem(AdapterView<?> parent, View view, int position, long id, String role){
        recentLayout.setVisibility(View.GONE);
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        String selectedCity;
        searchImage.setVisibility(View.GONE);
        if (role.equals("suggest")){
            selectedCity = suggestionsAdapter.getItem(position);
        }else{
            selectedCity = recentCompareAdapter.getItem(position);
        }

        if (!recentCompare.contains(selectedCity)) {
            recentCompare.add(0, selectedCity);
            if (recentCompare.size() > 4) {
                recentCompare.remove(Storage.recentCompare.size() - 1);
            }
            Storage.recentCompare = recentCompare;

            recentCompareAdapter.notifyDataSetChanged();
        }


        String text = "Comparing Field?";
        descriptionText.setText(text);
        descriptionText.setTextSize(35);
        editMunicipalityName.setVisibility(View.GONE);
        suggestionsListView.setVisibility(View.GONE);

        dataCompareButton.setVisibility(View.VISIBLE);
        weatherCompareButton.setVisibility(View.VISIBLE);

        if (selectedCity.equals("WHOLE COUNTRY")||currentCityName.equals("WHOLE COUNTRY")){
            weatherCompareButton.setVisibility(View.GONE);
        }

        weatherCompareButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                dataCompareButton.setVisibility(View.GONE);
                weatherCompareButton.setVisibility(View.GONE);
                searchImage.setVisibility(View.GONE);
                descriptionText.setText("Comparing Weather");
                descriptionText.setTextSize(30);

                layout2.setVisibility(View.VISIBLE);

                nameWeather1.setText(currentCityName);
                nameWeather2.setText(selectedCity);

                ExecutorService service = Executors.newSingleThreadExecutor();
                service.execute(() -> {

                    WeatherData weather2 = weatherDataRetriever.getData(selectedCity);

                    getActivity().runOnUiThread(() -> {

                        String condition1 = weather1.getMain();
                        if (condition1.equals("Thunderstorm")){
                            image1.setImageResource(R.drawable.storm);
                        }else if (condition1.equals("Drizzle")){
                            image1.setImageResource(R.drawable.dizzle);
                        }else if (condition1.equals("Rain")){
                            image1.setImageResource(R.drawable.rain);
                        }else if (condition1.equals("Snow")){
                            image1.setImageResource(R.drawable.snow);
                        }else if (condition1.equals("Clear")){
                            image1.setImageResource(R.drawable.clear);
                        }else if (condition1.equals("Clouds")){
                            image1.setImageResource(R.drawable.clouds);
                        }

                        des1.setText(weather1.getDescription());

                        String temp1 = String.format("%.1f",Float.parseFloat(weather1.getTemperature())-273.15)+"°C";
                        tem1.setText(temp1);

                        String windSpeedText1 = weather1.getWindSpeed()+"m/s";
                        windSpeed1.setText(windSpeedText1);

                        String humidityString1 = weather1.getHumidity()+"%";
                        humidity1.setText(humidityString1);


                        signWeather2.setText("Temperature");
                        signWeather3.setText("Wind Speed");
                        signWeather4.setText("Humidity");

                        des2.setText(weather2.getDescription());
                        String temp2 = String.format("%.1f",Float.parseFloat(weather2.getTemperature())-273.15)+"°C";
                        tem2.setText(temp2);

                        String windSpeedText2 = weather2.getWindSpeed()+"m/s";
                        windSpeed2.setText(windSpeedText2);

                        String humidityString2 = weather2.getHumidity()+"%";
                        humidity2.setText(humidityString2);





                        String condition2 = weather2.getMain();
                        if (condition2.equals("Thunderstorm")){
                            image2.setImageResource(R.drawable.storm);
                        }else if (condition2.equals("Drizzle")){
                            image2.setImageResource(R.drawable.dizzle);
                        }else if (condition2.equals("Rain")){
                            image2.setImageResource(R.drawable.rain);
                        }else if (condition2.equals("Snow")){
                            image2.setImageResource(R.drawable.snow);
                        }else if (condition2.equals("Clear")){
                            image2.setImageResource(R.drawable.clear);
                        }else if (condition2.equals("Clouds")){
                            image2.setImageResource(R.drawable.clouds);
                        }
                    });
                });

            }
        });



        dataCompareButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){


                descriptionText.setText("Comparing Statistical Data");
                descriptionText.setTextSize(30);
                compareButton.setVisibility(View.VISIBLE);
                dataCompareButton.setVisibility(View.GONE);
                weatherCompareButton.setVisibility(View.GONE);
                layout.setVisibility(View.VISIBLE);
                name1.setText(currentCityName);
                name2.setText(selectedCity);

                ArrayAdapter<String> adapter1 = new ArrayAdapter<>(
                        requireActivity(), R.layout.custom_spinner_item, years);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinner1.setAdapter(adapter1);

                spinner2.setAdapter(adapter1);

                spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        popu1.setVisibility(View.GONE);
                        popu2.setVisibility(View.GONE);

                        popuInc1.setVisibility(View.GONE);
                        popuInc2.setVisibility(View.GONE);

                        wSS1.setVisibility(View.GONE);
                        wSS2.setVisibility(View.GONE);

                        eR1.setVisibility(View.GONE);
                        eR2.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Code to execute when nothing is selected (optional)
                    }
                });


                spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        popu1.setVisibility(View.GONE);
                        popu2.setVisibility(View.GONE);

                        popuInc1.setVisibility(View.GONE);
                        popuInc2.setVisibility(View.GONE);

                        wSS1.setVisibility(View.GONE);
                        wSS2.setVisibility(View.GONE);

                        eR1.setVisibility(View.GONE);
                        eR2.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Code to execute when nothing is selected (optional)
                    }
                });

                compareButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        String year1 = (String) spinner1.getSelectedItem();
                        String year2 = (String) spinner2.getSelectedItem();

                        ExecutorService service = Executors.newSingleThreadExecutor();
                        service.execute(() -> {

                            ArrayList<MunicipalityData> fetchedData = municipalityDataRetriever.getMunicipalityData(getContext(), name2.getText().toString());




                            if (fetchedData == null || getActivity() == null) return;
                            MunicipalityData muni1 ;
                            MunicipalityData muni2 ;


                            muni1 = currentMuni.stream().filter(muni -> String.valueOf(muni.getPopulationData().getYear()).equals(year1)).findFirst().orElse(null);

                            muni2 = fetchedData.stream().filter(muni -> String.valueOf(muni.getPopulationData().getYear()).equals(year2)).findFirst().orElse(null);

                            getActivity().runOnUiThread(() -> {


                                popu1.setVisibility(View.VISIBLE);
                                popu2.setVisibility(View.VISIBLE);

                                popuInc1.setVisibility(View.VISIBLE);
                                popuInc2.setVisibility(View.VISIBLE);

                                wSS1.setVisibility(View.VISIBLE);
                                wSS2.setVisibility(View.VISIBLE);

                                eR1.setVisibility(View.VISIBLE);
                                eR2.setVisibility(View.VISIBLE);


                                popu1.setText(String.valueOf(muni1.getPopulationData().getPopulation()));
                                popu2.setText(String.valueOf(muni2.getPopulationData().getPopulation()));

                                popuInc1.setText(String.valueOf(muni1.getPopulationData().getPopulationIncrease()));
                                popuInc2.setText(String.valueOf(muni2.getPopulationData().getPopulationIncrease()));





                                String wssString1 =muni1.getWorkplaceSelfSufficiency().getWorkspaceSS()+"%";
                                wSS1.setText(wssString1);

                                String wssString2 =muni2.getWorkplaceSelfSufficiency().getWorkspaceSS()+"%";
                                wSS2.setText(wssString2);


                                String eRString1 = muni1.getEmploymentRateData().getEmploymentRate() + "%";
                                eR1.setText(eRString1);

                                String eRString2 = muni2.getEmploymentRateData().getEmploymentRate() + "%";
                                eR2.setText(eRString2);


                            });
                        });

                    }
                });
            }
        });
    }
}
