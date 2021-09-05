package com.example.marsgateway.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.marsgateway.R
import com.example.marsgateway.databinding.ItemMarsWeatherBinding
import com.example.marsgateway.model.MarsWeather.Weather

class  WeatherListAdapter(val DataList:ArrayList<Weather>): RecyclerView.Adapter<WeatherListAdapter.MyViewHolder>(){
    class MyViewHolder(val binding: ItemMarsWeatherBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mars_weather, parent, false)
        return MyViewHolder(ItemMarsWeatherBinding.bind(view))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.dateText.text = DataList[position].earthDate
        holder.binding.solText.text = DataList[position].sol
        holder.binding.temperText.text = DataList[position].weather
    }
    override fun getItemCount() = DataList.size

}