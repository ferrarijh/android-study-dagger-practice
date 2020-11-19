package com.jonathan.practice.daggerpractice.car

import android.util.Log
import com.jonathan.practice.daggerpractice.car.engine.Engine
import com.jonathan.practice.daggerpractice.car.remote.Remote
import com.jonathan.practice.daggerpractice.car.wheel.Wheel
import javax.inject.Inject

class Car @Inject constructor(e: Engine, w: Wheel) {
    private val engine: Engine = e
    private val wheel: Wheel = w

    fun drive(){
        engine.start()
        Log.d("", "driving..")
    }

    @Inject //method injection
    fun enableRemote(r: Remote){
        r.setListener(this) //cannot be run in constructor since 'this' is not fully instantiated in constructor.
    }
}