package com.example.di

import com.example.di.fixture.CartRepository
import com.example.di.fixture.CartUseCase
import com.example.di.fixture.CartViewModel
import com.example.di.fixture.DefaultCartRepository
import com.example.di.fixture.DefaultProductRepository
import com.example.di.fixture.FakeCartRepository
import com.example.di.fixture.InMemory
import com.example.di.fixture.LocalDatabase
import com.example.di.fixture.MainViewModel
import com.example.di.fixture.ProductRepository
import com.example.di.fixture.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert.assertNotNull
import org.junit.Test

class DiContainerTest {
    private val appComponent = Component.Singleton

    @Test
    fun `클래스 타입으로 인스턴스를 생성할 수 있다`() {
        DiContainer.bindBinds(
            fromInterface = ProductRepository::class,
            toImplementation = DefaultProductRepository::class,
            installIn = { Component.Singleton },
            isScoped = true,
        )

        val productRepository =
            DiContainer.get(ProductRepository::class, appComponent)

        assertNotNull(productRepository)
        assertThat(productRepository).isInstanceOf(DefaultProductRepository::class.java)
    }

    @Test
    fun `캐싱한 동일한 인스턴스를 반환한다`() {
        DiContainer.bindBinds(
            fromInterface = ProductRepository::class,
            toImplementation = DefaultProductRepository::class,
            installIn = { Component.Singleton },
            isScoped = true,
        )

        val instance1 = DiContainer.get(ProductRepository::class, appComponent)
        val instance2 = DiContainer.get(ProductRepository::class, appComponent)

        assertThat(instance1).isSameAs(instance2)
    }

    @Test
    fun `생성자 주입을 통해 의존성을 해결할 수 있다`() {
        DiContainer.bindBinds(
            fromInterface = CartRepository::class,
            toImplementation = DefaultCartRepository::class,
            installIn = { Component.Singleton },
            isScoped = true,
        )

        val cartViewModel = DiContainer.get(CartViewModel::class, appComponent)
        val cartRepository = DiContainer.get(CartRepository::class, appComponent)

        assertThat(cartViewModel.cartRepository).isSameAs(cartRepository)
    }

    @Test
    fun `Inject 어노테이션으로 필드 주입을 할 수 있다`() {
        DiContainer.bindBinds(
            fromInterface = ProductRepository::class,
            toImplementation = DefaultProductRepository::class,
            installIn = { Component.Singleton },
            isScoped = true,
        )
        DiContainer.bindBinds(
            fromInterface = CartRepository::class,
            toImplementation = DefaultCartRepository::class,
            installIn = { Component.Singleton },
            isScoped = true,
        )

        val mainViewModel = DiContainer.get(MainViewModel::class, appComponent)
        val productRepository = DiContainer.get(ProductRepository::class, appComponent)
        val cartRepository = DiContainer.get(CartRepository::class, appComponent)

        assertThat(mainViewModel.productRepository).isSameAs(productRepository)
        assertThat(mainViewModel.cartRepository).isSameAs(cartRepository)
    }

    @Test
    fun `CartRepository의 Qualifier를 사용하여 의존성을 구분할 수 있다`() {
        DiContainer.bindBinds(
            fromInterface = CartRepository::class,
            toImplementation = FakeCartRepository::class,
            installIn = { Component.Singleton },
            isScoped = true,
            qualifier = InMemory::class,
        )
        DiContainer.bindBinds(
            fromInterface = CartRepository::class,
            toImplementation = DefaultCartRepository::class,
            installIn = { Component.Singleton },
            isScoped = true,
            qualifier = LocalDatabase::class,
        )

        val cartUseCase = DiContainer.get(CartUseCase::class, appComponent)

        assertThat(cartUseCase.fakeCartRepository)
            .isExactlyInstanceOf(FakeCartRepository::class.java)
        assertThat(cartUseCase.realCartRepository)
            .isExactlyInstanceOf(DefaultCartRepository::class.java)
        assertThat(cartUseCase.fakeCartRepository)
            .isNotSameAs(cartUseCase.realCartRepository)
    }

    @Test(expected = IllegalStateException::class)
    fun `바인딩되지 않은 인터페이스를 요청하면 예외가 발생한다`() {
        DiContainer.get(UserRepository::class, appComponent)
    }
}