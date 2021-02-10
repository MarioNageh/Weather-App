package com.musala.app.weather.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.musala.app.weather.databinding.RvWeatherItemBinding;
import com.musala.app.weather.modules.WeatherProperties;

import java.util.ArrayList;

public class PropertiesAdapter extends RecyclerView.Adapter<PropertiesAdapter.ViewHolder> {

    private ArrayList<WeatherProperties> weatherProperties;
    private Context context;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(context==null)
            context=parent.getContext();
        return new ViewHolder(RvWeatherItemBinding.inflate(LayoutInflater.from(context),parent,false));
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.viewHolder.txtPro.setText(weatherProperties.get(position).getPro_name());
        holder.viewHolder.txtProValue.setText(weatherProperties.get(position).getPro_value());
        holder.viewHolder.imgPro.setImageResource(weatherProperties.get(position).getImg_drawable());
    }

    @Override
    public int getItemCount() {
        return weatherProperties==null?0:weatherProperties.size();
    }

    public void swipeAdapter(ArrayList<WeatherProperties> prosList){
        if(prosList!=null){
            this.weatherProperties=prosList;
            notifyDataSetChanged();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private RvWeatherItemBinding viewHolder;

        public ViewHolder(RvWeatherItemBinding rvWeatherItemBinding) {
            super(rvWeatherItemBinding.getRoot());
            viewHolder=rvWeatherItemBinding;
        }
    }



}
