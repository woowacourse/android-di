package com.yrsel.di

import com.yrsel.di.fixture.FakeRepositoryConstructorFixture
import com.yrsel.di.fixture.module.FakeConstructorFixtureModule
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.types.shouldBeInstanceOf

class DependencyInjectorTest :
    StringSpec({
//    "singleton은 동일한 인스턴스를 반환한다" {
//        // given
//        InjectContainer.registerSingleton<SingletonFixtureCar> { _ ->
//            Provider { SingletonFixtureCar() }
//        }
//
//        // when
//        val first = InjectContainer.get<SingletonFixtureCar>()
//        val second = InjectContainer.get<SingletonFixtureCar>()
//
//        first shouldBeSameInstanceAs second
//    }
//
//    "factory는 매번 새로운 인스턴스를 반환한다" {
//        // given
//        val name = "factory"
//        InjectContainer.registerFactory<FactoryFixtureCar> { _ ->
//            Provider { FactoryFixtureCar(name) }
//        }
//
//        // when
//        val first = InjectContainer.get<FactoryFixtureCar>()
//        val second = InjectContainer.get<FactoryFixtureCar>()
//
//        first shouldNotBeSameInstanceAs second
//    }
//
//    "생성자 주입은 qualifier로 동일 타입을 구분할 수 있다" {
//        // given
//        InjectContainer.registerSingleton<FixtureCar>(qualifier = Qualifier.Named("engine")) { _ ->
//            Provider { EngineFixtureCarImpl() }
//        }
//        InjectContainer.registerSingleton<FixtureCar>(qualifier = Qualifier.Named("electric")) { _ ->
//            Provider { ElectricFixtureCarImpl() }
//        }
//
//        // when
//        val engineCar = InjectContainer.get<FixtureCar>(qualifier = Qualifier.Named("engine"))
//        val electricCar =
//            InjectContainer.get<FixtureCar>(qualifier = Qualifier.Named("electric"))
//
//        // then
//        engineCar.shouldBeInstanceOf<EngineFixtureCarImpl>()
//        electricCar.shouldBeInstanceOf<ElectricFixtureCarImpl>()
//    }

        "생성자 주입으로 의존성을 주입할 수 있다" {
            // given
            val module = FakeConstructorFixtureModule()
            val key = DefinitionKey(FakeRepositoryConstructorFixture::class)

            // when
            DependencyContainer.init(module)

            // then
            val provider = DependencyContainer.get(key)
            val instance = provider.get()

            instance.shouldBeInstanceOf<FakeRepositoryConstructorFixture>()
        }
    })
