package com.jonathan.practice.daggerpractice.car.engine

import android.util.Log
import javax.inject.Inject
import javax.inject.Named

class PetrolEngine @Inject constructor(
    @Named("hp") private val horsepower: Int,
    @Named("torque") private val torque: Int
) : Engine{
    override fun start() {
        Log.d("", "starting Petrol Engine with: hp=$horsepower, torque=$torque")
    }
}