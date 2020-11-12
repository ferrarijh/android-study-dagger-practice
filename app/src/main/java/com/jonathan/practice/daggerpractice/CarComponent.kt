package com.jonathan.practice.daggerpractice

import dagger.Component

@Component
interface CarComponent{
    fun getCar(): Car
}