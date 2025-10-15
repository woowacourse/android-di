package woowacourse.bibi.di

import androidx.lifecycle.ViewModel
import org.junit.Assert
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import woowacourse.bibi.di.androidx.InjectingViewModelFactory
import woowacourse.bibi.di.core.AppContainer
import woowacourse.bibi.di.core.Inject
import woowacourse.bibi.di.core.Local
import woowacourse.bibi.di.core.Remote
import kotlin.reflect.KClass
import kotlin.reflect.KType

class InjectingViewModelFactoryTest {
    interface ProductRepository

    class FakeProductRepository : ProductRepository

    interface CartRepository

    class FakeCartRepository : CartRepository

    class ConstructorTestViewModel(
        @Remote val product: ProductRepository,
        @Local val cart: CartRepository,
    ) : ViewModel()

    class FieldTestViewModel : ViewModel() {
        @Inject @Remote
        lateinit var product: ProductRepository
        var notInjected: CartRepository? = null
    }

    private lateinit var factory: InjectingViewModelFactory
    private lateinit var productRepo: ProductRepository
    private lateinit var cartRepo: CartRepository

    @Before
    fun setup() {
        productRepo = FakeProductRepository()
        cartRepo = FakeCartRepository()

        val container =
            MapBackedContainer(
                mapOf(
                    Key(ProductRepository::class, Remote::class) to productRepo,
                    Key(CartRepository::class, Local::class) to cartRepo,
                ),
            )

        factory = InjectingViewModelFactory(container)
    }

    @Test
    fun `생성자 주입 ViewModel 생성 테스트`() {
        // when
        val viewModel = factory.create(ConstructorTestViewModel::class.java)

        // then
        assertNotNull(viewModel)
        assertSame(productRepo, viewModel.product)
        assertSame(cartRepo, viewModel.cart)
    }

    @Test
    fun `@Inject가 붙은 필드만 주입된다`() {
        // when
        val viewModel = factory.create(FieldTestViewModel::class.java)

        // then
        assertNotNull(viewModel)
        assertSame(productRepo, viewModel.product)
        assertNull(viewModel.notInjected)
    }

    @Test(expected = IllegalStateException::class)
    fun `바인딩 누락 시 예외를 던진다`() {
        // given
        val brokenContainer =
            MapBackedContainer(
                mapOf(Key(ProductRepository::class, Remote::class) to productRepo),
            )
        val brokenFactory = InjectingViewModelFactory(brokenContainer)

        // when
        brokenFactory.create(ConstructorTestViewModel::class.java)
    }

    private class MapBackedContainer(
        private val map: Map<Key, Any>,
    ) : AppContainer {
        override fun resolve(
            type: KType,
            qualifier: KClass<out Annotation>?,
        ): Any {
            val kClass =
                type.classifier as? KClass<*>
                    ?: error("Unsupported type: $type")
            return map[Key(kClass, qualifier)]
                ?: error("No binding for $kClass (qualifier=${qualifier?.simpleName})")
        }
    }

    private data class Key(
        val type: KClass<*>,
        val qualifier: KClass<out Annotation>?,
    )
}
