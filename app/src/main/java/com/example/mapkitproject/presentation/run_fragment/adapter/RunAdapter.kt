package com.example.mapkitproject.presentation.run_fragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mapkitproject.databinding.ItemRunBinding
import com.example.mapkitproject.domain.models.Run
import com.example.mapkitproject.other.TrackingUtility
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RunAdapter(

) : RecyclerView.Adapter<RunAdapter.RunViewHolder>() {


    val diffCallback = object : DiffUtil.ItemCallback<Run>() {
        override fun areItemsTheSame(oldItem: Run, newItem: Run): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Run, newItem: Run): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(list: List<Run>) = differ.submitList(list)


    class RunViewHolder(val binding: ItemRunBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRunBinding.inflate(inflater, parent, false)

        return RunViewHolder(binding)
    }


    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: RunViewHolder, position: Int) {
        val run = differ.currentList[position]

        with(holder.binding) {
            holder.itemView.apply {
                Glide.with(this).load(run.img).into(holder.binding.ivRunImage)

                val calendar = Calendar.getInstance().apply {
                    timeInMillis = run.timestamp
                }
                val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
                tvDate.text = dateFormat.format(calendar.time)

                val avgSpeed = "${run.avgSpeedInKMH}km/h"
                tvAvgSpeed.text = avgSpeed

                val distanceInKm = "${run.distanceInMeters / 1000f}km"
                tvDistance.text = distanceInKm

                tvTime.text = TrackingUtility.getFormattedStopWatchTime(run.timeInMillis)

                val caloriesBurned = "${run.caloriesBurned}kcal"
                tvCalories.text = caloriesBurned
            }
        }
    }


}