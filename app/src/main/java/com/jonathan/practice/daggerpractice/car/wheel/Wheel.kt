package com.jonathan.practice.daggerpractice.car.wheel

class Wheel (private val rim: Rim, private val tire: Tire) {}
//how does dagger create rim/tire? -> with WheelModule