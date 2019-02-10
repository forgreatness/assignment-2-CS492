package com.example.android.connectedweather;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Debug;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class ForecastDetail extends AppCompatActivity {
    private TextView mDetailForecastTV;
    private ImageView mForecastIconIV;
    private String mForecast;
    private String mForecastIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast_detail);

        mDetailForecastTV = findViewById(R.id.tv_detail_forecast);
        mForecastIconIV = findViewById(R.id.iv_forecast_icon);

        Intent intent = getIntent();
        if(intent != null && intent.getExtras() != null){
            Bundle extras = intent.getExtras();
            mForecast = extras.getString(OpenWeatherAPIUtils.OPEN_WEATHER_API_FORECAST);
            mForecastIcon = extras.getString(OpenWeatherAPIUtils.OPEN_WEATHER_API_FORECAST_ICON);
            mDetailForecastTV.setText(mForecast);
            Glide.with(getBaseContext()).load("https://openweathermap.org/img/w/"+mForecastIcon+".png").into(mForecastIconIV);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.forecast_detail_activity_option, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.option_share:
                shareForecast();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void shareForecast(){
        if(mForecast != null){
            ShareCompat.IntentBuilder.from(this)
                    .setType("text/plain")
                    .setText(mForecast)
                    .setChooserTitle("How would you like to share this forecast")
                    .startChooser();

        }
    }
}
