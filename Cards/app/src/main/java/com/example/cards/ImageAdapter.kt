package com.example.cards

import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView


class ImageAdapter(private var captions: Array<String>, private var imageIds: Array<Int>): RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    lateinit var viewHolder: CardView;

    class ViewHolder(var cardView: CardView) : RecyclerView.ViewHolder(cardView) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cv = LayoutInflater.from(parent.context).inflate(R.layout.card_image, parent, false) as CardView;
        viewHolder = cv;
        return ViewHolder(cv);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cardView = holder.cardView;
        val imageView = cardView.findViewById<ImageView>(R.id.info_image)
        val drawable = ContextCompat.getDrawable(cardView.context, imageIds[position])
        imageView.foreground = drawable;
        imageView.contentDescription = captions[position]
        val textView = cardView.findViewById<View>(R.id.info_text) as TextView
        textView.text = captions[position];
        cardView.setOnClickListener {
            listener.onClick(position);
        }
    }

    override fun getItemCount(): Int {
        return captions.size;
    }

    lateinit var listener: Listener;

    interface Listener {
        fun onClick(position: Int);
    }
}