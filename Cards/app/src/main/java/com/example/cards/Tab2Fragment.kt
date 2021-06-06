package com.example.cards

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView


class Tab2Fragment : Fragment() {
    private var carId = -1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState != null) {
            carId = savedInstanceState.getInt("id");
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tab2, container, false);
        return view;
    }

    override fun onStart() {
        if(carId != -1) {
            id = carId;
        }
        return super.onStart();
    }

    fun setId(carId: Int) {
        this.carId = carId;
        val carName: String = Car.cars[carId].name
        val carColor = Car.cars[carId].color;
        val carDesc = Car.cars[carId].details
        val textView = view!!.findViewById<View>(R.id.car_text) as TextView
        textView.text = "$carName\nColor is $carColor\nDetails: $carDesc"
        val cocktailImage: Int = Car.cars[carId].imageId
        val imageView: ImageView = view!!.findViewById<View>(R.id.car_image) as ImageView
        imageView.setImageDrawable(ContextCompat.getDrawable(context!!, cocktailImage))
        imageView.contentDescription = carName
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("id", carId);
    }
}