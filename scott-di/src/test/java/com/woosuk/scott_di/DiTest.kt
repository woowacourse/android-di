package com.woosuk.scott_di

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

@Qualifier
annotation class InMemory

@Qualifier
annotation class Database

interface SingletonFakeRepository
interface FakeRepository
interface Fake2Repository

class DefaultSingletonRepository() : SingletonFakeRepository
class InMemoryFakeRepository() : FakeRepository

class DatabaseFakeRepository() : FakeRepository

class DefaultFake2Repository(
    private val fakeRepository: FakeRepository
) : Fake2Repository

object FakeModule : Module {

    @Singleton
    fun provideSingletonFakeRepo(
    ): SingletonFakeRepository {
        return DefaultSingletonRepository()
    }

    @Singleton
    @InMemory
    fun provideInMemoryFakeRepo(): FakeRepository {
        return InMemoryFakeRepository()
    }

    @Database
    fun provideDatabaseFakeRepo(): FakeRepository {
        return DatabaseFakeRepository()
    }

    fun provideDatabaseFake2Repo(
        @InMemory fakeRepository: FakeRepository
    ): Fake2Repository {
        return DefaultFake2Repository(fakeRepository)
    }
}
class DiTest {
    @Test
    fun `어노테이션이 없는 객체를 주입할 수 있다`() {
        // given
        startDI { loadModule(FakeModule) }
        class FakeViewModel(
            private val fakeRepository: FakeRepository
        )
        // when
        val actual = inject<FakeViewModel>()
        // then
        assertThat(actual).isInstanceOf(FakeViewModel::class.java)
    }

    @Test
    fun `Singleton 어노테이션이 붙은 객체를 주입할 수 있다`() {
        // given
        startDI { loadModule(FakeModule) }
        class FakeViewModel(
            private val fake2Repository: Fake2Repository
        )
        // when
        val actual = inject<FakeViewModel>()
        // then
        assertThat(actual).isInstanceOf(FakeViewModel::class.java)
    }

    @Test
    fun `Singleton 어노테이션이 붙은 객체는 계속 같은 객체를 내뱉는다`() {
        // given
        startDI { loadModule(FakeModule) }
        class FakeViewModel(
            val singletonFakeRepository: SingletonFakeRepository
        )

        class Fake2ViewModel(
            val singletonFakeRepository: SingletonFakeRepository
        )
        // when
        val fake1 = inject<FakeViewModel>()
        val fake2 = inject<Fake2ViewModel>()
        // then
        assertThat(fake1.singletonFakeRepository).isEqualTo(fake2.singletonFakeRepository)
    }

    @Test
    fun `Qualifier 로 구분이 가능하다`() {
        // given
        startDI { loadModule(FakeModule) }
        class FakeViewModel(
            @Database val fakeRepository: FakeRepository
        )

        class Fake2ViewModel(
            @InMemory val fakeRepository: FakeRepository
        )
        // when
        val fake1 = inject<FakeViewModel>()
        val fake2 = inject<Fake2ViewModel>()
        // then
        assertThat(fake1.fakeRepository).isInstanceOf(DatabaseFakeRepository::class.java)
        assertThat(fake2.fakeRepository).isInstanceOf(InMemoryFakeRepository::class.java)
    }

    @Test
    fun `Recursive DI 가 가능하다`() {
        // given
        startDI { loadModule(FakeModule) }
        class FakeViewModel(
            val fake2Repository: Fake2Repository
        )
        // when
        val fake1 = inject<FakeViewModel>()
        // then
        assertThat(fake1).isInstanceOf(FakeViewModel::class.java)
    }
}
