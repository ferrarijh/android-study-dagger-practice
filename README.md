# Android Dagger Practice
* 3 types of injections - Constructor/Method(setter)/Field Injection

In the example, class `Car` depends on `Engine` and `Wheel`.
Interface `CarComponent` will be implemented to class `DaggerCarComponent` automatically by Dagger.

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
@Component(modules = [WheelModule::class, PetrolEngineModule::class])    //change to any type of Engine here.
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

### @Component.Builder + @BindsInstance
* In the above example `EngineWithHpModule` is instantiated directly and it's too primitive. Let's change it so we can use it as below.
The example will change `PetrolEngine` so it will have property `horsepower` and `torque`

We want to get `Car` instance as below in `MainActivity`.

MainActivity.kt:
```kotlin
class MainActivity: AppCompatActivity(){
    @Inject lateinit var mCar: Car

    override fun onCreate(savedInstanceState: Bundle?){

        val component = DaggerCarComponent.builder()
            .horsePower(250)
            .torque(50)
            .build()
    
        val mCar = component.inject(this)
        mCar.drive()
    }
}
```

First change PetrolEngine.kt:
```kotlin
class PetrolEngine @Inject constructor(@Named("hp")private val hp: Int, @Named("torque")private val torque: Int): Engine{
    override fun start(){
        Log.d("", "starting Petrol Engine with: hp=$horsepower, torque=$torque")
    }
}
```
`@Named` annotation will be used for mapping passed values at runtime.

`PetrolEngineModule` stays the same as:
```kotlin
@Module
abstract class PetrolEngineModule{
    @Binds
    abstract fun bindEngine(e: PetrolEngine): Engine
}
```

Builder should be configured manually on class `CarComponent` as below.

CarComponent.kt:
```kotlin
@Component(modules=[WheelModule::class, PetrolEngineModule::class])
interface CarComponent{
    fun getCar(): Car
    fun inject(activity: MainActivity)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun horsepower(@Name("hp")hp: Int): Builder
    
        @BindsInstance
        fun torque(@Name("torque")torque: Int): Builder

        fun build(): CarComponent
    }
}
```

Without annotation `@Named` dagger won't know where the provided integer should be injected to between `horsepower` and `torque`
so below will invoke compile time error.
```kotlin
DaggerCarComponent.builder()
            .horsepower(250)
            .torque(50)
            .build()
```

## @Singleton
* Dependencies annotated `@Singleton` are singleton ONLY throughout the Components instance it's confined to.

Now we add property `DriverJiho` which is singleton to class `Car`. (below)

DriverJiho.kt:
```kotlin
@Singleton
class DriverJiho @Inject constructor(){}
```

(can't use `object DriverJiho` since constructor is not allowed for kotlin `object)

CarComponent.kt:
```kotlin
@Singleton
@Component
interface CarComponent //...
```

MainActivity.kt:
```kotlin
val component = DaggerCarComponent.builder()
    .horsepower(200)
    .torque(50)
    .build()

val car1 = component.getCar()
val car2 = component.getCar()

car1.drive()
car2.drive()
```
From the above `car1` and `car2` shares same `DriverJiho` instance.
However in the example below two cars do NOT share same `DriverJiho` instance.

MainActivity.kt:
```kotlin
val component1 = DaggerCarComponent.builder()
    .horsepower(200)
    .torque(50)
    .build()

val component2 = DaggerCarComponent.builder()
    .horsepower(200)
    .torque(50)
    .build()

val car1 = component1.getCar()
val car2 = component2.getCar()

car1.drive()
car2.drive()
```

Here as mentioned you'll see different `DriverJiho` instances between `car1` and `car2`.
Also there still is an issue until now - every time Activity is destroyed(e.g. at screen rotation)
new instances are created which can be wasteful and they're not 'true' singleton.
The next section explains how to solve the issue by setting custom scopes.

## Custom scopes and Component dependencies
* In actual development custom scopes are set for different classes and components with annotations.

Now let's set scope for each classes and Components - Application scope & Activity scope.
In the example instance of `DriverJiho` will last through entire application's life span(`@Singleton`) and instance of
`Car` will last only through activity's life span(`@MainActivityScope` - custom annotation).
Before jumping in set MainActivity

1) Create base application class for the app. (Then set `AndroidManifest.xml` to tell Android it's your base app)

BaseApplication.kt:
```kotlin
class BaseApplication: Application(){
    val appComponent by lazy{DaggerAppComponent.create()}   //AppComponent will be created in a moment!
}
```

2) Create custom annotation for `MainActivity`'s lifecycle.

MainActivityScope.java:
```java
@Scope
@Documented
@Retention(RUNTIME)
public @interface MainActivityScope{}
```

3) Create interface `AppComponent`(responsible for providing `DriverJiho`) and set scope annotation for each Component interfaces.

