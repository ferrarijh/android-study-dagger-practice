package com.jonathan.practice.daggerpractice

import com.jonathan.practice.daggerpractice.car.wheel.Rim
import com.jonathan.practice.daggerpractice.car.wheel.Tire
import com.jonathan.practice.daggerpractice.car.wheel.Wheel
import dagger.Module
import dagger.Provides

@Module
class WheelModule{

    @Provides
    fun provideRims(): Rim { //method's name does not matter
        return Rim()
    }

    @Provides   //with @Provide, dagger knows how to provide each instance
    fun provideTires(): Tire {
        val tires = Tire()
        tires.inflate()
        return tires
    }

    @Provides
    fun provideWheels(rim: Rim, tire: Tire): Wheel {
        return Wheel(rim, tire)
    }
}