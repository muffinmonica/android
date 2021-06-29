package com.example.weather

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.RecoverySystem
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.gms.maps.model.LatLng

class CityListActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {
    private lateinit var city: City;
    private lateinit var dbHelper: DBHelper;
    private lateinit var cityList: ArrayList<City>;
    private lateinit var adapter: CityListAdapter;
    private lateinit var recyclerView: RecyclerView;
    private lateinit var swipeLayout: SwipeRefreshLayout;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_list);
        dbHelper = DBHelper(this);

        recyclerView = findViewById<RecyclerView>(R.id.city_list);
        val layout = LinearLayoutManager(this);
        recyclerView.layoutManager = layout;
        swipeLayout = findViewById(R.id.swipe_layout);
        swipeLayout.setOnRefreshListener(this);


        Thread {
            run {
                cityList = dbHelper.getCityList(this);
            }
            runOnUiThread {
                adapter = CityListAdapter(cityList, this);
                val clickListener = object : ItemClickListener {
                    override fun onItemClick(view: View?, position: Int) {
                        val dialogClickListener = DialogInterface.OnClickListener { dialog, which ->
                            when(which) {
                                DialogInterface.BUTTON_POSITIVE -> {
                                    val city = cityList[position];
                                    cityList.remove(city);
                                    dbHelper.removeCity(city);
//                                    recyclerView.adapter = null;
//                                    recyclerView.layoutManager = null;
//                                    recyclerView.adapter = adapter;
//                                    recyclerView.layoutManager = layout;
                                    adapter.notifyItemRemoved(position);
                                }
                                DialogInterface.BUTTON_NEGATIVE -> {}
                            }
                        }
                        val dialog = AlertDialog.Builder(this@CityListActivity);
                        dialog.setMessage("Do you want to delete ${cityList[position].cityName}?")
                            .setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener)
                            .show();
                    }
                }
                val swipeCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                    override fun onMove(
                        recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder
                    ): Boolean {
                        return false;
                    }

                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        val position = viewHolder.adapterPosition;
                        val city = cityList[position];
                        cityList.remove(city);
                        dbHelper.removeCity(city);
                        adapter.notifyItemRemoved(position);
                    }

                }
                val itemTouchHelper = ItemTouchHelper(swipeCallback);
                itemTouchHelper.attachToRecyclerView(recyclerView);
                adapter.setClickListener(clickListener);
                recyclerView.adapter = adapter;
            }
        }.start()
    }

    override fun onRefresh() {
        Thread {
            run {
                adapter.updateData();
            }
            runOnUiThread {
                adapter.notifyDataSetChanged();
                swipeLayout.isRefreshing = false;
            }
        }.start()
    }
}