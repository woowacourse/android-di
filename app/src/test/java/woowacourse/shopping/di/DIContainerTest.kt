package woowacourse.shopping.di

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class DIContainerTest {
    data class Brand(
        val name: String = "카카오",
    )

    interface Wheels {
        val count: Int
    }

    class FourWheels : Wheels {
        override val count: Int = 4
    }

    class Driver(
        val person: Person,
    )

    data class Person(
        val name: String = "민택",
        val age: Int = 42,
    )

    @Test
    fun `어노테이션이 붙은 필드는 자동 주입된다`() {
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
    fun `어노테이션이 없는 필드는 주입되지 않는다`() {
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
    fun `필드의 인자는 재귀적으로 자동 주입된다`() {
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
    fun `인터페이스 매핑을 추가시 자동으로 주입된다`() {
        // given
        class TestCar {
            @InjectField
            lateinit var wheels: Wheels
        }

        // when
        val diContainer = DIContainer(interfaceMapping = mapOf(Wheels::class to FourWheels::class))
        val car = TestCar()
        diContainer.injectFields(car)

        // then
        assertThat(car.wheels.count).isEqualTo(4)
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
        val car = diContainer.getInstance(TestCar::class) as TestCar
        diContainer.injectFields(car)

        // then
        assertThat(car.driver.person.name).isEqualTo("민택")
        assertThat(car.brand.name).isEqualTo("카카오")
    }
}
