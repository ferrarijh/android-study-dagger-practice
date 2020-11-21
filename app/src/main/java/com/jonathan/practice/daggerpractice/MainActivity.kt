package com.jonathan.practice.daggerpractice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jonathan.practice.daggerpractice.application.BaseApplication
import com.jonathan.practice.daggerpractice.car.Car
import com.jonathan.practice.daggerpractice.di.component.DaggerAppComponent
import com.jonathan.practice.daggerpractice.di.module.DieselEngineModule
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

        /** test custom scopes and dependencies **/
//        val carComponent = DaggerCarComponent.builder()
//            .horsepower(200)
//            .torque(50)
//            .appComponent((application as BaseApplication).appComponent)    //driver injected here
//            .build()

        /** test Subcomponent **/
//        val carComponent = (application as BaseApplication).appComponent
//            .getCarComponent(DieselEngineModule(200))

        /** test Subcomponent.Builder **/
//        val carComponent = (application as BaseApplication).appComponent
//            .getCarComponentBuilder()
//            .horsepower(200)
//            .torque(50)
//            .build()

        /** test Subcomponent.Factory **/
        val carComponent = (application as BaseApplication).appComponent
            .getCarComponentFactory()
            .create(200, 50)

        carComponent.inject(this)
        car1.drive()
        car2.drive()


    }
}