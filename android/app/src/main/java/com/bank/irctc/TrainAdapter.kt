package com.bank.irctc

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bank.irctc.databinding.ItemTrainBinding
import com.bank.irctc.models.Train
import com.bank.irctc.models.TrainSchedule

class TrainAdapter(
    private val items: List<Any>, // Can be Train or TrainSchedule
    private val onTrainClick: (Any) -> Unit
) : RecyclerView.Adapter<TrainAdapter.TrainViewHolder>() {

    class TrainViewHolder(val binding: ItemTrainBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainViewHolder {
        val binding = ItemTrainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrainViewHolder, position: Int) {
        val item = items[position]
        holder.binding.apply {
            if (item is TrainSchedule) {
                val train = item.train
                trainName.text = train.trainName
                trainNumber.text = "#${train.trainNumber}"
                depTime.text = item.departureTime ?: train.departureTime
                arrTime.text = item.arrivalTime ?: train.arrivalTime
                source.text = train.source
                destination.text = train.destination
                fare.text = "₹${train.sleeperFare}"
                availableSeats.text = "Available: ${item.availableSeats}"
            } else if (item is Train) {
                trainName.text = item.trainName
                trainNumber.text = "#${item.trainNumber}"
                depTime.text = item.departureTime
                arrTime.text = item.arrivalTime
                source.text = item.source
                destination.text = item.destination
                fare.text = "₹${item.sleeperFare}"
                availableSeats.text = "View Route"
            }
            
            root.setOnClickListener { onTrainClick(item) }
        }
    }

    override fun getItemCount() = items.size
}
