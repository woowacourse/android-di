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
                register(typeOf<ProductRepository>(), FakeProductRepository::class) { FakeProductRepository() }
                register(typeOf<CartRepository>(), FakeCartRepository::class) { FakeCartRepository() }
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
    fun `Qualifier에_따라_서로_다른_구현체를_구분하여_조회할_수_있다`() {
        val databaseQualifier = Database()
        val inMemoryQualifier = InMemory()

        val container =
            AppContainerImpl().apply {
                register(typeOf<ProductRepository>(), DefaultProductRepository::class, databaseQualifier) { DefaultProductRepository() }
                register(typeOf<ProductRepository>(), FakeProductRepository::class, inMemoryQualifier) { FakeProductRepository() }
            }

        val databaseRepository = container.resolve<ProductRepository>(databaseQualifier)
        val inMemoryRepository = container.resolve<ProductRepository>(inMemoryQualifier)

        assertThat(databaseRepository).isNotNull
        assertThat(inMemoryRepository).isNotNull
        assertThat(databaseRepository).isNotSameAs(inMemoryRepository)
        assertThat(databaseRepository).isInstanceOf(DefaultProductRepository::class.java)
        assertThat(inMemoryRepository).isInstanceOf(FakeProductRepository::class.java)
    }

    @Test
    fun `싱글톤_스코프로_등록된_객체는_항상_같은_인스턴스를_반환한다`() {
        val container =
            AppContainerImpl().apply {
                register(typeOf<CartRepository>(), FakeCartRepository::class) { FakeCartRepository() }
            }

        val instance1 = container.resolve<CartRepository>()
        val instance2 = container.resolve<CartRepository>()

        assertThat(instance1).isNotNull
        assertThat(instance2).isNotNull
        assertThat(instance1).isSameAs(instance2)
    }
}
