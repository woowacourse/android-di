package com.example.di

import androidx.lifecycle.ViewModel
import com.example.di.annotation.Inject
import com.example.di.annotation.Qualifier
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class FakeModule : Module {
    fun provideFakeCartDao(): FakeCartDao {
        return FakeCartDao()
    }

    @Qualifier(FakeDatabaseCartRepository::class)
    fun provideFakeDatabaseCartRepository(
        fakeCartDao: FakeCartDao,
        fakeProductRepository: FakeProductRepository,
    ): FakeCartRepository {
        return FakeDatabaseCartRepository(fakeCartDao, fakeProductRepository)
    }

    fun provideFakeInMemoryCartRepository(): FakeCartRepository {
        return FakeInMemoryCartRepository()
    }

    fun provideFakeProductRepository(): FakeProductRepository {
        return FakeProductRepository()
    }

    fun provideFakeFieldRepository(): FakeFieldRepository {
        return FakeFieldRepository()
    }
}

class FakeCartDao

interface FakeCartRepository

class FakeDatabaseCartRepository(
    @Inject val fakeCartDao: FakeCartDao,
    val fakeProductRepository: FakeProductRepository? = null,
) : FakeCartRepository

class FakeInMemoryCartRepository : FakeCartRepository

class FakeProductRepository

class FakeFieldRepository

class FakeViewModel(
    @Qualifier(FakeDatabaseCartRepository::class) @Inject val fakeDatabaseCartRepository: FakeCartRepository,
) : ViewModel() {
    @Inject
    lateinit var fakeFieldRepository: FakeFieldRepository
}

class DIInjectorTest {
    private lateinit var viewModel: FakeViewModel

    @Before
    fun setup() {
        DIContainer.clear()
        DIInjector.injectModule(FakeModule())
        viewModel = DIInjector.createInstance(FakeViewModel::class)
    }

    @Test
    fun `Inject Annotation이 붙은 생성자는 자동으로 의존성이 주입된다`() {
        val cartDao = DIContainer.getInstance(FakeCartDao::class)
        assertThat(cartDao).isNotNull()
    }

    @Test
    fun `Inject Annotation이 붙은 필드는 자동으로 의존성이 주입된다`() {
        assertThat(viewModel.fakeFieldRepository).isNotNull()
    }

    @Test
    fun `Qualifier Annotation이 붙은 생성자는 정확한 타입의 의존성이 주입된다`() {
        val databaseCartRepository = viewModel.fakeDatabaseCartRepository
        assertThat(databaseCartRepository).isInstanceOf(FakeDatabaseCartRepository::class.java)
    }

    @Test
    fun `Qualifier Annotation이 없는 경우 기본 구현체는 주입되지 않는다`() {
        val inMemoryCartRepository = DIContainer.getInstance(FakeInMemoryCartRepository::class)
        assertThat(inMemoryCartRepository).isNull()
    }
}
