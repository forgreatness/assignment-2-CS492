package com.example.android.connectedweather;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastItemViewHolder> {

    private ArrayList<String> mForecastData;
    private ArrayList<String> mForecastIcons;
    private OnForecastItemClickListener mOnForecastItemClickListener;

    public ForecastAdapter(OnForecastItemClickListener onForecastItemClickListener) {
        mOnForecastItemClickListener = onForecastItemClickListener;
    }

    public void updateForecastData(ArrayList<String> forecastData, ArrayList<String> forecastIcons) {
        mForecastData = forecastData;
        mForecastIcons = forecastIcons;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mForecastData != null) {
            return mForecastData.size();
        } else {
            return 0;
        }
    }

    @Override
    public ForecastItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.forecast_list_item, parent, false);
        return new ForecastItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ForecastItemViewHolder holder, int position) {
        String routeToIcon = "https://openweathermap.org/img/w/"+mForecastIcons.get(position)+".png";
        holder.bind(mForecastData.get(position),routeToIcon);
    }

    public interface OnForecastItemClickListener {
        void onForecastItemClick(String detailedForecast);
    }

    class ForecastItemViewHolder extends RecyclerView.ViewHolder {
        private TextView mForecastTextView;
        private ImageView mForecastImageView;
        private Context context;

        public ForecastItemViewHolder(View itemView) {
            super(itemView);
            mForecastTextView = (TextView)itemView.findViewById(R.id.tv_forecast_text);
            mForecastImageView = (ImageView)itemView.findViewById(R.id.iv_forecast_icon);
            context = itemView.getContext();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String forecast = mForecastData.get(getAdapterPosition());
                    String forecastIcon = mForecastIcons.get(getAdapterPosition());
                    mOnForecastItemClickListener.onForecastItemClick(forecast);
                }
            });
        }

        public void bind(String forecast, String routeToIcon) {
            mForecastTextView.setText(forecast);
            Glide.with(context).load(routeToIcon).into(mForecastImageView);
        }
    }
}
