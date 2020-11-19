package com.jonathan.practice.daggerpractice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jonathan.practice.daggerpractice.car.Car
import com.jonathan.practice.daggerpractice.car.EngineWithHpModule
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject lateinit var mCar: Car

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val component = DaggerCarComponent.builder()
            .horsepower(200)
            .torque(50)
            .build()

        //mCar = component.getCar()

        component.inject(this)
        mCar.drive()
    }
}
