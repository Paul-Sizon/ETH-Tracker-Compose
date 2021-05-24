package com.example.test


import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.test.ViewModel.MyViewModel
import com.example.test.databinding.ActivityMainBinding
import com.example.test.sharedPrefs.SharedPrefs
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var sharedPref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    private val viewModel: MyViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = SharedPrefs(
            applicationContext
        ).sharedPref
        editor = SharedPrefs(
            applicationContext
        ).editor

        subscribeUi()
        subsrcibeObservers()


    }


    private fun subscribeUi() {
        binding.btn.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            viewModel.post()
        }

    }

    private fun subsrcibeObservers() {
//        viewModel.trackLiveData.observe(this, { response ->
//            if (response != null) {
//                val price = response.rate
//                binding.tvPrice.text = price.toString()
//                highOrLow(price.toString())
//                binding.progressBar.visibility = View.GONE
//                editor.putString("btc", price.toString()).apply()
//            } else {
//                binding.progressBar.visibility = View.GONE
//                Toast.makeText(this, "Oops", Toast.LENGTH_SHORT).show()
//            }
//        })

    }

    private fun highOrLow(currentPrice: String){
        val lastPrice = sharedPref.getString("btc", "0")
        if (currentPrice.toFloat() >= lastPrice?.toFloat()!!) {
            binding.apply {
                tvMoon.visibility = View.VISIBLE
                tvOuch.visibility = View.GONE
            }
        } else {
            binding.apply {
                tvMoon.visibility = View.GONE
                tvOuch.visibility = View.VISIBLE
            }
        }



    }


}