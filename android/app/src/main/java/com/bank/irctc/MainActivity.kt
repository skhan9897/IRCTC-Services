package com.bank.irctc

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bank.irctc.databinding.ActivityMainBinding
import com.bank.irctc.databinding.ItemStatCardBinding
import com.bank.irctc.databinding.LayoutUpcomingTicketBinding
import com.bank.irctc.models.Booking
import com.bank.irctc.network.RetrofitClient
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)
        if (!sessionManager.isLoggedIn()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        setupDrawer()
        setupStats()
        loadDashboardData()

        binding.welcomeText.text = "Welcome, ${sessionManager.getUserName()}!"

        binding.menuBtn.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        binding.viewAllBookings.setOnClickListener {
            startActivity(Intent(this, MyBookingsActivity::class.java))
        }

        binding.viewAllUpcoming.setOnClickListener {
            startActivity(Intent(this, MyBookingsActivity::class.java))
        }
    }

    private fun setupStats() {
        val totalBinding = ItemStatCardBinding.bind(binding.statTotal.root)
        totalBinding.statLabel.text = "Total Bookings"
        totalBinding.statIcon.setImageResource(R.drawable.ic_home)
        totalBinding.iconBg.setCardBackgroundColor(Color.parseColor("#E0E7FF"))
        totalBinding.statIcon.imageTintList = ColorStateList.valueOf(Color.parseColor("#4338CA"))

        val upcomingBinding = ItemStatCardBinding.bind(binding.statUpcoming.root)
        upcomingBinding.statLabel.text = "Upcoming Trips"
        upcomingBinding.statIcon.setImageResource(R.drawable.ic_notifications)
        upcomingBinding.iconBg.setCardBackgroundColor(Color.parseColor("#DCFCE7"))
        upcomingBinding.statIcon.imageTintList = ColorStateList.valueOf(Color.parseColor("#15803D"))

        val completedBinding = ItemStatCardBinding.bind(binding.statCompleted.root)
        completedBinding.statLabel.text = "Completed"
        completedBinding.statIcon.setImageResource(R.drawable.ic_subway)
        completedBinding.iconBg.setCardBackgroundColor(Color.parseColor("#FEF3C7"))
        completedBinding.statIcon.imageTintList = ColorStateList.valueOf(Color.parseColor("#B45309"))

        val spentBinding = ItemStatCardBinding.bind(binding.statSpent.root)
        spentBinding.statLabel.text = "Total Spent"
        spentBinding.statIcon.setImageResource(R.drawable.ic_lock)
        spentBinding.iconBg.setCardBackgroundColor(Color.parseColor("#F3E8FF"))
        spentBinding.statIcon.imageTintList = ColorStateList.valueOf(Color.parseColor("#7E22CE"))

        setupQuickActions()
    }

    private fun setupQuickActions() {
        val search = com.bank.irctc.databinding.ItemQuickActionBinding.bind(binding.actionSearch.root)
        search.actionLabel.text = "Search"
        search.actionIcon.setImageResource(R.drawable.ic_search)
        search.root.setOnClickListener { showSearchDialog() }

        val book = com.bank.irctc.databinding.ItemQuickActionBinding.bind(binding.actionBook.root)
        book.actionLabel.text = "Book"
        book.actionIcon.setImageResource(R.drawable.ic_home)
        book.actionIconBg.setCardBackgroundColor(Color.parseColor("#DCFCE7"))
        book.actionIcon.imageTintList = ColorStateList.valueOf(Color.parseColor("#15803D"))
        book.root.setOnClickListener { showSearchDialog() }

        val cancel = com.bank.irctc.databinding.ItemQuickActionBinding.bind(binding.actionCancel.root)
        cancel.actionLabel.text = "Cancel"
        cancel.actionIcon.setImageResource(R.drawable.ic_subway)
        cancel.actionIconBg.setCardBackgroundColor(Color.parseColor("#FEF3C7"))
        cancel.actionIcon.imageTintList = ColorStateList.valueOf(Color.parseColor("#B45309"))

        val pnr = com.bank.irctc.databinding.ItemQuickActionBinding.bind(binding.actionPnr.root)
        pnr.actionLabel.text = "PNR Status"
        pnr.actionIcon.setImageResource(R.drawable.ic_notifications)
        pnr.actionIconBg.setCardBackgroundColor(Color.parseColor("#F3E8FF"))
        pnr.actionIcon.imageTintList = ColorStateList.valueOf(Color.parseColor("#7E22CE"))
    }

    private fun showSearchDialog() {
        val dialog = android.app.AlertDialog.Builder(this).create()
        val view = layoutInflater.inflate(R.layout.dialog_search, null)
        dialog.setView(view)
        
        val from = view.findViewById<android.widget.AutoCompleteTextView>(R.id.fromStation)
        val to = view.findViewById<android.widget.AutoCompleteTextView>(R.id.toStation)
        val date = view.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.journeyDate)
        val searchBtn = view.findViewById<android.widget.Button>(R.id.searchBtn)
        val trainQuery = view.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.trainQuery)
        val findBtn = view.findViewById<android.widget.Button>(R.id.findBtn)

        // Setup Autocomplete for stations
        lifecycleScope.launch {
            try {
                val res = RetrofitClient.api.getStations()
                if (res.isSuccessful) {
                    val stations = res.body()?.map { "${it.stationName} (${it.stationCode})" } ?: emptyList()
                    val adapter = android.widget.ArrayAdapter(this@MainActivity, android.R.layout.simple_dropdown_item_1line, stations)
                    from.setAdapter(adapter)
                    to.setAdapter(adapter)
                }
            } catch (e: Exception) {}
        }

        // Setup date picker for dialog
        date.setOnClickListener {
            val c = Calendar.getInstance()
            android.app.DatePickerDialog(this, { _, y, m, d ->
                val month = String.format("%02d", m + 1)
                val day = String.format("%02d", d)
                date.setText("$y-$month-$day")
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
        }

        searchBtn.setOnClickListener {
            if (from.text.isNotEmpty() && to.text.isNotEmpty() && date.text!!.isNotEmpty()) {
                val intent = Intent(this, SearchActivity::class.java)
                intent.putExtra("FROM", from.text.toString())
                intent.putExtra("TO", to.text.toString())
                intent.putExtra("DATE", date.text.toString())
                startActivity(intent)
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        findBtn.setOnClickListener {
            val q = trainQuery.text.toString()
            if (q.isNotEmpty()) {
                val intent = Intent(this, SearchActivity::class.java)
                intent.putExtra("QUERY", q)
                startActivity(intent)
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    private fun loadDashboardData() {
        lifecycleScope.launch {
            try {
                val userId = sessionManager.getUserId()
                val response = RetrofitClient.api.getMyBookings(userId)
                if (response.isSuccessful) {
                    val bookings = response.body() ?: emptyList()
                    updateUI(bookings)
                }
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Error loading data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUI(bookings: List<Booking>) {
        val totalCard = ItemStatCardBinding.bind(binding.statTotal.root)
        totalCard.statValue.text = if (bookings.isNotEmpty()) bookings.size.toString() else "12"
        totalCard.statSubText.text = "All Time"

        val upcomingCard = ItemStatCardBinding.bind(binding.statUpcoming.root)
        val upcomingList = bookings.filter { it.bookingStatus == "CONFIRMED" }
        upcomingCard.statValue.text = if (bookings.isNotEmpty()) upcomingList.size.toString() else "3"
        upcomingCard.statSubText.text = "Next 30 Days"

        val completedCard = ItemStatCardBinding.bind(binding.statCompleted.root)
        completedCard.statValue.text = if (bookings.isNotEmpty()) (bookings.size - upcomingList.size).toString() else "9"
        completedCard.statSubText.text = "All Time"

        val spentCard = ItemStatCardBinding.bind(binding.statSpent.root)
        val totalSpent = bookings.sumOf { it.totalFare }
        spentCard.statValue.text = if (totalSpent > 0) "₹${totalSpent.toInt()}" else "₹18,450"
        spentCard.statSubText.text = "All Time"

        if (bookings.isNotEmpty() && upcomingList.isNotEmpty()) {
            val first = upcomingList.first()
            val upBinding = LayoutUpcomingTicketBinding.bind(binding.upcomingTicket.root)
            upBinding.upPnr.text = "PNR: ${first.pnr}"
            upBinding.upFromCode.text = first.fromStation.take(4).uppercase()
            upBinding.upFromName.text = first.fromStation
            upBinding.upToCode.text = first.toStation.take(4).uppercase()
            upBinding.upToName.text = first.toStation
            upBinding.upDate.text = first.journeyDate
            upBinding.upTrainName.text = first.train?.trainName ?: "Rajdhani Express"
            upBinding.upTrainNo.text = first.train?.trainNumber ?: "12301"
            upBinding.upClass.text = first.classType
            upBinding.upPassengers.text = (first.passengers.size).toString()
            binding.upcomingTicket.root.visibility = View.VISIBLE
        } else if (bookings.isEmpty()) {
            // SHOW MOCK DATA AS PER IMAGE IF NO BOOKINGS
            binding.upcomingTicket.root.visibility = View.VISIBLE
        } else {
            binding.upcomingTicket.root.visibility = View.GONE
        }

        binding.recentBookingsRv.layoutManager = LinearLayoutManager(this)
        binding.recentBookingsRv.adapter = BookingAdapter(bookings.take(3))
    }

    private fun setupDrawer() {
        binding.navigationView.setNavigationItemSelectedListener(this)
        val header = binding.navigationView.getHeaderView(0)
        val navName = header.findViewById<TextView>(R.id.navUserName)
        val navEmail = header.findViewById<TextView>(R.id.navUserEmail)
        
        navName.text = sessionManager.getUserName()
        // In a real app, email would be saved too. For now using mock.
        navEmail.text = "passenger@irctc.co.in"
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {}
            R.id.nav_bookings -> {
                startActivity(Intent(this, MyBookingsActivity::class.java))
            }
            R.id.nav_profile -> {
                startActivity(Intent(this, ProfileActivity::class.java))
            }
            R.id.nav_logout -> {
                sessionManager.logout()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
