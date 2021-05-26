package com.example.test

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.test.ViewModel.MyViewModel
import com.example.test.sharedPrefs.SharedPrefs
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ComposeMainActivity : AppCompatActivity() {

    lateinit var sharedPref: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private var lastPrice: String? = ""
    val viewModel: MyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref = SharedPrefs(
            applicationContext
        ).sharedPref
        editor = SharedPrefs(
            applicationContext
        ).editor

        viewModel.post()

        setContent {
            App()
        }
    }


    @Composable
    fun App() {
        val priceLiveData by viewModel.trackLiveData.observeAsState()
        var price = priceLiveData.toString()

        Box(
            modifier = Modifier
                .background(Color.Gray)
                .fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ) {
                Spacer(modifier = Modifier.padding(60.dp))
                Image(
                    painter = painterResource(id = R.drawable.ethhh),
                    contentDescription = "ethereum logo",
                )
                Spacer(modifier = Modifier.padding(30.dp))

                Text(text = price, color = decideColor(price), fontSize = 30.sp)

                Spacer(modifier = Modifier.padding(30.dp))
                Button(
                    onClick = { viewModel.post() },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray)
                ) {
                    Text(text = "GET LATEST PRICE", color = Color.White)
                }

            }


        }
    }



    private fun decideColor(price: String): Color {
        lastPrice = sharedPref.getString("eth", "0.0")
        editor.putString("eth", price).apply()

        if (lastPrice == "null") lastPrice = "0.0"
        if (price != "null") {
            if (price.toFloat() >= lastPrice?.toFloat()!!) {
                return Color.Green
            } else {
                return Color.Red
            }
        } else {
            return Color.White
        }


    }


    @Preview
    @Composable
    fun PriceData() {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "2342.23564563345".dropLast(7), color = Color.White, fontSize = 30.sp)
            Spacer(modifier = Modifier.padding(30.dp))
            Button(
                onClick = { viewModel.post() },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray)
            ) {
                Text(text = "GET LATEST PRICE", color = Color.White)
            }
        }


    }
}

//container with grey color
//image eth logo
//text eth
//price (livedata / state)
//button to update



