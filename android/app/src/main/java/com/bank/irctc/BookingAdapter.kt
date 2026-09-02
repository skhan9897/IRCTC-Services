package com.bank.irctc

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bank.irctc.databinding.ItemBookingBinding
import com.bank.irctc.models.Booking

class BookingAdapter(private val bookings: List<Booking>) : RecyclerView.Adapter<BookingAdapter.BookingViewHolder>() {

    class BookingViewHolder(val binding: ItemBookingBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val binding = ItemBookingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val booking = bookings[position]
        holder.binding.apply {
            pnrText.text = "PNR: ${booking.pnr ?: "N/A"}"
            statusText.text = booking.bookingStatus ?: "CONFIRMED"
            trainName.text = booking.train?.trainName ?: "Train Details"
            routeText.text = "${booking.fromStation} -> ${booking.toStation}"
            dateText.text = "Date: ${booking.journeyDate}"
        }
    }

    override fun getItemCount() = bookings.size
}
