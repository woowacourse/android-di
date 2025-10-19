package com.example.di

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DependencyInjectorTest {
    @Before
    fun setUp() {
        val application = ApplicationProvider.getApplicationContext<Application>()
        DependencyContainer.initialize(application, RepositoryModule())
    }

    @Test
    fun `의존성이 정상적으로 주입된다`() {
        val viewModel = ViewModelFactory.create(ViewModelA::class.java)
        assertThat(viewModel.productRepository).isInstanceOf(ProductRepository::class.java)
    }

    @Test
    fun `같은 인터페이스의 다른 구현체를 구별할 수 있다`() {
        val viewModel = ViewModelFactory.create(ViewModelC::class.java)
        assertThat(viewModel.cartRepositoryA).doesNotHaveSameClassAs(viewModel.cartRepositoryB)
    }

    @Test
    fun `같은 의존성을 사용할 경우 하나의 인스턴스를 공유한다`() {
        val viewModelA = ViewModelFactory.create(ViewModelA::class.java)
        val viewModelB = ViewModelFactory.create(ViewModelB::class.java)
        assertThat(viewModelA.productRepository).isSameAs(viewModelB.productRepository)
    }
}
