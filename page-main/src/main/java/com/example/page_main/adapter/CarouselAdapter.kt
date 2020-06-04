package com.example.page_main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.page_main.R

class CarouselAdapter(val gameNames: Array<String>): RecyclerView.Adapter<CarouselAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return gameNames.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.carousel_item, parent, false))
    }

    override fun onBindViewHolder(holder: CarouselAdapter.ViewHolder, position: Int) {
        holder?.tvName.text = gameNames.get(position)
    }

    fun getItem(index: Int) : String{
        return gameNames.get(index)
    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each animal to
//        val tvStationId = view.findViewById<TextView>(R.id.tv_item_id)
        val tvName = view.findViewById<TextView>(R.id.tvItemName)
    }
}