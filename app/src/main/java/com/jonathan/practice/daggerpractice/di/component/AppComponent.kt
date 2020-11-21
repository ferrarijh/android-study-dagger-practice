package com.jonathan.practice.daggerpractice.di.component

import com.jonathan.practice.daggerpractice.DriverJiho
import com.jonathan.practice.daggerpractice.di.module.DieselEngineModule
import com.jonathan.practice.daggerpractice.di.module.DriverJihoModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules=[DriverJihoModule::class])
interface AppComponent{
    //fun getCarComponent(dem: DieselEngineModule): CarComponent
    fun getCarComponentBuilder(): CarComponent.Builder
}