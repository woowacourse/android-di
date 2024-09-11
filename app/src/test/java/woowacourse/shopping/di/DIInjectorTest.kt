package woowacourse.shopping.di

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class FakeModule : Module {
    fun provideFakeRepository(): FakeRepository {
        return FakeRepository(FakeCartRepository(), FakeProductRepository())
    }
}

class FakeRepository(
    @DI private val fakeCartRepository: FakeCartRepository,
    private val fakeProductRepository: FakeProductRepository,
)

class FakeCartRepository {
    @DI
    val fakeFieldRepository: FakeFieldRepository? = null
}

class FakeProductRepository

class FakeFieldRepository

class DIInjectorTest {
    @Before
    fun setup() {
        DIContainer.clear()
        DIInjector.injectModule(FakeModule())
    }

    @Test
    fun `Inject Annotation이 붙은 필드는 자동으로 의존성이 주입된다`() {
        val cartRepository = DIContainer.getInstance(FakeCartRepository::class)

        assertThat(cartRepository).isNotNull()
        assertThat(cartRepository?.fakeFieldRepository).isNotNull()
    }

    @Test
    fun `Inject Annotation이 붙은 필드는 의존성이 주입되지 않는다`() {
        val productRepository = DIContainer.getInstance(FakeProductRepository::class)
        assertThat(productRepository).isNull()
    }
}
