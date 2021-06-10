package com.example.test.ext

import java.math.BigDecimal
import java.math.RoundingMode

fun Double.round(places:Int):Double{
    return BigDecimal(this).setScale(places, RoundingMode.HALF_EVEN).toDouble()
}