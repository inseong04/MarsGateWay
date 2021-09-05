package com.example.marsgateway.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.example.marsgateway.R
import com.example.marsgateway.viewmodel.MarsRoverLandingViewModel

class MarsRoverAdapter() : RecyclerView.Adapter<MarsRoverAdapter.ViewHolder>() {

    private lateinit var viewModel : MarsRoverLandingViewModel

    constructor(viewModel : MarsRoverLandingViewModel) : this() {
        this.viewModel = viewModel
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarsRoverAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_rover_photo, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MarsRoverAdapter.ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return if (viewModel.photosList.value != null) {
            viewModel.photosList.value!!.photos.size
        } else
            0
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val rowRoverIv : ImageView = itemView.findViewById(R.id.rowRoverIv)
        val rowRoverDateTv : TextView = itemView.findViewById(R.id.rowRoverDateTv)
        val rowRoverCameraTv : TextView = itemView.findViewById(R.id.rowRoverCameraTv)
        val rowRoverSolTv : TextView = itemView.findViewById(R.id.rowRoverSolTv)
        val rowRoverRoverTv : TextView = itemView.findViewById(R.id.rowRoverRoverTv)

        fun bind(position: Int) {
            val list = viewModel.photosList.value!!.photos[position]
            rowRoverIv.load(list.img_src) {
                crossfade(true)
            }

            rowRoverDateTv.text = list.earth_date
            rowRoverCameraTv.text = list.camera.full_name
            rowRoverSolTv.text = list.sol.toString()
            rowRoverRoverTv.text = list.rover.name

        }
    }
}