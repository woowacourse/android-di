package com.example.di

import com.example.di.fixture.CartRepository
import com.example.di.fixture.DefaultCartRepository
import com.example.di.fixture.DefaultProductRepository
import com.example.di.fixture.MainViewModel
import com.example.di.fixture.ProductRepository
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test
import kotlin.test.assertNotNull


class InjectorTest {

    private val appComponent = Component.Singleton

    @Test
    fun `@Inject 붙은 repository 필드들이 자동 주입된다`() {
        // given
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
        val viewModel = MainViewModel()

        // when
        Injector.inject(viewModel, appComponent)

        // then
        assertNotNull(viewModel.productRepository)
        assertNotNull(viewModel.cartRepository)
        assertThat(viewModel.productRepository).isInstanceOf(DefaultProductRepository::class.java)
        assertThat(viewModel.cartRepository).isInstanceOf(DefaultCartRepository::class.java)
    }

    @Test
    fun `주입된 객체는 싱글톤처럼 동일 인스턴스가 유지된다`() {
        // given
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
        val vm1 = MainViewModel()
        val vm2 = MainViewModel()

        // when
        Injector.inject(vm1, appComponent)
        Injector.inject(vm2, appComponent)

        // then
        assertThat(vm1.productRepository).isSameAs(vm2.productRepository)
        assertThat(vm1.cartRepository).isSameAs(vm2.cartRepository)
    }
}