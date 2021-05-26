package com.example.test

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

                showPrice(price = price)

                Spacer(modifier = Modifier.padding(30.dp))

                buttonAndProgress()

            }


        }
    }

    @Composable
    fun buttonAndProgress() {
        var progress by remember { mutableStateOf(0.0f) }
        var mycolor by remember { mutableStateOf(Color.DarkGray) }
        var enableButton by remember { mutableStateOf(true) }

        val animatedProgress = animateFloatAsState(
            targetValue = progress,
            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
        ).value
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(
                colors = ButtonDefaults.buttonColors(backgroundColor = mycolor),
                enabled = enableButton,
                onClick = {
                    viewModel.post()
                    enableButton = false
                    CoroutineScope(Dispatchers.Main).launch {
                        mycolor = Color.LightGray
                        while (progress <= 1f) {
                            progress += 0.1f
                            delay(300)
                        }
                        delay(100)
                        mycolor = Color.DarkGray
                        progress = 0.0f
                        enableButton = true
                    }
                }
            ) {
                Text(text = "GET LATEST PRICE", color = Color.White)
            }

            Spacer(Modifier.height(20.dp))
            if (!enableButton) {
                LinearProgressIndicator(
                    progress = animatedProgress,
                    color = Color.White,
                    modifier = Modifier.width(100.dp)
                )
            }

        }


    }

    @Composable
    private fun showPrice(price: String) {
        lastPrice = sharedPref.getString("eth", "0.0")
        editor.putString("eth", price).apply()

        if (lastPrice == "null") lastPrice = "0.0"
        if (price != "null") {
            if (price.toFloat() >= lastPrice?.toFloat()!!) {
                Text(text = price, color = Color.Green, fontSize = 30.sp)
            } else {
                Text(text = price, color = Color.Red, fontSize = 30.sp)
            }
        } else {
            Text(text = price, color = Color.White, fontSize = 30.sp)
        }
    }

    @Preview
    @Composable
    fun MyPreview() {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray),
                onClick = {}
            ) {
                Text(text = "GET LATEST PRICE", color = Color.White)
            }
            Spacer(Modifier.height(20.dp))
                LinearProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.width(150.dp)
                )
        }
    }
}




