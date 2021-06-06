package com.example.cards

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ImageView
import android.widget.TextView


class CarDetailActivity : AppCompatActivity() {

    companion object {
        val EXTRA_CAR_ID = "carId";
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_detail)

        val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        val actionBar: ActionBar? = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        val carId = intent.extras!![EXTRA_CAR_ID] as Int
        val carName: String = Car.cars[carId].name
        val carColor = Car.cars[carId].color;
        val carDesc = Car.cars[carId].details
        val textView = findViewById<View>(R.id.car_text) as TextView
        textView.text = "$carName\nColor is $carColor\nDetails: $carDesc"
        val cocktailImage: Int = Car.cars[carId].imageId
        val imageView: ImageView = findViewById<View>(R.id.car_image) as ImageView
        imageView.setImageDrawable(ContextCompat.getDrawable(this, cocktailImage))
        imageView.contentDescription = carName
    }
}