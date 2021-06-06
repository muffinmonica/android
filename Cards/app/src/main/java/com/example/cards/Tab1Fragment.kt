package com.example.cards

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast


class Tab1Fragment : Fragment() {

    var carId = -1;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tab1, container, false);
        val carRecycler = view.findViewById(R.id.tab1_recycler) as RecyclerView;
        val size = Car.cars.size;
        val carNames = Array(size){""};
        val carIds = Array(size){0};
        for(i in Car.cars.indices) {
            carNames[i] = Car.cars[i].name;
            carIds[i] = Car.cars[i].imageId;
        }
        val adapter = ImageAdapter(carNames, carIds);
        carRecycler.adapter = adapter;

        val layoutManager = GridLayoutManager(activity, 2);
        carRecycler.layoutManager = layoutManager;

        adapter.listener = (object : ImageAdapter.Listener {
            override fun onClick(position: Int) {
                val myAdapter = carRecycler.adapter;
                val myLayout = carRecycler.layoutManager;

                for (i in carIds.indices) {
                    val str = carRecycler.findViewHolderForAdapterPosition(i)!!.itemView.findViewById<TextView>(R.id.info_text)
                    str.text = carNames[i]
                }

                val str = carRecycler.findViewHolderForAdapterPosition(position)!!.itemView.findViewById<TextView>(R.id.info_text);
                str.text = carNames[position] + "\nCar is " + Car.cars[position].color;
                carId = position;
//                val intent = Intent(activity, CarDetailActivity::class.java)
//                intent.putExtra(CarDetailActivity.EXTRA_CAR_ID, position)
//                activity!!.startActivity(intent)
            }
        })

        return view;
    }
}