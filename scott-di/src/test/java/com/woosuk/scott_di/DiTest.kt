package com.woosuk.scott_di

import org.junit.jupiter.api.Test

@Qualifier
annotation class InMemory

@Qualifier
annotation class Database

interface FakeRepository
interface Fake2Repository

class InMemoryFakeRepository() : FakeRepository

class DatabaseFakeRepository() : FakeRepository

class DefaultFake2Repository(
    private val fakeRepository: FakeRepository
) : Fake2Repository

object FakeModule : Module {
    fun provideFakeRepo(): FakeRepository {
        return InMemoryFakeRepository()
    }

    @Singleton
    fun provideFakeRepoSingleton(
        fakeRepository: FakeRepository
    ): Fake2Repository {
        return DefaultFake2Repository(fakeRepository)
    }

    @InMemory
    fun provideInMemoryFakeRepo(): FakeRepository {
        return InMemoryFakeRepository()
    }

    @Database
    fun provideDatabaseFakeRepo(): FakeRepository {
        return DatabaseFakeRepository()
    }

    fun provideDatabaseFake2Repo(
        @Database fakeRepository: FakeRepository
    ): Fake2Repository {
        return DefaultFake2Repository(fakeRepository)
    }
}
class DiTest {
    @Test
    fun `생성자가 없는 객체를 주입할 수 있다`(){
        // given

        // when

        // then
    }

}
