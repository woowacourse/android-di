package woowacourse.shopping.data.di

import com.google.common.truth.Truth.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

class RepositoryContainerTest {
    @Test
    fun `어노테이션이 붙은 필드에만 의존성이 주입된다`() {
        val container = RepositoryContainer(
            dataContainer = DataContainer.testInstanceCreate(
                CartProductDao::class to { FakeCartProductDao() }
            ),
            bindings = listOf(
                RepositoryBinding(
                    type = CartRepository::class,
                    implementation = DefaultCartRepository::class,
                ),
                RepositoryBinding(
                    type = ProductRepository::class,
                    implementation = ProductRepository::class,
                ),
            )
        )

        val target = InjectionTarget()

        container.inject(target)

        assertThat(target.productRepository)
            .isSameInstanceAs(container.getInstance(ProductRepository::class))

        assertThat(target.cartRepository)
            .isSameInstanceAs(container.getInstance(CartRepository::class))


        assertThat(target.productNotInjected).isNull()
        assertThat(target.cartNotInjected).isNull()
    }

    @Test
    fun `Qualifier에 맞는 구현체를 각각 주입한다`() {
        val container = RepositoryContainer(
            dataContainer = DataContainer.testInstanceCreate(
                CartProductDao::class to { FakeCartProductDao() }
            ),
            bindings = listOf(
                RepositoryBinding(
                    type = CartRepository::class,
                    implementation = RoomCartRepositoryFake::class,
                    qualifier = RoomBacked::class
                ),
                RepositoryBinding(
                    type = CartRepository::class,
                    implementation = InMemoryCartRepositoryFake::class,
                    qualifier = InMemory::class
                ),
            )
        )
        val target = InjectionTargetWithQualifier()

        container.inject(target)

        assertThat(target.roomCartRepositoryFake)
            .isInstanceOf(RoomCartRepositoryFake::class.java)
        assertThat(target.inMemoryCartRepositoryFake)
            .isInstanceOf(InMemoryCartRepositoryFake::class.java)

        assertThat(target.roomCartRepositoryFake)
            .isSameInstanceAs(container.getInstance(CartRepository::class, RoomBacked::class))
        assertThat(target.inMemoryCartRepositoryFake)
            .isSameInstanceAs(container.getInstance(CartRepository::class, InMemory::class))
    }

    @Test
    fun `Qualifier가 없고 구현 후보가 여러 개면 예외가 발생한다`() {
        val container = RepositoryContainer(
            dataContainer = DataContainer.testInstanceCreate(
                CartProductDao::class to { FakeCartProductDao() }
            ),
            bindings = listOf(
                RepositoryBinding(
                    type = CartRepository::class,
                    implementation = DefaultCartRepository::class,
                ),
                RepositoryBinding(
                    type = CartRepository::class,
                    implementation = RoomCartRepositoryFake::class,
                    qualifier = RoomBacked::class
                ),
                RepositoryBinding(
                    type = CartRepository::class,
                    implementation = InMemoryCartRepositoryFake::class,
                    qualifier = InMemory::class
                ),
            )
        )

        assertThatThrownBy {
            container.inject(UnqualifiedTarget())
        }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessageContaining("Qualifier")
    }

    private class InjectionTarget {
        @field:CustomFieldInjection
        lateinit var productRepository: ProductRepository

        @field:CustomFieldInjection
        lateinit var cartRepository: CartRepository

        var productNotInjected: ProductRepository? = null

        var cartNotInjected: CartRepository? = null
    }

    private class InjectionTargetWithQualifier {
        @field:CustomFieldInjection
        @field:RoomBacked
        lateinit var roomCartRepositoryFake: CartRepository

        @field:CustomFieldInjection
        @field:InMemory
        lateinit var inMemoryCartRepositoryFake: CartRepository
    }

    private class UnqualifiedTarget {
        @field:CustomFieldInjection
        lateinit var repository: CartRepository
    }

    private class FakeCartProductDao: CartProductDao {

        override suspend fun getAll(): List<CartProductEntity> = emptyList()

        override suspend fun insert(cartProduct: CartProductEntity) = Unit

        override suspend fun delete(id: Long) = Unit
    }
    
    class RoomCartRepositoryFake: CartRepository {
        override suspend fun addCartProduct(product: Product) = Unit

        override suspend fun getAllCartProducts(): List<CartProduct> =
            emptyList()
        
        override suspend fun deleteCartProduct(id: Long) = Unit
    }
    
    class InMemoryCartRepositoryFake: CartRepository {
        override suspend fun addCartProduct(product: Product) = Unit

        override suspend fun getAllCartProducts(): List<CartProduct> =
            emptyList()

        override suspend fun deleteCartProduct(id: Long) = Unit
    }
}
