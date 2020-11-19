package com.jonathan.practice.daggerpractice

import com.jonathan.practice.daggerpractice.car.Car
import com.jonathan.practice.daggerpractice.car.EngineWithHpModule
import com.jonathan.practice.daggerpractice.car.engine.PetrolEngine
import dagger.BindsInstance
import dagger.Component
import javax.inject.Named

@Component
    (modules = [WheelModule::class, PetrolEngineModule::class])  //now dagger knows what to do to make 'wheel'
interface CarComponent{
    fun getCar(): Car
    fun inject(activity: MainActivity)

    @Component.Builder
    interface Builder{
        @BindsInstance
        fun horsepower(@Named("hp") hp: Int): Builder

        @BindsInstance
        fun torque(@Named("torque") torque: Int): Builder

        fun build(): CarComponent
    }
}