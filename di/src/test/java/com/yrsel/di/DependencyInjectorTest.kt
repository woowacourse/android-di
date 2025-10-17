package com.yrsel.di

import com.yrsel.di.fixture.FakeDataSource
import com.yrsel.di.fixture.FakeInMemoryDataSource
import com.yrsel.di.fixture.FakeRepository
import com.yrsel.di.fixture.FakeRepositoryConstructorInject
import com.yrsel.di.fixture.FakeRoomDataSource
import com.yrsel.di.fixture.InMemory
import com.yrsel.di.fixture.Room
import com.yrsel.di.fixture.module.FakeConstructorModule
import com.yrsel.di.fixture.module.FakeFactoryModule
import com.yrsel.di.fixture.module.FakeQualifierModule
import com.yrsel.di.fixture.module.FakeSingletonModule
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.kotest.matchers.types.shouldNotBeSameInstanceAs

class DependencyInjectorTest :
    StringSpec({
        "singleton은 동일한 인스턴스를 반환한다" {
            // given
            val singletonModule = FakeSingletonModule()
            val key = DefinitionKey(FakeRepository::class)
            DependencyContainer.init(singletonModule)

            // when
            val firstProvider = DependencyContainer.get(key)
            val firstInstance = firstProvider.get()
            val secondProvider = DependencyContainer.get(key)
            val secondInstance = secondProvider.get()

            // then
            firstInstance shouldBeSameInstanceAs secondInstance
        }

        "factory는 매번 새로운 인스턴스를 반환한다" {
            // given
            val factoryModule = FakeFactoryModule()
            val key = DefinitionKey(FakeRepository::class)
            DependencyContainer.init(factoryModule)

            // when
            val provider = DependencyContainer.get(key)
            val firstInstance = provider.get()
            val secondInstance = provider.get()

            // then
            firstInstance shouldNotBeSameInstanceAs secondInstance
        }

        "qualifier로 동일 타입을 구분할 수 있다" {
            // given
            val qualifierModule = FakeQualifierModule()
            DependencyContainer.init(qualifierModule)
            val inMemoryKey = DefinitionKey(FakeDataSource::class, InMemory::class)
            val roomKey = DefinitionKey(FakeDataSource::class, Room::class)

            // when
            val inMemoryProvider = DependencyContainer.get(inMemoryKey)
            val roomProvider = DependencyContainer.get(roomKey)
            val inMemoryDataSource = inMemoryProvider.get()
            val roomDataSource = roomProvider.get()

            // then
            inMemoryDataSource.shouldBeInstanceOf<FakeInMemoryDataSource>()
            roomDataSource.shouldBeInstanceOf<FakeRoomDataSource>()
        }

        "생성자 주입으로 의존성을 주입할 수 있다" {
            // given
            val module = FakeConstructorModule()
            val key = DefinitionKey(FakeRepository::class)
            DependencyContainer.init(module)

            // when
            val provider = DependencyContainer.get(key)
            val instance = provider.get()

            // then
            instance.shouldBeInstanceOf<FakeRepositoryConstructorInject>()
        }
    })
