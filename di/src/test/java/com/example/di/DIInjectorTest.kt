package com.example.di

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class FakeModule : com.example.di.Module {
    fun provideFakeRepository(): FakeRepository {
        return FakeRepository(FakeCartRepository(), FakeProductRepository())
    }

    fun provideFakeCartRepository(): FakeCartRepository {
        return FakeCartRepository()
    }

    fun provideFakeFieldRepository(): FakeFieldRepository {
        return FakeFieldRepository()
    }
}

class FakeRepository(
    @com.example.di.annotation.Inject private val fakeCartRepository: FakeCartRepository,
    private val fakeProductRepository: FakeProductRepository,
)

class FakeCartRepository {
    @com.example.di.annotation.Inject
    val fakeFieldRepository: FakeFieldRepository? = null
}

class FakeProductRepository

class FakeFieldRepository

class DIInjectorTest {
    @Before
    fun setup() {
        com.example.di.DIContainer.clear()
        com.example.di.DIInjector.injectModule(FakeModule())
    }

    @Test
    fun `Inject Annotation이 붙은 필드는 자동으로 의존성이 주입된다`() {
        val cartRepository = com.example.di.DIContainer.getInstance(FakeCartRepository::class)

        assertThat(cartRepository).isNotNull()
        assertThat(cartRepository?.fakeFieldRepository).isNotNull()
    }

    @Test
    fun `Inject Annotation이 붙은 필드는 의존성이 주입되지 않는다`() {
        val productRepository = com.example.di.DIContainer.getInstance(FakeProductRepository::class)
        assertThat(productRepository).isNull()
    }
}
