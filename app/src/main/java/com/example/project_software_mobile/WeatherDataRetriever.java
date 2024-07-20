package com.example.project_software_mobile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class WeatherDataRetriever {

    private final String API_KEY =  "90bd7f03ed39d5afb5ba810abfbefe7a";

    // https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}
    private final String WEATHER_API_URL = "https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s";

    // http://api.openweathermap.org/geo/1.0/direct?q={city name},{state code},{country code}&appid={API key}
    private  final String GEOLOCATION_API_URL = "http://api.openweathermap.org/geo/1.0/direct?q=%s&appid=%s";


    public WeatherData getData(String municipalityName) {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode areas = null;
        try {
            // String.format replaces %s with the parameters
            // municipalityName (city name),1 (limit, aka how many cities will be returned) and API_KEY
            URL locationUrl = new URL(String.format(GEOLOCATION_API_URL, municipalityName, API_KEY));



            areas = objectMapper.readTree(locationUrl);
        }
        catch (MalformedURLException e) {
            throw new RuntimeException();
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }


        String latitude = areas.get(0).get("lat").toString();
        String longitude = areas.get(0).get("lon").toString();

        JsonNode weatherJson;

        try {
            URL weatherUrl = new URL(String.format(WEATHER_API_URL, latitude, longitude, API_KEY));
            weatherJson = objectMapper.readTree(weatherUrl);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



        WeatherData weatherData = new WeatherData(
                weatherJson.get("name").asText(),
                weatherJson.get("weather").get(0).get("main").asText(),
                weatherJson.get("weather").get(0).get("description").asText(),
                weatherJson.get("main").get("temp").asText(),
                weatherJson.get("wind").get("speed").asText(),
                weatherJson.get("main").get("humidity").asText()
        );

        return weatherData;
    }

}
