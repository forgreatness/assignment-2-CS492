package com.example.android.connectedweather;

import android.net.Uri;

import com.google.gson.Gson;

import java.io.Serializable;

public class OpenWeatherAPIUtils {
    private final static String OPEN_WEATHER_API_SERVER = "https://api.openweathermap.org/data/2.5/forecast";
    private final static String OPEN_WEATHER_API_LOCATION_PARAM = "q";
    private final static String OPEN_WEATHER_API_LOCATION_VALUE = "Corvallis,US";
    private final static String OPEN_WEATHER_API_UNITS_PARAM = "units";
    private final static String OPEN_WEATHER_API_UNITS_VALUE = "imperial";
    private final static String OPEN_WEATHER_API_KEY_PARAM = "APPID";
    private final static String OPEN_WEATHER_API_KEY_VALUE = "521b74e266c0a0fc0c280747d5848b82";

    public final static String OPEN_WEATHER_API_FORECAST = "OpenWeatherAPIUtils.Forecast";
    public final static String OPEN_WEATHER_API_FORECAST_ICON = "OpenWeatherAPIUtils.Description";
    public final static String OPEN_WEATHER_API_TEMP_UNIT = "F";

    public static class Description implements Serializable{
        public String main;
        public String icon;
    }

    public static class Temperature implements Serializable{
        public float temp;
    }

    public static class Forecast implements Serializable {
        public String dt_txt;
        public Temperature main;
        public Description[] weather;
    }

    public static class OpenWeatherAPISearchResults{
        public Forecast[] list;
    }

    public static String buildOpenWeatherAPISearchURL(){
        return Uri.parse(OPEN_WEATHER_API_SERVER).buildUpon()
                .appendQueryParameter(OPEN_WEATHER_API_LOCATION_PARAM, OPEN_WEATHER_API_LOCATION_VALUE)
                .appendQueryParameter(OPEN_WEATHER_API_UNITS_PARAM, OPEN_WEATHER_API_UNITS_VALUE)
                .appendQueryParameter(OPEN_WEATHER_API_KEY_PARAM, OPEN_WEATHER_API_KEY_VALUE)
                .build()
                .toString();
    }

    public static Forecast[] parseOpenWeatherAPISearchResults(String json){
        Gson gson = new Gson();
        OpenWeatherAPISearchResults results = gson.fromJson(json, OpenWeatherAPISearchResults.class);
        if(results != null && results.list != null){
            return  results.list;
        }else{
            return null;
        }
    }
}
