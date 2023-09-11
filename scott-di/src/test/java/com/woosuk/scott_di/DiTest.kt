package com.woosuk.scott_di

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

interface FakeRepository

interface FakeRepository2

class DefaultFakeRepository() : FakeRepository

class DefaultFake2Repository(
    private val fakeRepository: FakeRepository
) : FakeRepository2

object FakeModule : Module {
    fun provideFakeRepo(): FakeRepository {
        return DefaultFakeRepository()
    }

    @Singleton
    fun provideFakeRepoSingleton(): FakeRepository2 {
        return DefaultFake2Repository(get())
    }

    @Qualifier("fake")
    fun provideFakeRepoQualified(): FakeRepository {
        return DefaultFakeRepository()
    }

    @Qualifier("fake2")
    @Singleton
    fun provideFake2RepoQualified(): FakeRepository2 {
        return DefaultFake2Repository(get())
    }
}


class DiTest {

    @Test
    fun `get 함수를 통해, DiContainer 에 있는 객체를 가져올 수 있다`() {
        // given
        DiContainer.singletons[FakeRepository::class] = DefaultFakeRepository()
        // when
        val actual = get(FakeRepository::class)
        // then
        assertThat(actual).isInstanceOf(FakeRepository::class.java)
    }

    @Test
    fun `get 함수를 통해, 생성할 객체의 생성자에 알맞은 객체 주입이 가능하다`() {
        // given
        DiContainer.singletons[FakeRepository::class] = DefaultFakeRepository()
        // when
        val actual = DefaultFake2Repository(get())
        // then
        assertThat(actual).isInstanceOf(FakeRepository2::class.java)
    }

    @Test
    fun `Annotation이 없는 함수를 Load 하면, 일반 Declaration Map에 추가된다`() {
        // when
        DiContainer.loadModule(FakeModule)
        // then
        assertThat(DiContainer.declarations[FakeRepository::class]?.invoke()).isInstanceOf(
            DefaultFakeRepository::class.java
        )
    }

    @Test
    fun `Singleton 어노테이션이 붙은 함수를 Load 하면, Singletons Map 에 추가된다`() {
        // when
        DiContainer.loadModule(FakeModule)
        // then
        assertThat(DiContainer.singletons[FakeRepository::class]).isInstanceOf(
            DefaultFakeRepository::class.java
        )
    }

    @Test
    fun `Qualifier 어노테이션이 붙은 함수를 Load 하면, qualifiedDeclarations Map 에 추가된다`() {
        // when
        DiContainer.loadModule(FakeModule)
        // then
        assertThat(DiContainer.qualifiedDeclarations["fake"]?.invoke()).isInstanceOf(
            DefaultFakeRepository::class.java
        )
    }

    @Test
    fun `Qualifier과 Singleton 어노테이션이 붙은 함수를 Load 하면, qualifiedSingleton Map 에 추가된다`() {
        // when
        DiContainer.loadModule(FakeModule)
        // then
        assertThat(DiContainer.qualifiedDeclarations["fake2"]?.invoke()).isInstanceOf(
            DefaultFake2Repository::class.java
        )
    }

    @Test
    fun `inject 함수를 통해, 의존성 주입을 이용한 객체 생성이 가능하다`() {
        // when
        val actual = inject<DefaultFake2Repository>()
        // then
        assertThat(actual).isInstanceOf(DefaultFake2Repository::class.java)
    }
}
