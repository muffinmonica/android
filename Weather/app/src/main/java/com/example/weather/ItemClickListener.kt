package com.example.weather

import android.view.View

interface ItemClickListener {
    fun onItemClick(view: View?, position: Int)
}