package com.example.sh1mj1

import com.example.sh1mj1.annotation.Inject
import com.example.sh1mj1.component.singleton.InjectedSingletonComponent
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

interface StubVehicle {
    class Car : StubVehicle {
        @Inject
        lateinit var engine: StubEngine

        @Inject
        lateinit var wheel: StubWheel
    }

    class Bike : StubVehicle
}

interface StubEngine {
    class GasolineEngine : StubEngine
}

interface StubWheel {
    class BigWheel : StubWheel
}

class InjectedComponentTest {
    @Test
    fun `자전거는 주입할 수 있는 프로퍼티가 없다`() {
        // given & when
        val bike =
            InjectedSingletonComponent(
                injectedClass = StubVehicle::class,
                instance = StubVehicle.Bike(),
            )

        // then
        val actualInjectableProperties = bike.injectableProperties()
        actualInjectableProperties shouldBe emptyList()
    }

    @Test
    fun `자동차는 주입해야 하는 프로퍼티가 두개 있다`() {
        // given & when
        val car =
            InjectedSingletonComponent(
                injectedClass = StubVehicle::class,
                instance = StubVehicle.Car(),
            )

        // then
        val actualInjectableProperties = car.injectableProperties()
        actualInjectableProperties.size shouldBe 2
    }

    @Test
    fun `자동차는 inject 애노테이션이 있는 엔진을 주입해야 한다`() {
        // given & when
        val car =
            InjectedSingletonComponent(
                injectedClass = StubVehicle::class,
                instance = StubVehicle.Car(),
            )

        // then
        val actualInjectableProperties = car.injectableProperties()
        actualInjectableProperties.size shouldBe 2
        actualInjectableProperties[0].returnType.classifier shouldBe StubEngine::class
        actualInjectableProperties[1].returnType.classifier shouldBe StubWheel::class
    }
}
