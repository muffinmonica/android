package com.example.weather

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CityListAdapter(private val data: ArrayList<City>, private var context: Context): RecyclerView.Adapter<CityListAdapter.ViewHolder>() {

    private var mClickListener: ItemClickListener? = null;

    inner class ViewHolder(var row: View): RecyclerView.ViewHolder(row), View.OnClickListener{

        init {
            row.setOnClickListener(this);
        }

        override fun onClick(v: View?) {
            mClickListener?.onItemClick(v, adapterPosition);
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.city_list_element, parent, false);

        return ViewHolder(view);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val city = data[position];

        holder.row.findViewById<TextView>(R.id.wind_speed_text).text = context.getString(R.string.wind_speed_res).format(city.windSpeed);
        holder.row.findViewById<TextView>(R.id.temp_text).text = "${city.temperature} °C";
        val displayName = if(city.cityName == "") city.getDisplayCoords() else city.cityName;
        holder.row.findViewById<TextView>(R.id.location_text).text = "$displayName";

        val id = context.resources.getIdentifier("flag_${city.countryCode.lowercase()}", "drawable", context.packageName);
        holder.row.findViewById<ImageView>(R.id.country_flag).setImageResource(id);
    }

    override fun getItemCount() = data.size;
}