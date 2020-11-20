package com.jonathan.practice.daggerpractice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jonathan.practice.daggerpractice.application.BaseApplication
import com.jonathan.practice.daggerpractice.car.Car
import com.jonathan.practice.daggerpractice.di.component.DaggerCarComponent
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject lateinit var car1: Car
    @Inject lateinit var car2: Car

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /** test singleton per component **/
//        val component1 = DaggerCarComponent.builder()
//            .horsepower(200)
//            .torque(50)
//            .build()
//
//        val component2 = DaggerCarComponent.builder()
//            .horsepower(200)
//            .torque(50)
//            .build()
//
//        val car1 = component1.getCar()
//        val car2 = component2.getCar()
//
//        car1.drive()
//        car2.drive()

        /** test custom scopes and dependencies **/
        val carComponent = DaggerCarComponent.builder()
            .horsepower(200)
            .torque(50)
            .appComponent((application as BaseApplication).appComponent)    //driver injected here
            .build()

        carComponent.inject(this)

        car1.drive()
        car2.drive()

    }
}