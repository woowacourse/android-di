package com.example.di

import com.example.di.fixture.CartRepository
import com.example.di.fixture.DefaultCartRepository
import com.example.di.fixture.DefaultProductRepository
import com.example.di.fixture.MainViewModel
import com.example.di.fixture.ProductRepository
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

class InjectorTest {

    @Test
    fun `@Inject 붙은 repository 필드들이 자동 주입된다`() {
        // given
        val viewModel = MainViewModel()
        DiContainer.addProviders(ProductRepository::class) { DefaultProductRepository() }
        DiContainer.addProviders(CartRepository::class) { DefaultCartRepository() }

        // when
        Injector.inject(viewModel)

        // then
        assertThat(viewModel.productRepository).isInstanceOf(DefaultProductRepository::class.java)
        assertThat(viewModel.cartRepository).isInstanceOf(DefaultCartRepository::class.java)
    }

    @Test
    fun `주입된 객체는 싱글톤처럼 동일 인스턴스가 유지된다`() {
        // given
        DiContainer.addProviders(ProductRepository::class) { DefaultProductRepository() }
        DiContainer.addProviders(CartRepository::class) { DefaultCartRepository() }

        val vm1 = MainViewModel()
        val vm2 = MainViewModel()

        // when
        Injector.inject(vm1)
        Injector.inject(vm2)

        // then
        assertThat(vm1.productRepository).isSameAs(vm2.productRepository)
        assertThat(vm1.cartRepository).isSameAs(vm2.cartRepository)
    }
}