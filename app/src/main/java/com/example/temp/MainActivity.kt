package com.example.temp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.example.temp.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.widget.Toast
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.Permission
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val requestcode = 1001
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(
            layoutInflater
        )
        setContentView(binding.root)
//        binding.lottieAnimationView.setAnimation(R.raw.rain)
        getlocation()
        searchcity()
    }

    private fun searchcity() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fetchWeatherData(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
    }

    private fun fetchWeatherData(cityname: String) {

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(Interface::class.java)
        val response =
            retrofit.getweatherdata(cityname, "b722636a385eb0b7643c69448e1cac83", "metric")
        response.enqueue(object : Callback<saveeee> {
            override fun onResponse(call: Call<saveeee>, response: Response<saveeee>) {
                val responsebody = response.body()
                if (response.isSuccessful && responsebody != null) {
                    val tempratue = responsebody.main.temp.toString()
                    val humidity = responsebody.main.humidity.toString()
                    val maxtemp = responsebody.main.temp_max.toString()
                    val feelslike = responsebody.main.feels_like.toString()
                    val sunrise = responsebody.sys.sunrise.toLong()
                    val sunset = responsebody.sys.sunset.toLong()
                    val pressure = responsebody.main.pressure.toString()
                    val windspeed = responsebody.wind.speed.toString()
                    val condition = responsebody.weather.firstOrNull()?.main ?: "unknown"
                    binding.temp.text = "${tempratue}"
                    binding.feelsLike.text = "feels like: ${feelslike}°C"
                    binding.min.text = "Max : ${maxtemp}°C"
                    binding.humidity.text = "${humidity}%"
                    binding.windspeed.text = "${windspeed}m/s"
                    binding.condition.text = condition
//                    binding.conditions.text = condition
                    binding.conditionfin.text = condition
                    binding.sunset.text = "${time(sunset)}"
                    binding.sunrise.text = "${time(sunrise)}"
                    binding.pressure.text = "${pressure}hpa"
                    binding.date.text = date()
                    binding.day.text = dayname(System.currentTimeMillis())
                    binding.cityname.text = cityname


                    changebackground(condition)


                }
            }

            override fun onFailure(call: Call<saveeee>, t: Throwable) {

            }


        })

    }

    private fun changebackground(condition: String) {
        when (condition) {
            "Haze", "Clouds", "Cloudy", "Windy", "Foggy" -> {
                binding.root.setBackgroundResource(R.drawable.colud_background)
                binding.lottieAnimationView.setAnimation(R.raw.cloud)
            }

            "Rain", "Drizzle", "Rainy", "Stormy", "Hail", "Storm", "Heavy rain", "Slight rain", "Rainstorm", "Thunderstorm" -> {
                binding.root.setBackgroundResource(R.drawable.rain_background)
                binding.lottieAnimationView.setAnimation(R.raw.rain)
            }

            "Sunny", "Hot", "Clear", "Heat wave" -> {
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sun)
            }

            "Snow", "Frost", "Snowy" -> {
                binding.root.setBackgroundResource(R.drawable.snow_background)
                binding.lottieAnimationView.setAnimation(R.raw.snow)
            }
        }
        binding.lottieAnimationView.playAnimation()

    }


    fun date(): String {
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdf.format((Date()))
    }

    fun time(timestamp: Long): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format((Date(timestamp * 1000)))
    }

    fun dayname(timestamp: Long): String {
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format((Date()))
    }

    fun getlocation() {
        var city:String = "temp"
        val fusedlocation: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),requestcode
            )

        }
        fusedlocation.lastLocation.addOnSuccessListener {
            location: Location? -> location?.let {

                city = getcity(it.latitude,it.longitude)

            fetchWeatherData(city)

        }
        }
//        Toast.makeText(this,"cityname : ${city}" , Toast.LENGTH_LONG).show()
    }

    private fun getcity(latitude:Double , longitude:Double):String {
        val geocoder = Geocoder(this,Locale.getDefault())
        val address = geocoder
            .getFromLocation(latitude,longitude,1)

        val city = address!![0].locality
        return city
    }

}