AppComponent.kt:
```kotlin
@Singleton
@Component(modules=[])  //DriverJihoModule will be created in a moment!
interface AppComponent{
    fun getDriver(): DriverJiho
}
```

`AppComponent` provides `DriverJiho` so `CarComponent` is dependent on `AppComponent`.
To inject instance of `AppComponent` in `CarComponent` in runtime add `appComponent()` method in `CarComponent.Builder` and 
dependency as below.

CarComponent.kt:
```kotlin
@MainActivityScope
@Component(
    dependencies=[AppComponent::class],     //tell dagger CarComponent is dependent on AppComponent to acquire instance of DriverJiho
    modules=[WheelModule::class,PetrolEngineModule::class]
)
interface CarComponent{
    //...
    @Component.Builder
    interface Builder{
        //...
        fun appComponent(appComponent: AppComponent): Builder   //inject AppComponent

        fun build(): CarComponent
    }
}
```

4) Create Module providing `DriverJiho` instance.

First modify `DriverJiho` class and use `@Provides` on `DriverJihoModule` - assume we can't inject on its constructor.

DriverJiho.kt:
```kotlin
class DriverJiho{
    //let's say we don't own this class to use @Inject to its constructor.
}
```

Then create `DriverJihoModule`.

DriverJihoModule.kt:
```kotlin
@Module
abstract class DriverJihoModule{
    companion object {
        @Provides
        @Singleton
        fun provideDriver() = DriverJiho()
    }
}
```

Specify required module in AppComponent.kt:
```kotlin
@Singleton
@Component(modules=[DriverJihoModule::class])   //specify module
interface AppComponent{
    fun getDriver(): DriverJiho
}
```

5) try it on MainActivity!

MainActivity.kt:
```kotlin
   @Inject private lateinit var car1: Car
   @Inject private lateinit var car2: Car
   //...
        val carComponent = DaggerCarComponent.builder()
            .horsepower(200)
            .torque(50)
            .appComponent((application as BaseApplication).appComponent)    //driver injected here
            .build()

        carComponent.inject(this)

        car1.drive()
        car2.drive()
```

Check log output log:
```
D/: Driver[com.jonathan.practice.daggerpractice.DriverJiho@e06cc5e] is driving car[com.jonathan.practice.daggerpractice.car.Car@9ddd83f]
    starting Petrol Engine with: hp=200, torque=50
    Driver[com.jonathan.practice.daggerpractice.DriverJiho@e06cc5e] is driving car[com.jonathan.practice.daggerpractice.car.Car@b12750c]

//..after screen rotation(Activity recreation)..

    Driver[com.jonathan.practice.daggerpractice.DriverJiho@e06cc5e] is driving car[com.jonathan.practice.daggerpractice.car.Car@641df88]
    starting Petrol Engine with: hp=200, torque=50
    Driver[com.jonathan.practice.daggerpractice.DriverJiho@e06cc5e] is driving car[com.jonathan.practice.daggerpractice.car.Car@757cd21]
```

Here instance of `DriverJiho` is alive through whole application scope.
`car1` and `car2` is recreated on par with activity.

## @Subcomponent
* Instead of setting `dependencies` attribute, `@Subcomponent` can be set to connect two components.
* Subcomponent can access the whole dependency graph of parent component.

In the example we'll change `CarComponent` to `AppComponent`'s Subcomponent.
`CarComponent` will now be accessed through `AppComponent`.
After going through the example we'll acquire `CarComponent` instance like below.

In MainActivity.kt:
```kotlin
val carComponent: CarComponent = (application as BaseApplication).getAppComponent()
    .getActivityComponent(DieselEngineModule(200))  //Assuming DieselEngineModule is not abstract and has no default constructor
```

To do the above follow the steps below.

1) Modify `CarComponent` to Subcomponent from Component.

Subcomponent does not have `dependencies` attribute so remove it.
Change `Builder`'s annotation to `@Subcomponent.Builder`.
From now on `AppComponent` is not injected to `CarComponent` directly.

CarComponent.kt:
```kotlin
@MainActivityScope
@Subcomponent(modlues=[WheelModule::class, DieselEngineModule::class])  //remove 'dependencies=[AppComponent::class]'
interface CarComponent{
    fun gerCar(): Car
    fun inject(activity: MainActivity)
    
/* we'll define @Subcomponent.Builder in next section
    @Component.Builder
    interface Builder{
        @BindsInstance
        fun horsepower(@Named("hp") hp: Int): Builder

        @BindsInstance
        fun torque(@Named("torque") torque: Int): Builder

        fun appComponent(appComponent: AppComponent): Builder

        fun build(): CarComponent
    }
*/
}
```


2) Replace `getDriver()` with `getCarComponent()` in `AppComponent`

