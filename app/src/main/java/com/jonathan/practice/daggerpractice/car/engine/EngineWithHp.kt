package com.jonathan.practice.daggerpractice.car.engine

import android.util.Log

//inject values at runtime - NO @Inject here.
class EngineWithHp(val horsepower: Int): Engine{
    override fun start() {
        Log.d("", "starting engine with hp[$horsepower]..")
    }
}