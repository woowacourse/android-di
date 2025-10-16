package woowacourse.shopping.di

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import woowacourse.shopping.di.fixture.DefaultProductRepository
import woowacourse.shopping.di.fixture.FakeCartRepository
import woowacourse.shopping.di.fixture.FakeProductRepository
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository
import kotlin.reflect.typeOf

class AppContainerImplTest {
    @Test
    fun `등록한_Repository는_저장되어_있다면_정상적으로_조회된다`() {
        val container =
            AppContainerImpl().apply {
                register(typeOf<ProductRepository>()) { FakeProductRepository() }
                register(typeOf<CartRepository>()) { FakeCartRepository() }
            }

        val productRepository = container.resolve<ProductRepository>()
        val cartRepository = container.resolve<CartRepository>()

        assertThat(productRepository).isInstanceOf(FakeProductRepository::class.java)
        assertThat(cartRepository).isInstanceOf(FakeCartRepository::class.java)
    }

    @Test
    fun `등록하지_않은_타입은_null을_반환한다`() {
        val container = AppContainerImpl()

        val productRepository = container.resolve<ProductRepository>()
        assertThat(productRepository).isNull()
    }

    @Test
    fun `Qualifier에_따라_서로_다른_구현체를_구분할_수_있다`() {
        val databaseQualifier = Database()
        val inMemoryQualifier = InMemory()

        val container =
            AppContainerImpl().apply {
                register(typeOf<ProductRepository>(), databaseQualifier) { DefaultProductRepository() }
                register(typeOf<ProductRepository>(), inMemoryQualifier) { FakeProductRepository() } // ← 여기!
            }

        val databaseRepository = container.resolve<ProductRepository>(databaseQualifier)
        val inMemoryRepository = container.resolve<ProductRepository>(inMemoryQualifier)

        assertThat(databaseRepository).isNotNull
        assertThat(inMemoryRepository).isNotNull
        assertThat(databaseRepository).isNotSameAs(inMemoryRepository)
    }
}
