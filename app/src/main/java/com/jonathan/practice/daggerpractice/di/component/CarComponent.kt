package com.jonathan.practice.daggerpractice.di.component

import com.jonathan.practice.daggerpractice.MainActivity
import com.jonathan.practice.daggerpractice.di.module.PetrolEngineModule
import com.jonathan.practice.daggerpractice.di.module.WheelModule
import com.jonathan.practice.daggerpractice.car.Car
import com.jonathan.practice.daggerpractice.di.annotation.MainActivityScope
import com.jonathan.practice.daggerpractice.di.module.DieselEngineModule
import dagger.BindsInstance
import dagger.Component
import dagger.Subcomponent
import javax.inject.Named
import javax.inject.Singleton

@MainActivityScope
@Subcomponent
    (modules = [WheelModule::class, PetrolEngineModule::class])  //now dagger knows how to acquire 'wheel'
interface CarComponent{
    fun getCar(): Car
    fun inject(activity: MainActivity)

    @Subcomponent.Builder
    interface Builder{
        @BindsInstance
        fun horsepower(@Named("hp") hp: Int): Builder

        @BindsInstance
        fun torque(@Named("torque") torque: Int): Builder

        //fun appComponent(appComponent: AppComponent): Builder

        fun build(): CarComponent
    }
}