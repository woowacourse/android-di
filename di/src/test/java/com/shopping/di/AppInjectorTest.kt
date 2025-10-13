package com.shopping.di

import com.shopping.di.definition.Qualifier
import com.shopping.fixture.ElectricFixtureCarImpl
import com.shopping.fixture.EngineFixtureCarImpl
import com.shopping.fixture.FactoryFixtureCar
import com.shopping.fixture.FixtureCar
import com.shopping.fixture.SingletonFixtureCar
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.kotest.matchers.types.shouldNotBeSameInstanceAs

class AppInjectorTest :
    StringSpec({
        beforeTest {
            InjectContainer.init()
        }

        "singleton 은 동일한 인스턴스를 반환한다" {
            // given
            val name = "singleton"
            InjectContainer.registerSingleton<SingletonFixtureCar> { _ ->
                Provider { SingletonFixtureCar(name) }
            }

            // when
            val first = InjectContainer.get<SingletonFixtureCar>()
            val second = InjectContainer.get<SingletonFixtureCar>()

            first shouldBeSameInstanceAs second
        }

        "factory 는 매번 새로운 인스턴스를 반환한다" {
            // given
            val name = "factory"
            InjectContainer.registerFactory<FactoryFixtureCar> { _ ->
                Provider { FactoryFixtureCar(name) }
            }

            // when
            val first = InjectContainer.get<FactoryFixtureCar>()
            val second = InjectContainer.get<FactoryFixtureCar>()

            first shouldNotBeSameInstanceAs second
        }

        "qualifier 로 동일 타입을 구분할 수 있다" {
            // given
            InjectContainer.registerSingleton<FixtureCar>(qualifier = Qualifier.Named("engine")) { _ ->
                Provider { EngineFixtureCarImpl("engineCarName") }
            }
            InjectContainer.registerSingleton<FixtureCar>(qualifier = Qualifier.Named("electric")) { _ ->
                Provider { ElectricFixtureCarImpl("electricCarName") }
            }

            // when
            val engineCar = InjectContainer.get<FixtureCar>(qualifier = Qualifier.Named("engine"))
            val electricCar =
                InjectContainer.get<FixtureCar>(qualifier = Qualifier.Named("electric"))

            // then
            engineCar.shouldBeInstanceOf<EngineFixtureCarImpl>()
            electricCar.shouldBeInstanceOf<ElectricFixtureCarImpl>()
        }

        "ViewModel의 경우 필드 주입으로 의존성을 주입할 수 있다" {
            // TODO: 재작성 필요
        }

        "ViewModel의 경우 생성자 주입으로 의존성을 주입할 수 있다" {
            // TODO: 재작성 필요
        }
    })
