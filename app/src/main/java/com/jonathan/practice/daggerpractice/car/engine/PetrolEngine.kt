package com.jonathan.practice.daggerpractice.car.engine

import android.util.Log
import javax.inject.Inject

class PetrolEngine @Inject constructor() : Engine{
    override fun start() {
        Log.d("", "Petrol Engine started")
    }

}