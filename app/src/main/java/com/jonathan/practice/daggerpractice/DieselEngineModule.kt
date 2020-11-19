package com.jonathan.practice.daggerpractice

import com.jonathan.practice.daggerpractice.car.engine.DieselEngine
import com.jonathan.practice.daggerpractice.car.engine.Engine
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class DieselEngineModule{

    @Binds
    abstract fun bindEngine(de: DieselEngine): Engine
}