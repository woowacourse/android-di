package com.medandro.di

import com.medandro.di.annotation.InjectField
import com.medandro.di.annotation.Qualifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class DIContainerTest {
    data class Brand(
        val name: String = "카카오",
    )

    interface Wheels {
        val count: Int
    }

    class ThreeWheels : Wheels {
        override val count: Int = 3
    }

    @Qualifier("FourWheels")
    class FourWheels : Wheels {
        override val count: Int = 4
    }

    @Qualifier("SixWheels")
    class SixWheels : Wheels {
        override val count: Int = 6
    }

    class Driver(
        val person: Person,
    )

    data class Person(
        val name: String = "민택",
        val age: Int = 42,
    )

    @Test
    fun `InjectField 어노테이션이 붙은 필드는 자동 생성되어 주입된다`() {
        // given
        class TestCar {
            @InjectField
            lateinit var brand: Brand
        }
        // when
        val diContainer = DIContainer()
        val car = TestCar()
        diContainer.injectFields(car)

        // then
        assertThat(car.brand.name).isEqualTo("카카오")
    }

    @Test
    fun `InjectField 어노테이션이 없는 필드는 주입되지 않는다`() {
        class TestCar {
            lateinit var brand: Brand

            fun isBrandInitialized(): Boolean = this::brand.isInitialized
        }

        // when
        val diContainer = DIContainer()
        val car = TestCar()
        diContainer.injectFields(car)

        // then
        assertThat(car.isBrandInitialized()).isFalse()
    }

    @Test
    fun `주입된 필드의 인자는 재귀적으로 자동 주입된다`() {
        // given
        class TestCar {
            @InjectField
            lateinit var driver: Driver
        }
        // when
        val diContainer = DIContainer()
        val car = TestCar()
        diContainer.injectFields(car)

        // then
        assertThat(car.driver.person.name).isEqualTo("민택")
        assertThat(car.driver.person.age).isEqualTo(42)
    }

    @Test
    fun `val 필드는 주입되지 않는다`() {
        // given
        class TestCar {
            @InjectField
            val brand: Brand? = null
        }

        // when
        val diContainer = DIContainer()
        val car = TestCar()
        diContainer.injectFields(car)

        // then
        assertThat(car.brand).isNull()
    }

    @Test
    fun `Qualifier가 없는 구현체 등록시 Qualifier가 없는 필드에 자동으로 주입된다`() {
        // given
        class TestCar {
            @InjectField
            lateinit var wheels: Wheels
        }

        // when
        val diContainer = DIContainer(ThreeWheels::class)
        val car = TestCar()
        diContainer.injectFields(car)

        // then
        assertThat(car.wheels.count).isEqualTo(3)
    }

    @Test
    fun `Qualifier 어노테이션을 통해 의존성을 구분하여 주입한다`() {
        // given
        class TestCar {
            @InjectField
            lateinit var threeWheels: Wheels

            @InjectField
            @Qualifier("FourWheels")
            lateinit var fourWheels: Wheels

            @InjectField
            @Qualifier("SixWheels")
            lateinit var sixWheels: Wheels
        }

        // when
        val diContainer = DIContainer(ThreeWheels::class, FourWheels::class, SixWheels::class)
        val car = TestCar()
        diContainer.injectFields(car)

        // then
        assertThat(car.threeWheels.count).isEqualTo(3)
        assertThat(car.fourWheels.count).isEqualTo(4)
        assertThat(car.sixWheels.count).isEqualTo(6)
    }

    @Test
    fun `생성자 주입과 필드 주입이 함께 작동한다`() {
        // given
        class TestCar(
            val driver: Driver,
        ) {
            @InjectField
            lateinit var brand: Brand
        }

        // when
        val diContainer = DIContainer()
        val car = TestCar(Driver(Person()))
        diContainer.injectFields(car)

        // then
        assertThat(car.driver.person.name).isEqualTo("민택")
        assertThat(car.brand.name).isEqualTo("카카오")
    }

    @Test
    fun `외부에서 생성한 인스턴스를 직접 등록 가능하다`() {
        // given
        class TestCar {
            @InjectField
            lateinit var wheels: Wheels
        }

        val tenWheels: Wheels =
            object : Wheels {
                override val count = 10
            }

        // when
        val diContainer = DIContainer().registerSingleton(tenWheels)
        val car = TestCar()
        diContainer.injectFields(car)

        // then
        assertThat(car.wheels.count).isEqualTo(10)
    }
}
