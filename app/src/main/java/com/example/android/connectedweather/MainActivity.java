package com.example.android.connectedweather;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements ForecastAdapter.OnForecastItemClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mForecastListRV;
    private ForecastAdapter mForecastAdapter;
    private TextView mLoadingErrorTV;
    private ProgressBar mLoadingPB;

    private static final String[] dummyForecastData = {
            "Sunny and Warm - 75F",
            "Partly Cloudy - 72F",
            "Mostly Sunny - 73F",
            "Partly Cloudy - 70F",
            "Occasional Showers - 65F",
            "Showers - 63F",
            "Occasional Showers - 64F",
            "Rainy - 62F",
            "Rainy - 61F",
            "Hurricane - 65F",
            "Windy and Clear - 70F",
            "Sunny and Warm - 77F",
            "Sunny and Warm - 81F"
    };

    private static final String[] dummyDetailedForecastData = {
            "Not a cloud in the sky today, with lows around 52F and highs near 75F.",
            "Clouds gathering in the late afternoon and slightly cooler than the day before, with lows around 49F and highs around 72F",
            "Scattered clouds all day with lows near 52F and highs near 73F",
            "Increasing cloudiness as the day goes on with some cooling; lows near 48F and highs near 70F",
            "Showers beginning in the morning and popping up intermittently throughout the day; lows near 46F and highs near 65F",
            "Showers scattered throughout the day, with lows near 46F and highs of 63F",
            "Showers increasing in intensity towards evening, with lows near 46F and highs near 64F",
            "Steady rain all day; lows near 47F and highs near 62F",
            "More steady rain, building in intensity towards evening; lows near 47F and highs near 61F",
            "Very, very strong winds and heavy rain; make sure you're wearing your raincoat today; lows near 50F and highs near 65F",
            "Rain ending in the very early morning, then clearing, with residual strong winds; lows near 61F and highs around 70F",
            "Beautiful day, with nothing but sunshine; lows near 55F and highs around 77F",
            "Another gorgeous day; lows near 56F and highs around 81F"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mForecastListRV = findViewById(R.id.rv_forecast_list);
        mLoadingErrorTV = findViewById(R.id.tv_loading_error);
        mLoadingPB = findViewById(R.id.pb_loading);

        mForecastListRV.setLayoutManager(new LinearLayoutManager(this));
        mForecastListRV.setHasFixedSize(true);

        mForecastAdapter = new ForecastAdapter(this);
        mForecastListRV.setAdapter(mForecastAdapter);

        doOpenWeatherAPISearch();
    }

    private void doOpenWeatherAPISearch() {
        String url = OpenWeatherAPIUtils.buildOpenWeatherAPISearchURL();
        Log.d(TAG, "Querying For Forecast: " + url);
        new OpenWeatherAPISearchTask().execute(url);
    }

    @Override
    public void onForecastItemClick(String detailedForecast, String forecastIcon) {
        Intent intent = new Intent(this, ForecastDetail.class);
        Bundle extras = new Bundle();
        extras.putString(OpenWeatherAPIUtils.OPEN_WEATHER_API_FORECAST, detailedForecast);
        extras.putString(OpenWeatherAPIUtils.OPEN_WEATHER_API_FORECAST_ICON, forecastIcon);
        intent.putExtras(extras);
        startActivity(intent);
    }

    class OpenWeatherAPISearchTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingPB.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... urls) {
            String url = urls[0];
            String results = null;
            try {
                results = NetworkUtils.doHTTPGet(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return results;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                mLoadingErrorTV.setVisibility(View.INVISIBLE);
                mForecastListRV.setVisibility(View.VISIBLE);
                OpenWeatherAPIUtils.Forecast[] forecasts = OpenWeatherAPIUtils.parseOpenWeatherAPISearchResults(s);
                ArrayList<String> results = new ArrayList<String>();
                ArrayList<String> icons = new ArrayList<String>();
                for (OpenWeatherAPIUtils.Forecast forecast : forecasts) {
                    String delimiter = " - ";
                    String icon = "";
                    String result = forecast.dt_txt + delimiter;
                    for (OpenWeatherAPIUtils.Description description : forecast.weather) {
                        icon = description.icon;
                        result = result + description.main + delimiter;
                    }
                    result = result + String.valueOf(forecast.main.temp) + OpenWeatherAPIUtils.OPEN_WEATHER_API_TEMP_UNIT;
                    results.add(result);
                    icons.add(icon);
                }
                mForecastAdapter.updateForecastData(results,icons);
            }
            else{
                mLoadingErrorTV.setVisibility(View.VISIBLE);
                mForecastListRV.setVisibility(View.INVISIBLE);
            }
            mLoadingPB.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_acitivity_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.option_map:
                mapForecastLocation();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void mapForecastLocation(){
        Uri intentLocation = Uri.parse("geo:0,0?q=Corvallis US");
        Intent intent = new Intent(Intent.ACTION_VIEW, intentLocation);
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }
}
