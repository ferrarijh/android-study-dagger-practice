package com.jonathan.practice.daggerpractice.di.module

import com.jonathan.practice.daggerpractice.car.engine.DieselEngine
import com.jonathan.practice.daggerpractice.car.engine.Engine
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
class DieselEngineModule(private val horsepower: Int){

    @Provides
    fun provideEngine(de: DieselEngine): Engine = de

    @Provides
    fun provideHorsepower(): Int = horsepower
}