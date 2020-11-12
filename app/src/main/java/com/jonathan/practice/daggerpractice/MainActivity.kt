package com.jonathan.practice.daggerpractice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    private lateinit var mCar: Car

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val component = DaggerCarComponent.create()
        mCar = component.getCar()
        mCar.drive()
    }
}

/*

    Car
  /     \
Wheel   Engine

 */