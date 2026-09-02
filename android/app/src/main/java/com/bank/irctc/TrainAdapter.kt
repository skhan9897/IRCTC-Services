package com.bank.irctc

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bank.irctc.databinding.ItemTrainBinding
import com.bank.irctc.models.TrainSchedule

class TrainAdapter(
    private val schedules: List<TrainSchedule>,
    private val onTrainClick: (TrainSchedule) -> Unit
) : RecyclerView.Adapter<TrainAdapter.TrainViewHolder>() {

    class TrainViewHolder(val binding: ItemTrainBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainViewHolder {
        val binding = ItemTrainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrainViewHolder, position: Int) {
        val schedule = schedules[position]
        val train = schedule.train
        holder.binding.apply {
            trainName.text = train.trainName
            trainNumber.text = "#${train.trainNumber}"
            depTime.text = schedule.departureTime ?: train.departureTime
            arrTime.text = schedule.arrivalTime ?: train.arrivalTime
            source.text = train.source
            destination.text = train.destination
            fare.text = "₹${train.sleeperFare}"
            availableSeats.text = "Available: ${schedule.availableSeats}"
            
            root.setOnClickListener { onTrainClick(schedule) }
        }
    }

    override fun getItemCount() = schedules.size
}
