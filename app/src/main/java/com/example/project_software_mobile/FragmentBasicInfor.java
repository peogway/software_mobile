package com.example.project_software_mobile;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FragmentBasicInfor extends Fragment {
    private static final String ARG_CITY_NAME = "cityName";
    private MunicipalityDataAdapter adapter ;
    private TextView txtWeather, txtTemperature, txtWindSpeed, txtHumidity;
    private ImageView conditionWeather, homeImage;



    TextView city;

    public static FragmentBasicInfor newInstance(String cityName) {
        FragmentBasicInfor fragment = new FragmentBasicInfor();
        Bundle args = new Bundle();
        args.putString(ARG_CITY_NAME, cityName);
        fragment.setArguments(args);
        return fragment;
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        MunicipalityDataRetriever municipalityDataRetriever = new MunicipalityDataRetriever();
        WeatherDataRetriever weatherDataRetriever = new WeatherDataRetriever();

        String cityName = getArguments().getString(ARG_CITY_NAME);
        View view = inflater.inflate(R.layout.fragment_basic_infor, container, false);
        LinearLayout weatherLayout = view.findViewById(R.id.weatherLayout);

        city =view.findViewById(R.id.cityName);
        city.setText(cityName);

        txtWeather = view.findViewById(R.id.txtWeather);
        txtTemperature = view.findViewById(R.id.txtTemperature);
        txtWindSpeed = view.findViewById(R.id.txtWindSpeed);
        txtHumidity = view.findViewById(R.id.txtHumidity);

        conditionWeather = view.findViewById(R.id.imageView);

        homeImage = view.findViewById(R.id.homeImage);

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

        RecyclerView recyclerView = view.findViewById(R.id.rvMunicipalities);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MunicipalityDataAdapter(getContext(), new ArrayList<>());
        recyclerView.setAdapter(adapter);



        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {
            WeatherData weatherData = new WeatherData("", "","", "0","0","0");
            ArrayList<MunicipalityData> fetchedData = municipalityDataRetriever.getMunicipalityData(getContext(), cityName);
            Storage.currentMunicipalityData = fetchedData;
            adapter.setMunicipalities(fetchedData);


            if (fetchedData == null || getActivity() == null) return;


            if (!cityName.equals("WHOLE COUNTRY")){
                weatherData = weatherDataRetriever.getData(cityName);
            } else {
                weatherLayout.setVisibility(View.INVISIBLE);
            }

            Storage.currentWeather = weatherData;


            WeatherData finalWeatherData = weatherData;
            getActivity().runOnUiThread(() -> {


                String weatherText = finalWeatherData.getMain() + "(" + finalWeatherData.getDescription() + ")";
                txtWeather.setText(weatherText);
                String condition = finalWeatherData.getMain();
                if (condition.equals("Thunderstorm")){
                    conditionWeather.setImageResource(R.drawable.storm);
                }else if (condition.equals("Drizzle")){
                    conditionWeather.setImageResource(R.drawable.dizzle);
                }else if (condition.equals("Rain")){
                    conditionWeather.setImageResource(R.drawable.rain);
                }else if (condition.equals("Snow")){
                    conditionWeather.setImageResource(R.drawable.snow);
                }else if (condition.equals("Clear")){
                    conditionWeather.setImageResource(R.drawable.clear);
                }else if (condition.equals("Clouds")){
                    conditionWeather.setImageResource(R.drawable.clouds);
                }




                @SuppressLint("DefaultLocale")
                String temText = String.format("%.1f",Float.parseFloat(finalWeatherData.getTemperature())-273.15)+"°C ("+ finalWeatherData.getTemperature()+"°K)";
                txtTemperature.setText(temText);


                String windSpeedText = "Wind speed: "+ finalWeatherData.getWindSpeed()+"m/s";
                txtWindSpeed.setText(windSpeedText);

                String humidityString = "Humidity: "+ finalWeatherData.getHumidity()+"%";
                txtHumidity.setText(humidityString);

                adapter.notifyDataSetChanged();

            });
        });

        ImageView resetImage = view.findViewById(R.id.resetImage);
        resetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExecutorService service = Executors.newSingleThreadExecutor();
                service.execute(() -> {
                    WeatherData weatherData = new WeatherData("", "","", "0","0","0");

                    if (!cityName.equals("WHOLE COUNTRY")){
                        weatherData = weatherDataRetriever.getData(cityName);
                    } else {
                        weatherLayout.setVisibility(View.INVISIBLE);
                    }

                    Storage.currentWeather = weatherData;


                    WeatherData finalWeatherData = weatherData;
                    getActivity().runOnUiThread(() -> {

                        String weatherText = finalWeatherData.getMain() + "(" + finalWeatherData.getDescription() + ")";
                        txtWeather.setText(weatherText);
                        String condition = finalWeatherData.getMain();
                        if (condition.equals("Thunderstorm")){
                            conditionWeather.setImageResource(R.drawable.storm);
                        }else if (condition.equals("Drizzle")){
                            conditionWeather.setImageResource(R.drawable.dizzle);
                        }else if (condition.equals("Rain")){
                            conditionWeather.setImageResource(R.drawable.rain);
                        }else if (condition.equals("Snow")){
                            conditionWeather.setImageResource(R.drawable.snow);
                        }else if (condition.equals("Clear")){
                            conditionWeather.setImageResource(R.drawable.clear);
                        }else if (condition.equals("Clouds")){
                            conditionWeather.setImageResource(R.drawable.clouds);
                        }




                        @SuppressLint("DefaultLocale")
                        String temText = String.format("%.1f",Float.parseFloat(finalWeatherData.getTemperature())-273.15)+"°C ("+ finalWeatherData.getTemperature()+"°K)";
                        txtTemperature.setText(temText);


                        String windSpeedText = "Wind speed: "+ finalWeatherData.getWindSpeed()+"m/s";
                        txtWindSpeed.setText(windSpeedText);

                        String humidityString = "Humidity: "+ finalWeatherData.getHumidity()+"%";
                        txtHumidity.setText(humidityString);
                        Animation blinkAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.blink);

                        // Apply the blink animation to the entire view or specific elements
                        view.startAnimation(blinkAnimation);

                    });
                });
            }
        });


        return view;
    }


}
