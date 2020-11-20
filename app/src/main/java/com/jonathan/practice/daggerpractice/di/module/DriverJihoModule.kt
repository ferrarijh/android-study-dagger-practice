package com.jonathan.practice.daggerpractice.di.module

import com.jonathan.practice.daggerpractice.DriverJiho
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class DriverJihoModule{
    companion object {
        @Provides
        @Singleton
        fun provideDriver() = DriverJiho()
    }

    //@Binds invokes dependency cycle..
//    @Binds
//    abstract fun bindDriver(driver: DriverJiho): DriverJiho
}
