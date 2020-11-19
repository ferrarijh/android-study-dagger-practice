package com.jonathan.practice.daggerpractice.car.remote

import android.util.Log
import com.jonathan.practice.daggerpractice.car.Car
import javax.inject.Inject

class Remote @Inject constructor(){

    fun setListener(car: Car){
        Log.d("", "Remote connected.")
    }
}