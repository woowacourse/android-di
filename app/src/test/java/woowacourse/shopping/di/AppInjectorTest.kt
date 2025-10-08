package woowacourse.shopping.di

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.kotest.matchers.types.shouldNotBeSameInstanceAs
import woowacourse.shopping.di.definition.Qualifier
import woowacourse.shopping.fixture.ElectricFixtureCarImpl
import woowacourse.shopping.fixture.EngineFixtureCarImpl
import woowacourse.shopping.fixture.FactoryFixtureCar
import woowacourse.shopping.fixture.FixtureCar
import woowacourse.shopping.fixture.SingletonFixtureCar

class AppInjectorTest :
    StringSpec({
        beforeTest {
            AppInjector.init(emptyList())
        }

        "singleton 은 동일한 인스턴스를 반환한다" {
            // given
            val name = "singleton"
            AppInjector.registerSingleton<SingletonFixtureCar> { _ ->
                Provider { SingletonFixtureCar(name) }
            }

            // when
            val first = AppInjector.get<SingletonFixtureCar>()
            val second = AppInjector.get<SingletonFixtureCar>()

            first shouldBeSameInstanceAs second
        }

        "factory 는 매번 새로운 인스턴스를 반환한다" {
            // given
            val name = "factory"
            AppInjector.registerFactory<FactoryFixtureCar> { _ -> Provider { FactoryFixtureCar(name) } }

            // when
            val first = AppInjector.get<FactoryFixtureCar>()
            val second = AppInjector.get<FactoryFixtureCar>()

            first shouldNotBeSameInstanceAs second
        }

        "quailfier 로 동일 타입을 구분할 수 있다" {
            // given
            AppInjector.registerSingleton<FixtureCar>(qualifier = Qualifier.Named("engine")) { _ ->
                Provider { EngineFixtureCarImpl("engineCarName") }
            }
            AppInjector.registerSingleton<FixtureCar>(qualifier = Qualifier.Named("electric")) { _ ->
                Provider { ElectricFixtureCarImpl("electricCarName") }
            }

            // when
            val engineCar = AppInjector.get<FixtureCar>(qualifier = Qualifier.Named("engine"))
            val electricCar =
                AppInjector.get<FixtureCar>(qualifier = Qualifier.Named("electric"))

            // then
            engineCar.shouldBeInstanceOf<EngineFixtureCarImpl>()
            electricCar.shouldBeInstanceOf<ElectricFixtureCarImpl>()
        }
    })
