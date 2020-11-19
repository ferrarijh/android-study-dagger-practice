package com.jonathan.practice.daggerpractice.car

import com.jonathan.practice.daggerpractice.car.engine.Engine
import com.jonathan.practice.daggerpractice.car.engine.EngineWithHp
import dagger.Module
import dagger.Provides

@Module
class EngineWithHpModule(private val horsepower: Int){
    @Provides
    fun provideEngine(): Engine = EngineWithHp(horsepower)
}