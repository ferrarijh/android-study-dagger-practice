package com.jonathan.practice.daggerpractice.car

import android.util.Log
import com.jonathan.practice.daggerpractice.DriverJiho
import com.jonathan.practice.daggerpractice.car.engine.Engine
import com.jonathan.practice.daggerpractice.car.remote.Remote
import com.jonathan.practice.daggerpractice.car.wheel.Wheel
import com.jonathan.practice.daggerpractice.di.annotation.MainActivityScope
import javax.inject.Inject

//@MainActivityScope
class Car @Inject constructor(
    private val engine: Engine,
    private val wheel: Wheel,
    private val driver: DriverJiho
    ) {

    fun drive(){
        engine.start()
        Log.d("", "Driver[$driver] is driving car[$this]")
    }

    @Inject //method injection
    fun enableRemote(r: Remote){
        r.setListener(this) //cannot be run in constructor since 'this' is not fully instantiated in constructor.
    }
}