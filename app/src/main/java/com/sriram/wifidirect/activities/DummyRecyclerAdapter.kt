package com.sriram.wifidirect.activities

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout

import androidx.recyclerview.widget.RecyclerView
import com.sriram.wifidirect.R


class DummyRecyclerAdapter : RecyclerView.Adapter<DummyRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DummyRecyclerAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_folder_layout, parent, false) as ConstraintLayout)
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onBindViewHolder(holder: DummyRecyclerAdapter.ViewHolder, position: Int) = Unit

    class ViewHolder(val layout: ConstraintLayout) : RecyclerView.ViewHolder(layout)

}