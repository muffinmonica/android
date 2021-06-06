package com.example.cards

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar;

class ActionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_action);

        val toolbar = findViewById<Toolbar>(R.id.toolbar);
        setSupportActionBar(toolbar);

        val actionBar = supportActionBar;
        actionBar!!.setDisplayHomeAsUpEnabled(true);
    }
}