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
import org.junit.Assert.assertTrue
import org.junit.Test

class DiContainerTest {

    @Test
    fun `클래스 타입으로 인스턴스를 생성할 수 있다`() {
        DiContainer.bind(ProductRepository::class, DefaultProductRepository::class)
        val productRepository = DiContainer.getProvider(ProductRepository::class)
        assertNotNull(productRepository)
        assertTrue(productRepository is DefaultProductRepository)
    }

    @Test
    fun `캐싱한 동일한 인스턴스를 반환한다`() {
        DiContainer.bind(ProductRepository::class, DefaultProductRepository::class)
        val instance1 = DiContainer.getProvider(ProductRepository::class)
        val instance2 = DiContainer.getProvider(ProductRepository::class)
        assertThat(instance1).isSameAs(instance2)
    }

    @Test
    fun `생성자 주입을 통해 의존성을 해결할 수 있다`() {
        DiContainer.bind(CartRepository::class, DefaultCartRepository::class)
        val cartViewModel = DiContainer.getProvider(CartViewModel::class)
        val cartRepository = DiContainer.getProvider(CartRepository::class)

        assertThat(cartRepository).isSameAs(cartViewModel.cartRepository)
    }

    @Test
    fun `Inject 어노테이션으로 필드 주입을 할 수 있다`() {
        DiContainer.bind(ProductRepository::class, DefaultProductRepository::class)
        DiContainer.bind(CartRepository::class, DefaultCartRepository::class)

        val mainViewModel = DiContainer.getProvider(MainViewModel::class)
        val productRepository = DiContainer.getProvider(ProductRepository::class)
        val cartRepository = DiContainer.getProvider(CartRepository::class)

        assertThat(productRepository).isSameAs(mainViewModel.productRepository)
        assertThat(cartRepository).isSameAs(mainViewModel.cartRepository)
    }


    @Test
    fun `CartRepository의 Qualifier를 사용하여 의존성을 구분할 수 있다`() {
        DiContainer.bind(CartRepository::class, FakeCartRepository::class, InMemory::class)
        DiContainer.bind(CartRepository::class, DefaultCartRepository::class, LocalDatabase::class)

        val cartUsecase = DiContainer.getProvider(CartUseCase::class)

        assertThat(cartUsecase.fakeCartRepository).isExactlyInstanceOf(FakeCartRepository::class.java)
        assertThat(cartUsecase.realCartRepository).isExactlyInstanceOf(DefaultCartRepository::class.java)
        assertThat(cartUsecase.fakeCartRepository).isNotSameAs(cartUsecase.realCartRepository)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `바인딩되지 않은 인터페이스를 요청하면 예외가 발생한다`() {
        DiContainer.getProvider(UserRepository::class)
    }
}