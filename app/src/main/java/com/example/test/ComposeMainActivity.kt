package com.example.test

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.test.ViewModel.Cost
import com.example.test.ViewModel.MyViewModel
import com.example.test.network.models.ViewState
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
    private var lastPrice: Double = 0.0
    val viewModel: MyViewModel by viewModels()
    val fontFamily = FontFamily(Font(R.font.cutivemono_regular))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref = SharedPrefs(
            applicationContext
        ).sharedPref
        editor = SharedPrefs(
            applicationContext
        ).editor

        Log.v("TAG", "onCreate")
        viewModel.post()

        setContent {
            App()
        }
    }


    @Composable
    fun App() {
        MainScreen()
    }

    @Composable
    fun MainScreen() {
        val priceLiveData by viewModel.trackLiveData.observeAsState()
        val price = priceLiveData ?: return
        Log.v("TAG", "mainSCreen")
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

                when (price) {
                    is ViewState.Response -> showPrice(price = price.data)
                    is ViewState.Error -> showError(price.text)
                }


                Spacer(modifier = Modifier.padding(30.dp))

                buttonAndProgress()

            }


        }
    }


    @Composable
    private fun showPrice(price: Cost) {
        val color by animateColorAsState(if (price.new >= (price.old)) Color.Green else Color.Red)
        Text(
            text = price.new.toString(),
            color = color,
            fontSize = 28.sp,
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold
        )

    }

    @Composable
    private fun showError(text: String) {
        Text(text = text, color = Color.White, fontSize = 28.sp)
    }

    @Composable
    fun buttonAndProgress() {
        var progress by remember { mutableStateOf(0.0f) }
        var alpha by remember { mutableStateOf(1f) }
        var enableButton by remember { mutableStateOf(true) }

        val animatedProgress = animateFloatAsState(
            targetValue = progress,
            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
        ).value

        Box(contentAlignment = Alignment.Center) {
            if (!enableButton) {
                LinearProgressIndicator(
                    progress = animatedProgress,
                    color = Color.DarkGray,
                    modifier = Modifier
                        .matchParentSize()
                        .padding(1.dp)
                        .testTag("progressBar")
                )
            }
            OutlinedButton(
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray),
                modifier = Modifier
                    .alpha(alpha),
                enabled = enableButton,
                onClick = {
                    viewModel.post()
                    enableButton = false
                    CoroutineScope(Dispatchers.Main).launch {
                        alpha = 0.8f
                        while (progress <= 1f) {
                            progress += 0.1f
                            delay(300)
                        }
                        delay(100)
                        alpha = 1f
                        progress = 0.0f
                        enableButton = true
                    }
                }
            ) {
                Text(text = "GET LATEST PRICE", color = Color.White)
            }
        }
    }
}


