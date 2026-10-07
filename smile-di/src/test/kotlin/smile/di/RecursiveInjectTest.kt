package smile.di

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RecursiveInjectTest {
    @Test
    fun `재귀 주입이 정상적으로 동작한다`() {
        val smileDi =
            SmileDi(
                module = object { },
                repository = FakeDependencyRepository(),
            )
        val car = smileDi.resolveDependencies(Car::class)
        assertThat(car).isInstanceOf(Car::class.java)
    }
}

data class Car(
    val engine: Engine,
)

class Engine(
    val cylinder: Cylinder,
)

class Cylinder
