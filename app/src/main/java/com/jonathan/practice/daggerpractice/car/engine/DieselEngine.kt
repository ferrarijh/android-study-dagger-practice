package com.jonathan.practice.daggerpractice.car.engine

import android.util.Log
import javax.inject.Inject
import javax.inject.Named

class DieselEngine @Inject constructor(private val horsepower: Int): Engine{
    override fun start() {
        Log.d("", "Starting Diesel Engine - hp[$horsepower]")
    }
}