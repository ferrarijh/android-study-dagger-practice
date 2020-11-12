package com.jonathan.practice.daggerpractice

import android.util.Log
import javax.inject.Inject

class Car @Inject constructor(e: Engine, w: Wheel) {
    private val engine: Engine = e
    private val wheel: Wheel = w

    fun drive(){
        Log.d("", "driving..")
    }
}