`getCarComponent()` method needs all the modules that is not abstract and lack default constructor in `CarComponent`.
Here `DieselEngineModule` is the case but not `WheelModule`.

AppComponent.kt:
```kotlin
@Singleton
@Component(modules=[DriverJihoModule::class])
interface AppComponent{
    //fun getDriver(): Driver
    fun getCarComponent(dieselEngineModule: DieselEngineModule): CarComponent
}
```

(FYI) DieselEngineModule.kt:
```kotlin
@Module
class DieselEngineModule(private val horsepower: Int){

    @Provides
    fun provideEngine(de: DieselEngine): Engine = de

    @Provides
    fun provideHorsepower(): Int = horsepower
}
```

(FYI) DieselEngine.kt:
```kotlin
class DieselEngine @Inject constructor(private val horsepower: Int): Engine{
    override fun start() {
        Log.d("", "Starting Diesel Engine - hp[$horsepower]")
    }
}
```

## @Subcomponent.Builder
Let's try builder pattern in `MainActivity` acquire `CarComponent` instance as below.

MainActivity.kt:
```kotlin
val carComponent = (application as BaseApplication).appComponent
    .getCarComponentBuilder()   //modify AppComponent for this line
    .horsepower(200)
    .torque(50)
    .build()
```

We use `PetrolEngineModule` here.

1) Modify `AppComponent`

AppComponent.kt:
```kotlin
@Singleton
@Component(modules=[DriverJiho::class])
interface AppComponent{
    //fun getCarComponent(dem: DieselEngineModule): CarComponent    //remove this line
    fun getCarComponentBuilder(): CarComponent.Builder
}
```

2) Modify `CarComponent`

CarComponent.kt:
```kotlin
@MainActivityScope
@Subcomponent(modlues=[WheelModule::class, PetrolEngineModule::class]) 
interface CarComponent{
    fun gerCar(): Car
    fun inject(activity: MainActivity)
    
    @Subcomponent.Builder   //changed to @Subcomponent.Builder
    interface Builder{
        @BindsInstance
        fun horsepower(@Named("hp") hp: Int): Builder

        @BindsInstance
        fun torque(@Named("torque") torque: Int): Builder

        //fun appComponent(appComponent: AppComponent): Builder     //remove this line

        fun build(): CarComponent
    }
}
```

We don't need `appComponent()` method since we now don't inject `AppComponent` to `CarComponent`.

## @Subcomponent.Factory
* Factory method is safer than Builder method as it invokes compile-time error for missing arguments while Builder method invokes runtime error on same circumstance.

For example, with Builder method in MainActivity.kt:
```kotlin
val carComponent = (application as BaseApplication).appComponent
    .getCarComponentBuilder()
    .horsepower(200)
    //.torque(50)   //oops - missing argument for 'torque'
    .build()
```
if we don't pass necessary argument to for `Car` this will compile like everything's okay and later invoke runtime error complaining there's missing argument.

To implement Factory method, 
1) Modify `CarComponent`

CarComponent.kt:
```kotlin
@MainActivityScope
@Subcomponent(modlues=[WheelModule::class, PetrolEngineModule::class]) 
interface CarComponent{
    fun gerCar(): Car
    fun inject(activity: MainActivity)
    
/*  replace this part with below @Subcomponent.Factory
    @Subcomponent.Builder
    interface Builder{
        @BindsInstance
        fun horsepower(@Named("hp") hp: Int): Builder

        @BindsInstance
        fun torque(@Named("torque") torque: Int): Builder

        fun build(): CarComponent
    }
*/
    @Subcomponent.Factory
    interface Factory{
        fun create(@BindsInstance @Named("hp") hp: Int,
            @BindsInstance @Named("torque") torque: Int): CarComponent
    }
}
```

2) Modify `AppComponent`

AppComponent.kt:
```kotlin
@Singleton
@Component(modules=[DriverJiho::class])
interface AppComponent{
    //fun getCarComponentBuilder(): CarComponent.Builder    //replace this line with below line
    fun getCarComponentFactory(): CarComponent.Factory
}
```

Now acquire `carComponent` with Factory method in `MainActivity`.

MainActivity.kt:
```kotlin
val carComponent = (application as BaseApplication).appComponent()
    .getCarComponentFactory()
    .create(200, 50)
```

## Furthermore..
* If all the `@Provides` methods are static in `@Module` annotated class, make the class abstract - then Dagger won't compile if any methods are non-static.
* Proguard will get rid of all the unnecessary codes created by Dagger when creating apk file.
* For `@Singleton` to be effective, instances should be created from a single component.

## Unresolved
* For modules providing singleton instance, `@Binds` invoke cycle error - Why?
-- Even after removing `getDriver()` from `AppComponent`..