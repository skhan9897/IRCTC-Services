package com.bank.irctc

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bank.irctc.databinding.ItemTrainBinding
import com.bank.irctc.models.Train

class TrainAdapter(
    private val trains: List<Train>,
    private val onTrainClick: (Train) -> Unit
) : RecyclerView.Adapter<TrainAdapter.TrainViewHolder>() {

    class TrainViewHolder(val binding: ItemTrainBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainViewHolder {
        val binding = ItemTrainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrainViewHolder, position: Int) {
        val train = trains[position]
        holder.binding.apply {
            trainName.text = train.trainName
            trainNumber.text = "#${train.trainNumber}"
            depTime.text = train.departureTime
            arrTime.text = train.arrivalTime
            source.text = train.source
            destination.text = train.destination
            fare.text = "₹${train.sleeperFare}"
            availableSeats.text = "Available: ${train.availableSeats}"
            
            root.setOnClickListener { onTrainClick(train) }
        }
    }

    override fun getItemCount() = trains.size
}
