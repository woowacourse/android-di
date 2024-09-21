package com.example.di

import FakeDatabaseCartRepository
import FakeModule
import FakeViewModel
import com.example.fake.FakeCartDao
import com.example.fake.FakeInMemoryCartRepository
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class InjectorTest {
    private lateinit var viewModel: FakeViewModel

    @Before
    fun setup() {
        Container.clear()
        Injector.injectModule(FakeModule())
        viewModel = Injector.createInstance(FakeViewModel::class)
    }

    @Test
    fun `Inject Annotation이 붙은 생성자는 자동으로 의존성이 주입된다`() {
        val cartDao = Container.getInstance(FakeCartDao::class)
        assertThat(cartDao).isNotNull()
    }

    @Test
    fun `Inject Annotation이 붙은 필드는 자동으로 의존성이 주입된다`() {
        assertThat(viewModel.fakeFieldRepository).isNotNull()
    }

    @Test
    fun `Qualifier Annotation이 붙은 생성자는 정확한 타입의 의존성이 주입된다`() {
        val cartRepository = viewModel.fakeDatabaseCartRepository
        assertThat(cartRepository).isInstanceOf(FakeDatabaseCartRepository::class.java)
    }

    @Test
    fun `Qualifier Annotation이 없는 경우 기본 구현체는 주입되지 않는다`() {
        val databaseCartRepository = viewModel.fakeDatabaseCartRepository
        assertThat(databaseCartRepository).isNotInstanceOf(FakeInMemoryCartRepository::class.java)
    }
}
