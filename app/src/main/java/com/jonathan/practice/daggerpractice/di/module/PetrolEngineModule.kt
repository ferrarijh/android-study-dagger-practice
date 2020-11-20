package com.jonathan.practice.daggerpractice.di.module

import com.jonathan.practice.daggerpractice.car.engine.Engine
import com.jonathan.practice.daggerpractice.car.engine.PetrolEngine
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class PetrolEngineModule{

    @Binds
    abstract fun bindEngine(pe: PetrolEngine): Engine
}