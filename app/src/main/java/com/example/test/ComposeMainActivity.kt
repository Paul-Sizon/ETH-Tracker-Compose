package com.example.test

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.tooling.preview.Preview
import com.example.test.ViewModel.MyViewModel
import com.yandex.metrica.impl.ob.by
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ComposeMainActivity : AppCompatActivity() {

    val viewModel: MyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.post()

        setContent {
            App()

        }
    }


    @Preview
    @Composable
    fun App() {
        Content()
    }


    @Composable
    fun Content(

    ) {
        val coinPrice by viewModel.trackLiveData.observeAsState()
        Text(coinPrice.toString())

    }
}

//container with grey color
//image eth logo
//text eth
//price (livedata / state)
//button to update



