# Android Dagger Practice
* class ```Car``` depends on ```Engine``` and ```Wheel```.
* Interface ```CarComponent``` will be implemented to class ```DaggerCarComponent``` automatically by Dagger.

```kotlin
@Component
Interface CarComponent{
    fun getCar(): Car
}
```

Instantiate like below:
```kotlin
val component = DaggerCarComponent.create()
val car = component.getCar()
```