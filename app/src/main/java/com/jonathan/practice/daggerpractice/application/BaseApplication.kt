package com.jonathan.practice.daggerpractice.application

import android.app.Application
import com.jonathan.practice.daggerpractice.di.component.DaggerAppComponent

class BaseApplication: Application(){

    val appComponent by lazy{ DaggerAppComponent.create()}
}