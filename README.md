# Android Dagger Practice
* 3 types of injections - Constructor/Method(setter)/Field Injection
* class `Car` depends on `Engine` and `Wheel`.
* Interface `CarComponent` will be implemented to class `DaggerCarComponent` automatically by Dagger.

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

## Field Injection
* Instantiate field(property)
* Useful when you have multiple fields to instantiate.
 
```kotlin
//in MainActivity
@Inject lateinit var mCar: Car  //fields should NOT be private
```

Add `inject()` method to `CarComponent` as below.

CarComponent.kt:
```kotlin
@Component
Interface CarComponent{
    fun getCar(): Car
    fun inject(activity: MainActivity)  //name does not matter
}
```

MainActivity.kt:
```kotlin
//in MainActivity
val component = DaggerCarComponent.create()
val car = component.getCar()
```

## Method Injection
* Useful when it's necessary to pass instance itself(`this`) to its dependency
* Order of injection: constructor -> field -> method

Below Example connects class `Remote` with a method of class `Car`.

Car.kt:
```kotlin
class Car 
{
    //...
    @Inject
    fun enableRemote(r: Remote){
        r.setRemoteListener(this)   //cannot be run in constructor since 'this' is not fully instantiated in constructor.
    }
}
```

Remote.kt:
```kotlin
class Remote @Inject constructor(){
    fun setListener(){
        Log.d("", "Remote connected.")
    }
}
```

## @Module
* What if we don't have direct access to class that we can't annotate with `@Inject`? -> use Module!
* Consider each Module class methods as static(companion object)

## @Binds
* `@Binds` can substitute `@Provides` - makes it more concise and efficient, even more than static @Provides method.
* With `@Binds`, Dagger does NOT create implementation of Module class, it rather creates only the instance of implemented class. (See example)

Class `Car` depends on class `Engine`, `Remote` and `Wheel`. Let's practice `@Binds` with class `Engine`.
Now there will be two kinds of engine - `PetrolEngine` and `DieselEngine`.
First change `Engine` to interface as below without `@Inject`.

Engine.kt:
```kotlin
interface Engine{
    fun start()
}
```

Second, create implemented class.

PetrolEngine.kt:
```kotlin
class PetrolEngine @Inject constructor(): Engine{
    override fun start(){
        Log.d("", "starting Petrol Engine..")
    }
}
```

DieselEngine.kt:
```kotlin
class DieselEngine @Inject constructor(): Engine{
    override fun start(){
        Log.d("", "starting Diesel Engine..")
    }
}
```

Third, create module classes which will let dagger know how to provide each `Engine`.

PetrolEngineModule.kt:
```kotlin
@Module
abstract class PetrolEngineModule{
    @Binds
    abstract fun bindEngine(e: PetrolEngine): Engine 
}
```

DieselEngineModule.kt:
```kotlin
@Module
abstract class DieselEngineModule{
    @Binds
    abstract fun bindEngine(e: DieselEngine): Engine 
}
```

Last, add module to Component Class.

CarComponent.kt:
```kotlin
@Component(modules = [WheelModule.class, PetrolEngineModule::class])    //change to any type of Engine here.
class CarComponent{
    //...
}
```

## Injecting values at runtime
Let's say we want to inject Engine `EngineWithHp` with horsepower into `Car` and horsepower is decided at runtime.

EngineWithHp.kt:
```kotlin
//inject values at runtime - NO @Inject here.
class EngineWithHp(val horsepower: Int): Engine{
    override fun start() {
        Log.d("", "starting engine with hp[$horsepower]..")
    }
}
```

EngineWithHpModule.kt:
```kotlin
@Module
class EngineWithHpModule(private val horsepower: Int){
    @Provides
    fun provideEngine(): Engine = EngineWithHp(horsepower)
}
```

To provide value at runtime get Component class with builder as below.

MainActivity.kt:
```kotlin
class MainActivity: AppCompatActivity(){
    @Inject lateinit var mCar: Car

    override fun onCreate(savedInstanceState: Bundle?){

        val component = DaggerCarComponent.builder()
            .engineWithHpModule(EngineWithHpModule(100))    //hp = 100
            .build()
    
        val mCar = component.inject(this)
        mCar.drive()
    }
}
```