package com.jonathan.practice.daggerpractice.di.module

import com.jonathan.practice.daggerpractice.car.engine.DieselEngine
import com.jonathan.practice.daggerpractice.car.engine.Engine
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class DieselEngineModule{

    companion object{
        @Provides
        fun bindEngine(de: DieselEngine) = DieselEngine()
    }
}