package com.jonathan.practice.daggerpractice

import com.jonathan.practice.daggerpractice.car.Car
import com.jonathan.practice.daggerpractice.car.EngineWithHpModule
import dagger.Component

@Component
    (modules = [WheelModule::class, EngineWithHpModule::class])  //now dagger knows what to do to make 'wheel'
interface CarComponent{
    fun getCar(): Car
    fun inject(activity: MainActivity)
}