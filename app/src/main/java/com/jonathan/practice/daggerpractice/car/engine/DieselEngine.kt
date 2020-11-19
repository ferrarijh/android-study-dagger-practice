package com.jonathan.practice.daggerpractice.car.engine

import android.util.Log
import javax.inject.Inject

class DieselEngine @Inject constructor(): Engine{
    override fun start() {
        Log.d("", "Diesel Engine started")
    }
}